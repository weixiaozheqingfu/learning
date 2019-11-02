CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `account` varchar(50) NOT NULL default '' COMMENT '账号',
  `password` varchar(50) NOT NULL default '' COMMENT '密码',
  `phone` varchar(20) NOT NULL default '' COMMENT '手机',
  `full_name` varchar(50) NOT NULL default '' COMMENT '姓名',
  `nick_name` varchar(50) NOT NULL default '' COMMENT '昵称',
  `sex` tinyint(3) NOT NULL default '0' COMMENT '0:未填写,1:男,2:女,3:未知',
  `age` tinyint(3) NOT NULL default '0' COMMENT '年龄',
  `remark` varchar(200) NOT NULL default '' COMMENT '备注',
  `delete_flag` bit(1) NOT NULL default '\0' COMMENT '0:未删除 1：已删除',
  `register_time` datetime default NULL COMMENT '注册时间',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

INSERT INTO `user_info` VALUES ('1', 'gaoshanliushui', 'qwer1234', '13120411529', '高山流水', '', '0', '0', '', '\0', '2019-10-25 11:01:25', '2019-10-25 11:01:25', '2019-10-25 11:01:25');

CREATE TABLE `oauth_client_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `client_id` varchar(50) NOT NULL default '' COMMENT '客户端应用id',
  `client_secret` varchar(50) NOT NULL default '' COMMENT '客户端应用秘钥',
  `client_name` varchar(50) NOT NULL default '' COMMENT '客户端应用名称',
  `redirect_uri` varchar(200) NOT NULL default '' COMMENT '客户端应用回调地址',
  `logout_uri` varchar(200) NOT NULL default '' COMMENT '客户端应用注销登录地址',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户端信息表';

INSERT INTO `oauth_client_info` VALUES (1, '1001', '123456', 'sso_client1', 'http://localhost:8081/auth/sso/callback', 'http://localhost:8081/auth/sso/logout', '2019-10-25 18:05:33', '2019-10-25 18:05:40');
INSERT INTO `oauth_client_info` VALUES (2, '1002', '654321', 'sso_client2', 'http://localhost:8082/auth/sso/callback', 'http://localhost:8082/auth/sso/logout', '2019-10-25 18:05:33', '2019-10-25 18:05:40');

-- 该表仅仅作为一种象征性的结果,不予创建,使用redis缓存会话代替本表。
CREATE TABLE `session_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `jsessionid` varchar(128) NOT NULL default '' COMMENT 'jsessionid',
  `user_id` bigint(20) unsigned NOT NULL default '0' COMMENT '用户id',
  `client_id` varchar(50) NOT NULL default '' COMMENT '用户通过哪个客户端应用进行登录的,意义不大,仅作为记录备查,因为用户不管通过哪个客户端登录,输入用户名密码的行为都是用户自己操作的,只要用户名和密码正确,那就是登录成功了,与用户通过哪个客户端登录关系不大',
  `expire_in` bigint(20) unsigned NOT NULL default '0' COMMENT '过期时长,单位秒',
  `expire_time` datetime NOT NULL default '1970-01-01 08:00:00' COMMENT '过期时间',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户登录会话信息表';

CREATE TABLE `oauth_code` (
  `id` bigint(20) unsigned NOT NULL auto_increment COMMENT '主键ID',
  `jsessionid` varchar(128) NOT NULL default '' COMMENT 'jsessionid',
  `user_id` bigint(20) unsigned NOT NULL default '0' COMMENT '用户id',
  `client_id` varchar(100) NOT NULL default '' COMMENT '应用id',
  `scope` varchar(200) NOT NULL default '' COMMENT '授权作用域,授权多个作用域用逗号（,）分隔',
  `interface_uri` varchar(200) NOT NULL default '' COMMENT '接口地址,授权多个接口地址用逗号（,）分隔',
  `code` varchar(50) NOT NULL default '' COMMENT '预授权码',
  `expire_in` bigint(20) unsigned NOT NULL default '0' COMMENT '预授权码过期时长,单位秒',
  `expire_time` datetime NOT NULL default '1970-01-01 08:00:00' COMMENT '预授权码过期时间',
  `create_time` datetime NOT NULL default '1970-01-01 08:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL default '1970-01-01 08:00:00' COMMENT '修改时间',
  PRIMARY KEY  (`id`),
  KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预授权码表';

CREATE TABLE `oauth_access_token` (
  `id` bigint(20) unsigned NOT NULL auto_increment COMMENT '主键ID',
  `jsessionid` varchar(128) NOT NULL default '' COMMENT 'jsessionid',
  `user_id` bigint(20) unsigned NOT NULL default '0' COMMENT '用户id',
  `client_id` varchar(100) NOT NULL default '' COMMENT '应用id',
  `scope` varchar(200) NOT NULL default '' COMMENT '授权作用域,授权多个作用域用逗号（,）分隔',
  `interface_uri` varchar(200) NOT NULL default '' COMMENT '接口地址,授权多个接口地址用逗号（,）分隔',
  `token_type` varchar(10) NOT NULL default '' COMMENT 'access_token类型,bearer类型或mac类型',
  `access_token` varchar(50) NOT NULL default '' COMMENT 'access_token',
  `access_token_expire_in` bigint(20) unsigned NOT NULL default '0' COMMENT 'access_token过期时长,单位秒',
  `access_token_expire_time` datetime NOT NULL default '1970-01-01 08:00:00' COMMENT 'access_token过期时间',
  `refresh_token` varchar(50) NOT NULL default '' COMMENT '刷新token',
  `refresh_token_expire_in` bigint(20) unsigned NOT NULL default '0' COMMENT 'refresh_token过期时长,单位秒',
  `refresh_token_expire_time` datetime NOT NULL default '1970-01-01 08:00:00' COMMENT 'access_token过期时间',
  `delete_flag` bit(1) NOT NULL default '\0' COMMENT '0:未删除 1：已删除',
  `create_time` datetime NOT NULL default '1970-01-01 08:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL default '1970-01-01 08:00:00' COMMENT '修改时间',
  PRIMARY KEY  (`id`),
  KEY `idx_access_token` (`access_token`),
  KEY `idx_refresh_token` (`refresh_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='accessToken表';