create table sqills.room
(
    roomid   serial not null
        constraint room_pkey
            primary key,
    roomname varchar
);

create unique index room_roomname_uindex
    on sqills.room (roomname);