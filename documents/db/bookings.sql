create table sqills.booking
(
    bookingid serial
        constraint booking_pk
            primary key,
    date date,
    starttime time without time zone,
    endtime time without time zone,
    roomid int not null
        constraint booking_room_roomid_fk
            references sqills.room,
    owner int not null
        constraint booking_users_userid_fk
            references sqills.users,
    isprivate boolean,
    title varchar
);

