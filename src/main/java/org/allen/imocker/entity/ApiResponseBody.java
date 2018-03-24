package org.allen.imocker.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString(exclude = "apiDoc")
public class ApiResponseBody {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
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
