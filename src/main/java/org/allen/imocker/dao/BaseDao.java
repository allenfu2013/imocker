package org.allen.imocker.dao;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
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
        Transaction t = Cat.newTransaction(CatConstants.TYPE_SQL, sqlId);
        try {
            Object obj = sqlMapClient.insert(sqlId, param);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("SQL.Method", "INSERT", Message.SUCCESS, null);
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
