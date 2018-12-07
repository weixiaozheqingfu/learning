package com.glitter.spring.boot.persistence.dao.mysql;

import com.glitter.spring.boot.bean.OauthInterfaceEnum;
import com.glitter.spring.boot.persistence.dao.IOauthInterfaceEnumDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OauthInterfaceEnumDaoImpl implements IOauthInterfaceEnumDao{

    private static final String NAME_SPACE = "OauthInterfaceEnum";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthInterfaceEnum oauthInterfaceEnum) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthInterfaceEnum);
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
    public int delete(OauthInterfaceEnum oauthInterfaceEnum) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthInterfaceEnum);
    }

    @Override
    public int updateById(OauthInterfaceEnum oauthInterfaceEnum) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthInterfaceEnum);
    }

    @Override
    public OauthInterfaceEnum getOauthInterfaceEnumById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<OauthInterfaceEnum> getOauthInterfaceEnumByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public List<OauthInterfaceEnum> getOauthInterfaceEnumByScopes(String[] scopes) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByScopes", scopes);
    }

    @Override
    public OauthInterfaceEnum getOauthInterfaceEnum(OauthInterfaceEnum oauthInterfaceEnum) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthInterfaceEnum);
    }

    @Override
    public List<OauthInterfaceEnum> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthInterfaceEnum> findList(OauthInterfaceEnum oauthInterfaceEnum) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthInterfaceEnum);
    }

    @Override
    public int getCount(OauthInterfaceEnum oauthInterfaceEnum) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthInterfaceEnum);
    }
   
}