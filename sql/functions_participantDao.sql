drop function if exists get_participants_of_booking(int);
create function get_participants_of_booking(booking_id int) returns table(user_id int, name text, email text, administrator boolean )
as $$
  select u.user_id, u.name, u.email, u.administrator
  from sqills.participants p
  join sqills.users u on u.user_id = p.user_id
  where p.booking_id = booking_id;
  $$
  language sql;
delete from sqills.participants;
  delete from sqills.booking_recurring;
                delete from sqills.booking;


select * from sqills.room