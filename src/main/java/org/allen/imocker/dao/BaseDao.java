package org.allen.imocker.dao;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.springframework.beans.factory.annotation.Autowired;
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
        Transaction t = Cat.newTransaction(CatConstants.TYPE_SQL, sqlId);
        try {
            Object obj = sqlMapClient.insert(sqlId, param);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("SQL.Method", "INSERT", Message.SUCCESS, null);
            Cat.logEvent("SQL.Database", databaseUrl);
            return obj;
        } catch (SQLException e) {
            t.setStatus(e);
            Cat.logEvent("SQL.Method", "INSERT", "99", null);
            throw new RuntimeException(String.format("DAO insert failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        } finally {
            t.complete();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int update(String sqlId, Object param) {
        Transaction t = Cat.newTransaction(CatConstants.TYPE_SQL, sqlId);
        try {
            int ret = sqlMapClient.update(sqlId, param);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("SQL.Method", "UPDATE", Message.SUCCESS, null);
            Cat.logEvent("SQL.Database", databaseUrl);
            return ret;
        } catch (SQLException e) {
            t.setStatus(e);
            Cat.logEvent("SQL.Method", "UPDATE", "99", null);
            throw new RuntimeException(String.format("DAO update failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        } finally {
            t.complete();
        }
    }

    public <T> List<T> queryForList(String sqlId, Object param) {
        Transaction t = Cat.newTransaction(CatConstants.TYPE_SQL, sqlId);
        try {
            List list = sqlMapClient.queryForList(sqlId, param);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("SQL.Method", "SELECT", Message.SUCCESS, null);
            Cat.logEvent("SQL.Database", databaseUrl);
            return list;
        } catch (SQLException e) {
            t.setStatus(e);
            Cat.logEvent("SQL.Method", "SELECT", "99", null);
            throw new RuntimeException(String.format("DAO queryForList failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        } finally {
            t.complete();
        }
    }

    public <T> T queryForObject(String sqlId, Object param) {
        Transaction t = Cat.newTransaction(CatConstants.TYPE_SQL, sqlId);
        try {
            Object obj = sqlMapClient.queryForObject(sqlId, param);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("SQL.Method", "SELECT", Message.SUCCESS, null);
            Cat.logEvent("SQL.Database", databaseUrl);
            return (T) obj;
        } catch (SQLException e) {
            t.setStatus(e);
            Cat.logEvent("SQL.Method", "SELECT", "99", null);
            throw new RuntimeException(String.format("DAO queryForObject failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        } finally {
            t.complete();
        }
    }

    public int delete(String sqlId, Object param) {
        Transaction t = Cat.newTransaction(CatConstants.TYPE_SQL, sqlId);
        try {
            int ret = sqlMapClient.delete(sqlId, param);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("SQL.Method", "DELETE", Message.SUCCESS, null);
            Cat.logEvent("SQL.Database", databaseUrl);
            return ret;
        } catch (SQLException e) {
            t.setStatus(e);
            Cat.logEvent("SQL.Method", "DELETE", "99", null);
            throw new RuntimeException(String.format("DAO delete failed, sqlId=%s, param=%s, error=%s", sqlId, param, e.getCause().getMessage()), e);
        } finally {
            t.complete();
        }
    }
}
