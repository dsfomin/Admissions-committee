delete from user_role;
delete from usr;

insert into usr(id, active, email, password, average_school_note) VALUES
(1, true, 'dsfomin2@gmail.com', '$2a$10$SMXmHd8dVdQRgb.F3OqBa.6E3Xf.G1oeeeEvIlw29tQSavrjBxx5C', 10),
(2, true, 'user1', '$2a$10$AijDQRZQAyMn9QKGg6LTpexmUfow.W4LGB2F6dh.qS18y5zeCuWVG', 9.8),
(3, true, 'user2', '$2a$10$N/mndN.qXnt7fa7MCIaFw.BrPPi3Czh24aGD/a8x8F3GZdtjEg7o6', 10.2);

insert into user_role(user_id, roles) VALUES
(1, 'ADMIN'), (1, 'USER'),
(2, 'USER'),
(3, 'USER');


