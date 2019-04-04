/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50027
Source Host           : localhost:3306
Source Database       : oauth_server

Target Server Type    : MYSQL
Target Server Version : 50027
File Encoding         : 65001

Date: 2019-04-04 13:44:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for developer_info
-- ----------------------------
DROP TABLE IF EXISTS `developer_info`;
CREATE TABLE `developer_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `account` varchar(50) NOT NULL default '' COMMENT '账号',
  `password` varchar(50) NOT NULL default '' COMMENT '密码',
  `full_name` varchar(50) NOT NULL default '' COMMENT '姓名',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开发者账号信息表';

-- ----------------------------
-- Records of developer_info
-- ----------------------------
INSERT INTO `developer_info` VALUES ('1', '915331408@qq.com', '1q2w3e4r', '高山流水', '2019-03-11 11:33:44', '2019-03-11 11:33:47');

-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `id` bigint(20) unsigned NOT NULL auto_increment COMMENT '主键ID',
  `user_id` bigint(20) unsigned NOT NULL default '0' COMMENT '用户id',
  `open_id` varchar(100) NOT NULL default '' COMMENT '用户对外开放id,对当前开发者帐号唯一',
  `union_id` varchar(100) NOT NULL default '' COMMENT '用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。',
  `client_id` varchar(100) NOT NULL default '' COMMENT '应用id',
  `scope` varchar(200) NOT NULL default '' COMMENT '授权作用域,授权多个作用域用逗号（,）分隔',
  `interface_uri` varchar(200) NOT NULL default '' COMMENT '接口地址,授权多个接口地址用逗号（,）分隔',
  `token_type` varchar(10) NOT NULL default '' COMMENT 'access_token类型,bearer类型或mac类型',
  `access_token` varchar(50) NOT NULL default '' COMMENT 'access_token',
  `access_token_expire_in` bigint(20) unsigned NOT NULL default '0' COMMENT 'access_token过期时长,单位秒',
  `access_token_expire_time` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT 'access_token过期时间',
  `refresh_token` varchar(50) NOT NULL default '' COMMENT '刷新token',
  `refresh_token_expire_in` bigint(20) unsigned NOT NULL default '0' COMMENT 'refresh_token过期时长,单位秒',
  `refresh_token_expire_time` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT 'access_token过期时间',
  `delete_flag` bit(1) NOT NULL default '\0' COMMENT '0:未删除 1：已删除',
  `create_time` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY  (`id`),
  KEY `idx_access_token` (`access_token`),
  KEY `idx_refresh_token` (`refresh_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='accessToken表';

