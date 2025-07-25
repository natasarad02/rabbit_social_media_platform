insert into student (index_number, first_name, last_name) values ('5', 'Marko', 'Marković');
insert into student (index_number, first_name, last_name) values ('ra2-2014', 'Milan', 'Milanović');
insert into student (index_number, first_name, last_name) values ('ra3-2014', 'Ivana', 'Ivanović');
insert into student (index_number, first_name, last_name) values ('ra4-2014', 'Bojan', 'Bojanović');
insert into student (index_number, first_name, last_name) values ('ra5-2014', 'Pera', 'Perić');
insert into student (index_number, first_name, last_name) values ('ra6-2014', 'Zoran', 'Zoranović');
insert into student (index_number, first_name, last_name) values ('ra7-2014', 'Bojana', 'Bojanović');
insert into student (index_number, first_name, last_name) values ('ra8-2014', 'Milana', 'Milanović');
insert into student (index_number, first_name, last_name) values ('ra9-2014', 'Jovana', 'Jovanić');

insert into course (name) values ('Matematika');
insert into course (name) values ('Osnove programiranja');
insert into course (name) values ('Objektno programiranje');

insert into teacher (first_name, last_name, deleted) values ('Strahinja', 'Simić', false);
insert into teacher (first_name, last_name, deleted) values ('Marina', 'Antić', false);
insert into teacher (first_name, last_name, deleted) values ('Siniša', 'Branković', false);

insert into teaching (course_id, teacher_id) values (1, 1);
insert into teaching (course_id, teacher_id) values (1, 2);
insert into teaching (course_id, teacher_id) values (2, 2);
insert into teaching (course_id, teacher_id) values (3, 3);

insert into exam (student_id, course_id, date, grade) values (1, 1, '2016-02-01', 9);
insert into exam (student_id, course_id, date, grade) values (1, 2, '2016-04-19', 8);
insert into exam (student_id, course_id, date, grade) values (2, 1, '2016-02-01', 10);
insert into exam (student_id, course_id, date, grade) values (2, 2, '2016-04-19', 10);







-- Inserting data into Profile with version column
INSERT INTO profile (
    email, username, password, name, surname, deleted, role, last_password_reset_date, last_active_date,
    address, activated, registration_time, minute_following, last_follow_time, version, currently_active
) VALUES
      ('marko@example.com', 'marko', '$2b$12$Iw1jjOdqw8JZJL/eAFBnXezQ4VUZtSqv0JV11D2U9Zh4EGLO1Fe.W',
       'Marko', 'Marković', false, 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,  'Trg Dositeja Obradovica 2, Novi Sad', true, '2024-10-01 10:00:00', 0, NULL, 0, true), -- password123
      ('milan@example.com', 'milan', '$2b$12$78BjztYb6FrR0kQQikohmu97pAWWtSzpD9c/dGYOE59dTA2rVhy2S',
       'Milan', 'Milanović', false, 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, true, '2024-10-05 11:30:00', 0, NULL, 0, true), -- password456
      ('ivana@example.com', 'ivana', '$2b$12$4bC1fa0JaOEZYG/eceeLDelbCNVJYtcLJrrSD34laTpLRCziNct.S',
       'Ivana', 'Ivanović', false, 'Administrator', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, true, '2024-10-10 15:45:00', 0, NULL, 0, true), -- password789
      ('bojan@example.com', 'bojan', '$2b$12$j4M37/WpAurLS/KlWoke2Oa0fdAoikEfbqepfWrtN0euqSOS4T.Nq',
       'Bojan', 'Bojanović', false, 'Administrator', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, true, '2024-10-12 09:15:00', 0, NULL, 0, true), -- password101
      ('jane@example.com', 'jane', '$2b$12$CyUjPoyeQbENBzVNCsqnBe08ZTCjk2X5WjEspmvxBrCx7ZMMdV/OC',
       'Jane', 'Doe', false, 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, true, '2024-10-15 14:20:00', 0, NULL, 0, false), -- password202
      ('alex@example.com', 'alex', '$2b$12$siwPZyZpPMG8WaYzp1YjCeBSBEt1.LV85sKh1T/fuJCjDcqTEHIaa',
       'Alex', 'Smith', false, 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, true, '2024-10-18 16:40:00', 0, NULL, 0, false), -- password303
      ('lisa@example.com', 'lisa', '$2b$12$PiY9CaeNOA/plJ76ZrYHUOa/xfhdSHh4SIbwRANv8okkkCV6LmwTu',
       'Lisa', 'Johnson', false, 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, true, '2024-10-20 12:00:00', 51, CURRENT_TIMESTAMP, 0, false), -- password404
      ('john@example.com', 'john', '$2b$12$3rPK12xTrJy8v.iaHxG6s.bNefU8dJmQnLxuVca8kHx8mxFIRdA6K',
       'John', 'Doe', false, 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, true, '2024-10-22 18:25:00', 0, NULL, 0, false), -- password505
      ('anna@example.com', 'anna', '$2b$12$Ils5FtBFBZgyXrRao.YkPe3wprDnZ50wt4/qzqrz4J1dT4vx8.rSq',
       'Anna', 'Taylor', false, 'Administrator', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, false, '2024-10-25 08:10:00', 0, NULL, 0, false), -- password606
      ('james@example.com', 'james', '$2b$12$A7Y4k.ql2AFTO6vRkWivte0FGC5fI/VBZadxHeHEr1nFnug7ti0k.',
       'James', 'Brown', false, 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, false, '2024-10-28 20:30:00', 0, NULL, 0, false); -- password707


