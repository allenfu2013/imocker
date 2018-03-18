package org.allen.imocker.service;

import org.allen.imocker.dao.BaseDao;
import org.allen.imocker.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiDocService {

    @Autowired
    private BaseDao baseDao;

    @Transactional
    public Long createApiDoc(ApiDoc apiDoc) {
        baseDao.insert("ApiDoc.insert", apiDoc);
        apiDoc.getApiHeaders().forEach(apiHeader -> {
            apiHeader.setApiDocId(apiDoc.getId());
            baseDao.insert("ApiHeader.insert", apiHeader);
        });

        apiDoc.getApiParameters().forEach(apiParameter -> {
            apiParameter.setApiDocId(apiDoc.getId());
            baseDao.insert("ApiParameter.insert", apiParameter);
        });

        apiDoc.getApiResponseBodies().forEach(apiResponseBody -> {
            apiResponseBody.setApiDocId(apiDoc.getId());
            baseDao.insert("ApiResponseBody.insert", apiResponseBody);
        });

        apiDoc.getApiErrors().forEach(apiError -> {
            apiError.setApiDocId(apiDoc.getId());
            baseDao.insert("ApiError.insert", apiError);
        });

        return apiDoc.getId();
    }

    public ApiDoc findById(Long id) {
        ApiDoc apiDoc = baseDao.queryForObject("ApiDoc.getById", id);
        if (apiDoc != null) {
            List<ApiHeader> headers = baseDao.queryForList("ApiHeader.getByApiDocId", id);
            apiDoc.setApiHeaders(headers);

            List<ApiParameter> parameters = baseDao.queryForList("ApiParameter.getByApiDocId", id);
            apiDoc.setApiParameters(parameters);

            List<ApiResponseBody> responses = baseDao.queryForList("ApiResponseBody.getByApiDocId", id);
            apiDoc.setApiResponseBodies(responses);

            List<ApiError> errors = baseDao.queryForList("ApiError.getByApiDocId", id);
            apiDoc.setApiErrors(errors);
        }
        return apiDoc;
    }

    public List<ApiDoc> findByCondition(Map<String, Object> cond) {
        return baseDao.queryForList("ApiDoc.findByCondition", cond);
    }

    public Long countByCondition(Map<String, Object> cond) {
        return baseDao.queryForObject("ApiDoc.countByCondition", cond);
    }

    @Transactional
    public void update(ApiDoc apiDoc) {
        baseDao.update("ApiDoc.update", apiDoc);

        baseDao.delete("ApiHeader.deleteByApiDocId", apiDoc.getId());
        apiDoc.getApiHeaders().forEach(apiHeader -> {
            apiHeader.setApiDocId(apiDoc.getId());
            baseDao.insert("ApiHeader.insert", apiHeader);
        });

        baseDao.delete("ApiParameter.deleteByApiDocId", apiDoc.getId());
        apiDoc.getApiParameters().forEach(apiParameter -> {
            apiParameter.setApiDocId(apiDoc.getId());
            baseDao.insert("ApiParameter.insert", apiParameter);
        });


        baseDao.delete("ApiResponseBody.deleteByApiDocId", apiDoc.getId());
        apiDoc.getApiResponseBodies().forEach(apiResponseBody -> {
            apiResponseBody.setApiDocId(apiDoc.getId());
            baseDao.insert("ApiResponseBody.insert", apiResponseBody);
        });

        baseDao.delete("ApiError.deleteByApiDocId", apiDoc.getId());
        apiDoc.getApiErrors().forEach(apiError -> {
            apiError.setApiDocId(apiDoc.getId());
            baseDao.insert("ApiError.insert", apiError);
        });
    }

    @Transactional
    public void delete(Long id) {
        baseDao.delete("ApiDoc.deleteById", id);
        baseDao.delete("ApiHeader.deleteByApiDocId", id);
        baseDao.delete("ApiParameter.deleteByApiDocId", id);
        baseDao.delete("ApiResponseBody.deleteByApiDocId", id);
        baseDao.delete("ApiError.deleteByApiDocId", id);
    }

    public List<ApiDoc> findByProject(String project) {
        Map<String, Object> cond = new HashMap();
        cond.put("project", project);
        return baseDao.queryForList("ApiDoc.findByProject", cond);
    }
}
