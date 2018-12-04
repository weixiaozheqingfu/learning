package com.glitter.spring.boot.persistence.dao.mysql;

import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.glitter.spring.boot.persistence.dao.IOauthClientScenarioConfigDao;
import com.glitter.spring.boot.bean.OauthClientScenarioConfig;
import java.util.List;

@Repository
public class OauthClientScenarioConfigDaoImpl implements IOauthClientScenarioConfigDao{

    private static final String NAME_SPACE = "OauthClientScenarioConfig";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthClientScenarioConfig oauthClientScenarioConfig) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthClientScenarioConfig);
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
    public int delete(OauthClientScenarioConfig oauthClientScenarioConfig) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthClientScenarioConfig);
    }

    @Override
    public int updateById(OauthClientScenarioConfig oauthClientScenarioConfig) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthClientScenarioConfig);
    }

    @Override
    public OauthClientScenarioConfig getOauthClientScenarioConfigById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<OauthClientScenarioConfig> getOauthClientScenarioConfigByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public OauthClientScenarioConfig getOauthClientScenarioConfig(OauthClientScenarioConfig oauthClientScenarioConfig) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthClientScenarioConfig);
    }

    @Override
    public List<OauthClientScenarioConfig> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthClientScenarioConfig> findList(OauthClientScenarioConfig oauthClientScenarioConfig) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthClientScenarioConfig);
    }

    @Override
    public int getCount(OauthClientScenarioConfig oauthClientScenarioConfig) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthClientScenarioConfig);
    }
   
}