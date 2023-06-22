create database backendserver;

create table `user` (
       id bigint not null auto_increment,
        birthdate date,
        email varchar(255),
        name varchar(255),
        pwd varchar(255),
        role varchar(255),
        surname varchar(255),
        primary key (id)
    ) engine=InnoDB;

create table `product` (
       id bigint not null auto_increment,
        brand varchar(255),
        category varchar(255),
        color varchar(255),
        price float,
        stock integer,
        title varchar(255),
        volume float,
        weight float,
        primary key (id)
    );

create table `address` (
       id bigint not null auto_increment,
        apartment varchar(255),
        city varchar(255),
        country varchar(255),
        home varchar(255),
        postal_code varchar(255),
        street varchar(255),
        user_id bigint,
        primary key (id),
        foreign key (user_id) references `user` (id)
    );
create table `order` (
       id bigint not null auto_increment,
        delivery_method varchar(255),
        payment varchar(255),
        user_address bigint,
        user_id bigint,
        primary key (id),
        foreign key (user_address) references address (id),
        foreign key (user_id) references `user` (id)
    );
create table `shopping_cart` (
       user_id bigint not null,
        product_id bigint not null,
        primary key (user_id, product_id),
        foreign key (user_id) references user (id),
        foreign key (product_id) references product (id)
    );

create table `goods` (
       order_id bigint not null,
        product_id bigint not null,
        primary key (order_id, product_id),
        foreign key (product_id) references product (id),
        foreign key (order_id) references `order` (id)
    );
