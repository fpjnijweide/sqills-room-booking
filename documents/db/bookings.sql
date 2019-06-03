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