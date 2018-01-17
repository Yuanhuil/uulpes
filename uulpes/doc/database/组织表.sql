/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/4/11 15:34:03                           */
/*==============================================================*/


drop table if exists organization;

/*==============================================================*/
/* Table: organization                                          */
/*==============================================================*/
create table organization
(
   id                   bigint(20) not null auto_increment comment '主键',
   code                 varchar(20),
   name                 varchar(50),
   parent_id            bigint(20) comment 'level of org:
            public static final int PROV_VALUE = 2;
            public static final int CITY_VALUE = 3;
            public static final int DIST_VALUE = 4;
            public static final int SHCOOL_VALUE = 5;',
   parent_ids           varchar(50) comment 'level of org:
            public static final int PROV_VALUE = 2;
            public static final int CITY_VALUE = 3;
            public static final int DIST_VALUE = 4;
            public static final int SHCOOL_VALUE = 5;',
   org_type             char(1),
   org_level            int(6),
   school_type          int,
   sort                 int,
   attributes           text,
   primary key (id)
);

