DROP TABLE knowledge;
DROP TABLE user;
DROP TABLE technology;

create table technology
(
    id bigint auto_increment
        primary key,
    relevant bit default b'0' not null,
    name varchar(255) not null,
    parentId bigint null,
    constraint technology_pk
        unique (name, parentId)
) ENGINE = InnoDB;

create table user
(
    id bigint auto_increment
        primary key,
    name varchar(255) null,
    email varchar(255) null,
    password varchar(255) null,
    role varchar(255) null,
    startDate datetime(6) null
) ENGINE = InnoDB;

create table knowledge
(
    id bigint auto_increment
        primary key,
    userId bigint not null,
    technologyId bigint not null,
    likeIt bit default b'0' not null,
    mainSkill bit default b'0' not null,
    constraint knowledge_pk
        unique (userId, technologyId),
    constraint foreign_key_name
        foreign key (userId) references user (id),
    constraint foreign_key_name2
        foreign key (technologyId) references technology (id)
) ENGINE = InnoDB;