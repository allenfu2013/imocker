package org.allen.imocker.controller.vo;

import lombok.Data;
import org.allen.imocker.controller.request.CreateApiInfoRequest;

import java.util.Date;

@Data
public class ApiInfoVo extends CreateApiInfoRequest {

    private Long id;

    private String mockUrl;

    private String createdBy;

    private String updatedBy;

    private Date updatedAt;
}
