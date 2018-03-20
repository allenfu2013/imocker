package org.allen.imocker.dao;

import java.util.List;

import com.google.common.collect.Lists;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionTemplate;、
//import org.mybatis.spring.support.SqlSessionDaoSu、pport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BaseDao  {

    private static final int BATCH_SIZE = 100;

    public int insert(String sqlId, Object param) {
        return 1;
    }

    public int update(String sqlId, Object param) {
        return 1;
    }

    public <T> T queryForObject(String sqlId, Object param) {
        return null;
    }

    public <T> List<T> queryForList(String sqlId, Object param) {
        return null;
    }

    public int delete(String sqlId, Object param) {
        return 1;
    }



}