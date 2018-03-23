package org.allen.imocker.controller.vo;

import lombok.Data;

@Data
public class ApiConditionVo {

    private String condKey;

    private String condType;

    private String condExpression;

    private String condValue;

    private String mockRetValue;
}
