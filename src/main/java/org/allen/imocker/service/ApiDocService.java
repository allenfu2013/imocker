package org.allen.imocker.service;

import org.allen.imocker.controller.request.QueryApiDocRequest;
import org.allen.imocker.dao.*;
import org.allen.imocker.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiDocService {

    @Autowired
    private ApiDocRepository apiDocRepository;

    @Autowired
    private ApiHeaderRepository apiHeaderRepository;

    @Autowired
    private ApiParameterRepository apiParameterRepository;

    @Autowired
    private ApiResponseBodyRepository apiResponseBodyRepository;

    @Autowired
    private ApiErrorRepository apiErrorRepository;

    @Transactional
    public Long createApiDoc(ApiDoc apiDoc) {
        return apiDocRepository.save(apiDoc).getId();
    }

    public ApiDoc findById(Long id) {
        return apiDocRepository.findOneById(id);
    }

    public Page<ApiDoc> pageQuery(final QueryApiDocRequest request, Pageable pageable) {
        return apiDocRepository.findAll((root, cq, cb) -> {
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

            if (!StringUtils.isEmpty(request.getUpdatedBy())) {
                list.add(cb.equal(root.get("updatedBy").as(String.class), request.getUpdatedBy()));
            }

            return cb.and(list.toArray(new Predicate[list.size()]));
        }, pageable);
    }

    @Transactional
    public void update(ApiDoc apiDoc) {
        apiHeaderRepository.deleteApiHeadersByApiDoc(apiDoc);
        apiParameterRepository.deleteApiParametersByApiDoc(apiDoc);
        apiResponseBodyRepository.deleteApiResponseBodiesByApiDoc(apiDoc);
        apiErrorRepository.deleteApiErrorsByApiDoc(apiDoc);
        apiDocRepository.save(apiDoc);
    }

    @Transactional
    public void delete(Long id) {
        apiDocRepository.delete(id);
    }

    public List<ApiDoc> findByProject(String project) {
        return apiDocRepository.findAllByProject(project);
    }
}
