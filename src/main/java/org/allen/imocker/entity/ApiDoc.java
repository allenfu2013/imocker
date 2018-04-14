package org.allen.imocker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode
@Entity
@NamedEntityGraph(name = "apiDoc.header.parameter.responseBody.error", attributeNodes = {
        @NamedAttributeNode("apiHeaders"),
        @NamedAttributeNode("apiParameters"),
        @NamedAttributeNode("apiResponseBodies"),
        @NamedAttributeNode("apiErrors")
}, includeAllAttributes = true)
public class ApiDoc extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column
    private String project;

    @Column
    private String apiName;

    @Column
    private String apiMethod;

    @Column
    private String apiDesc;

    @OneToMany(mappedBy = "apiDoc", cascade = CascadeType.ALL)
    private Set<ApiHeader> apiHeaders;

    @OneToMany(mappedBy = "apiDoc", cascade = CascadeType.ALL)
    private Set<ApiParameter> apiParameters;

    @Column
    private String apiParamExample;

    @OneToMany(mappedBy = "apiDoc", cascade = CascadeType.ALL)
    private Set<ApiResponseBody> apiResponseBodies;

    @Column
    private String apiResponseStatus;

    @Column
    private String apiResponseExample;

    @OneToMany(mappedBy = "apiDoc", cascade = CascadeType.ALL)
    private Set<ApiError> apiErrors;

    @Column
    private String testUrl;


}
