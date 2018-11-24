package org.allen.imocker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NamedEntityGraph(name = "apiInfo.condition", attributeNodes = {
        @NamedAttributeNode("apiConditionList")
})
public class ApiInfo extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column
    private String project;

    @Column
    private String apiName;

    @Column
    private String shortApiName;

    @Column
    private String method;

    @Column
    private String contentType;

    @Column
    private Boolean hasCondition;

    @Column
    private Integer httpStatus = HttpStatus.OK.value();

    @Column
    private String retResult;

    @Column
    private String uriVariable;

    @Column
    private String description;

    @Column
    private String qaUrl;

    @Column
    private Boolean status = true;

    @OneToMany(mappedBy = "apiInfo",  cascade = CascadeType.ALL)
    private List<ApiCondition> apiConditionList;

}
