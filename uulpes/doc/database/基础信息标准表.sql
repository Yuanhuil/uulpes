/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/4/30 10:55:56                           */
/*==============================================================*/


drop table if exists account;

drop table if exists chengfa;

drop table if exists class;

drop table if exists examscore;

drop table if exists family;

drop table if exists grade;

drop table if exists jiangli;

drop table if exists organization;

drop table if exists organization_ext;

drop table if exists parent;

drop table if exists parent_ext;

drop table if exists school;

drop table if exists school_ext;

drop table if exists schooleroll;

drop table if exists schoolrollchange;

drop table if exists student;

drop table if exists student_ext;

drop table if exists teacher;

drop table if exists teacher_ext;

/*==============================================================*/
/* Table: account                                               */
/*==============================================================*/
create table account
(
   id                   bigint(20) not null auto_increment,
   username             national varchar(30) not null,
   password             national varchar(80) not null,
   idcard               varchar(18),
   create_time          datetime not null,
   update_time          datetime not null,
   role_flag            int(5) not null,
   state                int(11),
   admin                tinyint(1),
   primary key (id)
);

/*==============================================================*/
/* Index: username                                              */
/*==============================================================*/
create index username on account
(
   username
);

/*==============================================================*/
/* Table: chengfa                                               */
/*==============================================================*/
create table chengfa
(
   id                   bigint not null,
   wjrq                 char(8) comment '违纪日期',
   wjjk                 varchar(100) comment '违纪简况',
   wjlbm                char(2) comment '违纪类别码 WJLB',
   cfmcm                char(1) comment '处分名称码CFMC',
   cfyy                 varchar(100) comment '处分原因',
   cfrq                 char(8) comment '处分日期',
   cfwh                 varchar(24) comment '处分文号',
   cfcxrq               char(8) comment '处分撤消日期',
   cfcxwh               varchar(24) comment '处分撤消文号',
   primary key (id)
);

alter table chengfa comment '惩处数据JCXS0205';

/*==============================================================*/
/* Table: class                                                 */
/*==============================================================*/
create table class
(
   id                   bigint(20) not null auto_increment comment '主键',
   bh                   varchar(10) comment '班号',
   bjmc                 varchar(20) comment '班级名称',
   jbny                 varchar(6) comment '建班年月',
   bzrgh                varchar(20) comment '班主任工号',
   bzxh                 varchar(20) comment '班长学号',
   bjrych               varchar(40) comment '班级荣誉称号',
   xz                   numeric(3,1) comment '学制',
   bjlxm                varchar(2) comment '班级类型码',
   wllx                 varchar(2) comment '文理类型',
   byrq                 varchar(8) comment '毕业日期',
   sfssmzsyjxb          varchar(1) comment '是否少数民族双语教学班',
   syjxmsm              varchar(1) comment '双语教学模式码',
   primary key (id)
);

/*==============================================================*/
/* Table: examscore                                             */
/*==============================================================*/
create table examscore
(
   id                   bigint not null,
   xn                   char(9) comment '学年（度）YYYYMMDD-YYYYMMDD',
   xqm                  char(1) comment '学期码',
   ksrq                 varchar(8) comment '考试日期',
   kch                  varchar(10) comment '课程号',
   pscj                 numeric(5,1) comment '平时成绩',
   ksfsm                char(2) comment '考试方式码',
   ksxzm                char(1) comment '考试性质码',
   fslkscj              numeric(5,1) comment '分数类考试成绩',
   djlkscj              char(1) comment '等级类考试成绩',
   kccj                 numeric(5,1) comment '课程成绩',
   kcdjcjm              char(1) comment '课程等级成绩码',
   rkjsgh               varchar(10) comment '任课教师工号',
   cjlrrh               varchar(10) comment '成绩录入人号',
   cjlrrq               char(8) comment '成绩录入日期',
   cjlrsj               char(6) comment '成绩录入时间',
   primary key (id)
);

alter table examscore comment '成绩数据JCXS0203';

/*==============================================================*/
/* Table: family                                                */
/*==============================================================*/
create table family
(
   id                   bigint not null,
   gxm                  char(2) comment '关系码GB/T 4761',
   cyxm                 varchar(36) comment '成员姓名',
   csny                 char(6) comment '出生年月',
   mzm                  char(2) comment '民族码',
   gjdqm                char(2) comment '国籍/地区码',
   jkzkm                char(2) comment '健康状况码',
   cygzdw               varchar(100) comment '成员工作单位',
   cyem                 char(2) comment '从业码GB/T 2261.4',
   zyjszwm              char(3) comment '专业技术职务GB/T 8561码',
   zwjbm                char(2) comment '职务级别码GB/T 12407',
   dh                   varchar(30) comment '电话',
   dzxx                 varchar(40) comment '电子信箱',
   sfjhr                char(1) comment '是否监护人',
   xbm                  char(1) comment '性别码',
   xlm                  char(2) comment '学历码',
   lxdz                 varchar(100) comment '联系地址',
   sjhm                 char(40) comment '手机号码',
   primary key (id)
);

