-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 02, 2015 at 03:32 AM
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

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`id`, `username`, `password`, `idcard`, `create_time`, `update_time`, `type_flag`, `state`, `admin`) VALUES
(1, 'admin', 'f6fdffe48c908deb0f4c3bd36c032e72', NULL, '2015-03-19 00:00:00', '2015-03-09 00:00:00', 0, NULL, 1);

--
-- Dumping data for table `auth`
--

INSERT INTO `auth` (`id`, `org_id`, `job_id`, `user_id`, `group_id`, `role_id`, `auth_type`) VALUES
(1, 0, 0, 0, 0, 1, 'user'),
(2, 0, 0, 2, 0, 2, 'user'),
(3, 0, 0, 3, 0, 3, 'user'),
(4, 0, 0, 4, 0, 4, 'user'),
(5, 0, 0, 5, 0, 5, 'user'),
(6, 0, 0, 6, 0, 6, 'user'),
(7, 0, 0, 7, 0, 7, 'user'),
(8, 0, 0, 8, 0, 8, 'user'),
(9, 0, 0, 9, 0, 9, 'user');

--
-- Dumping data for table `dic_account_state`
--

INSERT INTO `dic_account_state` (`id`, `name`, `sort`) VALUES
(1, '正常状态', 1),
(2, '锁定状态', 2),
(3, '删除状态', 3),
(4, '禁止访问', 4);

--
-- Dumping data for table `dic_sex`
--

INSERT INTO `dic_sex` (`id`, `name`) VALUES
('1', '男'),
('2', '女');

--
-- Dumping data for table `permission`
--

INSERT INTO `permission` (`id`, `perm_name`, `permission`, `perm_desc`, `is_show`) VALUES
(1, '所有', '*', '所有数据操作的权限', '1'),
(2, '增加', 'create', '新增数据操作的权限', '1'),
(3, '修改', 'update', '修改数据操作的权限', '1'),
(4, '删除', 'delete', '删除数据操作的权限', '1'),
(5, '查看', 'view', '查看数据操作的权限', '1'),
(6, '审核', 'audit', '审核数据操作的权限', '1'),
(7, '可授权', 'authorize', '可授权功能给其他人', '1');

--
-- Dumping data for table `resource`
--

INSERT INTO `resource` (`id`, `res_name`, `res_key`, `res_url`, `parent_id`, `parent_ids`, `res_sort`, `is_show`, `icon`) VALUES
(1, '资源', '', '', 0, '0/', 0, '1', NULL),
(2, '单位首页', 'homepage', '/homepage', 1, '0/1', 1, '1', NULL),
(3, '基本信息中心', 'systeminfo', '', 1, '0/1', 2, '1', NULL),
(4, '心理检测中心', 'assessmentcenter', '/assessmentcenter', 1, '0/1', 3, '1', NULL),
(5, '心理辅导中心', 'workschedule', '/workschedule', 1, '0/1', 4, '1', NULL),
(6, '心理设备中心', 'equipmanage', '/equipmanage', 1, '0/1', 5, '1', NULL),
(7, '心理干预中心', 'consultcenter', '/consultcenter', 1, '0/1', 6, '1', NULL),
(8, '心理档案中心', 'archivemanage', '/archivemanage', 1, '0/1', 7, '1', NULL),
(9, '用户管理', 'user', '/systeminfo/sys/rolej.do', 3, '0/1/3', 1, '1', NULL),
(11, '陶老师工作站', 'taostation', '/taostation', 1, '0/1', 8, '1', NULL),
(12, '机构设置', '', '', 3, '0/1/3', 2, '1', NULL),
(13, '本级机构设计', '', '', 12, '0/1/3/12', 1, '1', NULL),
(14, '下属机构设置', '', '', 12, '0/1/3/12', 2, '1', NULL),
(15, '角色权限', '', '', 3, '0/1/3', 3, '1', NULL),
(16, '系统管理', '', '', 3, '0/1/3', 4, '1', NULL),
(17, '个人空间', '', '', 3, '0/1/3', 5, '1', NULL),
(18, '操作日志', '', '', 3, '0/1/3', 6, '1', NULL),
(19, '资源管理', 'resource', '/systeminfo/sys/resource/tree.do', 3, '0/1/3', 7, '1', NULL);

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `role_name`, `role`, `role_desc`, `is_show`, `org_level`, `role_level`) VALUES
(1, '超级管理员', 'admin', '拥有所有权限', '1', 1, 1),
(2, '示例管理员', 'example_admin', '拥有示例管理的所有权限', '1', 1, 1),
(3, '系统管理员', 'sys_admin', '拥有系统管理的所有权限', '1', 1, 1),
(4, '维护管理员', 'conf_admin', '拥有维护管理的所有权限', '1', 1, 1),
(5, '新增管理员', 'create_admin', '拥有新增/查看管理的所有权限', '1', 1, 1),
(6, '修改管理员', 'update_admin', '拥有修改/查看管理的所有权限', '1', 1, 1),
(7, '删除管理员', 'delete_admin', '拥有删除/查看管理的所有权限', '1', 1, 1),
(8, '查看管理员', 'view_admin', '拥有查看管理的所有权限', '1', 1, 1),
(9, '审核管理员', 'audit_admin', '拥有审核管理的所有权限', '1', 1, 1),
(10, '监控管理员', 'audit_admin', '拥有审核管理的所有权限', '1', 1, 1),
(11, '市级管理员', 'city_admin', '市级管理员', '1', 3, 3),
(12, '市级负责人', 'city_vip', '市级负责人', '1', 3, 3);

--
-- Dumping data for table `role_res_perm`
--

INSERT INTO `role_res_perm` (`id`, `role_id`, `res_id`, `perm_ids`) VALUES
(1, 1, 2, '1'),
(2, 1, 3, '1'),
(3, 1, 4, '1'),
(4, 1, 5, '1');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