-- Inserting data into Post
INSERT INTO post (description, picture, deleted, posted_time, profile_id, address, longitude, latitude, like_count, version) VALUES
                                                                                                            ('This is my first post!', 'pic1.jpg', false, '2025-7-15 08:15:00', 1, 'Nemanjina 12, Beograd', 20.457273, 44.817611, 1, 0),
                                                                                                            ('Beautiful day at the park!', 'pic2.jpg', false, '2025-7-13 09:00:00', 1, 'Bulevar kralja Aleksandra 54, Beograd', 20.476521, 44.805850, 1, 0),
                                                                                                            ('Loving the city lights.', 'pic3.jpg', false, '2025-7-14 20:30:00', 2, 'Karađorđeva 65, Novi Sad', 19.842545, 45.255325, 1, 0),
                                                                                                            ('Foodie adventures!', 'pic4.jpg', false, '2024-11-10 14:45:00', 3, 'Vojvode Stepe 130, Niš', 21.895758, 43.321206, 1, 0),
                                                                                                            ('Just finished a run, feeling great!', 'pic5.jpg', false, '2024-12-05 07:30:00', 4, 'Trg slobode 1, Subotica', 19.668652, 46.097435, 1, 0),
                                                                                                            ('Sunset at the beach.', 'pic6.jpg', false, '2024-12-01 18:00:00', 5, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167, 1, 0),
                                                                                                            ('Exploring the countryside.', 'pic7.jpg', false, '2024-12-10 11:00:00', 4, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167, 1, 0),
                                                                                                            ('Weekend getaway in the mountains.', 'pic8.jpg', false, '2025-01-02 09:15:00', 7, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167, 1, 0),
                                                                                                            ('Cozy evening by the fire.', 'pic9.jpg', false, '2024-12-17 20:30:00', 8, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167, 1, 0),
                                                                                                            ('Delicious homemade dinner.', 'pic10.jpg', false, '2024-12-18 19:45:00', 4, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167, 1, 0);


-- Inserting data into Comment
INSERT INTO comment (text, profile_id, post_id, commented_time, deleted) VALUES
                                                                             ('Great post, Marko!', 2, 1, '2023-12-25 08:30:00', false),
                                                                             ('I love this picture!', 3, 1, '2025-07-05 09:30:00', false),
                                                                             ('Nice place!', 1, 2, '2024-12-01 10:00:00', false),
                                                                             ('Looks delicious!', 1, 4, '2025-06-20 12:45:00', false),
                                                                             ('What a beautiful sunset!', 4, 6, '2024-11-29 17:00:00', false),
                                                                             ('Amazing view!', 5, 7, '2024-12-12 09:30:00', false),
                                                                             ('This looks so relaxing.', 6, 8, '2024-12-15 14:30:00', false),
                                                                             ('Yum! I need this recipe!', 7, 10, '2025-01-02 20:00:00', false),
                                                                             ('So peaceful!', 8, 9, '2024-12-17 21:15:00', false),
                                                                             ('What an adventure!', 9, 5, '2024-12-14 22:00:00', false);

ALTER TABLE likes
    ADD COLUMN like_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
-- Inserting data into Likes (assumed the same as likedPosts)
INSERT INTO likes (profile_id, post_id, like_time) VALUES
                                                       (1, 1, '2025-07-08 10:15:00'),
                                                       (1, 2, '2025-07-09 12:30:00'),
                                                       (2, 3, '2025-07-10 14:45:00'),
                                                       (3, 4, '2025-07-11 16:00:00'),
                                                       (1, 5, '2025-07-12 18:15:00'),
                                                       (2, 6, '2025-07-13 20:30:00'),
                                                       (3, 7, '2025-07-14 21:00:00'),
                                                       (4, 8, '2025-07-14 09:00:00'),
                                                       (5, 9, '2025-07-15 08:45:00'),
                                                       (6, 10, '2025-07-15 10:00:00');


