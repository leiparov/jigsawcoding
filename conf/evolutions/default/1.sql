# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table grupo_experto (
  grupo_experto_id          bigint auto_increment not null,
  nombre                    varchar(255),
  descripcion               varchar(255),
  maximo_alumnos            integer,
  docente_dni               integer,
  constraint pk_grupo_experto primary key (grupo_experto_id))
;

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


create table grupo_experto_usuario (
  grupo_experto_grupo_experto_id bigint not null,
  usuario_dni                    integer not null,
  constraint pk_grupo_experto_usuario primary key (grupo_experto_grupo_experto_id, usuario_dni))
;
alter table grupo_experto add constraint fk_grupo_experto_docente_1 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_grupo_experto_docente_1 on grupo_experto (docente_dni);
alter table problema add constraint fk_problema_docente_2 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_problema_docente_2 on problema (docente_dni);



alter table grupo_experto_usuario add constraint fk_grupo_experto_usuario_grupo_experto_01 foreign key (grupo_experto_grupo_experto_id) references grupo_experto (grupo_experto_id) on delete restrict on update restrict;

alter table grupo_experto_usuario add constraint fk_grupo_experto_usuario_usuario_02 foreign key (usuario_dni) references usuario (dni) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table grupo_experto;

drop table grupo_experto_usuario;

drop table problema;

drop table usuario;

SET FOREIGN_KEY_CHECKS=1;

