/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/4/18 18:02:40                           */
/*==============================================================*/


drop table if exists dic_rolelevel;

drop table if exists role;

/*==============================================================*/
/* Table: dic_rolelevel                                         */
/*==============================================================*/
create table dic_rolelevel
(
   id                   int(10) unsigned not null auto_increment,
   rolelevelname        national varchar(45) not null,
   primary key (id)
);

alter table dic_rolelevel comment '½ÇÉ«¼¶±ð';

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   id                   bigint(20) unsigned not null auto_increment,
   role_name            national varchar(45) not null,
   role                 national varchar(45) not null,
   role_desc            national varchar(45) not null,
   is_show              national char(1) not null,
   org_level            int(10),
   role_level           int(10),
   primary key (id)
);

