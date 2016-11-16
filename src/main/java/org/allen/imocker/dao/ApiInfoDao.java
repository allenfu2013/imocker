package org.allen.imocker.dao;

import java.util.List;
import java.util.Map;

import org.allen.imocker.entity.ApiInfo;
import org.springframework.stereotype.Repository;

@Repository
public class ApiInfoDao extends BaseDao {

    public Integer insertApiInfo(ApiInfo apiInfo) {
        return (Integer) insert("ApiInfo.insert", apiInfo);
    }

    public List<ApiInfo> findByCondition(Map<String, Object> cond) {
        return queryForList("ApiInfo.findByCondition", cond);
    }

    public ApiInfo getById(long id) {
        return queryForObject("ApiInfo.getById", id);
    }

    public boolean update(ApiInfo apiInfo) {
        return update("ApiInfo.update", apiInfo) > 0 ? true : false;
    }
}
