package org.allen.imocker.service;

import org.allen.imocker.dao.TenantUserRepository;
import org.allen.imocker.entity.TenantUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantUserService {

    @Autowired
    private TenantUserRepository tenantUserRepository;

    public void createUser(TenantUser tenantUser) {
        tenantUser.setLoginPwd(DigestUtils.md5Hex(tenantUser.getLoginPwd()));
        tenantUserRepository.save(tenantUser);
    }

}
