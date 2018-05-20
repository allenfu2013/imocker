package org.allen.imocker.controller.request;

import lombok.Data;

@Data
public class QueryTenantUserRequest {

    private Long tenantId;

    private String loginName;

    private String nickName;

    private String email;

}
