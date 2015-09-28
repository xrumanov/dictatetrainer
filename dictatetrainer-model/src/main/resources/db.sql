create table dt_category (id bigserial not null primary key,	name varchar(25) not null unique);

create table dt_user (id bigserial not null primary key, created_at timestamp not null,	name varchar(40) not null, email varchar(70) not null unique,	password varchar(100) not null,	type varchar(20) not null);

create table dt_user_role (	user_id bigint not null, role varchar(30) not null,	primary key(user_id, role),	constraint fk_user_roles_user foreign key(user_id) references dt_user(id));

insert into dt_user (created_at, name, email, password, type) values(current_timestamp, 'Admin', 'adm@domain.com', 'jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=', 'TEACHER');
insert into dt_user_role (user_id, role) values((select id from dt_user where email = 'adm@domain.com'), 'TEACHER');
insert into dt_user_role (user_id, role) values((select id from dt_user where email = 'adm@domain.com'), 'ADMINISTRATOR');

create table dt_dictate (id bigserial not null primary key,	name varchar(150) not null,	description text not null, category_id bigint not null,	uploader_id bigint not null,	filename varchar(40) not null,	transcript text not null,	constraint fk_dictate_category foreign key(category_id) references dt_category(id),  constraint fk_dictate_uploader foreign key(uploader_id) references dt_user(id));

create table dt_trial (id bigserial not null primary key,	performed_at timestamp not null,	trial_text text not null,	student_id bigint not null,	dictate_id bigint not null, constraint fk_trial_student foreign key(student_id) references dt_user(id),	constraint fk_error_dictate foreign key(dictate_id) references dt_dictate(id));

create table dt_error (dt_trial_id bigint not null,	error_word varchar(0),	correct_word varchar(40),	description text not null,	type varchar(20) not null,	constraint fk_error_trial foreign key(dt_trial_id) references dt_trial(id));