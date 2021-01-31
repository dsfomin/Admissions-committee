create table if not exists faculty
(
    id              bigint       auto_increment primary key,
    budget_places   int          null,
    contract_places int          null,
    finalized       bit          null,
    name            varchar(255) null
);

create table if not exists hibernate_sequence
(
    next_val bigint null
);

create table if not exists usr
(
    id       bigint       auto_increment primary key,
    active   bit          null,
    email    varchar(255) null,
    password varchar(255) null
);

create table if not exists message
(
    id      bigint       not null primary key,
    tag     varchar(255) null,
    text    varchar(255) null,
    user_id bigint       null,
    constraint FK70bv6o4exfe3fbrho7nuotopf
        foreign key (user_id) references usr (id)
);

create table if not exists user_faculty
(
    user_id    bigint not null,
    faculty_id bigint not null,
    primary key (user_id, faculty_id),
    constraint FKb8mhj0j869ux4mkx1frx526ex
        foreign key (faculty_id) references faculty (id),
    constraint FKhsn4b9em21xec36potri1epoj
        foreign key (user_id) references usr (id)
);

create table if not exists user_role
(
    user_id bigint       not null,
    roles   varchar(255) null,
    constraint FKfpm8swft53ulq2hl11yplpr5
        foreign key (user_id) references usr (id)
);

insert into usr(active, email, password) values (true, 'dsfomin2@gmail.com', '$2a$10$SMXmHd8dVdQRgb.F3OqBa.6E3Xf.G1oeeeEvIlw29tQSavrjBxx5C');
insert into usr(active, email, password) values (true, 'user1', '$2a$10$AijDQRZQAyMn9QKGg6LTpexmUfow.W4LGB2F6dh.qS18y5zeCuWVG');

insert into user_role(user_id, roles) values (1, 'USER');
insert into user_role(user_id, roles) values (1, 'ADMIN');
insert into user_role(user_id, roles) values (2, 'USER');

insert into faculty (budget_places, contract_places, finalized, name) values (10, 15, false, 'Faculty1');
insert into faculty (budget_places, contract_places, finalized, name) values (20, 45, false, 'Faculty2');
insert into faculty (budget_places, contract_places, finalized, name) values (30, 15, false, 'Faculty3');
insert into faculty (budget_places, contract_places, finalized, name) values (40, 35, false, 'Faculty4');
insert into faculty (budget_places, contract_places, finalized, name) values (50, 25, false, 'Faculty5');
insert into faculty (budget_places, contract_places, finalized, name) values (15, 15, false, 'Faculty6');
insert into faculty (budget_places, contract_places, finalized, name) values (25, 25, false, 'Faculty7');
insert into faculty (budget_places, contract_places, finalized, name) values (35, 13, false, 'Faculty8');
insert into faculty (budget_places, contract_places, finalized, name) values (45, 28, false, 'Faculty9');
insert into faculty (budget_places, contract_places, finalized, name) values (7, 35, false, 'Faculty10');
insert into faculty (budget_places, contract_places, finalized, name) values (16, 45, false, 'Faculty11');
insert into faculty (budget_places, contract_places, finalized, name) values (18, 25, false, 'Faculty12');
insert into faculty (budget_places, contract_places, finalized, name) values (13, 11, false, 'Faculty13');
insert into faculty (budget_places, contract_places, finalized, name) values (11, 23, false, 'Faculty14');
insert into faculty (budget_places, contract_places, finalized, name) values (28, 38, false, 'Faculty15');
insert into faculty (budget_places, contract_places, finalized, name) values (31, 15, false, 'Faculty16');
