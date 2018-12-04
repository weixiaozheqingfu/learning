package com.glitter.spring.boot.persistence.dao.mysql;

import com.glitter.spring.boot.bean.OauthDeveloperRM;
import com.glitter.spring.boot.persistence.dao.IOauthDeveloperRMDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OauthDeveloperRMDaoImpl implements IOauthDeveloperRMDao{

    private static final String NAME_SPACE = "OauthDeveloperRM";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthDeveloperRM oauthDeveloperRM) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthDeveloperRM);
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
    public int delete(OauthDeveloperRM oauthDeveloperRM) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthDeveloperRM);
    }

    @Override
    public int updateById(OauthDeveloperRM oauthDeveloperRM) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthDeveloperRM);
    }

    @Override
    public OauthDeveloperRM getOauthDeveloperRMById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<OauthDeveloperRM> getOauthDeveloperRMByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public OauthDeveloperRM getOauthDeveloperRM(OauthDeveloperRM oauthDeveloperRM) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthDeveloperRM);
    }

    @Override
    public List<OauthDeveloperRM> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthDeveloperRM> findList(OauthDeveloperRM oauthDeveloperRM) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthDeveloperRM);
    }

    @Override
    public int getCount(OauthDeveloperRM oauthDeveloperRM) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthDeveloperRM);
    }
   
}