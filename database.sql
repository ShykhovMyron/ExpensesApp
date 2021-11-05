

drop database if exists application;
create database application;
use application;
drop table if exists hibernate_sequence;
drop table if exists purchases;
drop table if exists role;
drop table if exists user;
create table hibernate_sequence
(
    next_val bigint
) engine=InnoDB;
 insert into hibernate_sequence values ( 1 );
create table purchases
(
    id      integer not null,
    amount  bigint,
    type    varchar(50),
    user_id integer not null,
    primary key (id)
) engine=InnoDB;
create table role
(
    id   integer not null,
    role varchar(50)
) engine=InnoDB;
create table user
(
    id       integer not null,
    enabled  bit,
    password varchar(50),
    username varchar(30),
    budget long not null ,
    primary key (id)
) engine=InnoDB;
alter table purchases
    add constraint purchases_ibfk_1 foreign key (user_id) references user (id);
alter table role
    add constraint role_ibfk_1 foreign key (id) references user (id);
