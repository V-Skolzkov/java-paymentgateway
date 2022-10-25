create table if not exists t_card_holder
(
    id bigint auto_increment,
    name varchar(200) not null,
    email varchar(100) not null,
    constraint t_card_holder_pk primary key (id)
);

create table if not exists t_card
(
    id bigint auto_increment,
    pan varchar(32) not null,
    expiry varchar(8) not null,
    constraint t_card_pk primary key (id)
);

create table if not exists t_payment
(
    id bigint auto_increment,
    invoice bigint not null,
    amount integer not null,
    currency varchar(3),
    card_holder_id bigint,
    card_id bigint,
    constraint t_payment_pk primary key (id),
    constraint t_payment_uk unique (invoice),
    constraint t_payment_card_holder_fk foreign key (card_holder_id) references t_card_holder (id),
    constraint t_payment_card_fk foreign key (card_id) references t_card (id)
)
