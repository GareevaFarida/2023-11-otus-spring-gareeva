insert into users (username, password)
values ('user1', '$2a$12$dlO7jn2.e0iXMXcUHBI1AO0KBxpZkx1n0d25OLnrm4J.O7EvBiwVq'),--password1
       ('user2', '$2a$12$9qzHCLRT37SiRIBjz8u3XOyPVLhiNPEjge7QMR1iNnKuAOD38XnAC'),--password2
       ('user3', '$2a$12$fOFn1wiw3ckL1lpxkIQrEe1PGC3eeSYILCLMcc4FaFIc33g.H8ehK');--password3

insert into authorities(name)
values ('ROLE_MANAGER'),
       ('ROLE_CUSTOMER');

insert into users_authorities (user_id, authority_id)
values (1, 1),
       (2, 2),
       (3, 2);