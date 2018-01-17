DROP TABLE IF EXISTS `njproject`.`syslog`;
CREATE TABLE  `njproject`.`syslog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `orglevelid` int(10) unsigned NOT NULL,
  `orgid` int(10) unsigned NOT NULL,
  `roleid` int(10) unsigned NOT NULL,
  `menuid` int(10) unsigned NOT NULL COMMENT 'һ��ģ��',
  `permissionid` int(10) unsigned NOT NULL,
  `optime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `operator` varchar(45) NOT NULL COMMENT '������',
  `submenuid` int(10) unsigned NOT NULL COMMENT '����ģ��',
  `content` varchar(100) NOT NULL COMMENT '��������',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;