CREATE TABLE `oauth_client_info` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `client_id` varchar(50) NOT NULL default '' COMMENT '第三方auth服务平台分配的客户端id',
  `client_secret` varchar(50) NOT NULL default '' COMMENT '第三方auth服务平台分配的客户端密码',
  `redirect_uri` varchar(200) NOT NULL default '' COMMENT '客户端应用回调地址',
  `logout_uri` varchar(200) NOT NULL DEFAULT '' COMMENT '客户端应用注销登录地址,sso需求时有用',
  `scope` varchar(200) NOT NULL default '' COMMENT '针对于授权中心众多授权作用域,客户端应用声明需要用户确认授权的部分作用域,授权多个作用域用逗号（,）分隔',
  `server_type` varchar(50) NOT NULL default '' COMMENT '授权中心服务平台类型(如qq,wechart,sina,csdn,github,sso等)',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='auth客户端基本信息配置表';

INSERT INTO `oauth_client_info` VALUES ('1', '1002', '654321', 'http://localhost:8082/oauth/sso/callback', 'http://localhost:8082/oauth/sso/logout', 'get_user_open_info', 'sso', '2019-11-02 13:46:18', '2019-11-02 13:46:18');

-- 会话信息存redis里了,当jsessionid过期后,这里对应的记录也就失效了,可以晚上定时清理。
-- 在清理之前,每次得到jsessionid后,可以先查询redis,如果redis中存在,则表示会话为过期,则此处的对应记录是有效的。
-- 另外该表也可以去掉,转而也使用redis。
-- 但是使用redis会有一个问题,就是sso用户中心注销所有客户端会话时,客户端的access_token键可能自然到期后已经失效了,如果使用redis,就找不到access_token键了,也就找不到对应的jsession_id了。
CREATE TABLE `oauth_access_token` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键',
  `jsessionid` varchar(128) NOT NULL default '' COMMENT 'jsessionid',
  `user_id` bigint(20) NOT NULL default '0' COMMENT '用户id',
  `access_token` varchar(64) NOT NULL default '' COMMENT 'access_token',
  `expire_in` bigint(20) NOT NULL default '0' COMMENT 'access_token过期时间',
  `refresh_token` varchar(64) NOT NULL default '' COMMENT 'refresh_token',
  `scope` varchar(200) NOT NULL default '' COMMENT '用户授权作用域,授权多个作用域用逗号（,）分隔',
  `interface_uri` varchar(500) NOT NULL default '' COMMENT '用户授权scope对应的接口地址,授权多个接口地址用逗号（,）分隔',
  `server_type` varchar(50) NOT NULL default '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina,sso等)',
  `create_time` datetime default NULL COMMENT '创建时间',
  `update_time` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='access_token表';

