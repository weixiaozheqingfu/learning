package com.glitter.spring.boot.plugin.page;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName: PagePlugin
 * @Description: 自定义分页拦截器插件
 * @author limengjun
 * @date 2018年11月05日
 *
 * 本拦截器执行条件:
 * 方法中在执行查询列表语句之前执行了如下语句:PageHelper.startPage(pageNum, pageSize);
 *
 * mybatis 3.4.0之前写法
 * @Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
 * mybatis  3.40之后的写法
 * @Intercepts(value = {@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class,Integer.class})})
 */
@Intercepts(value = {@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class,Integer.class})})
public class PagePlugin implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(PagePlugin.class);

	private String dialect;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		try {
			RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
			StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");

			Page page = PageContext.getPage();
			if(null == page){
                return invocation.proceed();
            }

			// 动态构建查询总条数sql并执行
			BoundSql boundSql = delegate.getBoundSql();
			Connection connection = (Connection) invocation.getArgs()[0];
			int totalRecords = this.getTotalRecords(boundSql,mappedStatement,connection);
			page.setTotalRecords(totalRecords);

			// 给原查询列表sql赋予limit等分页属性后重新赋予boundSql对象,以便查询出分页后的结果
			String sql = boundSql.getSql();
			String pageSql = this.getPageSql(page, sql);
			ReflectUtil.setFieldValue(boundSql, "sql", pageSql);

			return invocation.proceed();
		} catch (InvocationTargetException e) {
			logger.error(JSONObject.toJSONString(e));
			return invocation.proceed();
		}
	}

	/**
	 * 拦截器入口方法
	 */
	@Override
	public Object plugin(Object target) {
		Object o = null;
		try {
			o = Plugin.wrap(target, this);
			return o;
		} catch (Exception e) {
			logger.error(JSONObject.toJSONString(e));
			return o;
		}
	}

	/**
	 * 设置注册拦截器时设定的属性
	 */
	@Override
	public void setProperties(Properties properties) {
		try {
			this.dialect = properties.getProperty("dialect");
		} catch (Exception e) {
			logger.error(JSONObject.toJSONString(e));
		}
	}

	private int getTotalRecords(BoundSql boundSql, MappedStatement mappedStatement, Connection connection) {
		int totalRecords = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Configuration configuration = mappedStatement.getConfiguration();
			String countSql = this.getCountSql(boundSql.getSql());
			List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
			Object parameterObject = boundSql.getParameterObject();

			// 动态构建一个mybatis可执行的sql对象,供mybatis动态生成sql语句并赋值参数,并执行该sql脚本
			BoundSql countBoundSql = new BoundSql(configuration, countSql, parameterMappings, parameterObject);
			ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, countBoundSql);

			pstmt = connection.prepareStatement(countSql);
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalRecords = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(JSONObject.toJSONString(e));
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (pstmt != null){
					pstmt.close();
				}
			} catch (SQLException e) {
				logger.error(JSONObject.toJSONString(e));
			}
		}
		return totalRecords;
	}

	private String getPageSql(Page<?> page, String sql) {
		StringBuffer sqlBuffer = new StringBuffer(sql);
		if ("mysql".equalsIgnoreCase(dialect)) {
			return getMysqlPageSql(page, sqlBuffer);
		} else if ("oracle".equalsIgnoreCase(dialect)) {
			return getOraclePageSql(page, sqlBuffer);
		}
		return sqlBuffer.toString();
	}

	private String getMysqlPageSql(Page<?> page, StringBuffer sqlBuffer) {
		sqlBuffer.insert(0, "select * from ( ");
		sqlBuffer.append(" ) alias limit ").append(page.getStartIndex()).append(",").append(page.getPageSize());
		return sqlBuffer.toString();
	}

	private String getOraclePageSql(Page<?> page, StringBuffer sqlBuffer) {
		int offset = (page.getPageNum() - 1) * page.getPageSize() + 1;
		sqlBuffer.insert(0, "select u.*, rownum r from (").append(") u where rownum < ").append(offset + page.getPageSize());
		sqlBuffer.insert(0, "select * from (").append(") where r >= ").append(offset);
		return sqlBuffer.toString();
	}

	private String getCountSql(String sql) {
		return "select count(*) as total from (" + sql + ") alias";
	}

}