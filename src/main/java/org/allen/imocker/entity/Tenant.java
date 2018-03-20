package org.allen.imocker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Tenant extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String abbrName;

    @Column
    private String displayName;

    @Column
    private String accessKey;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private Boolean status;


}
