insert into authors(full_name)
values ('Author_1'),
       ('Author_2'),
       ('Author_3'),
       ('Author_4'),
       ('Author_5'),
       ('Author_6'),
       ('Author_7'),
       ('Author_8'),
       ('Author_9'),
       ('Author_10')
;

insert into genres(name)
values ('Genre_1'),
       ('Genre_2'),
       ('Genre_3'),
       ('Genre_4'),
       ('Genre_5'),
       ('Genre_6')
;

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1),
       ('BookTitle_2', 2, 2),
       ('BookTitle_3', 3, 3),
       ('BookTitle_4', 4, 4),
       ('BookTitle_5', 5, 5),
       ('BookTitle_6', 6, 6),
       ('BookTitle_7', 7, 1),
       ('BookTitle_8', 8, 2),
       ('BookTitle_9', 9, 1),
       ('BookTitle_10', 10, 2)
;
insert into comments(comment_text, book_id)
values ('CommentText1_1', 1),
       ('CommentText2_1', 2),
       ('CommentText2_2', 2),
       ('CommentText3_1', 3),
       ('CommentText3_2', 3),
       ('CommentText3_3', 3),
       ('CommentText4_1', 4),
       ('CommentText4_2', 4),
       ('CommentText4_3', 4),
       ('CommentText4_4', 4),
       ('CommentText5_1', 5),
       ('CommentText5_2', 5),
       ('CommentText5_3', 5),
       ('CommentText5_4', 5),
       ('CommentText5_5', 5),
       ('CommentText6_1', 6),
       ('CommentText6_2', 6),
       ('CommentText6_3', 6),
       ('CommentText6_4', 6),
       ('CommentText6_5', 6),
       ('CommentText6_6', 6),
       ('CommentText7_1', 7),
       ('CommentText7_2', 7),
       ('CommentText7_3', 7),
       ('CommentText7_4', 7),
       ('CommentText7_5', 7),
       ('CommentText7_6', 7),
       ('CommentText7_7', 7),
       ('CommentText8_1', 8),
       ('CommentText8_2', 8),
       ('CommentText8_3', 8),
       ('CommentText8_4', 8),
       ('CommentText8_5', 8),
       ('CommentText8_6', 8),
       ('CommentText8_7', 8),
       ('CommentText8_8', 8)
;