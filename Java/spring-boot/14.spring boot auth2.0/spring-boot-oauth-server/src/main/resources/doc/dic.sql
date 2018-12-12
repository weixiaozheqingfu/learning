-- 系统字典分类表,由系统开发人员维护
CREATE TABLE `sys_dic_category` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '编码',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '名称',
  `type` bit(1) NOT NULL DEFAULT b'0' COMMENT '0:系统字典 1:业务字典',
  `desc` varchar(30) NOT NULL DEFAULT '' COMMENT '描述',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

-- 系统字典表,由系统开发人员维护
CREATE TABLE `sys_dic_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '编码',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '名称',
  `parent_code` varchar(20) NOT NULL DEFAULT '' COMMENT '上级编码',
  `category` varchar(30) NOT NULL DEFAULT '' COMMENT '所属分类code',
  `category_type` bit(1) NOT NULL DEFAULT b'0' COMMENT '冗余字段 0:系统字典 1:业务字典',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `desc` varchar(30) NOT NULL DEFAULT '' COMMENT '描述',
  `delete_flag` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 0正常  1删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

-- 业务字典表,由系统使用人员维护,如果是saas系统,还可以加入例如orgId的字段来做不同租户的区分
CREATE TABLE `bus_dic_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '名称',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '上级id',
  `category` varchar(30) NOT NULL DEFAULT '' COMMENT '所属分类code',
  `category_type` bit(1) NOT NULL DEFAULT b'0' COMMENT '冗余字段 0:系统字典 1:业务字典',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `desc` varchar(30) NOT NULL DEFAULT '' COMMENT '描述',
  `delete_flag` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 0正常  1删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;
-- ---------------------------------------------------------------------------------------------

-- 字典表设计第二种做法,但建议使用第一种做法

-- 字典分类表,由系统开发人员维护
CREATE TABLE `dic_category` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '编码',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '名称',
  `type` bit(1) NOT NULL DEFAULT b'0' COMMENT '0:系统字典 1:业务字典',
  `desc` varchar(30) NOT NULL DEFAULT '' COMMENT '描述',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

-- 字典信息表,由系统开发者或系统使用者维护
-- 如果是系统开发者维护,即系统字典,则使用code和parent_code字段来维系上下级字典关系,parent_id值为-1
-- 如果是系统使用者维护,即业务字典,则使用id和parent_id字段来维系上下级字典关系,code为null,parent_code值为-1
-- 如果是saas系统,需要类似于orgId的租户区分字段,那么对于系统字典来说,orgId值为-1。
CREATE TABLE `dic_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '编码',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '名称',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '上级id',
  `parent_code` varchar(20) NOT NULL DEFAULT '' COMMENT '上级编码',
  `category` varchar(30) NOT NULL DEFAULT '' COMMENT '所属分类code',
  `category_type` bit(1) NOT NULL DEFAULT b'0' COMMENT '冗余字段 0:系统字典 1:业务字典',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `desc` varchar(30) NOT NULL DEFAULT '' COMMENT '描述',
  `delete_flag` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 0正常  1删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;