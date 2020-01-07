package com.glitter.spring.boot.persistence.dao.mysql;

import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.glitter.spring.boot.persistence.dao.IUserInfoDao;
import com.glitter.spring.boot.bean.UserInfo;
import java.util.List;

@Repository
public class UserInfoDaoImpl implements IUserInfoDao{

    private static final String NAME_SPACE = "com.glitter.spring.boot.persistence.dao.IUserInfoDao";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(UserInfo userInfo) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", userInfo);
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
    public int delete(UserInfo userInfo) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", userInfo);
    }

    @Override
    public int updateById(UserInfo userInfo) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", userInfo);
    }

    @Override
    public UserInfo getById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<UserInfo> getByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public UserInfo get(UserInfo userInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", userInfo);
    }

    @Override
    public List<UserInfo> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<UserInfo> findList(UserInfo userInfo) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", userInfo);
    }

    @Override
    public int getCount(UserInfo userInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", userInfo);
    }
   
}