/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/4/16 9:21:50                            */
/*==============================================================*/

drop table if exists COMMON_SF;
drop table if exists JOB_CATALOG;
drop table if exists JOB_NOTICE_CATALOG;
drop table if exists JOB_NOTICE_SHARE;
drop table if exists JOB_NOTICE_STATE;
drop table if exists JOB_NOTICE_TYPE;


DROP TABLE IF EXISTS DIC_COMMON_SF;

DROP TABLE IF EXISTS DIC_JOB_CATALOG;

DROP TABLE IF EXISTS DIC_JOB_NOTICE_CATALOG;

DROP TABLE IF EXISTS DIC_JOB_NOTICE_SHARE;

DROP TABLE IF EXISTS DIC_JOB_NOTICE_STATE;

DROP TABLE IF EXISTS DIC_JOB_NOTICE_TYPE;

DROP TABLE IF EXISTS JOB_ATTACHMENT;

DROP TABLE IF EXISTS JOB_CONGRESS;

DROP TABLE IF EXISTS JOB_NOTICE;

DROP TABLE IF EXISTS JOB_PLAN;

DROP TABLE IF EXISTS JOB_PLAN_TASK;

DROP TABLE IF EXISTS JOB_RECORD;

DROP TABLE IF EXISTS JOB_REPORT;

DROP TABLE IF EXISTS JOB_STATISTIC;

/*==============================================================*/
/* Table: DIC_COMMON_SF                                         */
/*==============================================================*/
CREATE TABLE DIC_COMMON_SF
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   NAME                 VARCHAR(50) COMMENT '名称',
   SORT                 INT COMMENT '排序',
   PRIMARY KEY (ID)
);

ALTER TABLE DIC_COMMON_SF COMMENT '通用是否';

/*==============================================================*/
/* Table: DIC_JOB_CATALOG                                       */
/*==============================================================*/
CREATE TABLE DIC_JOB_CATALOG
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   NAME                 VARCHAR(50) COMMENT '名称',
   SORT                 INT COMMENT '排序',
   PRIMARY KEY (ID)
);

ALTER TABLE DIC_JOB_CATALOG COMMENT '心理工作部署--工作类型';

/*==============================================================*/
/* Table: DIC_JOB_NOTICE_CATALOG                                */
/*==============================================================*/
CREATE TABLE DIC_JOB_NOTICE_CATALOG
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   NAME                 VARCHAR(50) COMMENT '名称',
   SORT                 INT COMMENT '排序',
   PRIMARY KEY (ID)
);

ALTER TABLE DIC_JOB_NOTICE_CATALOG COMMENT '心理工作部署--通知公告--通知类型';

/*==============================================================*/
/* Table: DIC_JOB_NOTICE_SHARE                                  */
/*==============================================================*/
CREATE TABLE DIC_JOB_NOTICE_SHARE
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   NAME                 VARCHAR(50) COMMENT '名称',
   SORT                 INT COMMENT '排序',
   PRIMARY KEY (ID)
);

ALTER TABLE DIC_JOB_NOTICE_SHARE COMMENT '心理工作部署--通知公告--共享';

/*==============================================================*/
/* Table: DIC_JOB_NOTICE_STATE                                  */
/*==============================================================*/
CREATE TABLE DIC_JOB_NOTICE_STATE
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   NAME                 VARCHAR(50) COMMENT '名称',
   SORT                 INT COMMENT '排序',
   PRIMARY KEY (ID)
);

ALTER TABLE DIC_JOB_NOTICE_STATE COMMENT '心理工作部署--通知公告--状态';

/*==============================================================*/
/* Table: DIC_JOB_NOTICE_TYPE                                   */
/*==============================================================*/
CREATE TABLE DIC_JOB_NOTICE_TYPE
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   NAME                 VARCHAR(50) COMMENT '名称',
   SORT                 INT COMMENT '排序',
   PRIMARY KEY (ID)
);

ALTER TABLE DIC_JOB_NOTICE_TYPE COMMENT '心理工作部署--通知公告类型';

/*==============================================================*/
/* Table: JOB_ATTACHMENT                                        */
/*==============================================================*/
CREATE TABLE JOB_ATTACHMENT
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   FID                  BIGINT NOT NULL COMMENT '心理工_唯一标示',
   NAME                 VARCHAR(50) COMMENT '名称',
   SAVE_PATH            TEXT COMMENT '保存之后路径',
   MINE_TYPE            VARCHAR(30) COMMENT '附件类型',
   SORT                 INT COMMENT '排序',
   PRIMARY KEY (ID)
);

ALTER TABLE JOB_ATTACHMENT COMMENT '心理工作部署--附件信息';

/*==============================================================*/
/* Table: JOB_CONGRESS                                          */
/*==============================================================*/
CREATE TABLE JOB_CONGRESS
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   JOB_ID               BIGINT COMMENT '心理工_唯一标示',
   EMCEE                VARCHAR(30) COMMENT '主持人',
   PERSONS              TEXT COMMENT '参与人',
   CONTENT              TEXT COMMENT '内容',
   SHARE                INT COMMENT '共享',
   SUBJECT              TEXT COMMENT '主题',
   CONGRESSTIME         DATE COMMENT '会议时间',
   PLACE                VARCHAR(50) COMMENT '地点',
   PRIMARY KEY (ID)
);

