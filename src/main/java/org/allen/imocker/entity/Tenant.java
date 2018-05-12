package org.allen.imocker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.allen.imocker.entity.type.ApplyStatus;
import org.allen.imocker.entity.type.TenantType;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Tenant extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TenantType type;

    @Column
    private String abbrName;

    @Column
    private String displayName;

    @Column
    private String email;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ApplyStatus status = ApplyStatus.APPLYING;

}
