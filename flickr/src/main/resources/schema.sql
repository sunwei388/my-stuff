create table Spittle (
	id identity,
	message varchar(140) not null,
	created_at timestamp not null,
	latitude double,
	longitude double
);

create table Spitter (
	id identity,
	username varchar(20) unique not null,
	password varchar(20) not null,
	first_name varchar(30) not null,
	last_name varchar(30) not null,
	email varchar(30) not null
);

create table photos (
	photo_id identity,
	name varchar(140) not null,
	ext varchar(16) not null,
	original_name varchar(140) not null,
	size int,
	created timestamp
);

