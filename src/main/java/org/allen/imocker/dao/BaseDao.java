package org.allen.imocker.dao;

import java.util.List;

import com.google.common.collect.Lists;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BaseDao extends SqlSessionDaoSupport {

    private static final int BATCH_SIZE = 100;

    public int insert(String sqlId, Object param) {
        try {
            return this.getSqlSession().insert(sqlId, param);
        } catch (Exception e) {
            throw new RuntimeException(String.format("insert failed, sqlId=%s, param=%s, error=%s",
                    sqlId, param, e.getCause().getMessage()), e);
        }
    }

    public int update(String sqlId, Object param) {
        try {
            return this.getSqlSession().update(sqlId, param);
        } catch (Exception e) {
            throw new RuntimeException(String.format("update failed, sqlId=%s, param=%s, error=%s",
                    sqlId, param, e.getCause().getMessage()), e);
        }
    }

    public <T> T queryForObject(String sqlId, Object param) {
        try {
            return this.getSqlSession().selectOne(sqlId, param);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("queryObject failed, sqlId=%s, param=%s, error=%s", sqlId, param,
                            e.getCause().getMessage()),
                    e);
        }
    }

    public <T> List<T> queryForList(String sqlId, Object param) {
        try {
            return this.getSqlSession().selectList(sqlId, param);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("queryList failed, sqlId=%s, param=%s, error=%s", sqlId, param,
                            e.getCause().getMessage()),
                    e);
        }
    }

    public int delete(String sqlId, Object param) {
        try {
            return this.getSqlSession().delete(sqlId, param);
        } catch (Exception e) {
            throw new RuntimeException(String.format("delete failed, sqlId=%s, param=%s, error=%s",
                    sqlId, param, e.getCause().getMessage()), e);
        }
    }

    @Autowired
    @Override
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    @Autowired
    @Override
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

}