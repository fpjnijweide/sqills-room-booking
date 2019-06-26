drop  table if exists sqills.users CASCADE;
create table sqills.users
(
    user_id        serial not null unique,
    name          varchar,
    email         varchar,
    administrator boolean
);
drop  table if exists sqills.room CASCADE;
create table sqills.room
(
    room_id   serial not null unique,
      room_name varchar
);
drop  table if exists sqills.booking CASCADE;
create table sqills.booking
(
    booking_id serial not null unique,
    date      date,
    start_time time,
    end_time   time,
    room_id    integer not null  references sqills.room(room_id),
    owner     integer not null references  sqills.users(user_id),
    is_private boolean,
    title     varchar,
    is_recurring boolean

);
drop type repeat_type;
create type repeat_type as enum
  ('day','week','month','year');

drop table if exists  sqills.recurring_pattern cascade ;
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
  unique (repeat_every_type,repeat_every,starting_from,ending_at)
);
drop table if exists sqills.booking_recurring cascade ;
create table sqills.booking_recurring
(
  booking_recurring_id serial,
  recurring_pattern_id integer not null REFERENCES sqills.recurring_pattern(recurring_pattern_id),
  booking_id integer not null REFERENCES sqills.booking(booking_id),
  parent_booking_id integer not null REFERENCES sqills.booking(booking_id)
);

drop  table if exists sqills.participants CASCADE;
CREATE TABLE sqills.participants (
  user_id integer REFERENCES sqills.users(user_id),
  booking_id integer REFERENCES sqills.booking(booking_id)
);


drop table if exists sqills.google_calendar_sync;
create table sqills.google_calendar_sync (
  sync_id serial,
  date_time timestamp,
  sync_token varchar
  );

insert into sqills.users (name, email) values('admin','sqills_tablet@gmail.com');


insert into sqills.room(room_name) values ('1'),('2'),('3'),('4'),('5'),('6')