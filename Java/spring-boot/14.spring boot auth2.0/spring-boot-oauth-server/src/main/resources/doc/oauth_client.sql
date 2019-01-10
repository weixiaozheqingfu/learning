-- 在auth中,属于client端数据库表。另外有很多表可以使用缓存，但建表并不多余。
CREATE TABLE user_info (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  account varchar(50) NOT NULL DEFAULT '' COMMENT '账号',
  password varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  phone varchar(20) NOT NULL DEFAULT '' COMMENT '手机',
  email varchar(50) NOT NULL DEFAULT '' COMMENT '邮箱',
  phone_verified bit(1) NOT NULL DEFAULT 0 COMMENT '手机验证标识(0:未认证 1:已认证)',
  email_verified bit(1) NOT NULL DEFAULT 0 COMMENT '邮箱验证标识(0:未认证 1:已认证)',
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
) COMMENT='用户表';

-- 内容为客户端自定义录入的
CREATE TABLE oauth_server_enum (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  server_type varchar(20) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina等)',
  server_name varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台名称(如qq,微信,新浪微博等)',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='第三方auth服务平台枚举信息表';

-- 内容为从授权服务器开放平台摘录下的
-- 用于客户端运营平台可动态配置,这张表可以没有
CREATE TABLE oauth_scope_enum (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  scope_name varchar(100) NOT NULL default '0' COMMENT '授权范围名称',
  scope_desc varchar(100) NOT NULL default '0' COMMENT '授权范围描述',
  server_type varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina等)',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='授权作用域枚举表';

-- 内容为从授权服务器开放平台摘录下的
-- 用于客户端运营平台可动态配置,这张表可以没有
CREATE TABLE oauth_interface_enum (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  interface_name varchar(100) NOT NULL DEFAULT '' COMMENT '接口名称',
  interface_uri varchar(100) NOT NULL DEFAULT '' COMMENT '接口地址',
  interface_desc varchar(500) NOT NULL DEFAULT '' COMMENT '接口详细描述',
  scope_name bigint(20) NOT NULL DEFAULT 0 COMMENT '授权范围名称',
  server_type varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina等)',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='授权接口枚举表';


-- 客户端针对每一个第三方auth服务器都有唯一的一条配置记录与之对应
-- response_type的code对应grant_type的authorization_code
-- response_type的token对应grant_type的implicit
CREATE TABLE oauth_client_base_config (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  client_id varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台分配的客户端id',
  client_secret varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台分配的客户端密码',
  redirect_uri varchar(200) NOT NULL DEFAULT '' COMMENT '客户端应用回调地址',
  server_type varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina,csdn,github等)',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='auth客户端基本信息配置表';


-- 把这一块重新组织语言表达清楚该表的设计逻辑,该表暂定为此,约定该表中只要有一种场景为默认场景,当不传场景时,就使用默认场景,另外在完成一遍代码后,再回过头来重新审视完善该表.
-- client端针对某一个oauth服务,至少有一个默认场景,可能还会有其他多种场景.
-- client端封装标准的oauth客户端服务,比如针对response_type和grant_type各种取值下的标准处理流程。
-- client端根据业务场景的不同取到对应的请求参数,然后调用标准的client端oauth处理模块,标准的oauth模块处理完成后,再根据不同的业务场景,使用工厂模式调用不同的业务实现即可。

