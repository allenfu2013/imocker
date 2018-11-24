package org.allen.imocker.controller.request;

import lombok.Data;

import java.util.Date;

@Data
public class QueryApiInfoRequest {

    private Long tenantId;

    private String project;

    private String apiName;

    private String method;

    private String updatedBy;

    private String createdBy;

    private Long userId;

    private Date start;

    private Date end;

}
