package org.allen.imocker.service;

import org.allen.imocker.dao.TenantRepository;
import org.allen.imocker.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Transactional
    public void save(Tenant tenant) {
        System.out.println("@@@@@@@@");
        tenantRepository.save(tenant);
        System.out.println("########");
    }

    @Transactional
    public void find(Long id) {
        tenantRepository.findOne(id);
    }
}
