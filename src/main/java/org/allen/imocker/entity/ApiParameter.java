package org.allen.imocker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString(exclude = "apiDoc")
@EqualsAndHashCode(exclude = "apiDoc")
public class ApiParameter {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "api_doc_id")
    private ApiDoc apiDoc;

    @Column
    private String paramKey;

    @Column
    private String paramType;

    @Column
    private String paramRequired;

    @Column
    private String paramParent;

    @Column
    private String paramDesc;

}
