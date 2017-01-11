package org.allen.imocker.service;

import org.allen.imocker.dao.BaseDao;
import org.allen.imocker.dto.RemoteCallInfo;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.entity.ParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiInfoService {

    @Autowired
    private BaseDao baseDao;

    public RemoteCallInfo getApiInfoWithParams(long id) {
        RemoteCallInfo remoteCallInfo = null;
        ApiInfo apiInfo = getApiInfoById(id);
        if (apiInfo != null) {
            remoteCallInfo = new RemoteCallInfo();
            remoteCallInfo.setUrl(apiInfo.getQaUrl() + apiInfo.getApiName());
            remoteCallInfo.setMethod(apiInfo.getMethod());

            List<ParaInfo> paraInfos = baseDao.queryForList("ParaInfo.findParaInfoById", apiInfo.getId());
            if (paraInfos != null && !paraInfos.isEmpty()) {
                List<String> paramList = new ArrayList<>();
                remoteCallInfo.setParamList(paramList);
                for (ParaInfo paraInfo : paraInfos) {
                    paramList.add(paraInfo.getParaName());
                }
            }
        }
        return remoteCallInfo;
    }

    public ApiInfo getApiInfoById(long id) {
        return baseDao.queryForObject("ApiInfo.getById", id);
    }

    public List<ApiInfo> findByCondition(Map<String, Object> cond) {
        return baseDao.queryForList("ApiInfo.findByCondition", cond);
    }

    public Integer insertApiInfo(ApiInfo apiInfo) {
        return (Integer) baseDao.insert("ApiInfo.insert", apiInfo);
    }

    public List<ApiInfo> findApiInfoByName(String apiName) {
        return baseDao.queryForList("ApiInfo.findApiInfoByName", apiName);
    }

    public Long countByCondition(Map<String, Object> cond) {
        return baseDao.queryForObject("ApiInfo.countByCondition", cond);
    }

    public ApiInfo getById(long id) {
        return baseDao.queryForObject("ApiInfo.getById", id);
    }

    public boolean update(ApiInfo apiInfo) {
        return baseDao.update("ApiInfo.update", apiInfo) > 0 ? true : false;
    }

    public List<ApiInfo> findRegexApi() {
        return baseDao.queryForList("ApiInfo.findRegexApi", null);
    }

    public boolean deleteByCond(Map<String, Object> cond) {
        return baseDao.delete("ApiInfo.deleteByCond", cond) > 0 ? true : false;
    }
}
