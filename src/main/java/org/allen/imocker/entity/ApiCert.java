package org.allen.imocker.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class ApiCert {

    private Integer id;

    private String certName;

    private String clientStore;

    private String trustStore;

    private String clientKeyPwd;

    private String trustKeyPwd;

    private String createdAt;

    private String createdBy;

}
