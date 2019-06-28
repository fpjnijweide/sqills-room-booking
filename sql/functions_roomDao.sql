drop function if exists get_currently_available_rooms();
create function get_currently_available_rooms() returns table
                                                        (
                                                            room_name text
                                                        )
as
$$
select room_name
from sqills.room
where room_id not in (
    select room_id
    from sqills.booking
    where date = CURRENT_DATE
      and CURRENT_TIME between start_time and end_time
)
$$
    language sql;

drop function if exists get_free_until(p_room_name text);
create function get_free_until(p_room_name text) returns table
                                                         (
                                                             start_time time
                                                         )
as
$$
select min(b.start_time)
from sqills.booking b
         join sqills.room r on b.room_id = r.room_id
where r.room_name = p_room_name
  and b.date = CURRENT_DATE
  and b.start_time > CURRENT_TIME;
$$
    language sql;

drop function if exists get_booking_for_this_week(p_room_name text);
create function get_booking_for_this_week(p_room_name text) returns table
                                                                    (
                                                                        booking_id int,
                                                                        start_time time,
                                                                        end_time   time,
                                                                        date       date,
                                                                        name       text,
                                                                        is_private boolean,
                                                                        title      text
                                                                    )
as
$$
select b.booking_id, b.start_time, b.end_time, b.date, u.name, b.is_private, b.title
from sqills.booking b
         join sqills.room r on b.room_id = r.room_id
         join sqills.users u on b.owner = u.user_id
where EXTRACT(WEEK FROM date) = EXTRACT(WEEK FROM CURRENT_DATE)
  and r.room_name = p_room_name
order by b.date asc;
$$
    language sql;
