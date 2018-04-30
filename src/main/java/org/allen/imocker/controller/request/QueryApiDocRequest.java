package org.allen.imocker.controller.request;

import lombok.Data;

@Data
public class QueryApiDocRequest {

    private Long tenantId;

    private String project;

    private String apiName;

    private String updatedBy;
}
