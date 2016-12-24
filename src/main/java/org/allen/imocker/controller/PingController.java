package org.allen.imocker.controller;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.allen.imocker.dto.ApiResponse;
import org.allen.imocker.dto.ApiResponseCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PingController {

    @RequestMapping(value = "ping", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse ping() {
        return new ApiResponse(ApiResponseCode.SUCCESS).setRetMsg("imocker is running...");
    }

    @RequestMapping(value = "testCall", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse testRemoteCall() {
        Transaction t = Cat.newTransaction(CatConstants.TYPE_CALL, "TestCall");
        try {
            Thread.sleep(200);
            t.setStatus(Transaction.SUCCESS);
            Cat.logEvent(CatConstants.TYPE_CALL, "TestCall", Message.SUCCESS, null);
        } catch (Exception e) {
            t.setStatus(e);
            Cat.logEvent(CatConstants.TYPE_CALL, "TestCall", "99", null);
        } finally {
            t.complete();
        }
        return new ApiResponse(ApiResponseCode.SUCCESS);
    }

}
