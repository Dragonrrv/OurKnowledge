DROP TABLE verification;
DROP TABLE knowledge;
DROP TABLE uses;
DROP TABLE participation;
DROP TABLE user;
DROP TABLE technology;
DROP TABLE project;

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

create table project
(
    id bigint auto_increment
        primary key,
    name varchar(255) not null,
    description varchar(500) not null,
    startDate date null,
    status varchar(255) null,
    size int null,
    constraint technology_pk
        unique (name)
) ENGINE = InnoDB;

create table user
(
    id bigint auto_increment
        primary key,
    name varchar(255) not null,
    email varchar(255) null,
    role varchar(255) not null,
    startDate date null
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

create table uses
(
    id bigint auto_increment
        primary key,
    projectId bigint not null,
    technologyId bigint not null,
    constraint knowledge_pk
        unique (projectId, technologyId),
    constraint foreign_key_name3
        foreign key (projectId) references project (id) on delete cascade,
    constraint foreign_key_name4
        foreign key (technologyId) references technology (id)
) ENGINE = InnoDB;

create table participation
(
    id bigint auto_increment
        primary key,
    userId bigint not null,
    projectId bigint not null,
    startDate datetime(6) not null,
    endDate datetime(6) null,
    constraint knowledge_pk
        unique (userId, projectId, startDate),
    constraint foreign_key_name5
        foreign key (userId) references user (id),
    constraint foreign_key_name6
        foreign key (projectId) references project (id) on delete cascade
) ENGINE = InnoDB;

create table verification
(
    id bigint auto_increment
        primary key,
    knowledgeId bigint not null,
    projectId bigint not null,
    constraint knowledge_pk
        unique (knowledgeId, projectId),
    constraint foreign_key_name7
        foreign key (knowledgeId) references knowledge (id),
    constraint foreign_key_name8
        foreign key (projectId) references project (id) on delete cascade
) ENGINE = InnoDB;