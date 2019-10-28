/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50027
Source Host           : localhost:3306
Source Database       : oauth_client

Target Server Type    : MYSQL
Target Server Version : 50027
File Encoding         : 65001

Date: 2019-04-04 13:44:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account_binding
-- ----------------------------
DROP TABLE IF EXISTS `account_binding`;
CREATE TABLE `account_binding` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `user_id` bigint(20) NOT NULL default '0' COMMENT '用户表id',
  `account` varchar(50) NOT NULL default '' COMMENT '账号',
  `open_id` varchar(100) NOT NULL default '' COMMENT '用户对外开放id,对当前开发者帐号唯一',
  `union_id` varchar(100) NOT NULL default '' COMMENT '用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。不一定所有第三方都有此字段.',
  `server_type` varchar(50) NOT NULL default '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina等)',
  `bind_time` datetime default NULL COMMENT '绑定时间',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方账户绑定表';

-- ----------------------------
-- Records of account_binding
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `open_id` varchar(100) NOT NULL default '' COMMENT '用户对外开放id,对当前开发者帐号唯一',
  `union_id` varchar(100) NOT NULL default '' COMMENT '用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。',
  `access_token` varchar(50) NOT NULL default '' COMMENT 'access_token',
  `expire_in` bigint(20) NOT NULL default '0' COMMENT 'access_token过期时间',
  `refresh_token` varchar(20) NOT NULL default '' COMMENT 'refresh_token',
  `scope` varchar(200) NOT NULL default '' COMMENT '授权作用域,授权多个作用域用逗号（,）分隔',
  `interface_uri` varchar(500) NOT NULL default '' COMMENT '接口地址,授权多个接口地址用逗号（,）分隔',
  `server_type` varchar(50) NOT NULL default '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina等)',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='access_token表';

-- ----------------------------
-- Records of oauth_access_token
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_client_config
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_config`;
CREATE TABLE `oauth_client_config` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `client_id` varchar(50) NOT NULL default '' COMMENT '第三方auth服务平台分配的客户端id',
  `client_secret` varchar(50) NOT NULL default '' COMMENT '第三方auth服务平台分配的客户端密码',
  `redirect_uri` varchar(200) NOT NULL default '' COMMENT '客户端应用回调地址',
  `scope` varchar(200) NOT NULL default '' COMMENT '针对于第三方auth服务的众多授权作用域,客户端应用声明需要用户确认授权的作用域,授权多个作用域用逗号（,）分隔',
  `server_type` varchar(50) NOT NULL default '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina,csdn,github等)',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='auth客户端基本信息配置表';

-- ----------------------------
-- Records of oauth_client_config
-- ----------------------------
INSERT INTO `oauth_client_config` VALUES ('1', '1002', '1q2w3e4r', 'http://localhost:8081/auth/oauth_server/callback', 'get_user_open_info', 'oauth_server', '2019-03-11 11:29:09', '2019-03-11 11:29:12');
INSERT INTO `oauth_client_config` VALUES ('2', 'wx63d402790645b7e6', '123456', 'http://localhost:8081/auth/wechar/callback', 'get_user_open_info', 'wechat', '2019-03-11 11:31:24', '2019-03-11 11:31:26');
INSERT INTO `oauth_client_config` VALUES ('3', '1100511', 'qwer1234', 'http://localhost:8081/auth/csdn/callback', 'get_user_open_info', 'csdn', '2019-03-11 11:32:18', '2019-03-11 11:32:20');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `account` varchar(50) NOT NULL default '' COMMENT '账号',
  `password` varchar(50) NOT NULL default '' COMMENT '密码',
  `phone` varchar(20) NOT NULL default '' COMMENT '手机',
  `email` varchar(50) NOT NULL default '' COMMENT '邮箱',
  `phone_verified` bit(1) NOT NULL default '\0' COMMENT '手机验证标识(0:未认证 1:已认证)',
  `email_verified` bit(1) NOT NULL default '\0' COMMENT '邮箱验证标识(0:未认证 1:已认证)',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('1', 'gaoshanliushui', '1q2w3e4r', '13120411529', '915331408@qq.com', '\0', '\0', '', '高山流水', '0', '0', '', '\0', '2019-03-07 11:18:43', '2019-03-07 11:18:38', '2019-03-07 11:18:41');
