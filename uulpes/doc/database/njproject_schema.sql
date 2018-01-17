-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 02, 2015 at 03:39 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `njproject`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
CREATE TABLE IF NOT EXISTS `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(80) NOT NULL,
  `idcard` varchar(18) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `type_flag` int(5) NOT NULL,
  `state` int(11) DEFAULT NULL,
  `admin` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Table structure for table `auth`
--

DROP TABLE IF EXISTS `auth`;
CREATE TABLE IF NOT EXISTS `auth` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) unsigned NOT NULL,
  `job_id` bigint(20) unsigned NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL,
  `group_id` bigint(20) unsigned NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `auth_type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

-- --------------------------------------------------------

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
CREATE TABLE IF NOT EXISTS `class` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bh` varchar(10) DEFAULT NULL COMMENT '班号',
  `bjmc` varchar(20) DEFAULT NULL COMMENT '班级名称',
  `jbny` varchar(6) DEFAULT NULL COMMENT '建班年月',
  `bzrgh` varchar(20) DEFAULT NULL COMMENT '班主任工号',
  `bzxh` varchar(20) DEFAULT NULL COMMENT '班长学号',
  `bjrych` varchar(40) DEFAULT NULL COMMENT '班级荣誉称号',
  `xz` decimal(3,1) DEFAULT NULL COMMENT '学制',
  `bjlxm` varchar(2) DEFAULT NULL COMMENT '班级类型码',
  `wllx` varchar(2) DEFAULT NULL COMMENT '文理类型',
  `byrq` varchar(8) DEFAULT NULL COMMENT '毕业日期',
  `sfssmzsyjxb` varchar(1) DEFAULT NULL COMMENT '是否少数民族双语教学班',
  `syjxmsm` varchar(1) DEFAULT NULL COMMENT '双语教学模式码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dic_account_state`
--

