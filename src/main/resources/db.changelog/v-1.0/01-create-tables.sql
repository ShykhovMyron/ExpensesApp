create table types
(
    id   int auto_increment primary key,
    type varchar(255) null,

    constraint unique (type)
);

create table wallet
(
    id      bigint auto_increment primary key,
    balance decimal(19, 2) null,
    budget  decimal(19, 2) null
);

create table user
(
    id        bigint auto_increment primary key,
    enabled   bit null,
    password  varchar(255) null,
    username  varchar(255) null,
    wallet_id bigint null,

    constraint foreign key (wallet_id) references wallet (id)
);

create table expenses
(
    id         bigint auto_increment primary key,
    amount     decimal(19, 2) not null,
    date_added datetime       null,
    user_id    bigint         not null,

    constraint foreign key (user_id) references user (id)
);

create table expenses_types
(
    type_id    int    null,
    expense_id bigint not null primary key,

    constraint foreign key (expense_id) references expenses (id),
    constraint foreign key (type_id) references types (id)
);

create table role
(
    id   bigint       not null,
    role varchar(255) null,
    constraint foreign key (id) references user (id)
);

create table wallet_types
(
    wallet_id bigint not null,
    type_id   int    not null,
    primary key (wallet_id, type_id),
    constraint foreign key (type_id) references types (id),
    constraint foreign key (wallet_id) references wallet (id)
);