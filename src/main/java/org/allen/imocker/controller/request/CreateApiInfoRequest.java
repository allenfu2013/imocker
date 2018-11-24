package org.allen.imocker.controller.request;

import lombok.Data;
import org.allen.imocker.controller.vo.ApiConditionVo;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class CreateApiInfoRequest {

    @NotBlank
    private String apiName;

    @NotBlank
    private String method;

    @NotBlank
    private String contentType;

    private Integer httpStatus = HttpStatus.OK.value();

    private String retResult;

    private List<ApiConditionVo> apiConditionList;
}
