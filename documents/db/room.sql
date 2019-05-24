create table sqills.room
(
    roomid   serial not null
        constraint room_pkey
            primary key,
    roomname varchar
);