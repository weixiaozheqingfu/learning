CREATE DATABASE `demo_client` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use demo_client;

/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50027
Source Host           : localhost:3306
Source Database       : demo_client

Target Server Type    : MYSQL
Target Server Version : 50027
File Encoding         : 65001

Date: 2019-04-04 13:44:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for auth_client_info
-- ----------------------------
DROP TABLE IF EXISTS `auth_client_info`;
CREATE TABLE `auth_client_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `client_id` varchar(50) NOT NULL default '0' COMMENT '第三方auth服务平台分配的客户端id',
  `client_secret` varchar(50) NOT NULL default '0' COMMENT '第三方auth服务平台分配的客户端密码',
  `auth_server_info_id` bigint(20) NOT NULL default '0' COMMENT '第三方auth服务端信息表主键',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='auth客户端信息表';

-- ----------------------------
-- Records of auth_client_info
-- ----------------------------

-- ----------------------------
-- Table structure for auth_server_info
-- ----------------------------
DROP TABLE IF EXISTS `auth_server_info`;
CREATE TABLE `auth_server_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `server_id` varchar(50) NOT NULL default '' COMMENT '第三方auth服务平台id',
  `server_name` varchar(50) NOT NULL default '' COMMENT '第三方auth服务平台名称(如qq,微信,新浪微博等)',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方auth服务平台信息表';

-- ----------------------------
-- Records of auth_server_info
-- ----------------------------

-- ----------------------------
-- Table structure for auth_server_interface_info
-- ----------------------------
DROP TABLE IF EXISTS `auth_server_interface_info`;
CREATE TABLE `auth_server_interface_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `auth_server_info_id` bigint(20) NOT NULL default '0' COMMENT '第三方auth服务端信息表主键',
  `interface_name` varchar(100) NOT NULL default '' COMMENT '接口名称',
  `interface_url` varchar(200) NOT NULL default '' COMMENT '接口地址',
  `interface_remark` varchar(500) NOT NULL default '' COMMENT '接口说明',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方auth服务平台声明接口信息表';

-- ----------------------------
-- Records of auth_server_interface_info
-- ----------------------------

-- ----------------------------
-- Table structure for auth_token_info
-- ----------------------------
DROP TABLE IF EXISTS `auth_token_info`;
CREATE TABLE `auth_token_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `user_info_id` bigint(20) NOT NULL default '0' COMMENT '用户表主键',
  `third_account_id` varchar(50) NOT NULL default '' COMMENT '三方账号id',
  `access_token` varchar(50) NOT NULL default '' COMMENT 'access_token',
  `access_expire` bigint(20) NOT NULL default '0' COMMENT 'access_token过期时间',
  `refresh_token` varchar(20) NOT NULL default '' COMMENT 'refresh_token',
  `auth_server_info_id` bigint(20) NOT NULL default '0' COMMENT '第三方auth服务平台信息表主键',
  `auth_server_interface_info_id` varchar(20) NOT NULL default '' COMMENT '可通过auth授权访问第三方资源接口列表(多个以英文逗号分隔)',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='auth认证信息表,也是三方账户表';

-- ----------------------------
-- Records of auth_token_info
-- ----------------------------

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
  `age` tinyint(3) NOT NULL default '0' COMMENT '年龄',
  `height` smallint(6) NOT NULL default '0' COMMENT '身高(单位mm)',
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
INSERT INTO `user_info` VALUES ('1', 'weixiaozheqingfu', 'qwer1234', '13120411529', '915331408@qq.com', '\0', '\0', '李孟军', '高山流水', '29', '1680', '程序员', '\0', '2018-11-04 21:51:12', '2018-11-04 21:51:12', '2018-11-04 21:51:12');
INSERT INTO `user_info` VALUES ('2', 'weixiaozheqingfu1', 'qwer1234', '13120411528', '915331407@qq.com', '\0', '\0', '李孟军1', '高山流水1', '28', '1670', '程序员1', '\0', null, '2018-11-05 12:47:22', '2018-11-05 12:47:22');
INSERT INTO `user_info` VALUES ('3', 'weixiaozheqingfu2', 'qwer1234', '13120411527', '915331406@qq.com', '\0', '\0', '李孟军2', '高山流水2', '27', '1660', '程序员2', '\0', null, '2018-11-05 12:47:53', '2018-11-05 12:47:53');
INSERT INTO `user_info` VALUES ('4', 'weixiaozheqingfu3', 'qwer1234', '13120411526', '915331405@qq.com', '\0', '\0', '李孟军3', '高山流水3', '26', '1650', '程序员3', '\0', null, '2018-11-05 12:48:34', '2018-11-05 12:48:34');
INSERT INTO `user_info` VALUES ('5', 'weixiaozheqingfu4', 'qwer1234', '13120411525', '915331404@qq.com', '\0', '\0', '李孟军4', '高山流水4', '25', '1640', '程序员4', '\0', null, '2018-11-05 12:51:37', '2018-11-05 12:51:37');
INSERT INTO `user_info` VALUES ('6', 'weixiaozheqingfu5', 'qwer1234', '13120411524', '915331403@qq.com', '\0', '\0', '李孟军5', '高山流水5', '24', '1630', '程序员5', '\0', null, '2018-11-05 12:52:00', '2018-11-05 12:52:00');
INSERT INTO `user_info` VALUES ('7', 'weixiaozheqingfu6', 'qwer1234', '13120411524', '915331402@qq.com', '\0', '\0', '李孟军6', '高山流水6', '28', '1629', '程序员6', '\0', null, '2018-11-08 11:13:15', '2018-11-08 11:13:15');
