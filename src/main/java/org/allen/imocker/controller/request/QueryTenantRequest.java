package org.allen.imocker.controller.request;

import lombok.Data;
import org.allen.imocker.entity.type.ApplyStatus;
import org.allen.imocker.entity.type.TenantType;

@Data
public class QueryTenantRequest {

    private TenantType tenantType;

    private ApplyStatus applyStatus;
}
