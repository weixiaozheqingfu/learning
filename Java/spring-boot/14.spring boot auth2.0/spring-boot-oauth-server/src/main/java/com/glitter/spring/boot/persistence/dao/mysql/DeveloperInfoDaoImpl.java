package com.glitter.spring.boot.persistence.dao.mysql;

import com.glitter.spring.boot.bean.DeveloperInfo;
import com.glitter.spring.boot.persistence.dao.IDeveloperInfoDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeveloperInfoDaoImpl implements IDeveloperInfoDao{

    private static final String NAME_SPACE = "DeveloperInfo";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(DeveloperInfo developerInfo) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", developerInfo);
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
    public int delete(DeveloperInfo developerInfo) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", developerInfo);
    }

    @Override
    public int updateById(DeveloperInfo developerInfo) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", developerInfo);
    }

    @Override
    public DeveloperInfo getDeveloperInfoById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<DeveloperInfo> getDeveloperInfoByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public DeveloperInfo getDeveloperInfo(DeveloperInfo developerInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", developerInfo);
    }

    @Override
    public List<DeveloperInfo> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<DeveloperInfo> findList(DeveloperInfo developerInfo) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", developerInfo);
    }

    @Override
    public int getCount(DeveloperInfo developerInfo) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", developerInfo);
    }
   
}