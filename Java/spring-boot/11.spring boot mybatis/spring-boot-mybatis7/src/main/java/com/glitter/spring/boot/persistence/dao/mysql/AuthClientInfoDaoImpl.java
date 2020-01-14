package com.glitter.spring.boot.persistence.dao.mysql;

import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.glitter.spring.boot.persistence.dao.IAuthClientInfoDao;
import com.glitter.spring.boot.bean.AuthClientInfo;
import java.util.List;

@Repository
public class AuthClientInfoDaoImpl implements IAuthClientInfoDao{

    private static final String NAME_SPACE = "com.glitter.spring.boot.persistence.dao.IAuthClientInfoDao";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(AuthClientInfo authClientInfo) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", authClientInfo);
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
    public int delete(AuthClientInfo authClientInfo) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", authClientInfo);
    }

    @Override
    public int updateById(AuthClientInfo authClientInfo) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", authClientInfo);
    }

    /**
     * 按主键查询
     *
     * @param id
     * @return
     */
    @Override
    public AuthClientInfo getById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    /**
     * 按主键集合查询
     *
     * @param ids
     * @return
     */
    @Override
    public List<AuthClientInfo> getByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    /**
     * 按条件查询
     *
     * @param authClientInfo
     * @return
     */
    @Override
    public AuthClientInfo get(AuthClientInfo authClientInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", authClientInfo);
    }


    @Override
    public List<AuthClientInfo> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<AuthClientInfo> findList(AuthClientInfo authClientInfo) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", authClientInfo);
    }

    @Override
    public int getCount(AuthClientInfo authClientInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", authClientInfo);
    }
   
}