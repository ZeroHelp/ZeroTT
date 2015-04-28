/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;

CREATE TABLE `lottery_timetask_control` (
  `id` varchar(255) NOT NULL default '' COMMENT 'ʱ����������',
  `type` int(1) unsigned zerofill default '0' COMMENT '1: ʱ����� 2: ��ͨ����',
  `cronexp` varchar(255) default NULL COMMENT 'ʱ�������ʽ',
  `target_ip` varchar(15) NOT NULL default '' COMMENT '�������еĻ���IP',
  `name` varchar(255) NOT NULL default '' COMMENT 'ʱ���������',
  `status` int(11) NOT NULL default '0' COMMENT 'ʱ�����״̬ 0:���ùر� 1:������ 2:��������',
  `magic` varchar(255) default NULL COMMENT 'ʱ����򸽼�����',
  `last_target_ip` varchar(15) default '' COMMENT '���һ�����д�ʱ������IP',
  `last_start_time` datetime default '0000-00-00 00:00:00' COMMENT '���һ�����п�ʼʱ��',
  `last_end_time` datetime default '0000-00-00 00:00:00' COMMENT '���һ�����н���ʱ��',
  `gmt_created` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '��¼�Ĵ���ʱ��',
  `gmt_modified` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '��¼���޸�ʱ��',
  `category` varchar(255) default NULL COMMENT '��ѯ��������',
  PRIMARY KEY  (`id`,`target_ip`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��Ʊʱ�����';

INSERT INTO `lottery_timetask_control` VALUES ('SSQ_BJFC_Award',1,'*/1 * * * * ?','169.254.62.189','Beijing FC - SSQ - BOOK',1,'magic','169.254.62.189','2011-01-27 22:16:49','2011-01-27 22:16:49','2011-01-27 22:16:49','2011-01-27 22:16:49','updated categeory');
INSERT INTO `lottery_timetask_control` VALUES ('HELLO',1,'*/1 * * * * ?','169.254.62.189','Hello Test',1,'magic','10.32.20.69','2011-01-27 22:16:49','2011-01-27 22:16:49','2011-01-27 22:16:49','2011-01-27 22:16:49','updated categeory');

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
