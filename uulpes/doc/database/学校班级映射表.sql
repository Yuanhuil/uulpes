/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/5/1 16:40:21                            */
/*==============================================================*/


drop table if exists school_class;

/*==============================================================*/
/* Table: school_class                                          */
/*==============================================================*/
create table school_class
(
   bh                   varchar(10) not null comment '班号',
   xxdm                 varchar(10) not null comment '学校代码',
   primary key (bh, xxdm)
);

