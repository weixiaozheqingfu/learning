package com.glitter.spring.boot.persistence.dao.mysql;

import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.glitter.spring.boot.persistence.dao.IAccountBindingDao;
import com.glitter.spring.boot.bean.AccountBinding;
import java.util.List;

@Repository
public class AccountBindingDaoImpl implements IAccountBindingDao{

    private static final String NAME_SPACE = "AccountBinding";

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int insert(AccountBinding accountBinding) {
        return sqlSessionTemplate.insert(NAME_SPACE + ".insert", accountBinding);
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
    public int delete(AccountBinding accountBinding) {
        return sqlSessionTemplate.delete(NAME_SPACE + ".delete", accountBinding);
    }

    @Override
    public int updateById(AccountBinding accountBinding) {
        return sqlSessionTemplate.update(NAME_SPACE + ".updateById", accountBinding);
    }

    @Override
    public AccountBinding getAccountBindingById(Long id) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getById", id);
    }

    @Override
    public List<AccountBinding> getAccountBindingByIds(Long[] ids) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".getByIds", ids);
    }

    @Override
    public AccountBinding getAccountBinding(AccountBinding accountBinding) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".get", accountBinding);
    }

    @Override
    public List<AccountBinding> findAllList() {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findAllList");
    }

    @Override
    public List<AccountBinding> findList(AccountBinding accountBinding) {
        return sqlSessionTemplate.selectList(NAME_SPACE + ".findList", accountBinding);
    }

    @Override
    public int getCount(AccountBinding accountBinding) {
        return sqlSessionTemplate.selectOne(NAME_SPACE + ".getCount", accountBinding);
    }
   
}