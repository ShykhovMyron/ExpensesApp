create table types
(
    id   int auto_increment
        primary key,
    type varchar(255) null,
    constraint UK_4tchu0t1cxvps06xylvdhu0ik
        unique (type)
);

create table wallet
(
    id      bigint auto_increment
        primary key,
    balance decimal(19, 2) null,
    budget  decimal(19, 2) null
);

create table user
(
    id        bigint auto_increment
        primary key,
    enabled   bit          null,
    password  varchar(255) null,
    username  varchar(255) null,
    wallet_id bigint       null,
    constraint FKposytpmub7fhua33j3ts2jot6
        foreign key (wallet_id) references wallet (id)
);

create table expenses
(
    id         bigint auto_increment
        primary key,
    amount     decimal(19, 2) not null,
    date_added datetime           null,
    user_id    bigint         not null,
    constraint FK2qife4sxyeoi1kwgvg769ks8y
        foreign key (user_id) references user (id)
);

create table expenses_types
(
    type_id    int    null,
    expense_id bigint not null
        primary key,
    constraint FKayq9iybhki2axs33m8avi20pd
        foreign key (expense_id) references expenses (id),
    constraint FKnc2nyq9jd4jmfy93ri9k6x2aq
        foreign key (type_id) references types (id)
);

create table role
(
    id   bigint       not null,
    role varchar(255) null,
    constraint FKtmhh8ov8crluh7nn3uxdi841j
        foreign key (id) references user (id)
);

create table wallet_types
(
    wallet_id bigint not null,
    type_id   int    not null,
    primary key (wallet_id, type_id),
    constraint FK1o6rag4w9ulj0u6917lw66a2d
        foreign key (type_id) references types (id),
    constraint FKnloah2g8gt644vwxapph5qgcl
        foreign key (wallet_id) references wallet (id)
);