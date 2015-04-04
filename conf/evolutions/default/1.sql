# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table examen (
  id                        integer not null,
  titulo                    varchar(255),
  tiempo_apertura           timestamp,
  tiempo_clausura           timestamp,
  tiempo_creacion           timestamp,
  duracion                  integer,
  docente_dni               integer,
  constraint pk_examen primary key (id))
;

create table grupo (
  tipo                      varchar(31) not null,
  id                        integer not null,
  nombre                    varchar(255),
  descripcion               varchar(255),
  maximo_alumnos            integer,
  sesion_jigsaw_id          integer,
  problema_id               integer,
  constraint pk_grupo primary key (id))
;

create table nota_alumno (
  id                        integer not null,
  alumno_dni                integer,
  examen_id                 integer,
  nota                      integer,
  constraint pk_nota_alumno primary key (id))
;

create table problema (
  id                        integer not null,
  titulo                    varchar(255),
  enunciado                 varchar(255),
  fecha_creacion            timestamp,
  docente_dni               integer,
  constraint pk_problema primary key (id))
;

create table problema_examen (
  id                        integer not null,
  problema_id               integer,
  examen_id                 integer,
  puntaje_favor             integer,
  puntaje_contra            integer,
  constraint pk_problema_examen primary key (id))
;

create table respuesta_alumno (
  id                        integer not null,
  problema_examen_id        integer,
  alumno_dni                integer,
  ideone_link               varchar(255),
  puntaje_obtenido          integer,
  constraint pk_respuesta_alumno primary key (id))
;

create table sesion_jigsaw (
  id                        integer not null,
  tema                      varchar(255),
  inicio_reunion_expertos   timestamp,
  duracion_reunion_expertos integer,
  inicio_reunion_jigsaw     timestamp,
  duracion_reunion_jigsaw   integer,
  tiempo_apertura_examen    timestamp,
  tiempo_clausura_examen    timestamp,
  duracion_examen           integer,
  total_grupos_expertos     integer,
  docente_dni               integer,
  examen_id                 integer,
  constraint pk_sesion_jigsaw primary key (id))
;

create table usuario (
  tipo                      varchar(31) not null,
  dni                       integer not null,
  email                     varchar(255),
  password                  varchar(255),
  inhabilitado              boolean,
  nombres                   varchar(255),
  apellido_paterno          varchar(255),
  apellido_materno          varchar(255),
  sexo                      integer,
  constraint ck_usuario_sexo check (sexo in (0,1)),
  constraint pk_usuario primary key (dni))
;


create table grupo_usuario (
  grupo_id                       integer not null,
  usuario_dni                    integer not null,
  constraint pk_grupo_usuario primary key (grupo_id, usuario_dni))
;

create table grupo_problema (
  grupo_id                       integer not null,
  problema_id                    integer not null,
  constraint pk_grupo_problema primary key (grupo_id, problema_id))
;

create table sesion_jigsaw_usuario (
  sesion_jigsaw_id               integer not null,
  usuario_dni                    integer not null,
  constraint pk_sesion_jigsaw_usuario primary key (sesion_jigsaw_id, usuario_dni))
;
create sequence examen_seq;

create sequence grupo_seq;

create sequence nota_alumno_seq;

create sequence problema_seq;

create sequence problema_examen_seq;

create sequence respuesta_alumno_seq;

create sequence sesion_jigsaw_seq;

create sequence usuario_seq;

alter table examen add constraint fk_examen_docente_1 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_examen_docente_1 on examen (docente_dni);
alter table grupo add constraint fk_grupo_sesionJigsaw_2 foreign key (sesion_jigsaw_id) references sesion_jigsaw (id) on delete restrict on update restrict;
create index ix_grupo_sesionJigsaw_2 on grupo (sesion_jigsaw_id);
alter table grupo add constraint fk_grupo_sesionJigsaw_3 foreign key (sesion_jigsaw_id) references sesion_jigsaw (id) on delete restrict on update restrict;
create index ix_grupo_sesionJigsaw_3 on grupo (sesion_jigsaw_id);
alter table grupo add constraint fk_grupo_problema_4 foreign key (problema_id) references problema (id) on delete restrict on update restrict;
create index ix_grupo_problema_4 on grupo (problema_id);
alter table nota_alumno add constraint fk_nota_alumno_alumno_5 foreign key (alumno_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_nota_alumno_alumno_5 on nota_alumno (alumno_dni);
alter table nota_alumno add constraint fk_nota_alumno_examen_6 foreign key (examen_id) references examen (id) on delete restrict on update restrict;
create index ix_nota_alumno_examen_6 on nota_alumno (examen_id);
alter table problema add constraint fk_problema_docente_7 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_problema_docente_7 on problema (docente_dni);
alter table problema_examen add constraint fk_problema_examen_problema_8 foreign key (problema_id) references problema (id) on delete restrict on update restrict;
create index ix_problema_examen_problema_8 on problema_examen (problema_id);
alter table problema_examen add constraint fk_problema_examen_examen_9 foreign key (examen_id) references examen (id) on delete restrict on update restrict;
create index ix_problema_examen_examen_9 on problema_examen (examen_id);
alter table respuesta_alumno add constraint fk_respuesta_alumno_problemaE_10 foreign key (problema_examen_id) references problema_examen (id) on delete restrict on update restrict;
create index ix_respuesta_alumno_problemaE_10 on respuesta_alumno (problema_examen_id);
alter table respuesta_alumno add constraint fk_respuesta_alumno_alumno_11 foreign key (alumno_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_respuesta_alumno_alumno_11 on respuesta_alumno (alumno_dni);
alter table sesion_jigsaw add constraint fk_sesion_jigsaw_docente_12 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_sesion_jigsaw_docente_12 on sesion_jigsaw (docente_dni);
alter table sesion_jigsaw add constraint fk_sesion_jigsaw_examen_13 foreign key (examen_id) references examen (id) on delete restrict on update restrict;
create index ix_sesion_jigsaw_examen_13 on sesion_jigsaw (examen_id);



alter table grupo_usuario add constraint fk_grupo_usuario_grupo_01 foreign key (grupo_id) references grupo (id) on delete restrict on update restrict;

alter table grupo_usuario add constraint fk_grupo_usuario_usuario_02 foreign key (usuario_dni) references usuario (dni) on delete restrict on update restrict;

alter table grupo_problema add constraint fk_grupo_problema_grupo_01 foreign key (grupo_id) references grupo (id) on delete restrict on update restrict;

alter table grupo_problema add constraint fk_grupo_problema_problema_02 foreign key (problema_id) references problema (id) on delete restrict on update restrict;

alter table sesion_jigsaw_usuario add constraint fk_sesion_jigsaw_usuario_sesi_01 foreign key (sesion_jigsaw_id) references sesion_jigsaw (id) on delete restrict on update restrict;

alter table sesion_jigsaw_usuario add constraint fk_sesion_jigsaw_usuario_usua_02 foreign key (usuario_dni) references usuario (dni) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists examen;

drop table if exists grupo;

drop table if exists nota_alumno;

drop table if exists problema;

drop table if exists problema_examen;

drop table if exists respuesta_alumno;

drop table if exists sesion_jigsaw;

drop table if exists sesion_jigsaw_usuario;

drop table if exists usuario;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists examen_seq;

drop sequence if exists grupo_seq;

drop sequence if exists nota_alumno_seq;

drop sequence if exists problema_seq;

drop sequence if exists problema_examen_seq;

drop sequence if exists respuesta_alumno_seq;

drop sequence if exists sesion_jigsaw_seq;

drop sequence if exists usuario_seq;

