insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(comment_text,book_id)
values ('CommentText1_1',1),
       ('CommentText2_1',2),
       ('CommentText2_2',2),
       ('CommentText3_1',3),
       ('CommentText3_2',3),
       ('CommentText3_3',3)
       ;
