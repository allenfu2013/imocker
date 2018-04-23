package org.allen.imocker.controller.request;

import lombok.Data;
import org.allen.imocker.controller.vo.ApiConditionVo;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Data
public class CreateApiInfoRequest {

    @NotBlank
    private String apiName;

    @NotBlank
    private String method;

    private String contentType;

    private String retResult;

    private List<ApiConditionVo> apiConditionList;
}
