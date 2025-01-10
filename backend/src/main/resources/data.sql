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


-- Inserting data into Profile
INSERT INTO profile (email, username, password, name, surname, deleted, role, last_password_reset_date, address, activated, last_active_date) VALUES
                                                                                                                                ('marko@example.com', 'marko', '$2b$12$Iw1jjOdqw8JZJL/eAFBnXezQ4VUZtSqv0JV11D2U9Zh4EGLO1Fe.W', 'Marko', 'Marković', false, 'User', CURRENT_TIMESTAMP, NULL, true, CURRENT_TIMESTAMP), -- password123
                                                                                                                                ('milan@example.com', 'milan', '$2b$12$78BjztYb6FrR0kQQikohmu97pAWWtSzpD9c/dGYOE59dTA2rVhy2S', 'Milan', 'Milanović', false, 'User', CURRENT_TIMESTAMP, NULL, true, CURRENT_TIMESTAMP), -- password456
                                                                                                                                ('ivana@example.com', 'ivana', '$2b$12$4bC1fa0JaOEZYG/eceeLDelbCNVJYtcLJrrSD34laTpLRCziNct.S', 'Ivana', 'Ivanović', false, 'Administrator', CURRENT_TIMESTAMP, NULL, true, CURRENT_TIMESTAMP), -- password789
                                                                                                                                ('bojan@example.com', 'bojan', '$2b$12$j4M37/WpAurLS/KlWoke2Oa0fdAoikEfbqepfWrtN0euqSOS4T.Nq', 'Bojan', 'Bojanović', false, 'Administrator', CURRENT_TIMESTAMP, NULL, true, CURRENT_TIMESTAMP), -- password101
                                                                                                                                ('jane@example.com', 'jane', '$2b$12$CyUjPoyeQbENBzVNCsqnBe08ZTCjk2X5WjEspmvxBrCx7ZMMdV/OC', 'Jane', 'Doe', false, 'User', CURRENT_TIMESTAMP, NULL, true, CURRENT_TIMESTAMP), -- password202
                                                                                                                                ('alex@example.com', 'alex', '$2b$12$siwPZyZpPMG8WaYzp1YjCeBSBEt1.LV85sKh1T/fuJCjDcqTEHIaa', 'Alex', 'Smith', false, 'User', CURRENT_TIMESTAMP, NULL, true, CURRENT_TIMESTAMP), -- password303
                                                                                                                                ('lisa@example.com', 'lisa', '$2b$12$PiY9CaeNOA/plJ76ZrYHUOa/xfhdSHh4SIbwRANv8okkkCV6LmwTu', 'Lisa', 'Johnson', false, 'User', CURRENT_TIMESTAMP, NULL, true, CURRENT_TIMESTAMP), -- password404
                                                                                                                                ('john@example.com', 'john', '$2b$12$3rPK12xTrJy8v.iaHxG6s.bNefU8dJmQnLxuVca8kHx8mxFIRdA6K', 'John', 'Doe', false, 'User', CURRENT_TIMESTAMP, NULL, true, CURRENT_TIMESTAMP), -- password505
                                                                                                                                ('anna@example.com', 'anna', '$2b$12$Ils5FtBFBZgyXrRao.YkPe3wprDnZ50wt4/qzqrz4J1dT4vx8.rSq', 'Anna', 'Taylor', false, 'Administrator', CURRENT_TIMESTAMP, NULL, true, CURRENT_TIMESTAMP), -- password606
                                                                                                                                ('nata.radmilovic@gmail.com', 'james', '$2b$12$A7Y4k.ql2AFTO6vRkWivte0FGC5fI/VBZadxHeHEr1nFnug7ti0k.', 'James', 'Brown', false, 'User', CURRENT_TIMESTAMP, NULL, true, '2024-12-06 13:30:00');



-- Inserting data into Post
INSERT INTO post (description, picture, deleted, posted_time, profile_id, address, longitude, latitude) VALUES
                                                                                                            ('This is my first post!', 'pic1.jpg', false, NOW(), 1, 'Nemanjina 12, Beograd', 20.457273, 44.817611),
                                                                                                            ('Beautiful day at the park!', 'pic2.jpg', false, NOW(), 1, 'Bulevar kralja Aleksandra 54, Beograd', 20.476521, 44.805850),
                                                                                                            ('Loving the city lights.', 'pic3.jpg', false, NOW(), 2, 'Karađorđeva 65, Novi Sad', 19.842545, 45.255325),
                                                                                                            ('Foodie adventures!', 'pic4.jpg', false, NOW(), 3, 'Vojvode Stepe 130, Niš', 21.895758, 43.321206),
                                                                                                            ('Just finished a run, feeling great!', 'pic5.jpg', false, NOW(), 4, 'Trg slobode 1, Subotica', 19.668652, 46.097435),
                                                                                                            ('Sunset at the beach.', 'pic6.jpg', false, NOW(), 5, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167),
                                                                                                            ('Exploring the countryside.', 'pic7.jpg', false, NOW(), 6, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167),
                                                                                                            ('Weekend getaway in the mountains.', 'pic8.jpg', false, NOW(), 7, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167),
                                                                                                            ('Cozy evening by the fire.', 'pic9.jpg', false, NOW(), 8, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167),
                                                                                                            ('Delicious homemade dinner.', 'pic10.jpg', false, NOW(), 9, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167),
                                                                                                            ('Delicious homemade dinner.', 'pic10.jpg', false, '2024-12-06 13:30:00', 9, 'Cara Dušana 18, Kragujevac', 20.917978, 44.014167);

-- Inserting data into Comment
INSERT INTO comment (text, profile_id, post_id, commented_time, deleted) VALUES
                                                                             ('Great post, Marko!', 2, 1, NOW(), false),
                                                                             ('I love this picture!', 3, 1, NOW(), false),
                                                                             ('Nice place!', 1, 2, NOW(), false),
                                                                             ('Looks delicious!', 1, 4, NOW(), false),
                                                                             ('What a beautiful sunset!', 4, 6, NOW(), false),
                                                                             ('Amazing view!', 5, 7, NOW(), false),
                                                                             ('This looks so relaxing.', 6, 8, NOW(), false),
                                                                             ('Yum! I need this recipe!', 7, 10, NOW(), false),
                                                                             ('So peaceful!', 8, 9, NOW(), false),
                                                                             ('What an adventure!', 9, 5, NOW(), false);

-- Inserting data into Likes (assumed the same as likedPosts)
INSERT INTO likes (profile_id, post_id) VALUES
                                            (1, 1),
                                            (1, 2),
                                            (2, 3),
                                            (3, 4),
                                            (1, 5),
                                            (2, 6),
                                            (3, 7),
                                            (4, 8),
                                            (5, 9),
                                            (6, 10);

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