DROP TABLE IF EXISTS `dic_account_state`;
CREATE TABLE IF NOT EXISTS `dic_account_state` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `sort` tinyint(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户状态' AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `dic_common_sf`
--

DROP TABLE IF EXISTS `dic_common_sf`;
CREATE TABLE IF NOT EXISTS `dic_common_sf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通用是否' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dic_job_catalog`
--

DROP TABLE IF EXISTS `dic_job_catalog`;
CREATE TABLE IF NOT EXISTS `dic_job_catalog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--工作类型' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dic_job_notice_catalog`
--

DROP TABLE IF EXISTS `dic_job_notice_catalog`;
CREATE TABLE IF NOT EXISTS `dic_job_notice_catalog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--通知公告--通知类型' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dic_job_notice_share`
--

DROP TABLE IF EXISTS `dic_job_notice_share`;
CREATE TABLE IF NOT EXISTS `dic_job_notice_share` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--通知公告--共享' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dic_job_notice_state`
--

DROP TABLE IF EXISTS `dic_job_notice_state`;
CREATE TABLE IF NOT EXISTS `dic_job_notice_state` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--通知公告--状态' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dic_job_notice_type`
--

DROP TABLE IF EXISTS `dic_job_notice_type`;
CREATE TABLE IF NOT EXISTS `dic_job_notice_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--通知公告类型' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dic_organizationlevel`
--

DROP TABLE IF EXISTS `dic_organizationlevel`;
CREATE TABLE IF NOT EXISTS `dic_organizationlevel` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `orglevelname` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织机构级别' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dic_rolelevel`
--

DROP TABLE IF EXISTS `dic_rolelevel`;
CREATE TABLE IF NOT EXISTS `dic_rolelevel` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `rolelevelname` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色级别' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dic_sex`
--

DROP TABLE IF EXISTS `dic_sex`;
CREATE TABLE IF NOT EXISTS `dic_sex` (
  `id` char(1) NOT NULL,
  `name` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='性别代码';

-- --------------------------------------------------------

--
-- Table structure for table `examresult_dim_mental_health`
--

DROP TABLE IF EXISTS `examresult_dim_mental_health`;
CREATE TABLE IF NOT EXISTS `examresult_dim_mental_health` (
  `result_id` bigint(20) NOT NULL,
  `scale_id` int(11) NOT NULL,
  `w0` smallint(6) NOT NULL DEFAULT '0',
  `w1` smallint(6) NOT NULL DEFAULT '0',
  `w2` smallint(6) NOT NULL DEFAULT '0',
  `w3` smallint(6) NOT NULL DEFAULT '0',
  `w4` smallint(6) NOT NULL DEFAULT '0',
  `w5` smallint(6) NOT NULL DEFAULT '0',
  `w6` smallint(6) NOT NULL DEFAULT '0',
  `w7` smallint(6) NOT NULL DEFAULT '0',
  `w8` smallint(6) NOT NULL DEFAULT '0',
  `w9` smallint(6) NOT NULL DEFAULT '0',
  `w10` smallint(6) NOT NULL DEFAULT '0',
  `w11` smallint(6) NOT NULL DEFAULT '0',
  `w12` smallint(6) NOT NULL DEFAULT '0',
  `w13` smallint(6) NOT NULL DEFAULT '0',
  `w14` smallint(6) NOT NULL DEFAULT '0',
  `w15` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`result_id`,`scale_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `examresult_dim_student`
--

DROP TABLE IF EXISTS `examresult_dim_student`;
CREATE TABLE IF NOT EXISTS `examresult_dim_student` (
  `result_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `scale_id` int(11) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `grade_order_id` tinyint(4) NOT NULL,
  `class_id` int(11) NOT NULL,
  `gender` tinyint(4) NOT NULL,
  `ok_time` datetime NOT NULL,
  `w0` smallint(6) NOT NULL DEFAULT '0',
  `w1` smallint(6) NOT NULL DEFAULT '0',
  `w2` smallint(6) NOT NULL DEFAULT '0',
  `w3` smallint(6) NOT NULL DEFAULT '0',
  `w4` smallint(6) NOT NULL DEFAULT '0',
  `w5` smallint(6) NOT NULL DEFAULT '0',
  `w6` smallint(6) NOT NULL DEFAULT '0',
  `w7` smallint(6) NOT NULL DEFAULT '0',
  `w8` smallint(6) NOT NULL DEFAULT '0',
  `w9` smallint(6) NOT NULL DEFAULT '0',
  `w10` smallint(6) NOT NULL DEFAULT '0',
  `w11` smallint(6) NOT NULL DEFAULT '0',
  `w12` smallint(6) NOT NULL DEFAULT '0',
  `w13` smallint(6) NOT NULL DEFAULT '0',
  `w14` smallint(6) NOT NULL DEFAULT '0',
  `w15` smallint(6) NOT NULL DEFAULT '0',
  `w16` smallint(6) NOT NULL DEFAULT '0',
  `w17` smallint(6) NOT NULL DEFAULT '0',
  `w18` smallint(6) NOT NULL DEFAULT '0',
  `w19` smallint(6) NOT NULL DEFAULT '0',
  `w20` smallint(6) NOT NULL DEFAULT '0',
  `w21` smallint(6) NOT NULL DEFAULT '0',
  `w22` smallint(6) NOT NULL DEFAULT '0',
  `w23` smallint(6) NOT NULL DEFAULT '0',
  `w24` smallint(6) NOT NULL DEFAULT '0',
  `w25` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`result_id`),
  KEY `class_order_id` (`class_id`),
  KEY `gender` (`gender`),
  KEY `grader_order_id` (`grade_order_id`),
  KEY `ok_time` (`ok_time`),
  KEY `org_id` (`org_id`),
  KEY `scaleid` (`scale_id`),
  KEY `userid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `examresult_dim_teacher`
--

DROP TABLE IF EXISTS `examresult_dim_teacher`;
CREATE TABLE IF NOT EXISTS `examresult_dim_teacher` (
  `result_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `scale_id` int(11) NOT NULL,
  `org_id` bigint(20) NOT NULL,
  `grade_order_id` tinyint(4) NOT NULL DEFAULT '0',
  `class_id` int(11) NOT NULL DEFAULT '0',
  `gender` tinyint(4) NOT NULL,
  `ok_time` datetime NOT NULL,
  `w0` smallint(6) NOT NULL DEFAULT '0',
  `w1` smallint(6) NOT NULL DEFAULT '0',
  `w2` smallint(6) NOT NULL DEFAULT '0',
  `w3` smallint(6) NOT NULL DEFAULT '0',
  `w4` smallint(6) NOT NULL DEFAULT '0',
  `w5` smallint(6) NOT NULL DEFAULT '0',
  `w6` smallint(6) NOT NULL DEFAULT '0',
  `w7` smallint(6) NOT NULL DEFAULT '0',
  `w8` smallint(6) NOT NULL DEFAULT '0',
  `w9` smallint(6) NOT NULL DEFAULT '0',
  `w10` smallint(6) NOT NULL DEFAULT '0',
  `w11` smallint(6) NOT NULL DEFAULT '0',
  `w12` smallint(6) NOT NULL DEFAULT '0',
  `w13` smallint(6) NOT NULL DEFAULT '0',
  `w14` smallint(6) NOT NULL DEFAULT '0',
  `w15` smallint(6) NOT NULL DEFAULT '0',
  `w16` smallint(6) NOT NULL DEFAULT '0',
  `w17` smallint(6) NOT NULL DEFAULT '0',
  `w18` smallint(6) NOT NULL DEFAULT '0',
  `w19` smallint(6) NOT NULL DEFAULT '0',
  `w20` smallint(6) NOT NULL DEFAULT '0',
  `w21` smallint(6) NOT NULL DEFAULT '0',
  `w22` smallint(6) NOT NULL DEFAULT '0',
  `w23` smallint(6) NOT NULL DEFAULT '0',
  `w24` smallint(6) NOT NULL DEFAULT '0',
  `w25` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`result_id`),
  KEY `ok_time` (`ok_time`),
  KEY `scaleid` (`scale_id`),
  KEY `userid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `examresult_individual`
--

DROP TABLE IF EXISTS `examresult_individual`;
CREATE TABLE IF NOT EXISTS `examresult_individual` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NOT NULL,
  `scale_id` int(11) NOT NULL,
  `individual_score` text,
  `question_score` text,
  `dim_score` text,
  `ok_time` datetime NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `warning_grade` int(10) unsigned DEFAULT '0',
  `valid_val` int(10) unsigned DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `scaleid` (`scale_id`),
  KEY `userid` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `examresult_student`
--

DROP TABLE IF EXISTS `examresult_student`;
CREATE TABLE IF NOT EXISTS `examresult_student` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` bigint(20) NOT NULL,
  `scale_id` int(11) NOT NULL,
  `individual_score` text,
  `question_score` text,
  `dim_score` text,
  `ok_time` datetime NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `warning_grade` int(10) unsigned DEFAULT '0',
  `valid_val` int(10) unsigned DEFAULT '1',
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `scaleid` (`scale_id`),
  KEY `userid` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `examresult_teacher`
--

DROP TABLE IF EXISTS `examresult_teacher`;
CREATE TABLE IF NOT EXISTS `examresult_teacher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `teacher_id` bigint(20) NOT NULL,
  `scale_id` int(11) NOT NULL,
  `individual_score` text,
  `question_score` text,
  `dim_score` text,
  `ok_time` datetime NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `warning_grade` int(10) unsigned DEFAULT '0',
  `valid_val` int(10) unsigned DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `scaleid` (`scale_id`),
  KEY `userid` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `examscore`
--

DROP TABLE IF EXISTS `examscore`;
CREATE TABLE IF NOT EXISTS `examscore` (
  `id` bigint(20) NOT NULL,
  `xn` char(9) DEFAULT NULL COMMENT '学年（度）YYYYMMDD-YYYYMMDD',
  `xqm` char(1) DEFAULT NULL COMMENT '学期码',
  `ksrq` varchar(8) DEFAULT NULL COMMENT '考试日期',
  `kch` varchar(10) DEFAULT NULL COMMENT '课程号',
  `pscj` decimal(5,1) DEFAULT NULL COMMENT '平时成绩',
  `ksfsm` char(2) DEFAULT NULL COMMENT '考试方式码',
  `ksxzm` char(1) DEFAULT NULL COMMENT '考试性质码',
  `fslkscj` decimal(5,1) DEFAULT NULL COMMENT '分数类考试成绩',
  `djlkscj` char(1) DEFAULT NULL COMMENT '等级类考试成绩',
  `kccj` decimal(5,1) DEFAULT NULL COMMENT '课程成绩',
  `kcdjcjm` char(1) DEFAULT NULL COMMENT '课程等级成绩码',
  `rkjsgh` varchar(10) DEFAULT NULL COMMENT '任课教师工号',
  `cjlrrh` varchar(10) DEFAULT NULL COMMENT '成绩录入人号',
  `cjlrrq` char(8) DEFAULT NULL COMMENT '成绩录入日期',
  `cjlrsj` char(6) DEFAULT NULL COMMENT '成绩录入时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='成绩数据JCXS0203';

-- --------------------------------------------------------

--
-- Table structure for table `grade`
--

DROP TABLE IF EXISTS `grade`;
CREATE TABLE IF NOT EXISTS `grade` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nj` varchar(10) DEFAULT NULL COMMENT '年级',
  `njmc` varchar(30) DEFAULT NULL COMMENT '年级名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
CREATE TABLE IF NOT EXISTS `group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `group_name` varchar(45) NOT NULL,
  `group_type` varchar(45) NOT NULL,
  `is_show` char(1) NOT NULL,
  `group_sort` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `group_rel`
--

DROP TABLE IF EXISTS `group_rel`;
CREATE TABLE IF NOT EXISTS `group_rel` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) unsigned NOT NULL,
  `org_id` bigint(20) unsigned NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
CREATE TABLE IF NOT EXISTS `job` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `job_code` varchar(20) NOT NULL,
  `job_name` varchar(45) NOT NULL,
  `parent_id` bigint(20) unsigned NOT NULL,
  `parent_ids` varchar(200) NOT NULL,
  `job_sort` int(10) unsigned NOT NULL,
  `is_show` char(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `job_attachment`
--

DROP TABLE IF EXISTS `job_attachment`;
CREATE TABLE IF NOT EXISTS `job_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `fid` bigint(20) NOT NULL COMMENT '心理工_唯一标示',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `save_path` text COMMENT '保存之后路径',
  `mine_type` varchar(30) DEFAULT NULL COMMENT '附件类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--附件信息' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `job_congress`
--

DROP TABLE IF EXISTS `job_congress`;
CREATE TABLE IF NOT EXISTS `job_congress` (
  `id` bigint(20) NOT NULL COMMENT '唯一标示',
  `job_id` bigint(20) DEFAULT NULL COMMENT '心理工_唯一标示',
  `emcee` varchar(30) DEFAULT NULL COMMENT '主持人',
  `persons` text COMMENT '参与人',
  `content` text COMMENT '内容',
  `share` int(11) DEFAULT NULL COMMENT '共享',
  `subject` text COMMENT '主题',
  `congresstime` date DEFAULT NULL COMMENT '会议时间',
  `place` varchar(50) DEFAULT NULL COMMENT '地点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--会议记录';

-- --------------------------------------------------------

--
-- Table structure for table `job_notice`
--

DROP TABLE IF EXISTS `job_notice`;
CREATE TABLE IF NOT EXISTS `job_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `type` int(11) DEFAULT NULL COMMENT '类别',
  `title` text COMMENT '标题',
  `content` text COMMENT '内容',
  `catalog` bigint(20) DEFAULT NULL COMMENT '工作类别',
  `attachment` int(11) DEFAULT NULL COMMENT '是否有附件',
  `share` int(11) DEFAULT NULL COMMENT '共享',
  `author` bigint(20) DEFAULT NULL COMMENT '制定者',
  `write_time` date DEFAULT NULL COMMENT '上传时间',
  `dep` bigint(20) DEFAULT NULL COMMENT '上传者所在部门',
  `author_role` bigint(20) DEFAULT NULL COMMENT '上传者角色',
  `modified` date DEFAULT NULL COMMENT '修改时间',
  `adult_time` date DEFAULT NULL COMMENT '审核时间',
  `state` bigint(20) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--通知公告' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `job_plan`
--

DROP TABLE IF EXISTS `job_plan`;
CREATE TABLE IF NOT EXISTS `job_plan` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `title` text COMMENT '标题',
  `catalog` bigint(20) DEFAULT NULL COMMENT '工作类别',
  `target` text COMMENT '工作目标',
  `emcee` varchar(30) DEFAULT NULL COMMENT '主持人',
  `content` text COMMENT '内容',
  `start_time` date DEFAULT NULL COMMENT '开始时间',
  `end_time` date DEFAULT NULL COMMENT '结束时间',
  `share` int(11) DEFAULT NULL COMMENT '共享',
  `write_time` date DEFAULT NULL COMMENT '上传时间',
  `dep` bigint(20) DEFAULT NULL COMMENT '上传者所在部门',
  `author_role` bigint(20) DEFAULT NULL COMMENT '上传者角色',
  `year` int(11) DEFAULT NULL COMMENT '年份',
  `quarter` int(11) DEFAULT NULL COMMENT '季度',
  `adult_time` date DEFAULT NULL COMMENT '审核时间',
  `auditor` bigint(20) DEFAULT NULL COMMENT '审核人',
  `view` text COMMENT '审核意见',
  `state` bigint(20) DEFAULT NULL COMMENT '状态',
  `vip_enent` int(11) DEFAULT NULL COMMENT '是否是大事记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--工作计划' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `job_plan_task`
--

DROP TABLE IF EXISTS `job_plan_task`;
CREATE TABLE IF NOT EXISTS `job_plan_task` (
  `id` bigint(20) NOT NULL COMMENT '唯一标示',
  `job_id` bigint(20) NOT NULL COMMENT '心理工_唯一标示',
  `title` text COMMENT '标题',
  `start_time` date DEFAULT NULL COMMENT '开始时间',
  `end_time` date DEFAULT NULL COMMENT '结束时间',
  `persons` text COMMENT '参与人',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `state` bigint(20) DEFAULT NULL COMMENT '完成状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--工作计划--任务';

-- --------------------------------------------------------

--
-- Table structure for table `job_record`
--

DROP TABLE IF EXISTS `job_record`;
CREATE TABLE IF NOT EXISTS `job_record` (
  `id` bigint(20) NOT NULL COMMENT '唯一标示',
  `job_id` bigint(20) DEFAULT NULL COMMENT '心理工_唯一标示',
  `persons` text COMMENT '参与人',
  `emcee` varchar(30) DEFAULT NULL COMMENT '主持人',
  `content` text COMMENT '内容',
  `share` int(11) DEFAULT NULL COMMENT '共享',
  `catalog` bigint(20) DEFAULT NULL COMMENT '工作类别',
  `process` text COMMENT '基本过程',
  `special_record` text COMMENT '特别记录',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--工作记录';

-- --------------------------------------------------------

--
-- Table structure for table `job_report`
--

DROP TABLE IF EXISTS `job_report`;
CREATE TABLE IF NOT EXISTS `job_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `content` text COMMENT '内容',
  `ini_content` text COMMENT '初始内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心理工作部署--工作总结' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `job_statistic`
--

DROP TABLE IF EXISTS `job_statistic`;
CREATE TABLE IF NOT EXISTS `job_statistic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标示',
  `start_time` date DEFAULT NULL COMMENT '开始时间',
  `end_time` date DEFAULT NULL COMMENT '结束时间',
  `report_title` text COMMENT '报告主题',
  `org_id` bigint(20) DEFAULT NULL COMMENT '机构名称',
  `dep` bigint(20) DEFAULT NULL COMMENT '部门',
  `role` bigint(20) DEFAULT NULL COMMENT '角色',
  `catalog` bigint(20) DEFAULT NULL COMMENT '工作类别',
  `reporter` bigint(20) DEFAULT NULL COMMENT '报告统计者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
CREATE TABLE IF NOT EXISTS `organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dwh` varchar(10) DEFAULT NULL COMMENT '单位号',
  `dwmc` varchar(60) DEFAULT NULL COMMENT '单位名称',
  `dwywmc` varchar(180) DEFAULT NULL COMMENT '单位英文名称',
  `dwjc` varchar(12) DEFAULT NULL COMMENT '单位简称',
  `dwywjc` char(8) DEFAULT NULL COMMENT '单位英文简称',
  `dwjp` varchar(12) DEFAULT NULL COMMENT '单位简拼',
  `dwdz` varchar(180) DEFAULT NULL COMMENT '单位地址',
  `lsdwh` varchar(10) DEFAULT NULL COMMENT '隶属单位号',
  `dwlbm` varchar(1) DEFAULT NULL COMMENT '单位类别码',
  `dwbbm` varchar(1) DEFAULT NULL COMMENT '单位办别码',
  `dwyxbs` varchar(1) DEFAULT NULL COMMENT '单位有效标识',
  `sxrq` varchar(8) DEFAULT NULL COMMENT '失效日期',
  `sfst` varchar(1) DEFAULT NULL COMMENT '是否实体',
  `jlny` varchar(6) DEFAULT NULL COMMENT '建立年月',
  `dwfzrh` varchar(20) DEFAULT NULL COMMENT '单位负责人号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `organization_ext`
--

DROP TABLE IF EXISTS `organization_ext`;
CREATE TABLE IF NOT EXISTS `organization_ext` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `zzjb` int(6) DEFAULT NULL COMMENT '组织级别',
  `px` int(11) DEFAULT NULL COMMENT '排序',
  `bjxx` text COMMENT '背景信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `parent`
--

DROP TABLE IF EXISTS `parent`;
CREATE TABLE IF NOT EXISTS `parent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gxm` varchar(2) NOT NULL COMMENT '关系码',
  `cyxm` varchar(36) NOT NULL COMMENT '成员姓名',
  `csny` varchar(6) DEFAULT NULL COMMENT '出生年月',
  `mzm` varchar(2) DEFAULT NULL COMMENT '民族码',
  `gjdqm` varchar(3) DEFAULT NULL COMMENT '国籍/地区码',
  `jkzkm` varchar(1) DEFAULT NULL COMMENT '健康状况码',
  `cygzdw` varchar(60) DEFAULT NULL COMMENT '成员工作单位',
  `cyem` varchar(2) DEFAULT NULL COMMENT '从业码',
  `zyjszwm` varchar(3) DEFAULT NULL COMMENT '专业技术职务码',
  `zwjbm` varchar(2) DEFAULT NULL COMMENT '职务级别码',
  `dh` varchar(30) DEFAULT NULL COMMENT '电话',
  `dzxx` varchar(40) DEFAULT NULL COMMENT '电子信箱',
  `sfjhr` varchar(1) DEFAULT NULL COMMENT '是否监护人',
  `xbm` varchar(1) DEFAULT NULL COMMENT '性别码',
  `xlm` varchar(2) DEFAULT NULL COMMENT '学历码',
  `lxdz` varchar(180) DEFAULT NULL COMMENT '联系地址',
  `sjhm` varchar(30) DEFAULT NULL COMMENT '手机号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `parent_ext`
--

DROP TABLE IF EXISTS `parent_ext`;
CREATE TABLE IF NOT EXISTS `parent_ext` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `bjxx` text COMMENT '背景信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
CREATE TABLE IF NOT EXISTS `permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `perm_name` varchar(45) NOT NULL,
  `permission` varchar(45) NOT NULL,
  `perm_desc` varchar(100) NOT NULL,
  `is_show` char(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

-- --------------------------------------------------------

--
-- Table structure for table `punish`
--

DROP TABLE IF EXISTS `punish`;
CREATE TABLE IF NOT EXISTS `punish` (
  `id` bigint(20) NOT NULL,
  `wjrq` char(8) DEFAULT NULL COMMENT '违纪日期',
  `wjjk` varchar(100) DEFAULT NULL COMMENT '违纪简况',
  `wjlbm` char(2) DEFAULT NULL COMMENT '违纪类别码 WJLB',
  `cfmcm` char(1) DEFAULT NULL COMMENT '处分名称码CFMC',
  `cfyy` varchar(100) DEFAULT NULL COMMENT '处分原因',
  `cfrq` char(8) DEFAULT NULL COMMENT '处分日期',
  `cfwh` varchar(24) DEFAULT NULL COMMENT '处分文号',
  `cfcxrq` char(8) DEFAULT NULL COMMENT '处分撤消日期',
  `cfcxwh` varchar(24) DEFAULT NULL COMMENT '处分撤消文号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='惩处数据JCXS0205';

-- --------------------------------------------------------

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
CREATE TABLE IF NOT EXISTS `resource` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `res_name` varchar(45) NOT NULL,
  `res_key` varchar(45) NOT NULL,
  `res_url` varchar(200) NOT NULL,
  `parent_id` bigint(20) unsigned NOT NULL,
  `parent_ids` varchar(45) NOT NULL,
  `res_sort` int(10) unsigned NOT NULL,
  `is_show` char(1) NOT NULL,
  `icon` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20 ;

-- --------------------------------------------------------

--
-- Table structure for table `reward`
--

DROP TABLE IF EXISTS `reward`;
CREATE TABLE IF NOT EXISTS `reward` (
  `id` bigint(20) NOT NULL,
  `jlmc` varchar(60) DEFAULT NULL COMMENT '奖励名称',
  `jljbm` char(2) DEFAULT NULL COMMENT '奖励级别码JY/T 1001 JB 级别',
  `jldjm` char(1) DEFAULT NULL COMMENT '奖励等级码JLDJ',
  `hjlbm` char(1) DEFAULT NULL COMMENT '获奖类别码XSHJLB',
  `jlyy` varchar(100) DEFAULT NULL COMMENT '奖励原因',
  `jlje` float(8,2) DEFAULT NULL COMMENT '奖励金额',
  `jlwh` varchar(24) DEFAULT NULL COMMENT '奖励文号',
  `jlxnd` char(9) DEFAULT NULL COMMENT '奖励学年度',
  `bjdw` varchar(100) DEFAULT NULL COMMENT '颁奖单位',
  `jllxm` char(1) DEFAULT NULL COMMENT '奖励类型码JY/T 1001 HJLX',
  `jlfsm` char(1) DEFAULT NULL COMMENT '奖励方式码JY/T 1001 JLFS',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='奖励数据JCXS0204';

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) NOT NULL,
  `role` varchar(45) NOT NULL,
  `role_desc` varchar(45) NOT NULL,
  `is_show` char(1) NOT NULL,
  `org_level` int(10) DEFAULT NULL,
  `role_level` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

-- --------------------------------------------------------

--
-- Table structure for table `role_res_perm`
--

DROP TABLE IF EXISTS `role_res_perm`;
CREATE TABLE IF NOT EXISTS `role_res_perm` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) unsigned NOT NULL,
  `res_id` bigint(20) unsigned NOT NULL,
  `perm_ids` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Table structure for table `scale`
--

DROP TABLE IF EXISTS `scale`;
CREATE TABLE IF NOT EXISTS `scale` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '量表编号',
  `code` varchar(32) NOT NULL COMMENT '系统id',
  `title` varchar(255) DEFAULT NULL COMMENT '量表名称',
  `shortname` varchar(20) NOT NULL COMMENT '简称',
  `typeid` tinyint(4) NOT NULL COMMENT '类型',
  `assesstype` int(11) NOT NULL COMMENT '评价类型',
  `dimensions` varchar(30) DEFAULT NULL,
  `source` int(11) NOT NULL COMMENT '量表来源',
  `creationtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `flag` int(11) NOT NULL COMMENT '量表类型标识位',
  `questionnum` int(11) DEFAULT NULL COMMENT '题目数量',
  `startage` int(11) NOT NULL DEFAULT '0',
  `endage` int(11) NOT NULL DEFAULT '100',
  `grade_id` int(11) DEFAULT NULL,
  `reportchart` varchar(30) NOT NULL,
  `prewarn` tinyint(4) NOT NULL,
  `totalscore` tinyint(4) NOT NULL,
  `examtime` int(11) NOT NULL,
  `xmlstr` text COMMENT 'xml串',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `scaledimexplain`
--

DROP TABLE IF EXISTS `scaledimexplain`;
CREATE TABLE IF NOT EXISTS `scaledimexplain` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scaleid` int(11) DEFAULT NULL,
  `wid` varchar(45) DEFAULT NULL,
  `wlevel` int(11) NOT NULL,
  `firststr` varchar(3000) NOT NULL,
  `otherstr` varchar(3000) DEFAULT NULL,
  `advice` varchar(3000) DEFAULT NULL,
  `grade` int(10) unsigned NOT NULL,
  `typeflag` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `scaletype`
--

DROP TABLE IF EXISTS `scaletype`;
CREATE TABLE IF NOT EXISTS `scaletype` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `title` varchar(225) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `scale_norm`
--

DROP TABLE IF EXISTS `scale_norm`;
CREATE TABLE IF NOT EXISTS `scale_norm` (
  `scale_id` int(11) NOT NULL,
  `w_id` varchar(8) NOT NULL,
  `grade_order_id` tinyint(4) NOT NULL,
  `gender` tinyint(4) NOT NULL,
  `m` float NOT NULL,
  `sd` float NOT NULL,
  `type` tinyint(1) DEFAULT NULL COMMENT '1-系统常模,2-自定义常模',
  PRIMARY KEY (`grade_order_id`,`gender`,`scale_id`,`w_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `scale_scoregrade`
--

DROP TABLE IF EXISTS `scale_scoregrade`;
CREATE TABLE IF NOT EXISTS `scale_scoregrade` (
  `scale_id` varchar(45) NOT NULL,
  `wid` varchar(45) NOT NULL,
  `wlevel` int(10) unsigned NOT NULL DEFAULT '0',
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `z` tinyint(4) NOT NULL DEFAULT '1',
  `score1` decimal(5,2) NOT NULL DEFAULT '0.00',
  `score2` decimal(5,2) NOT NULL DEFAULT '0.00',
  `score3` decimal(5,2) NOT NULL DEFAULT '0.00',
  `score4` decimal(5,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `scale_warning`
--

DROP TABLE IF EXISTS `scale_warning`;
CREATE TABLE IF NOT EXISTS `scale_warning` (
  `scale_id` varchar(45) NOT NULL,
  `wid` varchar(45) NOT NULL,
  `wlevel` int(10) unsigned NOT NULL DEFAULT '0',
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `z` tinyint(4) NOT NULL DEFAULT '1',
  `w1` decimal(5,2) DEFAULT '0.00',
  `w2` decimal(5,2) DEFAULT '0.00',
  `w3` decimal(5,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `school`
--

DROP TABLE IF EXISTS `school`;
CREATE TABLE IF NOT EXISTS `school` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `xxdm` varchar(10) DEFAULT NULL COMMENT '学校代码',
  `xxmc` varchar(60) NOT NULL COMMENT '学校名称',
  `xxywmc` varchar(180) DEFAULT NULL COMMENT '学校英文名称',
  `xxdz` varchar(180) DEFAULT NULL COMMENT '学校地址',
  `xxyzbm` varchar(6) DEFAULT NULL COMMENT '学校邮政编码',
  `xzqhm` varchar(6) DEFAULT NULL COMMENT '行政区划码',
  `jxny` varchar(6) DEFAULT NULL COMMENT '建校年月',
  `xqr` varchar(60) DEFAULT NULL COMMENT '校庆日',
  `xxbxlxm` varchar(3) DEFAULT NULL COMMENT '学校办学类型码',
  `xxjbzm` varchar(3) DEFAULT NULL COMMENT '学校举办者码',
  `xxzgbmm` varchar(3) DEFAULT NULL COMMENT '学校主管部门码',
  `fddbrh` varchar(20) DEFAULT NULL COMMENT '法定代表人号',
  `frzsh` varchar(20) DEFAULT NULL COMMENT '法人证书号',
  `xzgh` varchar(20) DEFAULT NULL COMMENT '校长工号',
  `xzxm` varchar(36) DEFAULT NULL COMMENT '校长姓名',
  `dwfzrh` varchar(20) DEFAULT NULL COMMENT '党委负责人号',
  `zzjgm` varchar(10) DEFAULT NULL COMMENT '组织机构码',
  `lxdh` varchar(30) DEFAULT NULL COMMENT '联系电话',
  `czdh` varchar(30) DEFAULT NULL COMMENT '传真电话',
  `dzxx` varchar(40) DEFAULT NULL COMMENT '电子信箱',
  `zydz` varchar(60) DEFAULT NULL COMMENT '主页地址',
  `lsyg` text COMMENT '历史沿革',
  `xxbbm` varchar(2) DEFAULT NULL COMMENT '学校办别码',
  `sszgdwm` varchar(6) DEFAULT NULL COMMENT '所属主管单位码',
  `szdcxlxm` varchar(3) DEFAULT NULL COMMENT '所在地城乡类型码',
  `szdjjsxm` varchar(1) DEFAULT NULL COMMENT '所在地经济属性码',
  `szdmzsx` varchar(1) DEFAULT NULL COMMENT '所在地民族属性',
  `xxxz` decimal(3,1) DEFAULT NULL COMMENT '小学学制',
  `xxrxnl` decimal(1,0) DEFAULT NULL COMMENT '小学入学年龄',
  `czxz` decimal(3,1) DEFAULT NULL COMMENT '初中学制',
  `czrxnl` decimal(2,0) DEFAULT NULL COMMENT '初中入学年龄',
  `gzxz` decimal(3,1) DEFAULT NULL COMMENT '高中学制',
  `zjxyym` varchar(3) DEFAULT NULL COMMENT '主教学语言码',
  `fjxyym` varchar(3) DEFAULT NULL COMMENT '辅教学语言码',
  `zsbj` varchar(30) DEFAULT NULL COMMENT '招生半径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `schooleroll`
--

DROP TABLE IF EXISTS `schooleroll`;
CREATE TABLE IF NOT EXISTS `schooleroll` (
  `id` bigint(20) NOT NULL,
  `rxny` char(6) NOT NULL COMMENT '入学年月',
  `xslbm` varchar(5) NOT NULL COMMENT '学生类别码',
  `xz` varchar(10) NOT NULL COMMENT '学制',
  `zym` varchar(3) DEFAULT NULL COMMENT '专业码',
  `szbh` varchar(10) DEFAULT NULL COMMENT '所在班号',
  `sznj` varchar(10) DEFAULT NULL COMMENT '所在年级',
  `xsdqztm` varchar(2) NOT NULL COMMENT '学生当前状态码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学籍基本信息';

-- --------------------------------------------------------

--
-- Table structure for table `schoolrollchange`
--

DROP TABLE IF EXISTS `schoolrollchange`;
CREATE TABLE IF NOT EXISTS `schoolrollchange` (
  `id` bigint(20) NOT NULL,
  `ydlbm` char(2) DEFAULT NULL COMMENT '异动类别码',
  `ydrq` char(8) DEFAULT NULL COMMENT '异动日期',
  `ydyym` char(2) DEFAULT NULL COMMENT '异动原因码',
  `sprq` char(8) DEFAULT NULL COMMENT '审批日期',
  `spwh` varchar(24) DEFAULT NULL COMMENT '审批文号',
  `ydlyxxm` varchar(10) DEFAULT NULL COMMENT '异动来源学校码',
  `ydqxxxm` varchar(10) DEFAULT NULL COMMENT '异动去向学校码',
  `ydsm` text COMMENT '异动说明',
  `yyxsh` varchar(10) DEFAULT NULL COMMENT '原院系所号',
  `yzym` varchar(10) DEFAULT NULL COMMENT '原专业码',
  `ybh` varchar(10) DEFAULT NULL COMMENT '原班号',
  `ynj` varchar(10) DEFAULT NULL COMMENT '原年级',
  `yxz` varchar(10) DEFAULT NULL COMMENT '原学制',
  `xyxsh` varchar(10) DEFAULT NULL COMMENT '现院系所号',
  `xzym` varchar(10) DEFAULT NULL COMMENT '现专业码',
  `xbh` varchar(10) DEFAULT NULL COMMENT '现班号',
  `xnj` varchar(10) DEFAULT NULL COMMENT '现年级',
  `xxz` varchar(10) DEFAULT NULL COMMENT '现学制',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学籍异动数据';

-- --------------------------------------------------------

--
-- Table structure for table `school_class`
--

DROP TABLE IF EXISTS `school_class`;
CREATE TABLE IF NOT EXISTS `school_class` (
  `bh` varchar(10) NOT NULL COMMENT '班号',
  `xxdm` varchar(10) NOT NULL COMMENT '学校代码',
  PRIMARY KEY (`bh`,`xxdm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `school_ext`
--

DROP TABLE IF EXISTS `school_ext`;
CREATE TABLE IF NOT EXISTS `school_ext` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `px` int(5) DEFAULT NULL COMMENT '排序',
  `bjxx` text COMMENT '背景信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `xh` varchar(20) DEFAULT NULL COMMENT '学号',
  `xm` varchar(36) DEFAULT NULL COMMENT '姓名',
  `ywxm` varchar(60) DEFAULT NULL COMMENT '英文姓名',
  `xmpy` varchar(60) DEFAULT NULL COMMENT '姓名拼音',
  `cym` varchar(36) DEFAULT NULL COMMENT '曾用名',
  `xbm` char(1) DEFAULT NULL COMMENT '性别码',
  `csrq` date DEFAULT NULL COMMENT '出生日期',
  `csdm` varchar(6) DEFAULT NULL COMMENT '出生地码',
  `jg` varchar(20) DEFAULT NULL COMMENT '籍贯',
  `mzm` varchar(2) DEFAULT NULL COMMENT '民族码',
  `gjdqm` char(3) DEFAULT NULL COMMENT '国籍/地区码',
  `sfzjlxm` char(1) DEFAULT NULL COMMENT '身份证类型码',
  `sfzjh` varchar(18) DEFAULT NULL COMMENT '身份证件号',
  `hyzkm` varchar(2) DEFAULT NULL COMMENT '婚姻状况码',
  `gatqwm` varchar(2) DEFAULT NULL COMMENT '港澳台侨外码',
  `zzmmm` varchar(2) DEFAULT NULL COMMENT '政治面貌码',
  `jkzkm` char(1) DEFAULT NULL COMMENT '健康状况码',
  `xyzjm` varchar(2) DEFAULT NULL COMMENT '信仰宗教码',
  `xxm` char(1) DEFAULT NULL COMMENT '血型码',
  `zp` blob COMMENT '照片',
  `sfzjyxq` varchar(20) DEFAULT NULL COMMENT '身份证件有效期',
  `dszybz` char(1) DEFAULT NULL COMMENT '独生子女标志',
  `rxny` char(6) DEFAULT NULL COMMENT '入学年月',
  `nj` varchar(10) DEFAULT NULL COMMENT '年级',
  `bh` varchar(20) DEFAULT NULL COMMENT '班号',
  `xslbm` varchar(2) DEFAULT NULL COMMENT '学生类别码',
  `xzz` varchar(100) DEFAULT NULL COMMENT '现住址',
  `hkszd` varchar(200) DEFAULT NULL COMMENT '户口所在地',
  `hkxzm` char(1) DEFAULT NULL COMMENT '户口性质码',
  `sfldrk` char(1) DEFAULT NULL COMMENT '是否流动人口',
  `tc` text COMMENT '特长',
  `lxdh` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `txdz` varchar(200) DEFAULT NULL COMMENT '通信地址',
  `yzbm` varchar(10) DEFAULT NULL COMMENT '邮政编码',
  `dzxx` varchar(50) DEFAULT NULL COMMENT '电子信箱',
  `zydz` varchar(200) DEFAULT NULL COMMENT '主页地址',
  `xjh` varchar(30) DEFAULT NULL COMMENT '学籍号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `studentgb`
--

DROP TABLE IF EXISTS `studentgb`;
CREATE TABLE IF NOT EXISTS `studentgb` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `xh` varchar(36) DEFAULT NULL COMMENT '学号',
  `xm` varchar(36) DEFAULT NULL COMMENT '姓名',
  `ywxm` varchar(60) DEFAULT NULL COMMENT '英文姓名',
  `xmpy` varchar(60) DEFAULT NULL COMMENT '姓名拼音',
  `cym` varchar(36) DEFAULT NULL COMMENT '曾用名',
  `xbm` char(1) DEFAULT NULL COMMENT '性别码 GB/T 2261.1',
  `csrq` char(8) DEFAULT NULL COMMENT '出生日期',
  `csdm` varchar(6) DEFAULT NULL COMMENT '出生地码 GB/T 2260',
  `jg` varchar(20) DEFAULT NULL COMMENT '籍贯',
  `mzm` varchar(2) DEFAULT NULL COMMENT '民族码 GB/T 3304',
  `gjdqm` char(3) DEFAULT NULL COMMENT '国籍/地区 GB/T 2659',
  `sfzjlxm` char(1) DEFAULT NULL COMMENT '身份证件类 JY/T 1001',
  `sfzjh` varchar(18) DEFAULT NULL COMMENT '身份证件号',
  `hyzkm` varchar(2) DEFAULT NULL COMMENT '婚姻状况码 GB/T 2261.2',
  `gatqwm` varchar(2) DEFAULT NULL COMMENT '港澳台侨外码 JY/T 1001 GATQW',
  `zzmmm` varchar(2) DEFAULT NULL COMMENT '政治面貌码 GB/T 4762',
  `jkzkm` char(1) DEFAULT NULL COMMENT '健康状况码 GB/T 2261.3',
  `xyzjm` varchar(2) DEFAULT NULL COMMENT '信仰宗教码 GA 214.12',
  `xxm` char(1) DEFAULT NULL COMMENT '血型码 JY/T 1001 XX',
  `zp` blob COMMENT '照片',
  `sfzjyxq` varchar(20) DEFAULT NULL COMMENT '身份证件有效期YYYYMMDD-YYYYMMDD',
  `dszybz` char(1) DEFAULT NULL COMMENT '是否独生子女 JY/T 1001 SFBZ',
  `rxny` char(6) DEFAULT NULL COMMENT '入学年月 YYYYMM',
  `nj` varchar(10) DEFAULT NULL COMMENT '年级',
  `bh` varchar(20) DEFAULT NULL COMMENT '班号',
  `xslbm` varchar(2) DEFAULT NULL COMMENT '学生类别码 JY/T 1001 XSLB',
  `xzz` varchar(100) DEFAULT NULL COMMENT '现住址',
  `hkszd` varchar(200) DEFAULT NULL COMMENT '户口所在地',
  `hkxzm` char(1) DEFAULT NULL COMMENT '户口性质码 GA 324.1',
  `sfldrk` char(1) DEFAULT NULL COMMENT '是否流动人口',
  `tc` text COMMENT '特长',
  `lxdh` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `txdz` varchar(200) DEFAULT NULL COMMENT '通信地址',
  `yzbm` varchar(10) DEFAULT NULL COMMENT '邮政编码',
  `dzxx` varchar(50) DEFAULT NULL COMMENT '电子信箱',
  `zydz` varchar(200) DEFAULT NULL COMMENT '主页地址',
  `xjh` varchar(30) DEFAULT NULL COMMENT '学籍号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='国标学生信息' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `student_ext`
--

DROP TABLE IF EXISTS `student_ext`;
CREATE TABLE IF NOT EXISTS `student_ext` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `bjxx` text COMMENT '背景信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `syslog`
--

DROP TABLE IF EXISTS `syslog`;
CREATE TABLE IF NOT EXISTS `syslog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `orglevelid` int(10) unsigned NOT NULL,
  `orgid` int(10) unsigned NOT NULL,
  `roleid` int(10) unsigned NOT NULL,
  `menuid` int(10) unsigned NOT NULL COMMENT 'һ��ģ��',
  `permissionid` int(10) unsigned NOT NULL,
  `optime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `operator` varchar(45) NOT NULL COMMENT '������',
  `submenuid` int(10) unsigned NOT NULL COMMENT '����ģ��',
  `content` varchar(100) NOT NULL COMMENT '��������',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gh` varchar(20) DEFAULT NULL COMMENT '工号',
  `xm` varchar(36) DEFAULT NULL COMMENT '姓名',
  `ywxm` varchar(60) NOT NULL COMMENT '英文姓名',
  `xmpy` varchar(60) DEFAULT '0' COMMENT '姓名拼音',
  `cym` varchar(36) DEFAULT '0' COMMENT '曾用名',
  `xbm` varchar(1) DEFAULT NULL COMMENT '性别码',
  `csrq` varchar(8) DEFAULT NULL COMMENT '出生日期',
  `csdm` varchar(6) DEFAULT NULL COMMENT '出生地码',
  `jg` varchar(20) DEFAULT NULL COMMENT '籍贯',
  `mzm` varchar(2) DEFAULT NULL COMMENT '民族码',
  `gjdqm` varchar(3) DEFAULT NULL COMMENT '国籍/地区码',
  `sfzjlxm` varchar(1) DEFAULT NULL COMMENT '身份证件类型码',
  `sfzjh` varchar(20) DEFAULT NULL COMMENT '身份证件号',
  `hyzkm` varchar(2) DEFAULT NULL COMMENT '婚姻状况码',
  `gatqwm` varchar(2) DEFAULT NULL COMMENT '港澳台侨外码',
  `zzmmm` varchar(2) DEFAULT NULL COMMENT '政治面貌码',
  `jkzkm` varchar(1) DEFAULT NULL COMMENT '健康状况码',
  `xyzjm` varchar(2) DEFAULT NULL COMMENT '信仰宗教码',
  `xxm` varchar(1) DEFAULT NULL COMMENT '血型码',
  `zp` blob COMMENT '照片',
  `sfzjyxq` varchar(17) DEFAULT NULL COMMENT '身份证件有效期\r\n                        格式：\r\n                        YYYYMMDD-YYYYMMDD',
  `jgh` varchar(10) DEFAULT NULL COMMENT '机构号',
  `jtzz` varchar(180) DEFAULT NULL COMMENT '家庭住址',
  `xzz` varchar(180) DEFAULT NULL COMMENT '现住址',
  `hkszd` varchar(180) DEFAULT NULL COMMENT '户口所在地',
  `hkxzm` varchar(1) DEFAULT NULL COMMENT '户口性质码',
  `xlm` varchar(2) DEFAULT NULL COMMENT '学历码',
  `gzny` varchar(6) DEFAULT NULL COMMENT '参加工作年月',
  `lxny` varchar(6) DEFAULT NULL COMMENT '来校年月',
  `cjny` varchar(6) DEFAULT NULL COMMENT '从教年月',
  `bzlbm` varchar(1) DEFAULT NULL COMMENT '编制类别码',
  `dabh` varchar(10) DEFAULT NULL COMMENT '档案编号',
  `dawb` text COMMENT '档案文本',
  `txdz` varchar(180) DEFAULT NULL COMMENT '通信地址',
  `lxdh` varchar(30) DEFAULT NULL COMMENT '联系电话',
  `yzbm` varchar(6) DEFAULT NULL COMMENT '邮政编码',
  `dzxx` varchar(40) DEFAULT NULL COMMENT '电子信箱',
  `zydz` varchar(60) DEFAULT NULL COMMENT '主页地址',
  `tc` text COMMENT '特长',
  `gwzym` varchar(2) DEFAULT NULL COMMENT '岗位职业码',
  `zyrkxd` varchar(1) DEFAULT NULL COMMENT '主要任课学段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `teacher_ext`
--

DROP TABLE IF EXISTS `teacher_ext`;
CREATE TABLE IF NOT EXISTS `teacher_ext` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `bjxx` text COMMENT '背景信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_org_job`
--

DROP TABLE IF EXISTS `user_org_job`;
CREATE TABLE IF NOT EXISTS `user_org_job` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `org_id` bigint(20) unsigned NOT NULL,
  `job_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `user_res_perm`
--

DROP TABLE IF EXISTS `user_res_perm`;
CREATE TABLE IF NOT EXISTS `user_res_perm` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `res_id` bigint(20) unsigned NOT NULL,
  `perm_ids` varchar(45) NOT NULL,
  `auth_type` varchar(45) NOT NULL COMMENT '管理员直接分配，或者是用户授权',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='给用户直接授权' AUTO_INCREMENT=1 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `examresult_dim_mental_health`
--
ALTER TABLE `examresult_dim_mental_health`
  ADD CONSTRAINT `fk_reference_38` FOREIGN KEY (`result_id`) REFERENCES `examresult_dim_teacher` (`result_id`),
  ADD CONSTRAINT `fk_reference_37` FOREIGN KEY (`result_id`) REFERENCES `examresult_dim_student` (`result_id`);

--
-- Constraints for table `job_congress`
--
ALTER TABLE `job_congress`
  ADD CONSTRAINT `fk_reference_28` FOREIGN KEY (`id`) REFERENCES `dic_job_notice_type` (`id`),
  ADD CONSTRAINT `fk_reference_1` FOREIGN KEY (`id`) REFERENCES `account` (`id`),
  ADD CONSTRAINT `fk_reference_10` FOREIGN KEY (`id`) REFERENCES `class` (`id`),
  ADD CONSTRAINT `fk_reference_13` FOREIGN KEY (`id`) REFERENCES `dic_common_sf` (`id`),
  ADD CONSTRAINT `fk_reference_16` FOREIGN KEY (`id`) REFERENCES `dic_job_catalog` (`id`),
  ADD CONSTRAINT `fk_reference_19` FOREIGN KEY (`id`) REFERENCES `dic_job_notice_catalog` (`id`),
  ADD CONSTRAINT `fk_reference_22` FOREIGN KEY (`id`) REFERENCES `dic_job_notice_share` (`id`),
  ADD CONSTRAINT `fk_reference_25` FOREIGN KEY (`id`) REFERENCES `dic_job_notice_state` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
