package com.glitter.spring.boot.persistence.dao.mysql;

import com.glitter.spring.boot.bean.OauthClientRM;
import com.glitter.spring.boot.persistence.dao.IOauthClientRMDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OauthClientRMDaoImpl implements IOauthClientRMDao{

    private static final String NAME_SPACE = "OauthClientRM";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthClientRM oauthClientRM) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthClientRM);
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
    public int delete(OauthClientRM oauthClientRM) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthClientRM);
    }

    @Override
    public int updateById(OauthClientRM oauthClientRM) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthClientRM);
    }

    @Override
    public OauthClientRM getOauthClientRMById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<OauthClientRM> getOauthClientRMByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public OauthClientRM getOauthClientRM(OauthClientRM oauthClientRM) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthClientRM);
    }

    @Override
    public List<OauthClientRM> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthClientRM> findList(OauthClientRM oauthClientRM) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthClientRM);
    }

    @Override
    public int getCount(OauthClientRM oauthClientRM) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthClientRM);
    }
   
}