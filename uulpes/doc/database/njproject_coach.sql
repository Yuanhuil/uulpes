/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/4/26 17:27:09                           */
/*==============================================================*/







drop table if exists common_attachment;

/*==============================================================*/
/* Table: common_attachment     文件附件表公用                                */
/*==============================================================*/
create table common_attachment
(
   id                   bigint not null comment '唯一标示',
   name                 varchar(50) comment '名称',
   save_path            text comment '保存之后路径',
   mine_type            varchar(30) comment '附件类型',
   save_name            varchar(100),
   primary key (id)
);

alter table common_attachment comment '附件信息,包含文档和图片信息';










drop table if exists COACH_CONSULT_TYPE;

drop table if exists COACH_INTRODUCE;

drop table if exists COACH_INTRODUCE_JOBTIME;

/*==============================================================*/
/* Table: COACH_ATTACHMENT                                      */
/*==============================================================*/
drop table if exists COACH_ATTACHMENT;


create table COACH_ATTACHMENT
(
   id                   bigint not null comment '唯一标示',
   sort                 int comment '排序',
   fid                  bigint comment '外表id',
   fileid               bigint comment '文件id',
   primary key (id)
);

alter table COACH_ATTACHMENT comment '心理工作部署-附件信息,包含文档和图片信息';


/*==============================================================*/
/* Table: COACH_CONSULT_TYPE                                    */
/*==============================================================*/
create table COACH_CONSULT_TYPE
(
   id                   bigint not null comment '唯一标示',
   name                 varchar(50) comment '名称',
   status               varchar(4) comment '开关状态',
   sort                 int comment '排序',
   fid                  bigint,
   primary key (id)
);

alter table COACH_CONSULT_TYPE comment '心理辅导中心--咨询类型';

/*==============================================================*/
/* Table: COACH_INTRODUCE                                       */
/*==============================================================*/
create table COACH_INTRODUCE
(
   id                   bigint not null comment '唯一标示',
   name                 varchar(50) comment '名称',
   content              text comment '简介',
   address              national varchar(100) comment '地址',
   telphone             varchar(11) comment '电话',
   primary key (id)
);

alter table COACH_INTRODUCE comment '心理辅导中心--中心简介';

/*==============================================================*/
/* Table: COACH_INTRODUCE_JOBTIME                               */
/*==============================================================*/
create table COACH_INTRODUCE_JOBTIME
(
   id                   bigint not null comment '唯一标示',
   week                 smallint comment '星期',
   endtimeid            int comment '结束时间',
   starttimeid          int comment '开始时间',
   fid                  bigint comment '中心id',
   primary key (id)
);

alter table COACH_INTRODUCE_JOBTIME comment '心理辅导中心--中心简介--工作时间';



drop table if exists COACH_SCHEDULING;

/*==============================================================*/
/* Table: COACH_SCHEDULING                                      */
/*==============================================================*/
create table COACH_SCHEDULING
(
   id                   bigint not null comment '唯一标示',
   schoolid             bigint comment '学校id',
   timeid               int comment '时间id',
   teacherid            bigint comment '教师id',
   date                 datetime comment '日期',
   primary key (id)
);

alter table COACH_SCHEDULING comment '心理辅导中心--预约管理--排班';


drop table if exists COACH_APPOINTMENT;

/*==============================================================*/
/* Table: COACH_APPOINTMENT                                     */
/*==============================================================*/
create table COACH_APPOINTMENT
(
   id                   bigint not null comment '唯一标示',
   scheduingids         varchar(100) comment '排班id',
   name                 varchar(20) comment '姓名',
   type                 bigint comment '咨询类型',
   describes            txt comment '咨询问题',
   contact              varchar(100) comment '联系方式',
   status               char(1) comment '状态',
   model                bigint comment '咨询方式',
   primary key (id)
);


alter table COACH_APPOINTMENT comment '心理辅导中心--预约管理--预约';





drop table if exists COACH_RECORD;

