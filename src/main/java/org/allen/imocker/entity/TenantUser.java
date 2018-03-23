package org.allen.imocker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class TenantUser extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Tenant tenant;

    @Column
    private String loginName;

    @Column
    private String loginPwd;

    @Column
    private Integer maxRetryTimes = 5;

    @Column
    private Integer retryTimes;

    @Column
    private Boolean isLocked;


}
