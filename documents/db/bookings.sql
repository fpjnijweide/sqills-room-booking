create table sqills.booking
(
    bookingid serial  not null
        constraint booking_pk
            primary key,
    date      date not null,
    starttime time not null,
    endtime   time not null,
    roomid    integer not null
        constraint booking_room_roomid_fk
            references sqills.room,
    owner     integer not null
        constraint booking_users_userid_fk
            references sqills.users,
    isprivate boolean not null,
    title     varchar
);