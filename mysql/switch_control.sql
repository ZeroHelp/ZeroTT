/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;

CREATE TABLE `lottery_switch_control` (
  `id` varchar(255) NOT NULL default '' COMMENT '开关ID',
  `name` varchar(255) NOT NULL default '' COMMENT '开关名称',
  `status` int(11) NOT NULL default '0' COMMENT '时间程序状态 0:关闭 1:打开',
  `gmt_created` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '记录的创建时间',
  `gmt_modified` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '记录的修改时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='彩票开关表';

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
