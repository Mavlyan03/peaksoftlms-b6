drop table if exists users;
drop table if exists groups;
drop table if exists courses;
drop table if exists groups_courses;
drop table if exists instructors;
drop table if exists instructors_courses;
drop table if exists students;
drop table if exists lessons;
drop table if exists links;
drop table if exists presentations;
drop table if exists videos;
drop table if exists tasks;
drop table if exists contents;
drop table if exists tests;
drop table if exists questions;
drop table if exists options;
drop table if exists tests_question;
drop table if exists questions_options;
create sequence if not exists user_seq;
create sequence if not exists group_seq;
create sequence if not exists course_seq;
create sequence if not exists instructor_seq;
create sequence if not exists student_seq;
create sequence if not exists lesson_seq;
create sequence if not exists link_seq;
create sequence if not exists presentation_seq;
create sequence if not exists video_seq;
create sequence if not exists task_seq;
create sequence if not exists content_seq;
create sequence if not exists test_seq;
create sequence if not exists question_seq;
create sequence if not exists option_seq;
create sequence if not exists result_seq;

create table users
(
    id          bigserial not null
        primary key,
    email    varchar(255),
    password varchar(255),
    role     varchar(255)
);

create table groups
(
    id          bigserial not null
        primary key,
    date_of_start     date,
    group_description varchar(100000),
    group_image       varchar(255),
    group_name        varchar(255)
);

create table courses
(
    id           bigserial not null
        primary key,
    course_description varchar(10000),
    course_image       varchar(255),
    course_name        varchar(255),
    date_of_start      date
);

create table groups_courses
(
    group_id   bigserial not null
        constraint fkb5nvrwxye8n0ct77q8s3war1x
            references groups,
    courses_id bigserial not null
        constraint fkhevyqbh4w8xk28d2d3s5ba5f7
            references courses
);

create table instructors
(
    id            bigserial not null
        primary key,
    first_name     varchar(255),
    last_name      varchar(255),
    phone_number   varchar(255),
    specialization varchar(255),
    user_id        bigserial
        constraint fkds2m3jgxj98sd5mr1qw23ecjp
            references users
);

create table instructors_courses
(
    instructors_id bigserial not null
        constraint fk3vd7m03ky1kp4oaqygm0ynrmm
            references instructors,
    courses_id     bigserial not null
        constraint fk49jakytuu6knj9mf6n1khlqlo
            references courses
);

create table students
(
    id           bigserial not null
        primary key,
    first_name   varchar(255),
    last_name    varchar(255),
    phone_number varchar(255),
    study_format varchar(255),
    group_id      bigserial
        constraint fkmsev1nou0j86spuk5jrv19mss
            references groups,
    user_id      bigserial
        constraint fkdt1cjx5ve5bdabmuuf3ibrwaq
            references users
);

create table lessons
(
    id          bigserial not null
        primary key,
    lesson_name varchar(255),
    course_id   bigserial
        constraint fk17ucc7gjfjddsyi0gvstkqeat
            references courses
);

create table links
(
    id          bigserial not null
        primary key,
    link      varchar(255),
    link_text varchar(255),
    lesson_id bigserial
        constraint fkjirjvys02hemawim2y7vu6t4n
            references lessons
);

create table presentations
(
    id           bigserial not null
        primary key,
    presentation_description varchar(10000),
    presentation_link        varchar(255),
    presentation_name        varchar(255),
    lesson_id                bigserial
        constraint fkbegxylp5clxjks0g69uwa82dw
            references lessons
);

create table videos
(
    id            bigserial not null
        primary key,
    video_description varchar(100000),
    video_link        varchar(255),
    video_name        varchar(255),
    lesson_id         bigserial
        constraint fkeut3lv873fab335b20mkjx1kn
            references lessons
);

create table tasks
(
    id          bigserial not null
        primary key,
    task_name varchar(255),
    lesson_id bigserial
        constraint fkij2nuclq3hx0l5tanobfknbpw
            references lessons
);

create table contents
(
    id            bigserial not null
        primary key,
    content_format varchar(255),
    content_name   varchar(255),
    content_value  varchar(255),
    task_id        bigserial
        constraint fkjgxivmxhrkaxnex5u4xd2b792
            references tasks
);

create table tests
(
    id        bigserial not null
        primary key,
    is_enable boolean,
    test_name varchar(255),
    lesson_id bigserial
        constraint fka9ekvwmlio4eibo0hcwntdjxf
            references lessons
);

create table questions
(
    id            bigserial not null
        primary key,
    question      varchar(255),
    question_type varchar(255)
);

create table options
(
    id           bigserial not null
        primary key,
    is_true      boolean,
    option_value varchar(255)
);

create table tests_question
(
    test_id     bigserial not null
        constraint fkajgrkcyqv1gogw8xdmnttcps3
            references tests,
    question_id bigserial not null
        constraint uk_29k0qs3i1mryn41790igwh5qb
            unique
        constraint fkckmkg8v91r9xl9f4duquut4ec
            references questions
);

create table questions_options
(
    question_id bigserial not null
        constraint fkmi5crpqara9iodbjoerxy3up6
            references questions,
    options_id  bigserial not null
        constraint uk_ikpxwm3hxr5r2hdofaj6vlj2h
            unique
        constraint fk7boyu2mls3t78taee802fembf
            references options
);

create table results
(
    id           bigserial  not null
        primary key,
    date_of_pass date,
    percent      integer not null,
    student_id   bigserial
        constraint fkfri0f5doafs6f1ob36de88b6a
            references students,
    test_id      bigserial
        constraint fke9uvk96os1lxpp8pf93p13lmv
            references tests
);
