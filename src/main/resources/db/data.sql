insert into users(id, email, password, role)
VALUES (1, 'admin@gmail.com', '$2a$12$3A5GynoSX9x.o1Tgq4G1juWOLfL5ozqZqjib1fBYm52BACpVQS.Jm', 'ADMIN'),
       (2, 'instructor@gmail.com', '$2a$12$c2vzSJ1UIpODKy4dLqa50OMKTBnMQM7dx6lexC013ykPi9bJEAq4q', 'INSTRUCTOR'),
       (3, 'student@gmail.com', '$2a$12$MxbOPXg/taPYo/dynHIfsuiKLJefWaZ6CcpruHeMpEqos50a7fr6m', 'STUDENT');

insert into groups(id, date_of_start, group_description, group_image, group_name)
values (1, '2022/11/10', 'Group Description', 'Group image link', 'Group name');

insert into courses(id, course_description, course_image, course_name, date_of_start)
values (1, 'IT language', 'link image', 'Java', '2022/11/10');

insert into instructors(id, first_name, last_name, phone_number, specialization, user_id)
values (1, 'Instructor name', 'Instructor lastname', '555231245', 'Java developer', 2);

insert into students(id, first_name, last_name, phone_number, study_format, group_id, user_id)
values (1, 'Student name', 'Student lastname', ' 556545652', 'OFFLINE', 1, 3);

insert into lessons(id, lesson_name, course_id)
values (1, 'AWS', 1),
       (2,'Docker',1);

insert into links(id, link, link_text, lesson_id)
values (1, 'link', 'The link text', 1),
       (2,'spring','spring text',2);

insert into presentations(id, presentation_description, presentation_link, presentation_name, lesson_id)
values (1, 'Presentation description', 'link', 'S3', 1);

insert into videos(id, video_description, video_link, video_name, lesson_id)
values (1, 'Description', 'link.com', 'Lesson:19', 1);

insert into tasks(id, task_name, lesson_id)
values (1, 'Group crud', 1);

insert into contents(id, content_format, content_name, content_value, task_id)
values (1, 'VIDEO', 'video link', 'aws', 1);

insert into tests(id, is_enable, test_name, lesson_id)
values (1, false, 'Java core', 1);

insert into questions(id, question, question_type, test_id)
values (1, 'Type of variables?', 'SINGLETON', 1);

insert into options(id, is_true, option_value, question_id)
values (1, true, 'String and primitive', 1),
       (2, false, 'Varchar', 1);

insert into results(id, date_of_pass, percent, student_id, test_id)
values (1, '2022/11/10', 100, 1, 1);