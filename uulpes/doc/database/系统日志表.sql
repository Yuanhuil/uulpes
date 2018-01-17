DROP TABLE IF EXISTS `njproject`.`syslog`;
CREATE TABLE  `njproject`.`syslog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `orglevelid` int(10) unsigned NOT NULL,
  `orgid` int(10) unsigned NOT NULL,
  `roleid` int(10) unsigned NOT NULL,
  `menuid` int(10) unsigned NOT NULL COMMENT '一级模块',
  `permissionid` int(10) unsigned NOT NULL,
  `optime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `operator` varchar(45) NOT NULL COMMENT '操作人',
  `submenuid` int(10) unsigned NOT NULL COMMENT '二级模块',
  `content` varchar(100) NOT NULL COMMENT '操作内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;