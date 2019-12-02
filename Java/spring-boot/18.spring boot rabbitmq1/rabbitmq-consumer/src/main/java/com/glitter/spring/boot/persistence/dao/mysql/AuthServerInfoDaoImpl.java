package com.glitter.spring.boot.persistence.dao.mysql;

import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.glitter.spring.boot.persistence.dao.IAuthServerInfoDao;
import com.glitter.spring.boot.bean.AuthServerInfo;
import java.util.List;

@Repository
public class AuthServerInfoDaoImpl implements IAuthServerInfoDao{

    private static final String NAME_SPACE = "com.glitter.spring.boot.persistence.dao.IAuthServerInfoDao";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(AuthServerInfo authServerInfo) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", authServerInfo);
    }

    @Override
    public int deleteById(Long id) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".deleteById", id);
    }

    @Override
    public int deleteByIds(Long[] ids) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".deleteByIds", ids);
    }

    @Override
    public int delete(AuthServerInfo authServerInfo) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", authServerInfo);
    }

    @Override
    public int updateById(AuthServerInfo authServerInfo) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", authServerInfo);
    }

    @Override
    public AuthServerInfo getById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<AuthServerInfo> getByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public AuthServerInfo get(AuthServerInfo authServerInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", authServerInfo);
    }

    @Override
    public List<AuthServerInfo> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<AuthServerInfo> findList(AuthServerInfo authServerInfo) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", authServerInfo);
    }

    @Override
    public int getCount(AuthServerInfo authServerInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", authServerInfo);
    }
   
}