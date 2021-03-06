package org.allen.imocker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.allen.imocker.dto.RoleType;
import org.allen.imocker.entity.type.ApplyStatus;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "tenant")
@Entity
@NamedEntityGraph(name = "TenantUser.tenant", attributeNodes = {
        @NamedAttributeNode("tenant")
})
public class TenantUser extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column
    private String loginName;

    @Column
    private String loginPwd;

    @Column
    private String nickName;

    @Column
    private String email;

    @Column
    private Integer maxRetryTimes = 5;

    @Column
    private Integer retryTimes = 0;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ApplyStatus status;

    @Column
    @Enumerated(value = EnumType.STRING)
    private RoleType roleType;
}
