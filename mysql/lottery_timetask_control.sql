/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;

CREATE TABLE `lottery_timetask_control` (
  `id` varchar(255) NOT NULL default '' COMMENT '时间任务主键',
  `type` int(1) unsigned zerofill default '0' COMMENT '1: 时间程序 2: 普通开关',
  `cronexp` varchar(255) default NULL COMMENT '时间程序表达式',
  `target_ip` varchar(15) NOT NULL default '' COMMENT '允许运行的机器IP',
  `name` varchar(255) NOT NULL default '' COMMENT '时间程序名称',
  `status` int(11) NOT NULL default '0' COMMENT '时间程序状态 0:永久关闭 1:可运行 2:不可运行',
  `magic` varchar(255) default NULL COMMENT '时间程序附加条件',
  `last_target_ip` varchar(15) default '' COMMENT '最后一次运行此时间程序的IP',
  `last_start_time` datetime default '0000-00-00 00:00:00' COMMENT '最后一次运行开始时间',
  `last_end_time` datetime default '0000-00-00 00:00:00' COMMENT '最后一次运行结束时间',
  `gmt_created` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '记录的创建时间',
  `gmt_modified` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '记录的修改时间',
  `category` varchar(255) default NULL COMMENT '查询过滤条件',
  PRIMARY KEY  (`id`,`target_ip`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='彩票时间程序';

INSERT INTO `lottery_timetask_control` VALUES ('SSQ_BJFC_Award',1,'*/1 * * * * ?','169.254.62.189','Beijing FC - SSQ - BOOK',1,'magic','169.254.62.189','2011-01-27 22:16:49','2011-01-27 22:16:49','2011-01-27 22:16:49','2011-01-27 22:16:49','updated categeory');
INSERT INTO `lottery_timetask_control` VALUES ('HELLO',1,'*/1 * * * * ?','169.254.62.189','Hello Test',1,'magic','10.32.20.69','2011-01-27 22:16:49','2011-01-27 22:16:49','2011-01-27 22:16:49','2011-01-27 22:16:49','updated categeory');

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
