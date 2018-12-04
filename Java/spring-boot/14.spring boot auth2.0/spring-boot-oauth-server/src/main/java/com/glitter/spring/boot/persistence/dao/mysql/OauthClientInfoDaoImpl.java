package com.glitter.spring.boot.persistence.dao.mysql;

import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.persistence.dao.IOauthClientInfoDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OauthClientInfoDaoImpl implements IOauthClientInfoDao{

    private static final String NAME_SPACE = "OauthClientInfo";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthClientInfo oauthClientInfo) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthClientInfo);
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
    public int delete(OauthClientInfo oauthClientInfo) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthClientInfo);
    }

    @Override
    public int updateById(OauthClientInfo oauthClientInfo) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthClientInfo);
    }

    @Override
    public OauthClientInfo getOauthClientInfoById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<OauthClientInfo> getOauthClientInfoByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public OauthClientInfo getOauthClientInfo(OauthClientInfo oauthClientInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthClientInfo);
    }

    @Override
    public List<OauthClientInfo> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthClientInfo> findList(OauthClientInfo oauthClientInfo) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthClientInfo);
    }

    @Override
    public int getCount(OauthClientInfo oauthClientInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthClientInfo);
    }
   
}