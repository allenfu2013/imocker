package org.allen.imocker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ApiDoc extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
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
    private List<ApiHeader> apiHeaders;

    @OneToMany(mappedBy = "apiDoc", cascade = CascadeType.ALL)
    private List<ApiParameter> apiParameters;

    @Column
    private String apiParamExample;

    @OneToMany(mappedBy = "apiDoc", cascade = CascadeType.ALL)
    private List<ApiResponseBody> apiResponseBodies;

    @Column
    private String apiResponseStatus;

    @Column
    private String apiResponseExample;

    @OneToMany(mappedBy = "apiDoc", cascade = CascadeType.ALL)
    private List<ApiError> apiErrors;

    @Column
    private String testUrl;


}
