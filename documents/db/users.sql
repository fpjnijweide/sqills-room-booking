create table sqills.users
(
    userid        serial not null
        constraint users_pkey
            primary key,
    name          varchar not null,
    email         varchar not null,
    administrator boolean not null
);