/*==============================================================*/
/* Table: COACH_RECORD                                          */
/*==============================================================*/
create table COACH_RECORD
(
   id                   bigint not null comment '唯一标示',
   username             varchar(20) comment '用户姓名',
   data                 date comment '日期',
   teacherid            bigint comment '教师id',
   consultationModeId   bigint comment '咨询方式',
   consultationTypeId   bigint comment '咨询类型',
   describes            text comment '问题概述',
   visitorsComplain     text comment '来访者自述',
   familyReport         text comment '家属报告',
   strategy             text comment '心理辅导策略',
   result               text comment '心理辅导结果',
   objtype              char(1) comment '辅导对象',
   schoolid             bigint comment '学校id',
   primary key (id)
);

alter table COACH_RECORD comment '心理辅导中心--辅导记录';



drop table if exists COACH_RECORD_EVENT;

/*==============================================================*/
/* Table: COACH_RECORD_EVENT                                    */
/*==============================================================*/
create table COACH_RECORD_EVENT
(
   id                   bigint not null comment '唯一标示',
   userid               bigint comment '用户id',
   data                 date comment '日期',
   event                text comment '事件',
   fid                  bigint comment '咨询记录id',
   primary key (id)
);

alter table COACH_RECORD_EVENT comment '心理辅导中心--辅导记录--重大生活事件';


drop table if exists COACH_CONSULTATIONMODE_CODE;

/*==============================================================*/
/* Table: COACH_CONSULTATIONMODE_CODE                           */
/*==============================================================*/
create table COACH_CONSULTATIONMODE_CODE
(
   id                   bigint not null comment '唯一标示',
   name                 varchar(20) comment '名称',
   sort                 int comment '排序',
   primary key (id)
);

alter table COACH_CONSULTATIONMODE_CODE comment '心理辅导中心--辅导记录--咨询方式码表';


drop table if exists COACH_ONLINE_EMAIL;

/*==============================================================*/
/* Table: COACH_ONLINE_EMAIL                                    */
/*==============================================================*/
create table COACH_ONLINE_EMAIL
(
   id                   bigint not null comment '唯一标示',
   fromid               bigint comment '发件人id',
   data                 date comment '时间',
   toid                 bigint comment '接收人id',
   title                varchar(100) comment '邮件标题',
   describes            text comment '问题概述',
   emailstatus          char(1) comment '邮件状态（草稿，发送，接受，重要，删除）',
   is_read              char(1) comment '是否已读',
   is_replied           char(1) comment '是否回复',
   parent_id            bigint comment '回复邮件id',
   parent_ids           varchar(200) comment '回复树形ids',
   send_date            date comment '发送日期',
   read_date            date comment '读日期',
   schoolid             bigint comment '学校id',
   primary key (id)
);

alter table COACH_ONLINE_EMAIL comment '心理辅导中心--心里在线--信箱';



drop table if exists COACH_TEAM_RECORD;

/*==============================================================*/
/* Table: COACH_TEAM_RECORD                                     */
/*==============================================================*/
create table COACH_TEAM_RECORD
(
   id                   bigint not null comment '唯一标示',
   teamid               bigint comment '团队id',
   begintime            datetime comment '开始时间',
   endtime              datetime comment '结束时间',
   teacherid            bigint comment '教师id',
   consultationModeId   bigint comment '咨询方式',
   consultationTypeId   bigint comment '咨询类型',
   reason               text comment '辅导缘由',
   process              text comment '辅导过程',
   inspiration          text comment '辅导感悟',
   result               text comment '辅导效果',
   remark               text comment '备注',
   schoolid             bigint comment '学校id',
   primary key (id)
);

alter table COACH_TEAM_RECORD comment '心理辅导中心--团队辅导记录';




drop table if exists COACH_TEAM;

/*==============================================================*/
/* Table: COACH_TEAM                                            */
/*==============================================================*/
create table COACH_TEAM
(
   id                   bigint not null comment '唯一标示',
   name                 varchar(50) comment '团体名称',
   createtime           date comment '创建日期',
   teamtype             char(1) comment '团体类型',
   personnum            int comment '团体人数',
   schoolid             bigint comment '学校id',
   primary key (id)
);

alter table COACH_TEAM comment '心理辅导中心-团体信息';


drop table if exists COACH_TEAM_PERSON;

/*==============================================================*/
/* Table: COACH_TEAM_PERSON                                     */
/*==============================================================*/
create table COACH_TEAM_PERSON
(
   id                   bigint not null comment '唯一标示',
   teamid               bigint comment '团队id',
   memberid             bigint comment '成员id',
   primary key (id)
);

