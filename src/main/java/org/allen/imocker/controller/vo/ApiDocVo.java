package org.allen.imocker.controller.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ApiDocVo {

    private Long id;

    private String project;

    private String apiName;

    private String apiMethod;

    private String apiDesc;

    private String createdBy;

    private String updatedBy;

    private Date updatedAt;
}
