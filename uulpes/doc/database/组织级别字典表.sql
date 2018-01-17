DROP TABLE IF EXISTS `njproject`.`dic_organizationlevel`;
CREATE TABLE  `njproject`.`dic_organizationlevel` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `orglevelname` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织机构级别';