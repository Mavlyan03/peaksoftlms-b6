create table courses
(
    id                 bigint not null
        primary key,
    course_description varchar(10000),
    course_image       varchar(255),
    course_name        varchar(255),
    date_of_start      date
);

create table groups
(
    id                bigint not null
        primary key,
    date_of_start     date,
    group_description varchar(100000),
    group_image       varchar(255),
    group_name        varchar(255)
);

create table courses_group
(
    courses_id bigint not null
        constraint fkrer6lqc2qr8beyn9sjkuko1bo
            references courses,
    group_id   bigint not null
        constraint fkeveitm2t330dim0x0gtqi55k9
            references groups
);

create table users
(
    id       bigint not null
        primary key,
    email    varchar(255),
    password varchar(255),
    role     varchar(255)
);

create table instructors
(
    id             bigint not null
        primary key,
    first_name     varchar(255),
    last_name      varchar(255),
    phone_number   varchar(255),
    specialization varchar(255),
    user_id        bigint
        constraint fkds2m3jgxj98sd5mr1qw23ecjp
            references users
);

create table students
(
    id           bigint not null
        primary key,
    first_name   varchar(255),
    last_name    varchar(255),
    phone_number varchar(255),
    study_format varchar(255),
    group_id     bigint
        constraint fkmsev1nou0j86spuk5jrv19mss
            references groups,
    user_id      bigint
        constraint fkdt1cjx5ve5bdabmuuf3ibrwaq
            references users
);

create table instructors_courses
(
    instructors_id bigint not null
        constraint fk3vd7m03ky1kp4oaqygm0ynrmm
            references instructors,
    courses_id     bigint not null
        constraint fk49jakytuu6knj9mf6n1khlqlo
            references courses
);

create table lessons
(
    id          bigint not null
        primary key,
    lesson_name varchar(255),
    course_id   bigint
        constraint fk17ucc7gjfjddsyi0gvstkqeat
            references courses
);

create table links
(
    id        bigint not null
        primary key,
    link      varchar(255),
    link_text varchar(255),
    lesson_id bigint
        constraint fkjirjvys02hemawim2y7vu6t4n
            references lessons
);

create table presentations
(
    id                       bigint not null
        primary key,
    presentation_description varchar(10000),
    presentation_link        varchar(255),
    presentation_name        varchar(255),
    lesson_id                bigint
        constraint fkbegxylp5clxjks0g69uwa82dw
            references lessons
);

create table videos
(
    id                bigint not null
        primary key,
    video_description varchar(100000),
    video_link        varchar(255),
    video_name        varchar(255),
    lesson_id         bigint
        constraint fkeut3lv873fab335b20mkjx1kn
            references lessons
);

create table tasks
(
    id        bigint not null
        primary key,
    task_name varchar(255),
    lesson_id bigint
        constraint fkij2nuclq3hx0l5tanobfknbpw
            references lessons
);

create table contents
(
    id             bigint not null
        primary key,
    content_format varchar(255),
    content_name   varchar(255),
    content_value  varchar(255),
    task_id        bigint
        constraint fkjgxivmxhrkaxnex5u4xd2b792
            references tasks
);

create table tests
(
    id        bigint not null
        primary key,
    is_enable boolean,
    test_name varchar(255),
    lesson_id bigint
        constraint fka9ekvwmlio4eibo0hcwntdjxf
            references lessons
);

create table questions
(
    id            bigint not null
        primary key,
    question      varchar(255),
    question_type varchar(255)
);

create table tests_question
(
    test_id     bigint not null
        constraint fkajgrkcyqv1gogw8xdmnttcps3
            references tests,
    question_id bigint not null
        constraint uk_29k0qs3i1mryn41790igwh5qb
            unique
        constraint fkckmkg8v91r9xl9f4duquut4ec
            references questions
);

create table options
(
    id           bigint not null
        primary key,
    is_true      boolean,
    option_value varchar(255)
);

create table questions_options
(
    question_id bigint not null
        constraint fkmi5crpqara9iodbjoerxy3up6
            references questions,
    options_id  bigint not null
        constraint uk_ikpxwm3hxr5r2hdofaj6vlj2h
            unique
        constraint fk7boyu2mls3t78taee802fembf
            references options
);

create table results
(
    id           bigint  not null
        primary key,
    date_of_pass date,
    percent      integer not null,
    student_id   bigint
        constraint fkfri0f5doafs6f1ob36de88b6a
            references students,
    test_id      bigint
        constraint fke9uvk96os1lxpp8pf93p13lmv
            references tests
);