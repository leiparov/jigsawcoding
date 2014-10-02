# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table usuario (
  dtype                     varchar(10) not null,
  dni                       integer auto_increment not null,
  email                     varchar(255),
  password                  varchar(255),
  inhabilitado              tinyint(1) default 0,
  nombre                    varchar(255),
  apellido                  varchar(255),
  sexo                      integer,
  constraint ck_usuario_sexo check (sexo in (0,1)),
  constraint pk_usuario primary key (dni))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table usuario;

SET FOREIGN_KEY_CHECKS=1;

