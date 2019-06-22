drop function if exists get_currently_available_rooms;
create function get_currently_available_rooms() returns table(roomname text)
as $$
    select roomname
    from sqills.room
    where roomid not in (
      select roomid
      from sqills.booking
      where date = CURRENT_DATE
      and CURRENT_TIME  between starttime and endtime
    )
    and roomid > 0;
  $$
  language sql;

drop function if exists get_free_until;
create function get_free_until(room_name text) returns table(starttime time)
as $$
  select min(b.starttime)
  from sqills.booking b
  join sqills.room r on b.roomid = r.roomid
  where r.roomname = room_name
  and b.date = CURRENT_DATE
  and b.starttime > CURRENT_TIME;
  and b.starttime > CURRENT_TIME;
  $$
  language sql;

drop function if exists get_booking_for_this_week(room_name text);
create function get_booking_for_this_week(room_name text) returns table(booking_id int,start_time time, end_time time, date date, name text, is_private boolean, title text)
as $$
  select b.bookingid, b.starttime, b.endtime, b.date, u.name, b.isPrivate, b.title
  from sqills.booking b
  join sqills.room r on b.roomid = r.roomid
  join sqills.users u on b.owner = u.userid
  where EXTRACT(WEEK FROM date) = EXTRACT(WEEK FROM CURRENT_DATE)
  and r.roomname = room_name
  order by b.date asc;
  $$
  language sql;
