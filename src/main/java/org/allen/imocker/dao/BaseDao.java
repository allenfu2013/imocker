package org.allen.imocker.dao;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseDao {

    @Resource(name = "sqlMapClient")
    protected SqlMapClient sqlMapClient;

    @Resource(name = "sqlMapClientTemplate")
    protected SqlMapClientTemplate sqlMapClientTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public Object insert(String sqlId, Object param) {
        try {
            return sqlMapClient.insert(sqlId, param);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("DAO insert failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int update(String sqlId, Object param) {
        try {
            return sqlMapClient.update(sqlId, param);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("DAO update failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        }
    }

    public <T> List<T> queryForList(String sqlId, Object param) {
        try {
            return sqlMapClient.queryForList(sqlId, param);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("DAO queryForList failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        }
    }

    public <T> T queryForObject(String sqlId, Object param) {
        try {
            return (T) sqlMapClient.queryForObject(sqlId, param);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("DAO queryForObject failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        }
    }
}
