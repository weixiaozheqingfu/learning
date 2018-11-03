-- 在auth中,属于client端数据库表。另外有很多表可以使用缓存，但建表并不多余。
CREATE TABLE user_info (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  account varchar(50) NOT NULL DEFAULT '' COMMENT '账号',
  password varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  phone varchar(20) NOT NULL DEFAULT '' COMMENT '手机', 
  email varchar(50) NOT NULL DEFAULT '' COMMENT '邮箱',
  phone_verified bit(1) NOT NULL DEFAULT 0 COMMENT '手机验证标识(0:未认证 1:已认证)',
  email_verified bit(1) NOT NULL COMMENT DEFAULT 0 '邮箱验证标识(0:未认证 1:已认证)',
  full_name varchar(50) NOT NULL DEFAULT '' DEFAULT '' COMMENT '姓名', 
  nick_name varchar(50) NOT NULL DEFAULT '' COMMENT '昵称', 
  age tinyint(3) NOT NULL DEFAULT 0 COMMENT '年龄',
  height smallint(6) NOT NULL DEFAULT 0 COMMENT '身高(单位mm)',
  remark varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
  delete_flag bit(1) NOT NULL DEFAULT 0 COMMENT '0:未删除 1：已删除',
  register_time datetime DEFAULT NULL COMMENT '注册时间',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

CREATE TABLE auth_server_info (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  server_id varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台id',
  server_name varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台名称(如qq,微信,新浪微博等)',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='第三方auth服务平台信息表';

CREATE TABLE auth_server_interface_info (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  auth_server_info_id bigint(20) NOT NULL DEFAULT 0 COMMENT '第三方auth服务端信息表主键', 
  interface_name varchar(100) NOT NULL DEFAULT '' COMMENT '接口名称',
  interface_url varchar(200) DEFAULT NULL COMMENT '接口地址',
  interface_remark varchar(500) NOT NULL DEFAULT '' COMMENT '接口说明',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='第三方auth服务平台声明接口信息表';

CREATE TABLE auth_client_info (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  client_id varchar(50) NOT NULL DEFAULT 0 COMMENT '第三方auth服务平台分配的客户端id',
  client_secret varchar(50) NOT NULL DEFAULT 0 COMMENT '第三方auth服务平台分配的客户端密码',
  auth_server_info_id bigint(20) NOT NULL DEFAULT 0 COMMENT '第三方auth服务端信息表主键', 
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='auth客户端信息表';

-- 用户表与三方账号表是一对多关系，一个用户可以绑定多个三方账户，绑定的三方账户也可以解绑（删除记录，物理删除即可）。同时该表也是三方账户auth认证的授权表。
-- 与码云相同的做法,第一次就使用三方账号登陆,则先进行auth认证,通过后,将auth认证信息先保存在redis中,不直接保存在本表,并设置过期时间10分钟,将key返回到浏览器,
-- 最后点击【新建账号】按钮时,将key和用户信息一并传入后台,然后从redis中查询,如果能查到,则进行用户信息的创建,同时将redis中auth_token_info写入本表中,同时关联用户表主键。
-- 如果用户10分钟过了都没有点击按钮，则刚才的auth认证通过信息已经从redis中删除作废，此时可提示用户连接失败，请重试！，码云也是这样的。
CREATE TABLE auth_token_info (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_info_id bigint(20) NOT NULL DEFAULT 0 COMMENT '用户表主键',
  third_account_id varchar(50) NOT NULL DEFAULT '' COMMENT '三方账号id',
  access_token varchar(50) NOT NULL DEFAULT '' COMMENT 'access_token',
  access_expire bigint(20) NOT NULL DEFAULT 0 COMMENT 'access_token过期时间',
  refresh_token varchar(20) NOT NULL DEFAULT '' COMMENT 'refresh_token', 
  auth_server_info_id bigint(20) NOT NULL DEFAULT 0 COMMENT '第三方auth服务平台信息表主键', 
  auth_server_interface_info_id varchar(20) NOT NULL DEFAULT '' COMMENT '可通过auth授权访问第三方资源接口列表(多个以英文逗号分隔)', 
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='auth认证信息表,也是三方账户表';