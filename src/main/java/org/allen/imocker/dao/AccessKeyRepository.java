package org.allen.imocker.dao;

import org.allen.imocker.entity.AccessKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessKeyRepository extends JpaRepository<AccessKey, Long> {

    AccessKey findOneByAccessKey(String accessKey);

    List<AccessKey> findByTenantIdAndRefId(Long tenantId, Long refId);
}
