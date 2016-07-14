package org.allen.imocker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.allen.imocker.common.ApiResonse;
import org.allen.imocker.common.ApiResponseCode;
import org.allen.imocker.dao.ApiInfoDao;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;


@Controller
@RequestMapping("/api")
public class ApiController {
	
	private static final String PREFIX_API = "/api/";
	
	@Autowired
	private ApiInfoDao apiInfoDao;
	
	@RequestMapping(value = "/**", produces="application/json;charset=UTF-8")
	@ResponseBody
	public String mockApi(HttpServletRequest request) {
		ApiResonse apiResonse = new ApiResonse();
		String pathInfo = request.getPathInfo();
		String apiName = pathInfo.substring(PREFIX_API.length());
        String ret = null;
		LoggerUtil.info(this, String.format("pathInfo:%s, apiName: %s",  pathInfo, apiName));
		Map<String, Object> cond = new HashMap<String, Object>();
		cond.put("apiName", apiName);
		cond.put("status", 1);
		try {
			List<ApiInfo> apiInfoList = apiInfoDao.findByCondition(cond);
			if (!CollectionUtils.isEmpty(apiInfoList)) {
				ret = apiInfoList.get(0).getRetResult();
			} else {
				apiResonse = new ApiResonse(ApiResponseCode.SUCCESS);
				apiResonse.setData(String.format("No invalid api found, api: %s", apiName));
			}
		} catch (Exception e) {
			apiResonse = new ApiResonse(ApiResponseCode.SERVER_ERROR);
			LoggerUtil.error(this, String.format("find api_info failed, apiName: %s", apiName), e);
		}
        if (ret == null) {
            ret = new Gson().toJson(apiResonse);
        }
		LoggerUtil.info(this, String.format("[%s] result: %s", apiName, ret));
		return ret;
	}
}
