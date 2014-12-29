drop table photos;
drop table Spitter;
drop table Spittle;

create table Spittle (
	id integer not null auto_increment unique,
	message varchar(140) not null,
	created_at timestamp not null,
	latitude double,
	longitude double
);

create table Spitter (
	id integer not null auto_increment unique,
	username varchar(20) unique not null,
	password varchar(20) not null,
	first_name varchar(30) not null,
	last_name varchar(30) not null,
	email varchar(30) not null
);

create table photos (
    photo_id integer not null auto_increment unique,
	name varchar(140) not null,
	ext varchar(16) not null,
	original_name varchar(140) not null,
	size int,
	created timestamp,
        primary key (photo_id)
);


