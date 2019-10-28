package com.glitter.spring.boot.persistence.dao.mysql;

import com.glitter.spring.boot.bean.OauthClientConfig;
import com.glitter.spring.boot.persistence.dao.IOauthClientConfigDao;
import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Repository
public class OauthClientConfigDaoImpl implements IOauthClientConfigDao {

    private static final String NAME_SPACE = "com.glitter.spring.boot.persistence.dao.IOauthClientConfigDao";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthClientConfig oauthClientConfig) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthClientConfig);
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
    public int delete(OauthClientConfig oauthClientConfig) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthClientConfig);
    }

    @Override
    public int updateById(OauthClientConfig oauthClientConfig) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthClientConfig);
    }

    @Override
    public OauthClientConfig getById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<OauthClientConfig> getByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public OauthClientConfig getOauthClientConfig(OauthClientConfig oauthClientConfig) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthClientConfig);
    }

    @Override
    public List<OauthClientConfig> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthClientConfig> findList(OauthClientConfig oauthClientConfig) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthClientConfig);
    }

    @Override
    public int getCount(OauthClientConfig oauthClientConfig) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthClientConfig);
    }
   
}