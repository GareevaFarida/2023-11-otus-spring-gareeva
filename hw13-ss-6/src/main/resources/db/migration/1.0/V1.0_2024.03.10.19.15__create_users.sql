create table if not exists users
(
    id       bigserial primary key,
    username varchar unique,
    password varchar
)