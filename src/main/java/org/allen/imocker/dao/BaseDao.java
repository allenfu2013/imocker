package org.allen.imocker.dao;

import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BaseDao {

    @Resource(name = "sqlMapClient")
    protected SqlMapClient sqlMapClient;

    @Resource(name = "sqlMapClientTemplate")
    protected SqlMapClientTemplate sqlMapClientTemplate;

    @Value("${jdbc.url}")
    String databaseUrl;

    @Transactional(propagation = Propagation.REQUIRED)
    public Object insert(String sqlId, Object param) {
        try {
            Object obj = sqlMapClient.insert(sqlId, param);
            return obj;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("DAO insert failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int update(String sqlId, Object param) {
        try {
            int ret = sqlMapClient.update(sqlId, param);
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("DAO update failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        }
    }

    public <T> List<T> queryForList(String sqlId, Object param) {
        try {
            List list = sqlMapClient.queryForList(sqlId, param);
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("DAO queryForList failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        }
    }

    public <T> T queryForObject(String sqlId, Object param) {
        try {
            Object obj = sqlMapClient.queryForObject(sqlId, param);
            return (T) obj;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("DAO queryForObject failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        }
    }

    public int delete(String sqlId, Object param) {
        try {
            int ret = sqlMapClient.delete(sqlId, param);
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("DAO delete failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        }
    }
}
