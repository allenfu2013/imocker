package org.allen.imocker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString(exclude = "apiDoc")
@EqualsAndHashCode(exclude = "apiDoc")
public class ApiResponseBody {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "api_doc_id")
    private ApiDoc apiDoc;

    @Column
    private String responseKey;

    @Column
    private String responseType;

    @Column
    private String responseRequired;

    @Column
    private String responseParent;

    @Column
    private String responseDesc;

}
