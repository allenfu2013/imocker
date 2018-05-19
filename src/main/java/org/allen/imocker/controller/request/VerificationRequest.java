package org.allen.imocker.controller.request;

import lombok.Data;
import org.allen.imocker.entity.type.TenantType;

@Data
public class VerificationRequest {

    private TenantType tenantType;

    private Long refId;

}
