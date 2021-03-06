package org.allen.imocker.entity;

import lombok.Data;
import org.allen.imocker.entity.type.AccessKeyType;
import org.allen.imocker.entity.type.TenantType;

import javax.persistence.*;

@Entity
@Data
public class AccessKey extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TenantType type;

    /**
     * Reference to Tenant or TenantUser according to AccessKeyType
     */
    @Column
    private Long refId;

    @Column
    private String accessKey;

    @Column
    private boolean locked;


}
