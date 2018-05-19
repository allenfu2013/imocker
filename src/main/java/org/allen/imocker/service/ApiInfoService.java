package org.allen.imocker.service;

import lombok.extern.slf4j.Slf4j;
import org.allen.imocker.controller.request.QueryApiInfoRequest;
import org.allen.imocker.dao.ApiConditionRepository;
import org.allen.imocker.dao.ApiInfoRepository;
import org.allen.imocker.entity.ApiCondition;
import org.allen.imocker.entity.ApiInfo;
import org.allen.imocker.entity.Tenant;
import org.allen.imocker.entity.type.TenantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ApiInfoService {

    @Autowired
    private ApiInfoRepository apiInfoRepository;

    @Autowired
    private ApiConditionRepository apiConditionRepository;

    @Transactional
    public void insertApiInfo(ApiInfo apiInfo) {
        apiInfoRepository.save(apiInfo);
    }

    public Page<ApiInfo> pageQuery(final QueryApiInfoRequest request, Pageable pageable) {
        return apiInfoRepository.findAll((root, cq, cb) -> {
                List<Predicate> list = new ArrayList<>();

                if (request.getTenantId() != null) {
                    list.add(cb.equal(root.get("tenant").get("id").as(Long.class), request.getTenantId()));
                }

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

                if (!StringUtils.isEmpty(request.getCreatedBy())) {
                    list.add(cb.equal(root.get("createdBy").as(String.class), request.getCreatedBy()));
                }

                if (request.getUserId() != null) {
                    list.add(cb.equal(root.get("userId").as(Long.class), request.getUserId()));
                }

                return cb.and(list.toArray(new Predicate[list.size()]));
            }, pageable);
    }

    public List<ApiInfo> findApiInfoByName(String apiName) {
        return apiInfoRepository.findAllByApiName(apiName);
    }


    public ApiInfo findByShortApiNameAndMethod(TenantType tenantType, Long refId, String shortApiName, String method) {
        if (TenantType.ORG == tenantType) {
            Tenant tenant = new Tenant();
            tenant.setId(refId);
            return apiInfoRepository.findOneByTenantAndShortApiNameAndMethod(tenant, shortApiName, method);
        } {
            return apiInfoRepository.findOneByShortApiNameAndMethodAndUserId(shortApiName, method, refId);
        }
    }

    public boolean existByShortApiNameAndMethod(Long tenantId, String shortApiName, String method) {
        return apiInfoRepository.existByShortApiNameAndMethod(tenantId, shortApiName, method);
    }

    public List<ApiCondition> getApiConditionList(ApiInfo apiInfo) {
        return apiConditionRepository.findAllByApiInfo(apiInfo);
    }

    public ApiInfo getById(long id) {
        return apiInfoRepository.findById(id);
    }

    @Transactional
    public void update(ApiInfo apiInfo) {
        apiConditionRepository.deleteApiConditionsByApiInfo(apiInfo.getId());
        apiInfoRepository.save(apiInfo);
    }

    public List<ApiInfo> findUriVariableApi() {
        return apiInfoRepository.findAllByUriVariableIsNotNull();
    }

    @Transactional
    public void deleteById(Long id) {
        apiInfoRepository.delete(id);
    }
}