alter table family comment 'JCXS0301家庭成员数据';

/*==============================================================*/
/* Table: grade                                                 */
/*==============================================================*/
create table grade
(
   id                   int(5) not null auto_increment comment '主键',
   nj                   varchar(10) comment '年级',
   njmc                 varchar(30) comment '年级名称',
   primary key (id)
);

/*==============================================================*/
/* Table: jiangli                                               */
/*==============================================================*/
create table jiangli
(
   id                   bigint not null,
   jlmc                 varchar(60) comment '奖励名称',
   jljbm                char(2) comment '奖励级别码JY/T 1001 JB 级别',
   jldjm                char(1) comment '奖励等级码JLDJ',
   hjlbm                char(1) comment '获奖类别码XSHJLB',
   jlyy                 varchar(100) comment '奖励原因',
   jlje                 float(8,2) comment '奖励金额',
   jlwh                 varchar(24) comment '奖励文号',
   jlxnd                char(9) comment '奖励学年度',
   bjdw                 varchar(100) comment '颁奖单位',
   jllxm                char(1) comment '奖励类型码JY/T 1001 HJLX',
   jlfsm                char(1) comment '奖励方式码JY/T 1001 JLFS',
   primary key (id)
);

alter table jiangli comment '奖励数据JCXS0204';

/*==============================================================*/
/* Table: organization                                          */
/*==============================================================*/
create table organization
(
   id                   bigint(20) not null auto_increment comment '主键',
   dwh                  national varchar(10) comment '单位号',
   dwmc                 national varchar(60) comment '单位名称',
   dwywmc               varchar(180) comment '单位英文名称',
   dwjc                 national varchar(12) comment '单位简称',
   dwywjc               national char(8) comment '单位英文简称',
   dwjp                 varchar(12) comment '单位简拼',
   dwdz                 varchar(180) comment '单位地址',
   lsdwh                varchar(10) comment '隶属单位号',
   dwlbm                varchar(1) comment '单位类别码',
   dwbbm                varchar(1) comment '单位办别码',
   dwyxbs               varchar(1) comment '单位有效标识',
   sxrq                 varchar(8) comment '失效日期',
   sfst                 varchar(1) comment '是否实体',
   jlny                 varchar(6) comment '建立年月',
   dwfzrh               varchar(20) comment '单位负责人号',
   primary key (id)
);

/*==============================================================*/
/* Table: organization_ext                                      */
/*==============================================================*/
create table organization_ext
(
   id                   bigint(20) not null auto_increment comment '主键',
   zzjb                 int(6) comment '组织级别',
   px                   int(11) comment '排序',
   bjxx                 text comment '背景信息',
   primary key (id)
);

/*==============================================================*/
/* Table: parent                                                */
/*==============================================================*/
create table parent
(
   id                   bigint(20) not null auto_increment comment '主键',
   gxm                  national varchar(2) not null comment '关系码',
   cyxm                 varchar(36) not null comment '成员姓名',
   csny                 varchar(6) comment '出生年月',
   mzm                  varchar(2) comment '民族码',
   gjdqm                varchar(3) comment '国籍/地区码',
   jkzkm                varchar(1) comment '健康状况码',
   cygzdw               varchar(60) comment '成员工作单位',
   cyem                 varchar(2) comment '从业码',
   zyjszwm              varchar(3) comment '专业技术职务码',
   zwjbm                varchar(2) comment '职务级别码',
   dh                   varchar(30) comment '电话',
   dzxx                 varchar(40) comment '电子信箱',
   sfjhr                varchar(1) comment '是否监护人',
   xbm                  varchar(1) comment '性别码',
   xlm                  varchar(2) comment '学历码',
   lxdz                 varchar(180) comment '联系地址',
   sjhm                 varchar(30) comment '手机号码',
   primary key (id)
);

/*==============================================================*/
/* Table: parent_ext                                            */
/*==============================================================*/
create table parent_ext
(
   id                   bigint(20) not null auto_increment comment '主键',
   bjxx                 text comment '背景信息',
   primary key (id)
);

