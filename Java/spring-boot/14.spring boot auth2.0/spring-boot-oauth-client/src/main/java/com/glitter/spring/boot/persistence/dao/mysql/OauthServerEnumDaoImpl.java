package com.glitter.spring.boot.persistence.dao.mysql;

import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.glitter.spring.boot.persistence.dao.IOauthServerEnumDao;
import com.glitter.spring.boot.bean.OauthServerEnum;
import java.util.List;

@Repository
public class OauthServerEnumDaoImpl implements IOauthServerEnumDao{

    private static final String NAME_SPACE = "OauthServerEnum";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(OauthServerEnum oauthServerEnum) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", oauthServerEnum);
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
    public int delete(OauthServerEnum oauthServerEnum) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", oauthServerEnum);
    }

    @Override
    public int updateById(OauthServerEnum oauthServerEnum) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", oauthServerEnum);
    }

    @Override
    public OauthServerEnum getOauthServerEnumById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<OauthServerEnum> getOauthServerEnumByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public OauthServerEnum getOauthServerEnum(OauthServerEnum oauthServerEnum) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", oauthServerEnum);
    }

    @Override
    public List<OauthServerEnum> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<OauthServerEnum> findList(OauthServerEnum oauthServerEnum) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", oauthServerEnum);
    }

    @Override
    public int getCount(OauthServerEnum oauthServerEnum) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", oauthServerEnum);
    }
   
}