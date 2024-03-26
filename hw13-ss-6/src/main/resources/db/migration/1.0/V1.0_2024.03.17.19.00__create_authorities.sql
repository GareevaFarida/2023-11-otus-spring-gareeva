create table if not exists authorities
(
    id   bigserial primary key,
    name varchar unique not null
);

create table if not exists users_authorities
(
    id      bigserial primary key,
    user_id bigint references users (id),
    authority_id bigint references authorities (id)
);