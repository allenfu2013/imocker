package org.allen.imocker.dao;

import org.allen.imocker.entity.AccessKey;
import org.allen.imocker.entity.type.TenantType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessKeyRepository extends JpaRepository<AccessKey, Long> {

    AccessKey findOneByAccessKey(String accessKey);

    List<AccessKey> findByTypeAndRefId(TenantType type, Long refId);
}
