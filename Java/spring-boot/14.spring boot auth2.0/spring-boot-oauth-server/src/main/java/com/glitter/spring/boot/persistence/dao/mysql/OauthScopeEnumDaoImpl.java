package com.glitter.spring.boot.persistence.dao.mysql;

import com.glitter.spring.boot.bean.OauthScopeEnum;
import com.glitter.spring.boot.persistence.dao.IOauthScopeEnumDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OauthScopeEnumDaoImpl implements IOauthScopeEnumDao{

    private static final String NAME_SPACE = "OauthScopeEnum";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthScopeEnum oauthScopeEnum) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthScopeEnum);
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
    public int delete(OauthScopeEnum oauthScopeEnum) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthScopeEnum);
    }

    @Override
    public int updateById(OauthScopeEnum oauthScopeEnum) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthScopeEnum);
    }

    @Override
    public OauthScopeEnum getOauthScopeEnumById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<OauthScopeEnum> getOauthScopeEnumByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public List<OauthScopeEnum> getOauthScopeEnumByScopeNames(List<String> scopeNames) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByScopeNames", scopeNames);
    }

    @Override
    public OauthScopeEnum getOauthScopeEnum(OauthScopeEnum oauthScopeEnum) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthScopeEnum);
    }

    @Override
    public List<OauthScopeEnum> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthScopeEnum> findList(OauthScopeEnum oauthScopeEnum) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthScopeEnum);
    }

    @Override
    public int getCount(OauthScopeEnum oauthScopeEnum) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthScopeEnum);
    }
   
}