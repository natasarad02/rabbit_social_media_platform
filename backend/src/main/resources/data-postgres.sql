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

INSERT INTO location (longitude, latitude, address, number, deleted) VALUES
                                                                         (-74.0060, 40.7128, 'New York City', '1', false),
                                                                         (-73.9352, 40.7306, 'Brooklyn', '2', false),
                                                                         (-122.4194, 37.7749, 'San Francisco', '3', false),
                                                                         (-118.2437, 34.0522, 'Los Angeles', '4', false);
INSERT INTO profile (email, password, name, surname, deleted, role) VALUES
                                                                        ('marko@example.com', 'password123', 'Marko', 'Marković', false, 'User'),
                                                                        ('milan@example.com', 'password456', 'Milan', 'Milanović', false, 'User'),
                                                                        ('ivana@example.com', 'password789', 'Ivana', 'Ivanović', false, 'Administrator'),
                                                                        ('bojan@example.com', 'password101', 'Bojan', 'Bojanović', false, 'Administrator');

INSERT INTO post (description, picture, deleted, posted_time, profile_id, location_id) VALUES
                                                                                        ('This is my first post!', 'pic1.jpg', false, NOW(), 1, 1),
                                                                                        ('Beautiful day at the park!', 'pic2.jpg', false, NOW(), 1, 2),
                                                                                        ('Loving the city lights.', 'pic3.jpg', false, NOW(), 2, 1),
                                                                                        ('Foodie adventures!', 'pic4.jpg', false, NOW(), 3, 3);

-- Inserting data into Comment
INSERT INTO comment (text, profile_id, post_id, commented_time, deleted) VALUES
                                                                          ('Great post, Marko!', 2, 1, NOW(), false),
                                                                          ('I love this picture!', 3, 1, NOW(), false),
                                                                          ('Nice place!', 1, 2, NOW(), false),
                                                                          ('Looks delicious!', 1, 4, NOW(), false);

-- Inserting data into Likes (assumed the same as likedPosts)
INSERT INTO likes (profile_id, post_id) VALUES
                                         (1, 1),
                                         (1, 2),
                                         (2, 3),
                                         (3, 4);

INSERT INTO profile_following (profile_id, followed_profile_id) VALUES
                                                                    (1, 2),  -- Marko is following Milan
                                                                    (1, 3),  -- Marko is following Ivana
                                                                    (2, 3),  -- Milan is following Ivana
                                                                    (3, 4)