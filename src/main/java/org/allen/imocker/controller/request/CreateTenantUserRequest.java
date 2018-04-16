package org.allen.imocker.controller.request;

import lombok.Data;

@Data
public class CreateTenantUserRequest {

    private String loginName;

    private String loginPwd;
}