ALTER TABLE JOB_CONGRESS COMMENT '心理工作部署--会议记录';

/*==============================================================*/
/* Table: JOB_NOTICE                                            */
/*==============================================================*/
CREATE TABLE JOB_NOTICE
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   NAME                 VARCHAR(50) COMMENT '名称',
   TYPE                 INT COMMENT '类别',
   TITLE                TEXT COMMENT '标题',
   CONTENT              TEXT COMMENT '内容',
   CATALOG              BIGINT COMMENT '工作类别',
   ATTACHMENT           INT COMMENT '是否有附件',
   SHARE                INT COMMENT '共享',
   AUTHOR               BIGINT COMMENT '制定者',
   WRITE_TIME           DATE COMMENT '上传时间',
   DEP                  BIGINT COMMENT '上传者所在部门',
   AUTHOR_ROLE          BIGINT COMMENT '上传者角色',
   MODIFIED             DATE COMMENT '修改时间',
   ADULT_TIME           DATE COMMENT '审核时间',
   STATE                BIGINT COMMENT '状态',
   PRIMARY KEY (ID)
);

ALTER TABLE JOB_NOTICE COMMENT '心理工作部署--通知公告';

/*==============================================================*/
/* Table: JOB_PLAN                                              */
/*==============================================================*/
CREATE TABLE JOB_PLAN
(
   ID                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   TITLE                TEXT COMMENT '标题',
   CATALOG              BIGINT COMMENT '工作类别',
   TARGET               TEXT COMMENT '工作目标',
   EMCEE                VARCHAR(30) COMMENT '主持人',
   CONTENT              TEXT COMMENT '内容',
   START_TIME           DATE COMMENT '开始时间',
   END_TIME             DATE COMMENT '结束时间',
   SHARE                INT COMMENT '共享',
   WRITE_TIME           DATE COMMENT '上传时间',
   DEP                  BIGINT COMMENT '上传者所在部门',
   AUTHOR_ROLE          BIGINT COMMENT '上传者角色',
   YEAR                 INT COMMENT '年份',
   QUARTER              INT COMMENT '季度',
   ADULT_TIME           DATE COMMENT '审核时间',
   AUDITOR              BIGINT COMMENT '审核人',
   VIEW                 TEXT COMMENT '审核意见',
   STATE                BIGINT COMMENT '状态',
   VIP_ENENT            INT COMMENT '是否是大事记',
   PRIMARY KEY (ID)
);

ALTER TABLE JOB_PLAN COMMENT '心理工作部署--工作计划';

/*==============================================================*/
/* Table: JOB_PLAN_TASK                                         */
/*==============================================================*/
CREATE TABLE JOB_PLAN_TASK
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   JOB_ID               BIGINT NOT NULL COMMENT '心理工_唯一标示',
   TITLE                TEXT COMMENT '标题',
   START_TIME           DATE COMMENT '开始时间',
   END_TIME             DATE COMMENT '结束时间',
   PERSONS              TEXT COMMENT '参与人',
   SORT                 INT COMMENT '排序',
   STATE                BIGINT COMMENT '完成状态',
   PRIMARY KEY (ID)
);

ALTER TABLE JOB_PLAN_TASK COMMENT '心理工作部署--工作计划--任务';

/*==============================================================*/
/* Table: JOB_RECORD                                            */
/*==============================================================*/
CREATE TABLE JOB_RECORD
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   JOB_ID               BIGINT COMMENT '心理工_唯一标示',
   PERSONS              TEXT COMMENT '参与人',
   EMCEE                VARCHAR(30) COMMENT '主持人',
   CONTENT              TEXT COMMENT '内容',
   SHARE                INT COMMENT '共享',
   CATALOG              BIGINT COMMENT '工作类别',
   PROCESS              TEXT COMMENT '基本过程',
   SPECIAL_RECORD       TEXT COMMENT '特别记录',
   PRIMARY KEY (ID)
);

ALTER TABLE JOB_RECORD COMMENT '心理工作部署--工作记录';

/*==============================================================*/
/* Table: JOB_REPORT                                            */
/*==============================================================*/
CREATE TABLE JOB_REPORT
(
   ID                   BIGINT NOT NULL COMMENT '唯一标示',
   CONTENT              TEXT COMMENT '内容',
   INI_CONTENT          TEXT COMMENT '初始内容',
   PRIMARY KEY (ID)
);

ALTER TABLE JOB_REPORT COMMENT '心理工作部署--工作总结';

/*==============================================================*/
/* Table: JOB_STATISTIC                                         */
/*==============================================================*/
CREATE TABLE JOB_STATISTIC
(
   ID                   BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
   START_TIME           DATE COMMENT '开始时间',
   END_TIME             DATE COMMENT '结束时间',
   REPORT_TITLE         TEXT COMMENT '报告主题',
   ORG_ID               BIGINT COMMENT '机构名称',
   DEP                  BIGINT COMMENT '部门',
   ROLE                 BIGINT COMMENT '角色',
   CATALOG              BIGINT COMMENT '工作类别',
   REPORTER             BIGINT COMMENT '报告统计者',
   PRIMARY KEY (ID)
);
ALTER TABLE JOB_STATISTIC COMMENT '心理工作部署--汇总统计';
