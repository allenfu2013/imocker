package org.allen.imocker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionObj {

    private Long tenantId;

    private Long userId;

    private String nickName;

    private UserType userType;

}
