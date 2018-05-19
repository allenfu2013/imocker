package org.allen.imocker.controller.response;

import lombok.Data;
import org.allen.imocker.dto.RoleType;

@Data
public class LoginResponse {

    private Long userId;

    private String nickName;

    private RoleType roleType;

}
