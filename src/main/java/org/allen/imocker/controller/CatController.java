package org.allen.imocker.controller;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.allen.imocker.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("cat")
public class CatController {

    @RequestMapping(value = "error", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse testError() {
        LoggerUtil.error(this, "test error", new RuntimeException("test error"));
        return new ApiResponse(ApiResponseCode.SERVER_ERROR);
    }

    @RequestMapping(value = "memcached", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse testMemcached() {
        Transaction t = Cat.newTransaction("Cache.memcached", "users:get");
        try {
            Thread.sleep(50);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("Cache.memcached.server", "192.168.1.3", Message.SUCCESS, null);
        } catch (Exception e) {
            t.setStatus(e);
            Cat.logEvent("Cache.memcached.server", "192.168.1.3", "99", null);
        } finally {
            t.complete();
        }
        return new ApiResponse(ApiResponseCode.SUCCESS).setRetMsg("test memcached");
    }

    @RequestMapping(value = "redis", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse testRedis() {
        Transaction t = Cat.newTransaction("Cache.redis", "users:get");
        try {
            Thread.sleep(50);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("Cache.redis.server", "192.168.1.4", Message.SUCCESS, null);
        } catch (Exception e) {
            t.setStatus(e);
            Cat.logEvent("Cache.redis.server", "192.168.1.4", "99", null);
        } finally {
            t.complete();
        }
        return new ApiResponse(ApiResponseCode.SUCCESS).setRetMsg("test redis");
    }

    @Value("${jdbc.url}")
    String databaseUrl;

    @RequestMapping(value = "sql", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse testSQL() {
        Transaction t = Cat.newTransaction("SQL", "sqlId");
        try {
            Thread.sleep(50);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("SQL.Method", "SELECT", Message.SUCCESS, null);
            Cat.logEvent("SQL.Database", databaseUrl);
        } catch (Exception e) {
            t.setStatus(e);
            Cat.logEvent("SQL.Method", "SELECT", "99", null);
        } finally {
            t.complete();
        }
        return new ApiResponse(ApiResponseCode.SUCCESS).setRetMsg("test sql");
    }

    @RequestMapping(value = "pigeonCall", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse pigeonCall() {
        Transaction t = Cat.newTransaction("PigeonCall", "service1");
        try {
            Thread.sleep(50);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("PigeonCall.app", "test-app-1");
            Cat.logEvent("PigeonCall.server", "192.168.1.3");
            Cat.logEvent("PigeonCall.port", "9999");
        } catch (Exception e) {
            t.setStatus(e);
        } finally {
            t.complete();
        }
        return new ApiResponse(ApiResponseCode.SUCCESS).setRetMsg("test pigeon call");
    }

    @RequestMapping(value = "call", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse call() {
        Transaction t = Cat.newTransaction("Call", "service2");
        try {
            Thread.sleep(50);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent("Call.app", "test-app-2");
            Cat.logEvent("Call.server", "192.168.1.3");
            Cat.logEvent("Call.port", "9999");
        } catch (Exception e) {
            t.setStatus(e);
        } finally {
            t.complete();
        }
        return new ApiResponse(ApiResponseCode.SUCCESS).setRetMsg("test call");
    }

}
