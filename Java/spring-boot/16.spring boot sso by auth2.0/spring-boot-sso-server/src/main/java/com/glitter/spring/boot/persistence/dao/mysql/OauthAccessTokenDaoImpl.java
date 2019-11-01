package com.glitter.spring.boot.persistence.dao.mysql;

import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OauthAccessTokenDaoImpl implements IOauthAccessTokenDao{

    private static final String NAME_SPACE = "com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthAccessToken oauthAccessToken) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthAccessToken);
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
    public int delete(OauthAccessToken oauthAccessToken) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthAccessToken);
    }

    @Override
    public int updateById(OauthAccessToken oauthAccessToken) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthAccessToken);
    }

    @Override
    public OauthAccessToken getById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public OauthAccessToken getByRefreshToken(String refreshToken) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getByRefreshToken", refreshToken);
    }

    @Override
    public List<OauthAccessToken> getByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public OauthAccessToken get(OauthAccessToken oauthAccessToken) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthAccessToken);
    }

    @Override
    public List<OauthAccessToken> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthAccessToken> findList(OauthAccessToken oauthAccessToken) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthAccessToken);
    }

    @Override
    public int getCount(OauthAccessToken oauthAccessToken) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthAccessToken);
    }

    @Override
    public int updateJsessionidClientByAccessToken(String accessToken, Long jsessionidClient) {
        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("jsessionidClient", jsessionidClient);
        return sqlSessionTemplate.update(NAME_SPACE + ".updateJsessionidClientByAccessToken", map);
    }
}