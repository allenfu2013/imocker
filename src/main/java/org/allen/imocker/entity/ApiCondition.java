package org.allen.imocker.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@ToString(exclude = "apiInfo")
public class ApiCondition {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private ApiInfo apiInfo;

    @Column
    private String condKey;

    @Column
    private String condType;

    @Column
    private String condExpression;

    @Column
    private String condValue;

    @Column
    private String mockRetValue;

}