alter table COACH_TEAM_PERSON comment '心理辅导中心---团队人员列表';




drop table if exists COACH_WARNING_STUDENT;

/*==============================================================*/
/* Table: COACH_WARNING_STUDENT                                 */
/*==============================================================*/
create table COACH_WARNING_STUDENT
(
   id                   bigint not null comment '唯一标示',
   examresult_id        bigint  comment '测试结果id',
   studentid            bigint comment '学生id',
   name                 varchar(30) comment '姓名',
   sex                  char(1) comment '性别',
   cardid               varchar(18) comment '身份证号',
   class_name           varchar(20) comment '班级名称',
   grade                varchar(20) comment '年级',
   type                 char(1) comment '干预方式',
   result               char(1) comment '干预结果',
   status               char(1) comment '状态',
   level                char(1) comment '预警级别',
   warning_time         datetime comment '预警时间',
   intervene_time       datetime comment '干预时间',
   school_id            bigint  comment '学校id',
   groupid              int  comment '学段',
   primary key (id)
);

alter table COACH_WARNING_STUDENT comment '心理辅导中心--危机预警--学生';




drop table if exists COACH_WARNING_TEACHET;

/*==============================================================*/
/* Table: COACH_WARNING_TEACHET                                 */
/*==============================================================*/
create table COACH_WARNING_TEACHET
(
   id                   bigint not null comment '唯一标示',
   examresult_id        bigint  comment '测试结果id',
   studentid            bigint comment '老师id',
   name                 varchar(30) comment '姓名',
   sex                  char(1) comment '性别',
   cardid               varchar(18) comment '身份证号',
   role_name            varchar(20) comment '角色名称',
   type                 char(1) comment '干预方式',
   result               char(1) comment '干预结果',
   status               char(1) comment '状态',
   level                char(1) comment '预警级别',
   warning_time         datetime comment '预警时间',
   intervene_time       datetime comment '干预时间',
   school_id            bigint  comment '学校id',
   primary key (id)
);

alter table COACH_WARNING_TEACHET comment '心理辅导中心--危机预警--老师';




drop table if exists COACH_ANALYZE;

/*==============================================================*/
/* Table: COACH_ANALYZE                                         */
/*==============================================================*/
create table COACH_ANALYZE
(
   id                   bigint not null comment '唯一标示',
   role                 char(1) comment '学生or老师',
   grade                char(2) comment '年级',
   model                bigint comment '咨询方式',
   type                 bigint comment '咨询类型',
   sex                  char(1) comment '性别',
   date                 datetime comment '时间',
   coach_type           char(1) comment '辅导方式',
   result               char(1) comment '辅导结果',
   coach_object         char(1) comment '辅导对象',
   schoolid             bigint comment '学校id',
   primary key (id)
);

alter table COACH_ANALYZE comment '心理辅导中心--工作分析';



/*预约视图*/
drop  view view_appointment;

create  view view_appointment AS
select   a.id,a.name,a.model,a.type,a.describes,a.contact,a.status,s.schoolid,s.timeid,s.teacherid,s.date ,a.schedulingids
from coach_appointment a left join coach_scheduling s on a.id=s.id;


drop  view view_warning_student;
create  view view_warning_student AS
SELECT
	e.id,
	s.xm,
	s.xbm,
	s.sfzjh,
	e.nj,
	e.classid as bh,
	e.bjmc AS bjmch,
	e.njmc AS njmc,
	sc.title,
	e.scale_id,
	e.ok_time,
	sc.questionnum,
	e.iswarnsure,
	e.warning_grade,
	e.orgid,
  e.xd as  groupid
FROM
	examresult_student e
LEFT JOIN student s ON e.user_id = s.id
LEFT JOIN scale sc ON e.scale_id = sc.code where warning_grade!=0


drop  view view_warning_teacher;
create  view view_warning_teacher  AS
SELECT
	e.id,
	s.xm,
	s.xbm,
	s.sfzjh,
	sc.title,
	e.scale_id,
	e.ok_time,
	sc.questionnum,
	e.iswarnsure,
	e.warning_grade,
	a.org_id,
	a.role_id
FROM
	examresult_teacher e
LEFT JOIN teacher s ON e.user_id = s.id
LEFT JOIN scale sc ON e.scale_id = sc.code
LEFT JOIN auth a ON a.user_id = s.account_id where warning_grade!=0




