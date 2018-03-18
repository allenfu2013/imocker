package org.allen.imocker.service;

import org.allen.imocker.dao.BaseDao;
import org.allen.imocker.dto.RemoteCallInfo;
import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.entity.ParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        ApiInfo apiInfo = baseDao.queryForObject("ApiInfo.getById", id);
        List<ApiCondition> apiConditionList = baseDao.queryForList("ApiCondition.getByApiInfoId", id);
        apiInfo.setApiConditionList(apiConditionList);
        return apiInfo;
    }

    public List<ApiInfo> findByCondition(Map<String, Object> cond) {
        return baseDao.queryForList("ApiInfo.findByCondition", cond);
    }

    @Transactional
    public Integer insertApiInfo(ApiInfo apiInfo) {
        apiInfo.setHasCondition(!CollectionUtils.isEmpty(apiInfo.getApiConditionList()));
        int count = baseDao.insert("ApiInfo.insert", apiInfo);
        if (count > 0 && !CollectionUtils.isEmpty(apiInfo.getApiConditionList())) {
            apiInfo.getApiConditionList().forEach(apiCondition -> {
                apiCondition.setApiInfoId(apiInfo.getId());
                baseDao.insert("ApiCondition.insert", apiCondition);
            });
        }
        return count;
    }

    public List<ApiInfo> findApiInfoByName(String apiName) {
        return baseDao.queryForList("ApiInfo.findApiInfoByName", apiName);
    }

    public List<ApiCondition> getApiConditionList(Integer apiInfoId) {
        return baseDao.queryForList("ApiCondition.getByApiInfoId", apiInfoId);
    }

    public Long countByCondition(Map<String, Object> cond) {
        return baseDao.queryForObject("ApiInfo.countByCondition", cond);
    }

    public ApiInfo getById(long id) {
        ApiInfo apiInfo = baseDao.queryForObject("ApiInfo.getById", id);
        List<ApiCondition> apiConditionList = baseDao.queryForList("ApiCondition.getByApiInfoId", id);
        apiInfo.setApiConditionList(apiConditionList);
        return apiInfo;
    }

    @Transactional
    public boolean update(ApiInfo apiInfo) {
        apiInfo.setHasCondition(!CollectionUtils.isEmpty(apiInfo.getApiConditionList()));
        boolean flag = baseDao.update("ApiInfo.update", apiInfo) > 0 ? true : false;
        if (flag) {
            baseDao.delete("ApiCondition.deleteByApiInfoId", apiInfo.getId());
            apiInfo.getApiConditionList().forEach(apiCondition -> {
                apiCondition.setApiInfoId(apiInfo.getId());
                baseDao.insert("ApiCondition.insert", apiCondition);
            });
        }
        return flag;
    }

    public List<ApiInfo> findUriVariableApi() {
        return baseDao.queryForList("ApiInfo.findUriVariableApi", null);
    }

    @Transactional
    public boolean deleteById(Integer id) {
        boolean flag = baseDao.delete("ApiInfo.deleteById", id) > 0 ? true : false;
        if (flag) {
            baseDao.delete("ApiCondition.deleteByApiInfoId", id);
        }
        return flag;
    }
}
