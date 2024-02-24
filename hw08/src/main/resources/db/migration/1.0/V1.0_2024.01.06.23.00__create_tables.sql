create table if not exists authors
(
    id        bigserial,
    full_name varchar(255) not null,
    primary key (id)
);

create table if not exists genres
(
    id   bigserial,
    name varchar(255) not null unique,
    primary key (id)
);

create table if not exists books
(
    id        bigserial,
    title     varchar(255) not null,
    author_id bigint references authors (id),
    genre_id  bigint references genres (id),
    primary key (id)
);

create table if not exists comments
(
    id      bigserial,
    comment_text    varchar(1024),
    book_id bigint references books (id)
);
alter table if exists comments
    add constraint fk_books_id
        foreign key (book_id)
            references books;