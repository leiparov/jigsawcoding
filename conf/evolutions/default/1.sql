# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table problema (
  problema_id               bigint auto_increment not null,
  titulo                    varchar(255),
  enunciado                 varchar(255),
  fecha_creacion            datetime,
  docente_dni               integer,
  constraint pk_problema primary key (problema_id))
;

create table usuario (
  dtype                     varchar(10) not null,
  dni                       integer auto_increment not null,
  email                     varchar(255),
  password                  varchar(255),
  inhabilitado              tinyint(1) default 0,
  nombres                   varchar(255),
  apellido_paterno          varchar(255),
  apellido_materno          varchar(255),
  sexo                      integer,
  constraint ck_usuario_sexo check (sexo in (0,1)),
  constraint pk_usuario primary key (dni))
;

alter table problema add constraint fk_problema_docente_1 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_problema_docente_1 on problema (docente_dni);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table problema;

drop table usuario;

SET FOREIGN_KEY_CHECKS=1;

