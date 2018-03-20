package org.allen.imocker.entity;

import javax.persistence.Column;
import java.util.Date;

public abstract class BaseEntity {

    @Column
    private String createdBy;

    @Column
    private Date createdAt;

    @Column
    private String updatedBy;

    @Column
    private Date updatedAt;
}
