
use org_manager;

drop table manager_operation_log;

CREATE TABLE `manager_operation_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `browser_info` varchar(500) NOT NULL DEFAULT '' COMMENT '浏览器信息',
  `request_url` varchar(100) NOT NULL DEFAULT '' COMMENT '请求url',
  `request_class_method` varchar(500) NOT NULL DEFAULT '' COMMENT '具体方法',
  `operator_ip` varchar(30) NOT NULL DEFAULT '0' COMMENT '操作ip',
  `operator_desc` varchar(60) NOT NULL DEFAULT '' COMMENT '操作描述',
  `request_param` varchar(500) NOT NULL DEFAULT '' COMMENT '操作的入参',
  `org_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '操作者 组织id',
  `consume_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '业务操作耗费时间 毫秒',
  `member_id` varchar(100) NOT NULL DEFAULT '0' COMMENT '操作者',
  `login_org_id` bigint(20) NOT NULL COMMENT '登录者组织ID',
  `account` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '登陆的组织编码或邮箱',
  `operate_result` varchar(50) DEFAULT '成功' COMMENT '操作结果',
  `response_result` varchar(500) DEFAULT '' COMMENT '返回结果',
  `exception_message` varchar(200) DEFAULT NULL COMMENT '异常信息',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2373 DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';



