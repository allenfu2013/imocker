package org.allen.imocker.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString(exclude = "apiDoc")
public class ApiHeader {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private ApiDoc apiDoc;

    @Column
    private String headerKey;

    @Column
    private String headerValue;

}