/*==============================================================*/
/* Table: school                                                */
/*==============================================================*/
create table school
(
   id                   bigint(20) not null auto_increment comment '主键',
   xxdm                 national varchar(10) comment '学校代码',
   xxmc                 national varchar(60) not null comment '学校名称',
   xxywmc               varchar(180) comment '学校英文名称',
   xxdz                 national varchar(180) comment '学校地址',
   xxyzbm               national varchar(6) comment '学校邮政编码',
   xzqhm                varchar(6) comment '行政区划码',
   jxny                 varchar(6) comment '建校年月',
   xqr                  varchar(60) comment '校庆日',
   xxbxlxm              national varchar(3) comment '学校办学类型码',
   xxjbzm               varchar(3) comment '学校举办者码',
   xxzgbmm              varchar(3) comment '学校主管部门码',
   fddbrh               varchar(20) comment '法定代表人号',
   frzsh                varchar(20) comment '法人证书号',
   xzgh                 varchar(20) comment '校长工号',
   xzxm                 varchar(36) comment '校长姓名',
   dwfzrh               varchar(20) comment '党委负责人号',
   zzjgm                varchar(10) comment '组织机构码',
   lxdh                 varchar(30) comment '联系电话',
   czdh                 varchar(30) comment '传真电话',
   dzxx                 varchar(40) comment '电子信箱',
   zydz                 varchar(60) comment '主页地址',
   lsyg                 text comment '历史沿革',
   xxbbm                varchar(2) comment '学校办别码',
   sszgdwm              varchar(6) comment '所属主管单位码',
   szdcxlxm             varchar(3) comment '所在地城乡类型码',
   szdjjsxm             varchar(1) comment '所在地经济属性码',
   szdmzsx              varchar(1) comment '所在地民族属性',
   xxxz                 numeric(3,1) comment '小学学制',
   xxrxnl               numeric(1) comment '小学入学年龄',
   czxz                 numeric(3,1) comment '初中学制',
   czrxnl               numeric(2) comment '初中入学年龄',
   gzxz                 numeric(3,1) comment '高中学制',
   zjxyym               varchar(3) comment '主教学语言码',
   fjxyym               varchar(3) comment '辅教学语言码',
   zsbj                 varchar(30) comment '招生半径',
   primary key (id)
);

/*==============================================================*/
/* Table: school_ext                                            */
/*==============================================================*/
create table school_ext
(
   id                   bigint(20) not null auto_increment comment '主键',
   px                   int(5) comment '排序',
   bjxx                 text comment '背景信息',
   primary key (id)
);

/*==============================================================*/
/* Table: schooleroll                                           */
/*==============================================================*/
create table schooleroll
(
   id                   bigint not null,
   rxny                 char(6) not null comment '入学年月',
   xslbm                varchar(5) not null comment '学生类别码',
   xz                   varchar(10) not null comment '学制',
   zym                  varchar(3) comment '专业码',
   szbh                 varchar(10) comment '所在班号',
   sznj                 varchar(10) comment '所在年级',
   xsdqztm              varchar(2) not null comment '
            学生当前状态码',
   primary key (id)
);

alter table schooleroll comment '学籍基本信息';

/*==============================================================*/
/* Table: schoolrollchange                                      */
/*==============================================================*/
create table schoolrollchange
(
   id                   bigint not null,
   ydlbm                char(2) comment '异动类别码',
   ydrq                 char(8) comment '异动日期',
   ydyym                char(2) comment '异动原因码',
   sprq                 char(8) comment '审批日期',
   spwh                 varchar(24) comment '审批文号',
   ydlyxxm              varchar(10) comment '异动来源学校码',
   ydqxxxm              varchar(10) comment '异动去向学校码',
   ydsm                 text comment '异动说明',
   yyxsh                varchar(10) comment '原院系所号',
   yzym                 varchar(10) comment '原专业码',
   ybh                  varchar(10) comment '原班号',
   ynj                  varchar(10) comment '原年级',
   yxz                  varchar(10) comment '原学制',
   xyxsh                varchar(10) comment '现院系所号',
   xzym                 varchar(10) comment '现专业码',
   xbh                  varchar(10) comment '现班号',
   xnj                  varchar(10) comment '现年级',
   xxz                  varchar(10) comment '现学制',
   primary key (id)
);

alter table schoolrollchange comment '学籍异动数据';

