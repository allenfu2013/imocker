package org.allen.imocker.service;

import org.allen.imocker.controller.request.QueryApiInfoRequest;
import org.allen.imocker.dao.ApiConditionRepository;
import org.allen.imocker.dao.ApiInfoRepository;
import org.allen.imocker.dao.BaseDao;
import org.allen.imocker.dto.RemoteCallInfo;
import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.entity.ParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiInfoService {

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private ApiInfoRepository apiInfoRepository;

    @Autowired
    private ApiConditionRepository apiConditionRepository;

    public RemoteCallInfo getApiInfoWithParams(long id) {
        RemoteCallInfo remoteCallInfo = null;
        ApiInfo apiInfo = getApiInfoById(id);
        if (apiInfo != null) {
            remoteCallInfo = new RemoteCallInfo();
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
    public void insertApiInfo(ApiInfo apiInfo) {
        apiInfoRepository.save(apiInfo);
    }

    public Page<ApiInfo> pageQuery(final QueryApiInfoRequest request, Pageable pageable) {
        return apiInfoRepository.findAll(new Specification<ApiInfo>() {
            @Override
            public Predicate toPredicate(Root<ApiInfo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(request.getProject())) {
                    list.add(cb.equal(root.get("project").as(String.class), request.getProject()));
                }

                if (!StringUtils.isEmpty(request.getApiName())) {
                    list.add(cb.like(root.get("apiName").as(String.class), "%" + request.getApiName() + "%"));
                }

                if (!StringUtils.isEmpty(request.getMethod())) {
                    list.add(cb.equal(root.get("method").as(String.class), request.getMethod()));
                }

                if (!StringUtils.isEmpty(request.getUpdatedBy())) {
                    list.add(cb.equal(root.get("updatedBy").as(String.class), request.getUpdatedBy()));
                }


                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        }, pageable);
    }

    public List<ApiInfo> findApiInfoByName(Long tenantId, String apiName) {
        return apiInfoRepository.findByTenantIdAndApiName(tenantId, apiName);
    }

    public List<ApiCondition> getApiConditionList(Long apiInfoId) {
        return baseDao.queryForList("ApiCondition.getByApiInfoId", apiInfoId);
    }

    public Long countByCondition(Map<String, Object> cond) {
        return baseDao.queryForObject("ApiInfo.countByCondition", cond);
    }

    public ApiInfo getById(long id) {
        return apiInfoRepository.findOne(id);
    }

    @Transactional
    public void update(ApiInfo apiInfo) {
        apiConditionRepository.deleteApiConditionByApiInfo(apiInfo);
        apiInfoRepository.save(apiInfo);
    }

    public List<ApiInfo> findUriVariableApi() {
        return baseDao.queryForList("ApiInfo.findUriVariableApi", null);
    }

    @Transactional
    public void deleteById(Long id) {
        apiInfoRepository.delete(id);
    }
}
