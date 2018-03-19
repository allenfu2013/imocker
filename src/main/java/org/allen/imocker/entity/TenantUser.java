package org.allen.imocker.entity;

import lombok.Data;

@Data
public class TenantUser extends BaseEntity {

    private Long id;

    private Long tenantId;

    private String loginName;

    private String loginPwd;

    private Integer maxRetryTimes = 5;

    private Integer retryTimes;

    private Boolean isLocked;


}
