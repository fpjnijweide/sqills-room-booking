INSERT INTO sqills.room (roomid, roomname) VALUES (DEFAULT, 'CR1a');
INSERT INTO sqills.room (roomid, roomname) VALUES (DEFAULT, 'WA2');
INSERT INTO sqills.room (roomid, roomname) VALUES (DEFAULT, 'CR3222');
INSERT INTO sqills.room (roomid, roomname) VALUES (DEFAULT, 'BAPR2');

INSERT INTO sqills.users (userid, name, email, administrator) VALUES (DEFAULT, 'PRIVATE', 'PRIVATE', true);
INSERT INTO sqills.users (userid, name, email, administrator) VALUES (DEFAULT, 'Antonia Heath', 'antoniaheath@gmail.com', false);
INSERT INTO sqills.users (userid, name, email, administrator) VALUES (DEFAULT, 'Marten', 'marten@gmail.com', false);
INSERT INTO sqills.users (userid, name, email, administrator) VALUES (DEFAULT, 'Admin', 'admin@gmail.com', true);


INSERT INTO sqills.booking (bookingid, date, starttime, endtime, roomid, owner, isprivate, title) VALUES (DEFAULT, '2019-05-24', '14:00:00', '15:00:00', 2, 2, true, 'Earnings review');
INSERT INTO sqills.booking (bookingid, date, starttime, endtime, roomid, owner, isprivate, title) VALUES (DEFAULT, '2019-05-24', '15:00:00', '16:00:00', 1, 3, false, 'Design meeting');

INSERT INTO sqills.participants (userid, bookingid) VALUES (2, 2);