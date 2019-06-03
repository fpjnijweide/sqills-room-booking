create table sqills.users
(
    userid        serial not null
        constraint users_pkey
            primary key,
    name          varchar,
    email         varchar,
    administrator boolean
);


create table sqills.room
(
    roomid   serial not null
        constraint room_pkey
            primary key,
    roomname varchar
);

create table sqills.booking
(
    bookingid serial  not null
        constraint booking_pk
            primary key,
    date      date,
    starttime time,
    endtime   time,
    roomid    integer not null
        constraint booking_room_roomid_fk
            references sqills.room,
    owner     integer not null
        constraint booking_users_userid_fk
            references sqills.users,
    isprivate boolean,
    title     varchar
);


CREATE TABLE sqills.participants (
  userID integer,
  bookingID integer,
  FOREIGN KEY (userID) REFERENCES sqills.users(userID),
  FOREIGN KEY (bookingID) REFERENCES sqills.booking(bookingID)
);