-- ----------------------------
-- Records of oauth_access_token
-- ----------------------------
INSERT INTO `oauth_access_token` VALUES ('1', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', 'bearer', '80af6ca408b94a59adeaae8c73e1cb01', '60', '2018-12-26 11:16:43', '293ff7c805a14fecb0d4ed6de5a20434', '172800', '2018-12-28 11:15:43', '\0', '2018-12-26 11:15:43', '2018-12-26 11:15:43');
INSERT INTO `oauth_access_token` VALUES ('2', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', 'bearer', 'cb760498bc3e434d80293927a2fa7024', '60', '2018-12-26 11:22:07', 'bd0cd52268624624918011b7e75e0b45', '172800', '2018-12-28 11:21:07', '\0', '2018-12-26 11:21:07', '2018-12-26 11:21:07');
INSERT INTO `oauth_access_token` VALUES ('3', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', 'bearer', '981a16bc2b3b4b4da0e9d57f8eed3b56', '60', '2018-12-26 11:22:14', 'dd4aa33e340441afa19e94c2047e0d9e', '172800', '2018-12-28 11:21:14', '\0', '2018-12-26 11:21:14', '2018-12-26 11:21:14');
INSERT INTO `oauth_access_token` VALUES ('4', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', 'bearer', 'd3ab0b13185443dca4755e1e45feba68', '60', '2018-12-26 11:50:14', '7f690679d6eb4810a93f467927bf1722', '172800', '2018-12-28 11:49:14', '\0', '2018-12-26 11:49:14', '2018-12-26 11:49:14');

-- ----------------------------
-- Table structure for oauth_client_info
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_info`;
CREATE TABLE `oauth_client_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `client_id` varchar(50) NOT NULL default '' COMMENT '客户端应用id',
  `client_secret` varchar(50) NOT NULL default '' COMMENT '客户端应用秘钥',
  `client_name` varchar(50) NOT NULL default '' COMMENT '客户端应用名称',
  `redirect_uri` varchar(200) NOT NULL default '' COMMENT '客户端应用回调地址',
  `developer_id` varchar(50) NOT NULL default '' COMMENT '客户端应用所属开发者账号',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户端信息表';

-- ----------------------------
-- Records of oauth_client_info
-- ----------------------------
INSERT INTO `oauth_client_info` VALUES ('1', '1001', '123456', '码云', 'https://gitee.com/auth/oauth_server/callback', '1', '2018-12-15 14:14:27', '2018-12-15 14:14:30');
INSERT INTO `oauth_client_info` VALUES ('2', '1002', '1q2w3e4r', 'oauth_client', 'http://localhost:8081/auth/oauth_server/callback', '1', null, null);

-- ----------------------------
-- Table structure for oauth_client_r_m
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_r_m`;
CREATE TABLE `oauth_client_r_m` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `client_id` varchar(50) NOT NULL default '' COMMENT '客户端id',
  `user_id` bigint(20) unsigned NOT NULL default '0' COMMENT '用户id',
  `open_id` varchar(100) NOT NULL default '' COMMENT '用户对外开放id,对当前开发者帐号唯一',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户端的资源信息映射表';

-- ----------------------------
-- Records of oauth_client_r_m
-- ----------------------------
INSERT INTO `oauth_client_r_m` VALUES ('1', '1001', '1', 'o_a4b3dd13f53449f28393dde1b244316a', '2018-12-19 16:34:56', '2018-12-19 16:34:56');

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
  `id` bigint(20) unsigned NOT NULL auto_increment COMMENT '主键ID',
  `user_id` bigint(20) unsigned NOT NULL default '0' COMMENT '用户id',
  `open_id` varchar(100) NOT NULL default '' COMMENT '用户对外开放id,对当前开发者帐号唯一',
  `union_id` varchar(100) NOT NULL default '' COMMENT '用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。',
  `client_id` varchar(100) NOT NULL default '' COMMENT '应用id',
  `scope` varchar(200) NOT NULL default '' COMMENT '授权作用域,授权多个作用域用逗号（,）分隔',
  `interface_uri` varchar(200) NOT NULL default '' COMMENT '接口地址,授权多个接口地址用逗号（,）分隔',
  `code` varchar(50) NOT NULL default '' COMMENT '预授权码',
  `expire_in` bigint(20) unsigned NOT NULL default '0' COMMENT '预授权码过期时长,单位秒',
  `expire_time` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '预授权码过期时间',
  `create_time` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` datetime NOT NULL default '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY  (`id`),
  KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预授权码表';

-- ----------------------------
-- Records of oauth_code
-- ----------------------------
INSERT INTO `oauth_code` VALUES ('1', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', 'a475c0cd-b5ae-4998-9f66-c0afd0390be8', '60', '2018-12-20 12:01:23', '2018-12-20 12:00:23', '2018-12-20 12:00:23');
INSERT INTO `oauth_code` VALUES ('2', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', '63b6eec9-88d5-4a28-8404-eef38eed2c3b', '60', '2018-12-20 14:02:17', '2018-12-20 14:01:17', '2018-12-20 14:01:17');
INSERT INTO `oauth_code` VALUES ('3', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', '5ca67b5b-df9c-428e-83b2-c043038eece0', '60', '2018-12-20 14:03:30', '2018-12-20 14:02:30', '2018-12-20 14:02:30');
INSERT INTO `oauth_code` VALUES ('4', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', 'e3ec6eac-94ac-4586-a848-93239a0dbd3b', '60', '2018-12-20 14:06:53', '2018-12-20 14:05:28', '2018-12-20 14:05:53');
INSERT INTO `oauth_code` VALUES ('5', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', '45f1dc48-20c7-40b8-a3bd-4939a09fe382', '60', '2018-12-20 14:13:25', '2018-12-20 14:12:02', '2018-12-20 14:12:25');
INSERT INTO `oauth_code` VALUES ('6', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', '08629625-fadf-453b-a683-71981b5ac47d', '60', '2018-12-20 14:25:46', '2018-12-20 14:24:16', '2018-12-20 14:24:46');
INSERT INTO `oauth_code` VALUES ('7', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', 'aa0f1eba-7ab2-4848-9aaa-8fd2851a4081', '60', '2018-12-20 14:43:49', '2018-12-20 14:41:48', '2018-12-20 14:42:49');
INSERT INTO `oauth_code` VALUES ('8', '1', 'o_a4b3dd13f53449f28393dde1b244316a', 'u_e02e6d440f4346f2abee32e5d5f3738b', '1001', 'get_user_open_info', '', 'd733955a-4b95-4a9d-9546-711a77990518', '60', '2018-12-26 11:21:25', '2018-12-26 11:20:25', '2018-12-26 11:20:25');

-- ----------------------------
-- Table structure for oauth_developer_r_m
-- ----------------------------
DROP TABLE IF EXISTS `oauth_developer_r_m`;
CREATE TABLE `oauth_developer_r_m` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `developer_id` bigint(20) unsigned NOT NULL default '0' COMMENT '开发者用户id',
  `user_id` bigint(20) unsigned NOT NULL default '0' COMMENT '用户id',
  `union_id` varchar(100) NOT NULL default '' COMMENT '用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='开放平台开发者账号的资源信息映射表';

-- ----------------------------
-- Records of oauth_developer_r_m
-- ----------------------------
INSERT INTO `oauth_developer_r_m` VALUES ('1', '1', '1', 'u_e02e6d440f4346f2abee32e5d5f3738b', '2018-12-19 16:34:56', '2018-12-19 16:34:56');

-- ----------------------------
-- Table structure for oauth_interface_enum
-- ----------------------------
DROP TABLE IF EXISTS `oauth_interface_enum`;
CREATE TABLE `oauth_interface_enum` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `interface_name` varchar(100) NOT NULL default '' COMMENT '接口名称',
  `interface_uri` varchar(200) NOT NULL default '' COMMENT '接口地址',
  `interface_desc` varchar(500) NOT NULL default '' COMMENT '接口详细描述',
  `scope_name` varchar(100) NOT NULL default '0' COMMENT '授权范围名称',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权接口枚举表';

-- ----------------------------
-- Records of oauth_interface_enum
-- ----------------------------
INSERT INTO `oauth_interface_enum` VALUES ('1', 'xxx', 'xxx/yyy', '获取用户信息', 'get_user_open_info', '2018-12-15 12:09:58', '2018-12-15 12:10:02');

-- ----------------------------
-- Table structure for oauth_scope_enum
-- ----------------------------
DROP TABLE IF EXISTS `oauth_scope_enum`;
CREATE TABLE `oauth_scope_enum` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `scope_name` varchar(100) NOT NULL default '0' COMMENT '授权范围名称',
  `scope_desc` varchar(100) NOT NULL default '0' COMMENT '授权范围描述',
  `grant_type` varchar(50) NOT NULL default '' COMMENT '授权模式,表明在当前的scope_name适用于哪种授权模式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权作用域枚举表';

-- ----------------------------
-- Records of oauth_scope_enum
-- ----------------------------
INSERT INTO `oauth_scope_enum` VALUES ('1', 'get_user_open_info', '获取用户头像,昵称等基本信息', 'authorization_code', '2018-12-15 12:09:28', '2018-12-15 12:09:31');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
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

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('1', 'weixiaozheqingfu', 'qwer1234', '13120411529', '微笑着轻拂', '', '0', '0', '', '\0', '2018-12-19 16:26:25', '2018-12-19 16:26:28', '2018-12-19 16:26:31');
