package com.glitter.spring.boot.persistence.dao.mysql;

import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.glitter.spring.boot.persistence.dao.IAuthServerInterfaceInfoDao;
import com.glitter.spring.boot.bean.AuthServerInterfaceInfo;
import java.util.List;

@Repository
public class AuthServerInterfaceInfoDaoImpl implements IAuthServerInterfaceInfoDao{

    private static final String NAME_SPACE = "com.glitter.spring.boot.persistence.dao.IAuthServerInterfaceInfoDao";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(AuthServerInterfaceInfo authServerInterfaceInfo) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", authServerInterfaceInfo);
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
    public int delete(AuthServerInterfaceInfo authServerInterfaceInfo) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", authServerInterfaceInfo);
    }

    @Override
    public int updateById(AuthServerInterfaceInfo authServerInterfaceInfo) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", authServerInterfaceInfo);
    }

    @Override
    public AuthServerInterfaceInfo getById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<AuthServerInterfaceInfo> getByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public AuthServerInterfaceInfo get(AuthServerInterfaceInfo authServerInterfaceInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", authServerInterfaceInfo);
    }

    @Override
    public List<AuthServerInterfaceInfo> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<AuthServerInterfaceInfo> findList(AuthServerInterfaceInfo authServerInterfaceInfo) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", authServerInterfaceInfo);
    }

    @Override
    public int getCount(AuthServerInterfaceInfo authServerInterfaceInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", authServerInterfaceInfo);
    }
   
}