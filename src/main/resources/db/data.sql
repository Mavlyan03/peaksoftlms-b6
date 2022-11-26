insert into users(id, email, password, role)
VALUES (1, 'admin@gmail.com', '$2a$12$3A5GynoSX9x.o1Tgq4G1juWOLfL5ozqZqjib1fBYm52BACpVQS.Jm', 'ADMIN'),
       (2, 'instructor@gmail.com', '$2a$12$c2vzSJ1UIpODKy4dLqa50OMKTBnMQM7dx6lexC013ykPi9bJEAq4q', 'INSTRUCTOR'),
       (3, 'student@gmail.com', '$2a$12$MxbOPXg/taPYo/dynHIfsuiKLJefWaZ6CcpruHeMpEqos50a7fr6m', 'STUDENT'),
       (4, 'mavlyan@gmail.com', '$2a$12$9tlaIRbbKDpCjYKBUIoIuu3mUT8fBMlgd1Aqd1sdkUpxf/BPRWNPW', 'STUDENT'),
       (5, 'aiperi@gmail.com', '$$2a$12$IGMz1xjHhhbOahL5ASEvTO4aWxA6ceaJT27uq1F/q5/9J//LUdVIm', 'STUDENT'),
       (6, 'nurmatbek@gmail.com', '$2a$12$YS2goV2TOi4/kvzH1U1yeuZhb7LZWuwUS5E4NiTJ144qbFQNp1goa', 'STUDENT'),
       (7, 'nursultan@gmail.com', '$2a$12$ha29GR7qvop96HpsN.QvDuALIbqP86mUs6rD5/MuwAm1OKmCNnbU.', 'STUDENT'),
       (8,'chyngyz@gmail.com','$2a$12$WVy1fSSd3ylf7kEJ6fJyguO5oVAxeOrpRJgg.eXuqmsIsp3f0X.Tm','INSTRUCTOR');

insert into groups(id, date_of_start, group_description, group_image, group_name)
values (1, '2022/11/10', 'Group Description', 'Group image link', 'Group name'),
       (2, '2022/11/15', 'Second group', 'Image', 'Java 6');

insert into courses(id, course_description, course_image, course_name, date_of_start)
values (1, 'IT language', 'link image', 'Java', '2022/11/10'),
       (2, 'JS', 'Good language', 'JS', '2022/10/10');

insert into groups_courses(group_id, courses_id)
values (1, 1),
       (2, 1),
       (1, 2);

insert into instructors(id, first_name, last_name, phone_number, specialization, user_id)
values (1, 'Instructor name', 'Instructor lastname', '555231245', 'Java developer', 2),
       (2,'Chyngyz','Sharshekeev','4020424','Java developer',8);

insert into instructors_courses(instructors_id, courses_id)
values (1, 1),
       (1, 2);

insert into students(id, first_name, last_name, phone_number, study_format, group_id, user_id)
values (1, 'Student name', 'Student lastname', ' 556545652', 'OFFLINE', 1, 3),
       (2, 'Mavlyan', 'Sadirov', ' 0999123456', 'ONLINE', 1, 4),
       (3, 'Aiperi', 'Dzanyshalieva', ' 0700987654', 'OFFLINE', 1, 5),
       (4, 'Nurmatbek', 'Davranov', ' 0778341287', 'OFFLINE', 2, 6),
       (5, 'Nursultan', 'Kalilov', ' 0556981267', 'OFFLINE', 2, 7);

insert into lessons(id, lesson_name, course_id)
values (1, 'AWS', 1),
       (2, 'S3', 1),
       (3, 'Hibernate', 1),
       (4, 'JDBC', 2),
       (5, 'SQL', 2),
       (6, 'GIT', 2),
       (7, 'Posgres', 2);

insert into links(id, link, link_text, lesson_id)
values (1, 'link', 'The link text', 1),
       (2, 'Video Lesson', 'The link', 2),
       (3, 'link', 'The link text', 3);

insert into presentations(id, presentation_description, presentation_link, presentation_name, lesson_id)
values (1, 'Presentation description', 'link', 'S3', 1),
       (2, 'Presentation description', 'link', 'S3', 4),
       (3, 'Presentation description', 'link', 'S3', 5);

insert into videos(id, video_description, video_link, video_name, lesson_id)
values (1, 'Description', 'link.com', 'Lesson:19', 1),
       (2, 'Description', 'link.com', 'Lesson:19', 2),
       (3, 'Description', 'link.com', 'Lesson:19', 5);

insert into tasks(id, task_name, lesson_id)
values (1, 'Group crud', 1);

insert into contents(id, content_format, content_name, content_value, task_id)
values (1, 'VIDEO', 'video link', 'aws', 1);

insert into tests(id, is_enable, test_name, lesson_id)
values (1, false, 'Java core', 1);

insert into questions(id, question, question_type)
values (1, 'Type of variables?', 'SINGLETON');

insert into options(id, is_true, option_value)
values (1, true, 'String and primitive'),
       (2, false, 'Varchar');

insert into tests_question(test_id,question_id)
values (1,1);

insert into questions_options(question_id,options_id)
values (1,1),
       (1,2);

insert into results(id, date_of_pass, percent, student_id,test_id)
values (1,'2022/11/10', 100, 1,1),
       (2,'2022/12/22',100,4,1);


