package org.allen.imocker.service;

import org.allen.imocker.controller.request.QueryTenantUserRequest;
import org.allen.imocker.controller.request.QueryTenantRequest;
import org.allen.imocker.dao.TenantUserRepository;
import org.allen.imocker.entity.TenantUser;
import org.allen.imocker.entity.type.ApplyStatus;
import org.allen.imocker.entity.type.TenantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenantUserService {

    @Autowired
    private TenantUserRepository tenantUserRepository;

    public Page<TenantUser> pageQuery(final QueryTenantRequest request, PageRequest pageRequest) {
        return tenantUserRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();

            if (request.getApplyStatus() != null) {
                list.add(cb.equal(root.get("status").as(ApplyStatus.class), request.getApplyStatus()));
            }

            list.add(cb.equal(root.get("tenant").get("type").as(TenantType.class), TenantType.DEFAULT));

            return cb.and(list.toArray(new Predicate[list.size()]));
        }, pageRequest);
    }

    public Page<TenantUser> pageQuery(final QueryTenantUserRequest request, PageRequest pageRequest) {
        return tenantUserRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();

            list.add(cb.equal(root.get("tenant").get("id").as(Long.class), request.getTenantId()));

            if (!StringUtils.isEmpty(request.getLoginName())) {
                list.add(cb.equal(root.get("loginName").as(String.class), request.getLoginName()));
            }

            if (!StringUtils.isEmpty(request.getNickName())) {
                list.add(cb.equal(root.get("nickName").as(String.class), request.getNickName()));
            }

            if (!StringUtils.isEmpty(request.getEmail())) {
                list.add(cb.equal(root.get("email").as(String.class), request.getEmail()));
            }

            return cb.and(list.toArray(new Predicate[list.size()]));
        }, pageRequest);
    }

}
