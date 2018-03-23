package org.allen.imocker.controller.request;

import lombok.Data;
import org.allen.imocker.controller.vo.ApiConditionVo;

import java.util.List;

@Data
public class CreateApiInfoRequest {

    private String apiName;

    private String method;

    private String contentType;

    private String retResult;

    private List<ApiConditionVo> apiConditionList;
}
