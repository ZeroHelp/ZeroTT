/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;

CREATE TABLE `lottery_switch_control` (
  `id` varchar(255) NOT NULL default '' COMMENT '����ID',
  `name` varchar(255) NOT NULL default '' COMMENT '��������',
  `status` int(11) NOT NULL default '0' COMMENT 'ʱ�����״̬ 0:�ر� 1:��',
  `gmt_created` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '��¼�Ĵ���ʱ��',
  `gmt_modified` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '��¼���޸�ʱ��',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='��Ʊ���ر�';

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
