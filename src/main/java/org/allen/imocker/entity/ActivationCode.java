package org.allen.imocker.entity;

import lombok.Data;
import org.allen.imocker.entity.type.ActivateStatus;
import org.allen.imocker.entity.type.TenantType;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class ActivationCode extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TenantType type;

    @Column
    private Long refId;

    @Column
    private String activateCode;

    @Column
    private Date expiredDate;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ActivateStatus status;

}