/*==============================================================*/
/* Table: student                                               */
/*==============================================================*/
create table student
(
   id                   bigint(20) unsigned not null auto_increment comment '主键',
   xh                   national varchar(20) comment '学号',
   xm                   national varchar(36) comment '姓名',
   ywxm                 national varchar(60) comment '英文姓名',
   xmpy                 national varchar(60) comment '姓名拼音',
   cym                  national varchar(36) comment '曾用名',
   xbm                  char(1) comment '性别码',
   csrq                 date comment '出生日期',
   csdm                 national varchar(6) comment '出生地码',
   jg                   national varchar(20) comment '籍贯',
   mzm                  national varchar(2) comment '民族码',
   gjdqm                char(3) comment '国籍/地区码',
   sfzjlxm              char(1) comment '身份证类型码',
   sfzjh                national varchar(18) comment '身份证件号',
   hyzkm                national varchar(2) comment '婚姻状况码',
   gatqwm               national varchar(2) comment '港澳台侨外码',
   zzmmm                national varchar(2) comment '政治面貌码',
   jkzkm                char(1) comment '健康状况码',
   xyzjm                national varchar(2) comment '信仰宗教码',
   xxm                  char(1) comment '血型码',
   zp                   blob comment '照片',
   sfzjyxq              national varchar(20) comment '身份证件有效期',
   dszybz               char(1) comment '独生子女标志',
   rxny                 char(6) comment '入学年月',
   nj                   national varchar(10) comment '年级',
   bh                   national varchar(20) comment '班号',
   xslbm                national varchar(2) comment '学生类别码',
   xzz                  national varchar(100) comment '现住址',
   hkszd                national varchar(200) comment '户口所在地',
   hkxzm                char(1) comment '户口性质码',
   sfldrk               char(1) comment '是否流动人口',
   tc                   text comment '特长',
   lxdh                 national varchar(20) comment '联系电话',
   txdz                 national varchar(200) comment '通信地址',
   yzbm                 national varchar(10) comment '邮政编码',
   dzxx                 national varchar(50) comment '电子信箱',
   zydz                 national varchar(200) comment '主页地址',
   xjh                  national varchar(30) comment '学籍号',
   primary key (id)
);

/*==============================================================*/
/* Table: student_ext                                           */
/*==============================================================*/
create table student_ext
(
   id                   bigint(20) not null auto_increment comment '主键',
   bjxx                 text comment '背景信息',
   primary key (id)
);

/*==============================================================*/
/* Table: teacher                                               */
/*==============================================================*/
create table teacher
(
   id                   bigint(20) not null auto_increment comment '主键',
   gh                   national varchar(20) comment '工号',
   xm                   varchar(36) comment '姓名',
   ywxm                 varchar(60) not null comment '英文姓名',
   xmpy                 varchar(60) default '0' comment '姓名拼音',
   cym                  varchar(36) default '0' comment '曾用名',
   xbm                  varchar(1) comment '性别码',
   csrq                 varchar(8) comment '出生日期',
   csdm                 varchar(6) comment '出生地码',
   jg                   varchar(20) comment '籍贯',
   mzm                  varchar(2) comment '民族码',
   gjdqm                varchar(3) comment '国籍/地区码',
   sfzjlxm              varchar(1) comment '身份证件类型码',
   sfzjh                varchar(20) comment '身份证件号',
   hyzkm                varchar(2) comment '婚姻状况码',
   gatqwm               varchar(2) comment '港澳台侨外码',
   zzmmm                varchar(2) comment '政治面貌码',
   jkzkm                varchar(1) comment '健康状况码',
   xyzjm                varchar(2) comment '信仰宗教码',
   xxm                  varchar(1) comment '血型码',
   zp                   blob comment '照片',
   sfzjyxq              varchar(17) comment '身份证件有效期
            格式：
            YYYYMMDD-YYYYMMDD',
   jgh                  varchar(10) comment '机构号',
   jtzz                 varchar(180) comment '家庭住址',
   xzz                  varchar(180) comment '现住址',
   hkszd                varchar(180) comment '户口所在地',
   hkxzm                varchar(1) comment '户口性质码',
   xlm                  varchar(2) comment '学历码',
   gzny                 varchar(6) comment '参加工作年月',
   lxny                 varchar(6) comment '来校年月',
   cjny                 varchar(6) comment '从教年月',
   bzlbm                varchar(1) comment '编制类别码',
   dabh                 varchar(10) comment '档案编号',
   dawb                 text comment '档案文本',
   txdz                 varchar(180) comment '通信地址',
   lxdh                 varchar(30) comment '联系电话',
   yzbm                 varchar(6) comment '邮政编码',
   dzxx                 varchar(40) comment '电子信箱',
   zydz                 varchar(60) comment '主页地址',
   tc                   text comment '特长',
   gwzym                varchar(2) comment '岗位职业码',
   zyrkxd               varchar(1) comment '主要任课学段',
   primary key (id)
);


/*==============================================================*/
/* Table: teacher_ext                                           */
/*==============================================================*/
create table teacher_ext
(
   id                   bigint(20) not null auto_increment comment '主键',
   bjxx                 text comment '背景信息',
   primary key (id)
);

