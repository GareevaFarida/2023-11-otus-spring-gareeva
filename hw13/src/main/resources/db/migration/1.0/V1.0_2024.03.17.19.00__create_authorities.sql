create table if not exists roles
(
    id   bigserial primary key,
    name varchar unique not null
);

create table if not exists users_roles
(
    id      bigserial primary key,
    user_id bigint references users (id),
    role_id bigint references roles (id)
);