create table sqills.users
(
    userid        serial not null
        constraint users_pkey
            primary key,
    name          varchar,
    email         varchar,
    administrator boolean
);