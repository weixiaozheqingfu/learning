package com.glitter.spring.boot.persistence.dao.mysql;

import com.glitter.spring.boot.bean.OauthCode;
import com.glitter.spring.boot.persistence.dao.IOauthCodeDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OauthCodeDaoImpl implements IOauthCodeDao{

    private static final String NAME_SPACE = "com.glitter.spring.boot.persistence.dao.IOauthCodeDao";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthCode oauthCode) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthCode);
    }

    @Override
    public int deleteById(Long id) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".deleteById", id);
    }

    @Override
    public int deleteByCode(String code) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".deleteByCode", code);
    }

    @Override
    public int deleteByIds(Long[] ids) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".deleteByIds", ids);
    }

    @Override
    public int delete(OauthCode oauthCode) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthCode);
    }

    @Override
    public int updateById(OauthCode oauthCode) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthCode);
    }

    @Override
    public OauthCode getById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<OauthCode> getByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public OauthCode get(OauthCode oauthCode) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthCode);
    }

    @Override
    public OauthCode getByUserIdAndClient(String jsessionId, Long userId, String clientId) {
        Map<String, Object> map = new HashMap<>();
        map.put("jsessionId", jsessionId);
        map.put("userId", userId);
        map.put("clientId", clientId);
        map.put("now", new Date());
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getByUserIdAndClient", map);
    }

    @Override
    public void deleteByUserIdAndClient(String jsessionId, Long userId, String clientId) {
        Map<String, Object> map = new HashMap<>();
        map.put("jsessionId", jsessionId);
        map.put("userId", userId);
        map.put("clientId", clientId);
        map.put("now", new Date());
        sqlSessionTemplate.delete(NAME_SPACE + ".deleteByUserIdAndClient", map);
    }

    @Override
    public List<OauthCode> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthCode> findList(OauthCode oauthCode) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthCode);
    }

    @Override
    public int getCount(OauthCode oauthCode) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthCode);
    }
   
}