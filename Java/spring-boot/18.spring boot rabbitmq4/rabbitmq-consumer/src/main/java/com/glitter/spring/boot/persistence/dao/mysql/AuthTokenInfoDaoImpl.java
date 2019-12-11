package com.glitter.spring.boot.persistence.dao.mysql;

import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.glitter.spring.boot.persistence.dao.IAuthTokenInfoDao;
import com.glitter.spring.boot.bean.AuthTokenInfo;
import java.util.List;

@Repository
public class AuthTokenInfoDaoImpl implements IAuthTokenInfoDao{

    private static final String NAME_SPACE = "com.glitter.spring.boot.persistence.dao.IAuthTokenInfoDao";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(AuthTokenInfo authTokenInfo) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", authTokenInfo);
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
    public int delete(AuthTokenInfo authTokenInfo) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", authTokenInfo);
    }

    @Override
    public int updateById(AuthTokenInfo authTokenInfo) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", authTokenInfo);
    }

    @Override
    public AuthTokenInfo getById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<AuthTokenInfo> getByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public AuthTokenInfo get(AuthTokenInfo authTokenInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", authTokenInfo);
    }

    @Override
    public List<AuthTokenInfo> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<AuthTokenInfo> findList(AuthTokenInfo authTokenInfo) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", authTokenInfo);
    }

    @Override
    public int getCount(AuthTokenInfo authTokenInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", authTokenInfo);
    }
   
}