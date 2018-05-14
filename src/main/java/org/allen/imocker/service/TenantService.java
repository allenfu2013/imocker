package org.allen.imocker.service;

import org.allen.imocker.controller.request.QueryTenantRequest;
import org.allen.imocker.dao.TenantRepository;
import org.allen.imocker.entity.Tenant;
import org.allen.imocker.entity.type.ApplyStatus;
import org.allen.imocker.entity.type.TenantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Transactional
    public void save(Tenant tenant) {
        tenantRepository.save(tenant);
    }

    public Page<Tenant> pageQuery(final QueryTenantRequest request, Pageable pageable) {
        return tenantRepository.findAll((root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();

            if (request.getTenantType() != null) {
                list.add(cb.equal(root.get("type").as(TenantType.class), request.getTenantType()));
            }

            if (request.getApplyStatus() != null) {
                list.add(cb.equal(root.get("status").as(ApplyStatus.class), request.getApplyStatus()));
            }
            return cb.and(list.toArray(new Predicate[list.size()]));
        }, pageable);
    }

    @Transactional
    public void find(Long id) {
        tenantRepository.findOne(id);
    }
}
