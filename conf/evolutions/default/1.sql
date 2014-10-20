# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table curso (
  codigo                    integer auto_increment not null,
  nombre                    varchar(255),
  constraint pk_curso primary key (codigo))
;

create table examen (
  id                        bigint auto_increment not null,
  titulo                    varchar(255),
  tiempo_apertura           datetime,
  tiempo_clausura           datetime,
  tiempo_creacion           datetime,
  duracion                  integer,
  sesion_jigsaw_id          integer,
  docente_dni               integer,
  constraint pk_examen primary key (id))
;

create table grupo_experto (
  grupo_experto_id          bigint auto_increment not null,
  nombre                    varchar(255),
  descripcion               varchar(255),
  maximo_alumnos            integer,
  docente_dni               integer,
  constraint pk_grupo_experto primary key (grupo_experto_id))
;

create table par_grupo_experto_problema (
  par_id                    integer auto_increment not null,
  grupo_experto_grupo_experto_id bigint,
  problema_problema_id      bigint,
  sesion_jigsaw_id          integer,
  constraint pk_par_grupo_experto_problema primary key (par_id))
;

create table problema (
  problema_id               bigint auto_increment not null,
  titulo                    varchar(255),
  enunciado                 varchar(255),
  fecha_creacion            datetime,
  docente_dni               integer,
  constraint pk_problema primary key (problema_id))
;

create table problema_examen (
  gen_id                    bigint auto_increment not null,
  examen_id                 bigint not null,
  problema_problema_id      bigint,
  puntaje_favor             integer,
  puntaje_contra            integer,
  constraint pk_problema_examen primary key (gen_id))
;

create table sesion_jigsaw (
  id                        integer auto_increment not null,
  tema                      varchar(255),
  inicio_reunion_expertos   datetime,
  duracion_reunion_expertos integer,
  inicio_reunion_jigsaw     datetime,
  duracion_reunion_jigsaw   integer,
  total_grupos_expertos     integer,
  docente_dni               integer,
  examen_id                 bigint,
  constraint pk_sesion_jigsaw primary key (id))
;

create table tema (
  codigo                    integer auto_increment not null,
  nombre                    varchar(255),
  curso_codigo              integer,
  constraint pk_tema primary key (codigo))
;

create table usuario (
  tipo                      varchar(31) not null,
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
alter table examen add constraint fk_examen_sesionJigsaw_1 foreign key (sesion_jigsaw_id) references sesion_jigsaw (id) on delete restrict on update restrict;
create index ix_examen_sesionJigsaw_1 on examen (sesion_jigsaw_id);
alter table examen add constraint fk_examen_docente_2 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_examen_docente_2 on examen (docente_dni);
alter table grupo_experto add constraint fk_grupo_experto_docente_3 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_grupo_experto_docente_3 on grupo_experto (docente_dni);
alter table par_grupo_experto_problema add constraint fk_par_grupo_experto_problema_grupoExperto_4 foreign key (grupo_experto_grupo_experto_id) references grupo_experto (grupo_experto_id) on delete restrict on update restrict;
create index ix_par_grupo_experto_problema_grupoExperto_4 on par_grupo_experto_problema (grupo_experto_grupo_experto_id);
alter table par_grupo_experto_problema add constraint fk_par_grupo_experto_problema_problema_5 foreign key (problema_problema_id) references problema (problema_id) on delete restrict on update restrict;
create index ix_par_grupo_experto_problema_problema_5 on par_grupo_experto_problema (problema_problema_id);
alter table par_grupo_experto_problema add constraint fk_par_grupo_experto_problema_sesionJigsaw_6 foreign key (sesion_jigsaw_id) references sesion_jigsaw (id) on delete restrict on update restrict;
create index ix_par_grupo_experto_problema_sesionJigsaw_6 on par_grupo_experto_problema (sesion_jigsaw_id);
alter table problema add constraint fk_problema_docente_7 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_problema_docente_7 on problema (docente_dni);
alter table problema_examen add constraint fk_problema_examen_examen_8 foreign key (examen_id) references examen (id) on delete restrict on update restrict;
create index ix_problema_examen_examen_8 on problema_examen (examen_id);
alter table problema_examen add constraint fk_problema_examen_problema_9 foreign key (problema_problema_id) references problema (problema_id) on delete restrict on update restrict;
create index ix_problema_examen_problema_9 on problema_examen (problema_problema_id);
alter table sesion_jigsaw add constraint fk_sesion_jigsaw_docente_10 foreign key (docente_dni) references usuario (dni) on delete restrict on update restrict;
create index ix_sesion_jigsaw_docente_10 on sesion_jigsaw (docente_dni);
alter table sesion_jigsaw add constraint fk_sesion_jigsaw_examen_11 foreign key (examen_id) references examen (id) on delete restrict on update restrict;
create index ix_sesion_jigsaw_examen_11 on sesion_jigsaw (examen_id);
alter table tema add constraint fk_tema_curso_12 foreign key (curso_codigo) references curso (codigo) on delete restrict on update restrict;
create index ix_tema_curso_12 on tema (curso_codigo);



alter table grupo_experto_usuario add constraint fk_grupo_experto_usuario_grupo_experto_01 foreign key (grupo_experto_grupo_experto_id) references grupo_experto (grupo_experto_id) on delete restrict on update restrict;

alter table grupo_experto_usuario add constraint fk_grupo_experto_usuario_usuario_02 foreign key (usuario_dni) references usuario (dni) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table curso;

drop table examen;

drop table grupo_experto;

drop table grupo_experto_usuario;

drop table par_grupo_experto_problema;

drop table problema;

drop table problema_examen;

drop table sesion_jigsaw;

drop table tema;

drop table usuario;

SET FOREIGN_KEY_CHECKS=1;

