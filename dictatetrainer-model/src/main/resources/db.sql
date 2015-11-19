create table dt_category (id bigserial not null primary key,	name varchar(25) not null unique);


create table dt_user (id bigserial not null primary key, created_at timestamp not null,	name varchar(40) not null,
email varchar(70) not null unique,	password varchar(100) not null,	type varchar(20) not null);

create table dt_user_role (	user_id bigint not null, role varchar(30) not null,	primary key(user_id, role),
constraint fk_user_roles_user foreign key(user_id) references dt_user(id));


create table dt_dictate (id bigserial not null primary key,	name varchar(150) not null,	description varchar not null,
category_id bigint not null,	uploader_id bigint not null,	filename varchar(40) not null,	transcript varchar not null,
rep_dictate int not null,	rep_sentences int not null,	pause_sentences int not null,	sentence_endings varchar not null,
constraint fk_dictate_category foreign key(category_id) references dt_category(id),
constraint fk_dictate_uploader foreign key(uploader_id) references dt_user(id));


create table dt_trial (id bigserial not null primary key,	performed_at timestamp not null,
trial_text varchar not null,	student_id bigint not null,	dictate_id bigint not null,
constraint fk_trial_student foreign key(student_id) references dt_user(id),
constraint fk_trial_dictate foreign key(dictate_id) references dt_dictate(id));

create table dt_error (id bigserial not null primary key,	char_position int not null, correct_chars varchar(10) not null,
written_chars varchar(10) not null, correct_word varchar not null, written_word varchar(40), previous_word varchar(40),
next_word varchar(40), word_position int not null, lemma varchar(35) not null, pos_tag varchar(15) not null,
sentence varchar(35) not null, error_priority int not null, error_type VARCHAR(30) not null, description text not null,
student_id bigint not null, dictate_id bigint not null, trial_id bigint not null,
constraint fk_err_student foreign key(student_id) references dt_user(id),
constraint fk_err_dictate foreign key(dictate_id) references dt_dictate(id),
constraint fk_err_trial foreign key(trial_id) references dt_trial(id));


-- create default table rows
insert into dt_user (created_at, name, email, password, type)
values(current_timestamp, 'Admin', 'adm@domain.com', 'jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=', 'TEACHER');

insert into dt_user (created_at, name, email, password, type)
values(current_timestamp, 'Josef Prasek', 'pepaprasek@gmail.com', 'jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=', 'STUDENT');

insert into dt_user_role (user_id, role) values((select id from dt_user where email = 'pepaprasek@gmail.com'), 'STUDENT');
insert into dt_user_role (user_id, role) values((select id from dt_user where email = 'adm@domain.com'), 'TEACHER');
insert into dt_user_role (user_id, role) values((select id from dt_user where email = 'adm@domain.com'), 'ADMINISTRATOR');

insert into dt_category (id, name) values(1, 'Vybrané slová');

insert into dt_dictate (id, name, description, category_id, uploader_id, filename, transcript, rep_dictate, rep_sentences, pause_sentences, sentence_endings)
values(1, 'sample', 'This is sample dictate', 1, 1, 'sample.ogg', 'Som dobrý.', 2, 2, 2, '4');