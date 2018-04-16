package org.allen.imocker.controller.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String loginName;

    private String loginPwd;
}
