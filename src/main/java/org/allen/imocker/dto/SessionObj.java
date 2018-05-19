package org.allen.imocker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.allen.imocker.entity.type.TenantType;

@Data
@AllArgsConstructor
public class SessionObj {

    private Long tenantId;

    private TenantType tenantType;

    private Long userId;

    private String nickName;

    private RoleType roleType;

}
