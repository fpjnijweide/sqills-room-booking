

drop function if exists get_specific_booking(p_booking_id int);
CREATE or replace FUNCTION get_specific_booking(p_booking_id int) RETURNS TABLE(booking_id int, start_time time, end_time time, name text, room_name text, date date, is_private boolean, title text)
    AS $$
        SELECT b.booking_id, b.start_time, b.end_time, u.name, r.room_name, b.date, b.is_private, b.title
        FROM sqills.Booking b
        JOIN sqills.room r ON b.room_id = r.room_id
        JOIN sqills.users u ON u.user_id = b.owner
        WHERE b.booking_id = p_booking_id
     $$
    LANGUAGE SQL;

drop function if exists create_booking(p_start_time time, p_end_time time, p_date date ,p_room_name text, p_owner text, p_is_private boolean, p_title text);
create or replace function create_booking(p_start_time time, p_end_time time, p_date date ,p_room_name text, p_owner text, p_is_private boolean, p_title text) returns integer
  as $booking_id$
    INSERT INTO sqills.Booking (start_time, end_time, date, room_id, owner, is_private, title)
                                VALUES ( p_start_time, p_end_time, p_date,
                (SELECT r.room_id
                 FROM sqills.room r
                  WHERE r.room_name = p_room_name),
                (SELECT u.user_id
                FROM sqills.users u
                  WHERE email = p_owner),
                p_is_private,
                p_title)
                RETURNING booking_id;
    $booking_id$
    LANGUAGE SQL;

drop function if exists delete_booking(p_start_time time, p_end_time time, p_date date ,p_room_name text);
create or replace function delete_booking(p_start_time time, p_end_time time, p_date date ,p_room_name text)
RETURNS boolean as $$
declare
  f_booking_id integer;
  begin
     select b.booking_id
     from sqills.booking b
     join sqills.room r on r.room_id = b.room_id
     where b.start_time = p_start_time and
           b.end_time = p_end_time and
           b.date = p_date and
           r.room_name = p_room_name
     into f_booking_id;

     delete from sqills.participants p where f_booking_id = p.booking_id;
     delete from sqills.booking b where f_booking_id = b.booking_id;
     return true;
  end;

$$
  LANGUAGE plpgsql;


drop function if exists create_recurring_booking_parent(start_time time, end_time time, date date ,room_name text, owner text, is_private boolean, title text, repeat_every_type repeat_type, repeat_every int, ending_at date);
create or replace function create_recurring_booking_parent(start_time time, end_time time, date date ,room_name text, owner text, is_private boolean, title text, repeat_every_type repeat_type, repeat_every int, ending_at date)
RETURNS integer as $$
DECLARE
  v_booking_id integer;
  v_recurring_pattern_id integer;
BEGIN
  v_recurring_pattern_id :=  create_recurring_pattern(repeat_every_type, repeat_every, date, ending_at);
  v_booking_id :=  create_booking(start_time, end_time, date, room_name, owner, is_private, title);
    insert into sqills.booking_recurring(recurring_pattern_id, booking_id, parent_booking_id )
    values(v_recurring_pattern_id, v_booking_id, v_booking_id);

  PERFORM populate_recurring_bookings(v_booking_id, v_recurring_pattern_id);

    return v_booking_id;

  end;
    $$
  LANGUAGE plpgsql;
select * from sqills.booking where title = 'Test4';
drop function if exists create_recurring_pattern(p_repeat_every_type repeat_type, p_repeat_every int, p_starting_from date, p_ending_at date);
create or replace function create_recurring_pattern(p_repeat_every_type repeat_type, p_repeat_every int, p_starting_from date, p_ending_at date)
  RETURNS integer as $$
  declare v_recurring_pattern_id integer;
  begin
        select rp.recurring_pattern_id
          from sqills.recurring_pattern rp
        where p_repeat_every_type = rp.repeat_every_type and
          p_repeat_every = rp.repeat_every and
          p_starting_from = rp.starting_from and
          p_ending_at = rp.ending_at
          limit 1 into v_recurring_pattern_id;
      if (v_recurring_pattern_id is null) then
          insert into sqills.recurring_pattern(repeat_every_type, repeat_every, starting_from, ending_at)
          values( p_repeat_every_type, p_repeat_every, p_starting_from, p_ending_at)
            RETURNING recurring_pattern_id INTO v_recurring_pattern_id;
       END IF;
                 return v_recurring_pattern_id;
  end;
    $$
    LANGUAGE plpgsql;

