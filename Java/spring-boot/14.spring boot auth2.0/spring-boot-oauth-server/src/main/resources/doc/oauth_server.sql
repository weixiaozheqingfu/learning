CREATE TABLE user_info (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  account varchar(50) NOT NULL DEFAULT '' COMMENT '账号',
  password varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  phone varchar(20) NOT NULL DEFAULT '' COMMENT '手机',
  full_name varchar(50) NOT NULL DEFAULT '' COMMENT '姓名',
  nick_name varchar(50) NOT NULL DEFAULT '' COMMENT '昵称',
  sex tinyint(3) NOT NULL DEFAULT 0 COMMENT '0:未填写,1:男,2:女,3:未知',
  age tinyint(3) NOT NULL DEFAULT 0 COMMENT '年龄',
  remark varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
  delete_flag bit(1) NOT NULL DEFAULT 0 COMMENT '0:未删除 1：已删除',
  register_time datetime DEFAULT NULL COMMENT '注册时间',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='用户信息表';

-- scope_name和scope_desc是对外公开的
CREATE TABLE oauth_scope_enum (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  scope_name varchar(100) NOT NULL DEFAULT '' COMMENT '授权范围名称',
  scope_desc varchar(100) NOT NULL DEFAULT '' COMMENT '授权范围描述',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='授权作用域枚举表';

-- 与oauth_scope_info是多对一关系,该列表及与scope的对应关系是对外公开的,客户端明确知道需要申明申明scope,也知道这个scope包含了哪些接口,对于用户,只要知道scope名称即可。
CREATE TABLE oauth_interface_enum (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  interface_name varchar(100) NOT NULL DEFAULT '' COMMENT '接口名称',
  interface_uri varchar(200) NOT NULL DEFAULT '' COMMENT '接口地址',
  interface_desc varchar(500) NOT NULL DEFAULT '' COMMENT '接口详细描述',
  scope_name varchar(100) NOT NULL DEFAULT '' COMMENT '授权范围名称',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='授权接口枚举表';

CREATE TABLE developer_info (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  account varchar(50) NOT NULL DEFAULT '' COMMENT '账号',
  password varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  full_name varchar(50) NOT NULL DEFAULT '' COMMENT '姓名',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='开发者账号信息表';

CREATE TABLE oauth_client_info (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  client_id varchar(50) NOT NULL DEFAULT '' COMMENT '客户端应用id',
  client_secret varchar(50) NOT NULL DEFAULT '' COMMENT '客户端应用秘钥',
  client_name varchar(50) NOT NULL DEFAULT '' COMMENT '客户端应用名称',
  redirect_uri varchar(200) NOT NULL DEFAULT '' COMMENT '客户端应用回调地址',
  developer_id bigint(20) NOT NULL DEFAULT 0 COMMENT '客户端应用所属开发者账号',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='客户端信息表';

-- 该表数据有一个积累的过程,代码逻辑优先查询该表,如果没有查询到结果则生成一条记录.
-- 每一个开放平台开发者账号针对同一个资源获取到的资源id都是不同的,且在同账号下保持唯一不变,进一步增强了资源信息的安全性.因为不同的账号针对相同的资源拿到的资源id是不同的.
-- 同时,如果一个开发者账号下有多个client应用,那么这些应用之间针对相同资源得到的unionid是相同的,可以使同一个开发者账号下的不同应用针对资源信息可以通过unionid进行打通.
CREATE TABLE oauth_developer_r_m (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  developer_id bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '开发者用户id',
  user_id bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  union_id varchar(100) NOT NULL DEFAULT '' COMMENT '用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='开放平台开发者账号的资源信息映射表oauth_developer_resource_mapping';

-- 该表数据有一个积累的过程,代码逻辑优先查询该表,如果没有查询到结果则生成一条记录.
-- 每一个客户端针对同一个资源获取到的资源id都是不同的,且在同一个客户端下保持唯一不变,进一步增强了资源信息的安全性.因为不同的客户端针对相同的资源拿到的资源id是不同的.
CREATE TABLE oauth_client_r_m (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  client_id varchar(50) NOT NULL DEFAULT '' COMMENT '客户端id',
  user_id bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  open_id varchar(100) NOT NULL DEFAULT '' COMMENT '用户对外开放id,对当前开发者帐号唯一',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='客户端的资源信息映射表oauth_client_resource_mapping';

-- 根据client_id和user_id查询oauth_client_resource_mapping表,如果不能查到对应资源映射数据,则新增oauth_client_resource_mapping表记录.
-- 根据client_id查询到其所属的developer_account_id,再根据developer_account_id和user_id查询oauth_developer_resource_mapping表,如果不能查到对应资源映射数据,则新增oauth_developer_resource_mapping表记录
CREATE TABLE oauth_code (
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  open_id varchar(100) NOT NULL DEFAULT '' COMMENT '用户对外开放id,对当前开发者帐号唯一',
  union_id varchar(100) NOT NULL DEFAULT '' COMMENT '用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。',
  client_id varchar(100) NOT NULL DEFAULT '' COMMENT '应用id',
  scope varchar(200) NOT NULL DEFAULT '' COMMENT '授权作用域,授权多个作用域用逗号（,）分隔',
  interface_uri varchar(200) NOT NULL DEFAULT '' COMMENT '接口地址,授权多个接口地址用逗号（,）分隔',
  code varchar(50) NOT NULL DEFAULT '' COMMENT '预授权码',
  expire_in bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '预授权码过期时长,单位秒',
  expire_time datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '预授权码过期时间',
  delete_flag bit(1) NOT NULL DEFAULT 0 COMMENT '0:未删除 1：已删除',
  create_time datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  update_time datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (id),
  KEY idx_code (code)
) COMMENT='预授权码表';

CREATE TABLE oauth_access_token (
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  open_id varchar(100) NOT NULL DEFAULT '' COMMENT '用户对外开放id,对当前开发者帐号唯一',
  union_id varchar(100) NOT NULL DEFAULT '' COMMENT '用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。',
  client_id varchar(100) NOT NULL DEFAULT '' COMMENT '应用id',
  scope varchar(200) NOT NULL DEFAULT '' COMMENT '授权作用域,授权多个作用域用逗号（,）分隔',
  interface_uri varchar(200) NOT NULL DEFAULT '' COMMENT '接口地址,授权多个接口地址用逗号（,）分隔',
  token_type varchar(10) NOT NULL DEFAULT '' COMMENT 'access_token类型,bearer类型或mac类型',
  access_token varchar(50) NOT NULL DEFAULT '' COMMENT 'access_token',
  access_token_expire_in bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'access_token过期时长,单位秒',
  access_token_expire_time datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'access_token过期时间',
  refresh_token varchar(50) NOT NULL DEFAULT '' COMMENT '刷新token',
  refresh_token_expire_in bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'refresh_token过期时长,单位秒',
  refresh_token_expire_time datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'access_token过期时间',
  delete_flag bit(1) NOT NULL DEFAULT 0 COMMENT '0:未删除 1：已删除',
  create_time datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  update_time datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (id),
  KEY idx_access_token (access_token),
  KEY idx_refresh_token (refresh_token)
) COMMENT='accessToken表';
