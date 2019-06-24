

drop function if exists get_specific_booking(booking_id int);
CREATE FUNCTION get_specific_booking(booking_id int) RETURNS TABLE(starttime time, endtime time, name text, roomname text, date date, isprivate boolean, title text)
    AS $$
        SELECT b.starttime, b.endtime, u.name, r.roomname, b.date, b.isprivate, b.title
        FROM sqills.Booking b
        JOIN sqills.room r ON b.roomid = r.roomid
        JOIN sqills.users u ON u.userid = b.owner
        WHERE b.bookingid = booking_id
     $$
    LANGUAGE SQL;

select * from create_booking( CAST('08:30:00' as time(0)),cast('09:30:00'as time(0)), cast('2053-11-13' as date),'CR1a'::text, 'andrew@gmail.com'::text, false, 'test'::text  );
drop function if exists create_booking(start_time time, end_time time, date date ,room_name text, p_owner text, is_private boolean, title text);
create or replace function create_booking(start_time time, end_time time, date date ,room_name text, p_owner text, is_private boolean, title text) returns integer
  as $bookingid$
    INSERT INTO sqills.Booking (startTime, endTime, date, roomID, owner, isPrivate, title)
                                VALUES ( start_time, end_time, date,
                (SELECT sqills.room.roomid
                 FROM sqills.room
                  WHERE roomname = room_name),
                (SELECT sqills.users.userID
                FROM sqills.users
                  WHERE email = p_owner),
                is_private,
                title)
                RETURNING bookingid;
    $bookingid$
    LANGUAGE SQL;

drop function if exists create_recurring_booking_parent(start_time time, end_time time, date date ,room_name text, owner text, is_private boolean, title text, repeat_every_type repeat_type, repeat_every int, ending_at date);
create or replace function create_recurring_booking_parent(start_time time, end_time time, date date ,room_name text, owner text, is_private boolean, title text, repeat_every_type repeat_type, repeat_every int, ending_at date)
RETURNS integer as $$
DECLARE
  booking_id integer;
  recurring_pattern_id integer;
BEGIN
  recurring_pattern_id :=  create_recurring_pattern(repeat_every_type, repeat_every, date, ending_at);
  booking_id :=  create_booking(start_time, end_time, date, room_name, owner, is_private, title);
    insert into sqills.booking_recurring(recurring_pattern_id, booking_id, parent_booking_id )
    values(recurring_pattern_id, booking_id, booking_id);

  PERFORM populate_recurring_bookings(booking_id, recurring_pattern_id);

    return booking_id;

  end;
    $$
  LANGUAGE plpgsql;


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

drop function if exists populate_recurring_bookings(booking_id int, p_recurring_pattern_id int);
create OR REPLACE  function populate_recurring_bookings(booking_id int, p_recurring_pattern_id int)
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
    insert into sqills.booking (date, starttime, endtime, roomid, owner, isprivate, title)
      select cd.b_date, b.starttime, b.endtime, b.roomid, b.owner, b.isprivate, b.title
      from cte_booking_dates cd
             join sqills.booking b on b.bookingid = booking_id
    returning booking_id as child_booking_id
  )
  insert into sqills.booking_recurring(recurring_pattern_id, booking_id, parent_booking_id)
  select p_recurring_pattern_id, crb.child_booking_id d, booking_id
  from cte_create_recurring_bookings crb;
end;
    $$
LANGUAGE plpgsql;


drop function if exists update_booking(start_time time, end_time time, date date, room_name text, email text, is_private boolean, title text, booking_id int);
create or replace function update_booking(start_time time, end_time time, date date, room_name text, email text, is_private boolean, title text, booking_id int) returns void
  as $$
    UPDATE sqills.Booking
      SET startTime= start_time, endTime= end_time, date= date, roomID =
        (SELECT sqills.room.roomid
        FROM sqills.room
        WHERE roomname = room_name),
      owner= (SELECT sqills.users.userID
      FROM sqills.users
      WHERE email = email),
      isPrivate= is_private,
      title= title
      WHERE bookingID = booking_id
  $$
  LANGUAGE SQL;



drop function if exists booking_for_room_today(room_name text);
create or replace function booking_for_room_today(room_name text) returns table(booking_id int ,start_time time, end_time time, name text, date date, is_private boolean, title text)
as $$
  select b.bookingid as booking_id, b.starttime, b.endtime, u.name, b.date, b.isprivate, b.title
  from sqills.booking b
  join sqills.room r on b.roomid = r.roomid
  join sqills.users u on u.userid = b.owner
  where r.roomname ilike room_name and b.date = CURRENT_DATE
  $$
  language sql;


drop function if exists is_valid_booking(room_name text, booking_date date);
create or replace function is_valid_booking(room_name text, booking_date date) returns table(start_time time, end_time time, date date)
as $$
  select b.starttime, b.endtime, b.date
  from sqills.booking b
  join sqills.room r on b.roomid = r.roomid
  where r.roomname = room_name and b.date = booking_date;
  $$
  language sql;