-- 比如根据scenario_name处理完成标准的client端程序后,再根据对应的scenario_name找到该业务场景下的业务实现代码
-- 在业务实现代码中,对于该场景对应的state_type就可以根据该场景下的业务需要进行业务实现了,比如如果该值为1,则跳转至首页,该值为2,则跳转至会议模块页面等。
-- 前提:请求授权页时,根据请求授权页时的业务场景不同,需要将state_type拼接在state参数后.
CREATE TABLE oauth_client_scenario_config (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  client_id varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台分配的客户端id',
  response_type	varchar(50) NOT NULL DEFAULT '' COMMENT '声明第三方auth服务平台响应模式,使第三方auth平台知道客户端想使用哪一种响应模式,auth平台存在两种响应模式即code,token',
  grant_type varchar(50) NOT NULL DEFAULT '' COMMENT '声明第三方auth服务平台授权类型,使第三方auth平台知道客户端想使用哪一种授权类型进行授权流程,auth服务器端都会有对应的逻辑处理,auth平台存在的授权模式共5种,如authorization_code,implicit,refresh_token等',
  scope varchar(200) NOT NULL DEFAULT '' COMMENT '针对于第三方auth服务的众多授权作用域,客户端应用声明需要用户确认授权的作用域,授权多个作用域用逗号（,）分隔',
  state_type varchar(50) NOT NULL DEFAULT 0 COMMENT 'state参数后缀值,不同的值表示不同的含义,具体含义由客户端自行定义使用逻辑',
  state_type_desc varchar(100) NOT NULL DEFAULT 0 COMMENT 'state_type描述',
  response_url varchar(50) NOT NULL DEFAULT 0 COMMENT '自定义不同场景下获取accessToken成功后的响应地址',
  scenario_name varchar(20) NOT NULL DEFAULT 0 COMMENT '业务场景名称',
  scenario_desc varchar(50) NOT NULL DEFAULT 0 COMMENT '业务场景说明',
  server_type varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina等)',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='auth客户端业务场景信息配置表';


-- 用户表与三方账号表是一对多关系，一个用户可以绑定多个三方账户，绑定的三方账户也可以解绑（删除记录，物理删除即可）。同时该表也是三方账户auth认证的授权表。
-- 与码云相同的做法,第一次就使用三方账号登陆,则先进行auth认证,通过后,将auth认证信息先保存在redis中,不直接保存在本表,并设置过期时间10分钟,将key返回到浏览器,
-- 最后点击【新建账号】按钮时,将key和用户信息一并传入后台,然后从redis中查询,如果能查到,则进行用户信息的创建,同时将redis中auth_token_info写入本表中,同时关联用户表主键。
-- 如果用户10分钟过了都没有点击按钮，则刚才的auth认证通过信息已经从redis中删除作废，此时可提示用户连接失败，请重试！，码云也是这样的。
CREATE TABLE oauth_access_token (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  open_id varchar(100) NOT NULL DEFAULT '' COMMENT '用户对外开放id,对当前开发者帐号唯一',
  union_id varchar(100) NOT NULL DEFAULT '' COMMENT '用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。',
  access_token varchar(50) NOT NULL DEFAULT '' COMMENT 'access_token',
  expire_in bigint(20) NOT NULL DEFAULT 0 COMMENT 'access_token过期时间',
  refresh_token varchar(20) NOT NULL DEFAULT '' COMMENT 'refresh_token',
  scope varchar(200) NOT NULL DEFAULT '' COMMENT '授权作用域,授权多个作用域用逗号（,）分隔',
  interface_uri varchar(500) NOT NULL DEFAULT '' COMMENT '接口地址,授权多个接口地址用逗号（,）分隔',
  server_type varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina等)',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='auth认证信息表,也是三方账户表';

CREATE TABLE account_binding (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id bigint(20) NOT NULL DEFAULT 0 COMMENT '用户表id',
  open_id varchar(100) NOT NULL DEFAULT '' COMMENT '用户对外开放id,对当前开发者帐号唯一',
  union_id varchar(100) NOT NULL DEFAULT '' COMMENT '用户统一标识。针对一个开放平台开发者帐号下的应用，同一用户的unionid是唯一的。不一定所有第三方都有此字段.',
  server_type varchar(50) NOT NULL DEFAULT '' COMMENT '第三方auth服务平台类型(如qq,wechart,sina等)',
  bind_time datetime DEFAULT NULL COMMENT '绑定时间',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT='第三方账户绑定表';



-- 第三方账号表与user表的关系，这张表需要考虑如何设计,应该有一个绑定关系。