drop function if exists populate_recurring_bookings(p_booking_id int, p_recurring_pattern_id int);
create OR REPLACE  function populate_recurring_bookings(p_booking_id int, p_recurring_pattern_id int)
returns void as $$
declare
begin
  with cte_get_recurring_pattern as (
    select *
    from sqills.recurring_pattern rp
    where rp.recurring_pattern_id = p_recurring_pattern_id
  ),
  cte_booking_dates as(
     select
       generate_series(
       date_trunc(rp.repeat_every_type ::text, rp.starting_from),
       rp.ending_at, concat(rp.repeat_every,' ' , rp.repeat_every_type)::interval)::date as b_date
       from  cte_get_recurring_pattern rp
  ),
  cte_create_recurring_bookings as(
    insert into sqills.booking (date, start_time, end_time, room_id, owner, is_private, title)
      select cd.b_date, b.start_time, b.end_time, b.room_id, b.owner, b.is_private, b.title
      from cte_booking_dates cd
             join sqills.booking b on b.booking_id = p_booking_id
    returning booking_id as child_booking_id
  )
  insert into sqills.booking_recurring(recurring_pattern_id, booking_id, parent_booking_id)
  select p_recurring_pattern_id, crb.child_booking_id d, p_booking_id
  from cte_create_recurring_bookings crb;
end;
    $$
LANGUAGE plpgsql;


drop function if exists update_booking(start_time time, end_time time, date date, room_name text, email text, is_private boolean, title text, booking_id int);
create or replace function update_booking(p_start_time time, p_end_time time, p_date date, p_room_name text, p_email text, p_is_private boolean, p_title text, p_booking_id int) returns void
  as $$
    UPDATE sqills.Booking
      SET start_time = p_start_time, end_time = p_end_time, date = p_date, room_id =
        (SELECT sqills.room.room_id
        FROM sqills.room
        WHERE room_name = p_room_name),
      owner = (SELECT sqills.users.user_id
      FROM sqills.users
      WHERE email = p_email),
      is_private= p_is_private,
      title = p_title
      WHERE booking_id = p_booking_id
  $$
  LANGUAGE SQL;

drop function if exists booking_for_room_today(p_room_name text);
create or replace function booking_for_room_today(p_room_name text) returns table(booking_id int ,start_time time, end_time time, name text, date date, is_private boolean, title text)
as $$
  select b.booking_id as booking_id, b.start_time, b.end_time, u.name, b.date, b.is_private, b.title
  from sqills.booking b
  join sqills.room r on b.room_id = r.room_id
  join sqills.users u on u.user_id = b.owner
  where r.room_name = p_room_name and b.date = CURRENT_DATE
  $$
  language sql;

drop function if exists get_bookings_on_date_in_room(p_room_name text, p_booking_date date);
create or replace function get_bookings_on_date_in_room(p_room_name text, p_booking_date date) returns table(start_time time, end_time time, date date)
as $$
  select b.start_time, b.end_time, b.date
  from sqills.booking b
  join sqills.room r on b.room_id = r.room_id
  where r.room_name = p_room_name and b.date = p_booking_date;
  $$
  language sql;

drop function if exists is_valid_booking_booking_id(p_room_name text, booking_date date, p_booking_id int);
create or replace function is_valid_booking_booking_id(p_room_name text, booking_date date, p_booking_id int) returns table(start_time time, end_time time, date date)
as $$
  select b.start_time, b.end_time, b.date
  from sqills.booking b
         join sqills.room r on b.room_id = r.room_id
  where r.room_name = p_room_name and b.date = booking_date
  and b.booking_id != p_booking_id;
  $$
  language sql;
