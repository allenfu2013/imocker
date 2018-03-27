package org.allen.imocker.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class ApiCert extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String certName;

    @Column(columnDefinition = "longtext")
    private String clientStore;

    @Column(columnDefinition = "longtext")
    private String trustStore;

    @Column
    private String clientKeyPwd;

    @Column
    private String trustKeyPwd;

}
