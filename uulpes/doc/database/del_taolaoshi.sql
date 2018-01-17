-- 删除陶老师工作站的数据表

DROP TABLE IF EXISTS `tao_workstation`;

DROP TABLE IF EXISTS `tao_volunteer`;

DROP TABLE IF EXISTS `tao_training`;

DROP TABLE IF EXISTS `tao_supervision`;

DROP TABLE IF EXISTS `tao_ss_attachment`;

DROP TABLE IF EXISTS `tao_ssrecord`;

DROP TABLE IF EXISTS `tao_ssattachment_mapping`;

DROP TABLE IF EXISTS `tao_servicestation`;

DROP TABLE IF EXISTS `tao_ec_mapping`;

DROP TABLE IF EXISTS `tao_dailyreception`;

DROP TABLE IF EXISTS `tao_consultant`;

DROP TABLE IF EXISTS `tao_concertification`;

DROP TABLE IF EXISTS `tao_coach_warning_student`;

DROP TABLE IF EXISTS `tao_coach_train`;

DROP TABLE IF EXISTS `tao_coach_supervision`;

DROP TABLE IF EXISTS `tao_coach_record`;

DROP TABLE IF EXISTS `tao_coach_reception`;

DROP TABLE IF EXISTS `tao_appointment`;

DROP TABLE IF EXISTS `tao_allowancesetting`;

-- ----------------------------------------------------------------------

-- 删除resource

DELETE FROM `resource` WHERE (`id`IN(73,70,81,102,101,103,125,69,104,75,74,71,107,106,82,83,105,68,11,109,108,111,112,67));

-- ----------------------------------------------------------------------

-- 删除role

DELETE FROM `role` WHERE (`id`IN('42','48','49','50','51','52','53','54','68'));

-- ----------------------------------------------------------------------

-- 删除resource-role-perm的关系表数据

DELETE FROM `role_res_perm` WHERE (`role_id`IN('42','48','49','50','51','52','53','54','68'));
DELETE FROM `role_res_perm` WHERE (`res_id`IN(73,70,81,102,101,103,125,69,104,75,74,71,107,106,82,83,105,68,11,109,108,111,112,67));