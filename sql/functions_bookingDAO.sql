

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

drop function if exists recurring_booking;
create function recurring_booking(start_time time, end_time time, date date ,room_name text, owner text, is_private boolean, title text, repeat_every_x_weeks int, repeat_for_x_times int) returns integer
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


drop function if exists update_booking;
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


drop function if exists is_valid_booking;
create function is_valid_booking(room_name text, date date) returns table(start_time time, end_time time, date date)
as $$
  select b.starttime, b.endtime, b.date
  from sqills.booking b
  join sqills.room r on b.roomid = r.roomid
  where r.roomname = room_name and b.date = date;
  $$
  language sql;