-- Inserting data into Profile Following
INSERT INTO profile_following (profile_id, followed_profile_id) VALUES
                                                                    (1, 2),  -- Marko is following Milan
                                                                    (1, 3),  -- Marko is following Ivana
                                                                    (2, 3),  -- Milan is following Ivana
                                                                    (3, 4),  -- Ivana is following Bojan
                                                                    (1, 4),  -- Marko is following Bojan
                                                                    (4, 5),  -- Bojan is following Jane
                                                                    (5, 6),  -- Jane is following Alex
                                                                    (6, 7),  -- Alex is following Lisa
                                                                    (7, 8),  -- Lisa is following John
                                                                    (8, 9),  -- John is following Anna
                                                                    (9, 1),  -- Anna is following Marko
                                                                    (10, 3), -- James is following Ivana
                                                                    (3, 2),  -- Ivana is following Milan
                                                                    (5, 4);  -- Jane is following Bojan

INSERT INTO chat_group (name, admin_id, deleted)
VALUES
    ('Group A', 1, false),
    ('Group B', 2, false),
    ('Tech Talk', 5, false),
    ('Gaming Squad', 6, false),
    ('Travel Buddies', 7, false),
    ('Movie Lovers', 9, false),
    ('Fitness Freaks', 1, false);

INSERT INTO chat_group_member (chat_group_id, profile_id, join_date, deleted)
VALUES
    (1, 5, NOW(), false),
    (1, 2, NOW(), false),
    (2, 1, NOW(), false),
    (2, 6, NOW(), false),
    (3, 2, NOW(), false),
    (3, 7, NOW(), false),
    (4, 9, NOW(), false),
    (5, 1, NOW(), false),
    (5, 2, NOW(), false),
    (5, 5, NOW(), false),
    (6, 6, NOW(), false),
    (6, 7, NOW(), false),
    (7, 5, NOW(), false),
    (7, 9, NOW(), false);

INSERT INTO chat_message (message, sender_id, receiver_id, chat_group_id, timestamp, deleted)
VALUES
    ('Hello everyone!', 1, NULL, 1, '2025-02-18 10:00:00', false),
    ('Welcome to the group!', 2, NULL, 1, '2025-02-18 10:05:00', false),
    ('Who is up for a game tonight?', 6, NULL, 4, '2025-02-18 10:15:00', false),
    ('Good morning!', 5, NULL, 3, '2025-02-18 10:20:00', false),
    ('Where should we travel next?', 7, NULL, 5, '2025-02-18 10:30:00', false),
    ('Check out this new fitness challenge!', 1, NULL, 7, '2025-02-18 10:40:00', false),
    ('Hey, let’s plan a movie night!', 9, NULL, 6, '2025-02-18 10:50:00', false),
    ('What’s the latest tech news?', 2, NULL, 3, '2025-02-18 11:00:00', false),
    ('Anyone tried the new update?', 5, NULL, 3, '2025-02-18 11:10:00', false),
    ('Let’s meet for a workout session!', 7, NULL, 7, '2025-02-18 11:20:00', false),
    ('The new game patch is out!', 6, NULL, 4, '2025-02-18 11:30:00', false),
    ('What are your thoughts on this movie?', 9, NULL, 6, '2025-02-18 11:40:00', false),
    ('Let’s organize a weekend trip!', 5, NULL, 5, '2025-02-18 11:50:00', false),
    ('Hey, can you send me the notes?', 1, 5, NULL, '2025-02-18 12:00:00', false),
    ('Sure! Here they are.', 5, 1, NULL, '2025-02-18 12:05:00', false),
    ('Hey! Long time no see!', 2, 7, NULL, '2025-02-18 12:10:00', false),
    ('Yeah! We should catch up soon.', 7, 2, NULL, '2025-02-18 12:15:00', false),
    ('Check out this article on AI!', 5, NULL, 3, '2025-02-18 12:20:00', false),
    ('Who wants to go hiking this weekend?', 7, NULL, 5, '2025-02-18 12:30:00', false),
    ('Just watched the latest Marvel movie!', 9, NULL, 6, '2025-02-18 12:40:00', false),
    ('Join us for a gym session today.', 1, NULL, 7, '2025-02-18 12:50:00', false),

    -- Dodatne poruke za "Travel Buddies" (chat_group_id = 5)
    ('Let’s plan a road trip!', 5, NULL, 5, '2025-02-18 13:00:00', false),
    ('Has anyone been to Japan?', 7, NULL, 5, '2025-02-18 13:10:00', false),
    ('We should visit Greece this summer!', 1, NULL, 5, '2025-02-18 13:20:00', false),
    ('Who has recommendations for cheap flights?', 2, NULL, 5, '2025-02-18 13:30:00', false),
    ('Check out these travel hacks!', 9, NULL, 5, '2025-02-18 13:40:00', false),
    ('Let’s book our tickets soon!', 7, NULL, 5, '2025-02-18 13:50:00', false),
    ('What’s the best time to visit Italy?', 6, NULL, 5, '2025-02-18 14:00:00', false),
    ('Packing tips for long trips?', 5, NULL, 5, '2025-02-18 14:10:00', false),
    ('We should try backpacking next time!', 1, NULL, 5, '2025-02-18 14:20:00', false);