create table sqills.users
(
    userid        serial not null unique,
    name          varchar,
    email         varchar,
    administrator boolean
);


create table sqills.room
(
    roomid   serial not null unique,
      roomname varchar
);

create table sqills.booking
(
    bookingid serial not null unique,
    date      date,
    starttime time,
    endtime   time,
    roomid    integer not null,
    owner     integer not null,
    isprivate boolean,
    title     varchar,
    is_recurring boolean,

    roomid references sqills.room(roomid),
    owner references  sqills.users(userid)

);
drop table if exists sqills.booking_recurring;
create table sqills.booking_recurring
(
  booking_recurring_id serial,
  recurring_pattern_id integer not null REFERENCES sqills.recurring_pattern(recurring_pattern_id),
  booking_id integer not null REFERENCES sqills.booking(bookingid),
  parent_booking_id integer not null REFERENCES sqills.booking(bookingid)
);
drop type repeat_type;
create type repeat_type as enum
('day','week','month','year');

drop table if exists  sqills.recurring_pattern;
create table sqills.recurring_pattern
(
  recurring_pattern_id serial unique,
  repeat_every_type repeat_type not null,
--   day, Week, month or year
  repeat_every integer not null,
--   Every 1, 2 ... etc (repeat_type)
  starting_from date not null,
--   When to start repeating
  ending_at date,
--   When to end repeating

-- @TODO remove this, most likely not needed
--   gap of x (repeat_type)
  unique (repeat_every_type,repeat_every,starting_from,ending_at)
);

CREATE TABLE sqills.participants (
  userID integer,
  bookingID integer,
  userID REFERENCES sqills.users(userID),
  bookingID REFERENCES sqills.booking(bookingID)
);