/*学生测试视图*/

DROP VIEW view_test_student;

CREATE VIEW view_test_student AS 
SELECT
	e.id,
	e.user_id,
	e.flag,
	e.lo_time,
	e.hi_time,
	e.ok_time,
	e.taskid,
	sc.title,
	sc.id AS scale_id
	
FROM
	examdo_student e
LEFT JOIN scale sc ON e.scale_id = sc. CODE;



/*教师测试视图*/

drop  view view_test_teacher;
create  view view_test_teacher AS
SELECT
	e.id,
	e.user_id,
	e.flag,
	e.lo_time,
	e.hi_time,
	e.ok_time,
	e.taskid,
	sc.title,
	sc.id scale_id
FROM
	examdo_teacher e
LEFT JOIN scale sc ON e.scale_id = sc. CODE;




drop table if exists TAO_APPOINTMENT;

/*==============================================================*/
/* Table: TAO_APPOINTMENT                                       */
/*==============================================================*/
create table TAO_APPOINTMENT
(
   id                   bigint not null comment '唯一标示',
   schoolid             bigint comment '学校id',
   gradeid              bigint comment '年级',
   classid              bigint comment '班级',
   dotype               char(1) comment '处理方式',
   name                 varchar(20) comment '姓名',
   type                 bigint comment '咨询类型',
   describes            text comment '咨询问题',
   contact              varchar(100) comment '联系方式',
   status               char(1) comment '状态',
   model                bigint comment '咨询方式',
   date                 date comment '日期',
   timeid               int comment '时间',
   teacherid            bigint comment '志愿者id',
   otherorg             char(1) comment '转介去向',
   primary key (id)
);

alter table TAO_APPOINTMENT comment '陶老师工作站--面谈咨询--预约';





/*预约视图*/
drop  view view_tao_appointment;

create  view view_tao_appointment AS
select   a.id,a.name,a.model,a.type,a.describes,a.contact,a.status,s.schoolid,s.timeid,s.teacherid,s.date ,a.schedulingids
from tao_appointment a left join coach_scheduling s on a.id=s.id;




drop  view view_record_student;
create  view view_record_student  AS
SELECT
	r.id,
	r.username,
	s.xbm,
	s.sfzjh,
	s.nj,
	s.bh,
	r.`data`,
	r.consultationModeId,
	r.consultationTypeId,
	r.schoolid,
	r.teacherid, g.groupid,
	c.gradeid,
	g.title
FROM
	coach_record r
LEFT JOIN student s ON r.sfzjh = s.sfzjh
LEFT JOIN class c ON s.bjid = c.id
LEFT JOIN gradecode g ON g.id = c.gradeid
WHERE
	r.objtype=0;


drop  view view_record_teacher;
create  view view_record_teacher  AS
SELECT
	r.id,
	r.username,
	
t.xbm,
r.`data`,
r.consultationModeId,
r.consultationTypeId,
r.schoolid,
r.teacherid
FROM
	coach_record r
LEFT JOIN teacher t ON r.sfzjh = t.sfzjh where r.objtype=1;




drop  view view_warning_intervene_student;
create  view view_warning_intervene_student  AS
SELECT
	r.id,
	r. NAME,
	s.xbm,
	s.sfzjh,
	s.nj,
	s.bh,
	r.intervene_time,
	r.dispose_type,
	r. LEVEL,
	r.school_id,
	r.result,
	r.type,
	g.groupid,
	c.gradeid,
  g.title
FROM
	coach_warning_student r
LEFT JOIN student s ON r.cardid = s.sfzjh
LEFT JOIN class c ON s.bjid = c.id
LEFT JOIN gradecode g ON g.id = c.gradeid
WHERE
	r. STATUS = 4;


drop  view view_warning_intervene_teacher;
create  view view_warning_intervene_teacher  AS
SELECT
	r.id,
	r. NAME,
	t.xbm,
	t.sfzjh,
	r.intervene_time,
	r.dispose_type,
	r. LEVEL,
	r.school_id,
	r.result,
	r.type
FROM
	coach_warning_teacher r
LEFT JOIN teacher t ON r.cardid = t.sfzjh
WHERE
	r. STATUS = 4;


	
	
	
	

