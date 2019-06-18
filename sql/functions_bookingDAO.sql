

drop function if exists get_specific_booking;
CREATE FUNCTION get_specific_booking(booking_id int) RETURNS TABLE(starttime time, endtime time, name text, roomname text, date date, isprivate boolean, title text)
    AS $$
        SELECT b.starttime, b.endtime, u.name, r.roomname, b.date, b.isprivate, b.title
        FROM sqills.Booking b
        JOIN sqills.room r ON b.roomid = r.roomid
        JOIN sqills.users u ON u.userid = b.owner
        WHERE b.bookingid = booking_id
     $$
    LANGUAGE SQL;


drop function if exists create_booking;
create function create_booking(start_time time, end_time time, date date ,room_name text, owner text, is_private boolean, title text) returns integer
  as $$
    INSERT INTO sqills.Booking (startTime, endTime, date, roomID, owner, isPrivate, title)
                                VALUES ( start_time, end_time, date,
                (SELECT sqills.room.roomid
                 FROM sqills.room
                  WHERE roomname = room_name),
                (SELECT sqills.users.userID
                FROM sqills.users
                  WHERE email = owner),
                is_private,
                title)
                RETURNING bookingid
    $$
    LANGUAGE SQL;

drop function if exists create_recurring_booking(start_time time, end_time time, date date ,room_name text, owner text, is_private boolean, title text, repeat_every repeat_type, frequency int, starting_from date, ending_at date, with_gaps_of int);
create function create_recurring_booking(start_time time, end_time time, date date ,room_name text, owner text, is_private boolean, title text, repeat_every repeat_type, frequency int, starting_from date, ending_at date, with_gaps_of int)
RETURNS integer as $$
DECLARE
  booking_id integer;
  recurring_pattern_id integer;
BEGIN
  recurring_pattern_id :=  create_recurring_pattern(repeat_every, frequency, starting_from, ending_at, with_gaps_of);
  booking_id :=  create_booking(start_time, end_time, date, room_name, owner, is_private, title);
    insert into sqills.booking_recurring(recurring_pattern_id, booking_id)
    values(recurring_pattern_id, booking_id);
    return booking_id;
  end;
    $$
  LANGUAGE plpgsql;

drop function if exists create_recurring_pattern(p_repeat_every repeat_type, p_frequency int, p_starting_from date, p_ending_at date, p_with_gaps_of int);
create function create_recurring_pattern(p_repeat_every repeat_type, p_frequency int, p_starting_from date, p_ending_at date, p_with_gaps_of int)
  RETURNS integer as $$
  declare recurring_pattern_id integer;
  begin
        select rp.recurring_pattern_id
          from sqills.recurring_pattern rp
        where p_repeat_every = rp.repeat_every and
          p_frequency = rp.frequency and
          p_starting_from = rp.starting_from and
          p_ending_at = rp.ending_at and
          p_with_gaps_of = rp.with_gaps_of
          limit 1 into recurring_pattern_id;
      if (recurring_pattern_id is null) then
          insert into sqills.recurring_pattern(repeat_every, frequency, starting_from, ending_at, with_gaps_of)
          values( p_repeat_every, p_frequency, p_starting_from, p_ending_at, p_with_gaps_of);
       END IF;
                 return recurring_pattern_id;
  end;
    $$
    LANGUAGE plpgsql;

drop function if exists populate_recurring_bookings();
create OR REPLACE  function populate_recurring_bookings(booking_id int, recurring_pattern_id int)
as $$
declare

begin
--   TODO implement complex functions to populate recurring bookings

end;
    $$
LANGUAGE plpgsql;


drop function if exists update_booking(start_time time, end_time time, date date, room_name text, email text, is_private boolean, title text, booking_id int);
create function update_booking(start_time time, end_time time, date date, room_name text, email text, is_private boolean, title text, booking_id int) returns void
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



drop function if exists booking_for_room_today;
create function booking_for_room_today(room_name text) returns table(start_time time, end_time time, name text, date date, is_private boolean, title text)
as $$
  select b.starttime, b.endtime, u.name, b.date, b.isprivate, b.title
  from sqills.booking b
  join sqills.room r on b.roomid = r.roomid
  join sqills.users u on u.userid = b.owner
  where r.roomname = room_name and b.date = CURRENT_DATE
  $$
  language sql;


drop function if exists is_valid_booking(room_name text, booking_date date);
create function is_valid_booking(room_name text, booking_date date) returns table(start_time time, end_time time, date date)
as $$
  select b.starttime, b.endtime, b.date
  from sqills.booking b
  join sqills.room r on b.roomid = r.roomid
  where r.roomname = room_name and b.date = booking_date;
  $$
  language sql;
