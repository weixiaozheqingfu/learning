package com.glitter.spring.boot.persistence.dao.mysql;

import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.glitter.spring.boot.persistence.dao.IOauthClientBaseConfigDao;
import com.glitter.spring.boot.bean.OauthClientBaseConfig;
import java.util.List;

@Repository
public class OauthClientBaseConfigDaoImpl implements IOauthClientBaseConfigDao{

    private static final String NAME_SPACE = "OauthClientBaseConfig";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthClientBaseConfig oauthClientBaseConfig) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthClientBaseConfig);
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
    public int delete(OauthClientBaseConfig oauthClientBaseConfig) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthClientBaseConfig);
    }

    @Override
    public int updateById(OauthClientBaseConfig oauthClientBaseConfig) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthClientBaseConfig);
    }

    @Override
    public OauthClientBaseConfig getOauthClientBaseConfigById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<OauthClientBaseConfig> getOauthClientBaseConfigByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public OauthClientBaseConfig getOauthClientBaseConfig(OauthClientBaseConfig oauthClientBaseConfig) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthClientBaseConfig);
    }

    @Override
    public List<OauthClientBaseConfig> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthClientBaseConfig> findList(OauthClientBaseConfig oauthClientBaseConfig) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthClientBaseConfig);
    }

    @Override
    public int getCount(OauthClientBaseConfig oauthClientBaseConfig) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthClientBaseConfig);
    }
   
}