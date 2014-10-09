# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table curso (
  codigo                    integer auto_increment not null,
  nombre                    varchar(255),
  constraint pk_curso primary key (codigo))
;

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

create table sesion_jigsaw (
  id                        integer auto_increment not null,
  docente_dni               integer not null,
  curso_codigo              integer,
  tema_codigo               integer,
  inicio_reunion_expertos   datetime,
  duracion_reunion_expertos integer,
  inicio_reunion_jigsaw     datetime,
  duracion_reunion_jigsaw   integer,
  total_grupos_expertos     integer,
  constraint pk_sesion_jigsaw primary key (id))
;

create table tema (
  codigo                    integer auto_increment not null,
  nombre                    varchar(255),
  curso_codigo              integer,
  constraint pk_tema primary key (codigo))
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
alter table sesion_jigsaw add constraint fk_sesion_jigsaw_usuario_3 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_sesion_jigsaw_usuario_3 on sesion_jigsaw (docente_dni);
alter table sesion_jigsaw add constraint fk_sesion_jigsaw_curso_4 foreign key (curso_codigo) references curso (codigo) on delete restrict on update restrict;
create index ix_sesion_jigsaw_curso_4 on sesion_jigsaw (curso_codigo);
alter table sesion_jigsaw add constraint fk_sesion_jigsaw_tema_5 foreign key (tema_codigo) references tema (codigo) on delete restrict on update restrict;
create index ix_sesion_jigsaw_tema_5 on sesion_jigsaw (tema_codigo);
alter table tema add constraint fk_tema_curso_6 foreign key (curso_codigo) references curso (codigo) on delete restrict on update restrict;
create index ix_tema_curso_6 on tema (curso_codigo);



alter table grupo_experto_usuario add constraint fk_grupo_experto_usuario_grupo_experto_01 foreign key (grupo_experto_grupo_experto_id) references grupo_experto (grupo_experto_id) on delete restrict on update restrict;

alter table grupo_experto_usuario add constraint fk_grupo_experto_usuario_usuario_02 foreign key (usuario_dni) references usuario (dni) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table curso;

drop table grupo_experto;

drop table grupo_experto_usuario;

drop table problema;

drop table sesion_jigsaw;

drop table tema;

drop table usuario;

SET FOREIGN_KEY_CHECKS=1;

