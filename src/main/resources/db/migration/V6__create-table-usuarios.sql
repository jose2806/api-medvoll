create table usuarios (
    id bigint not null AUTO_INCREMENT,
    login varchar(100) not null,
    clave varchar(300) not null,

    primary key (id)
);
