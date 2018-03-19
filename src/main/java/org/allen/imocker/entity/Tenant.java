package org.allen.imocker.entity;

import lombok.Data;

@Data
public class Tenant extends BaseEntity {

    private Long id;

    private String abbrName;

    private String displayName;

    private String accessKey;

    private String email;

    private String phone;

    private Boolean status;


}
