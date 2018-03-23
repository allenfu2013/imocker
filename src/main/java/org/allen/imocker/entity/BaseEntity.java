package org.allen.imocker.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@MappedSuperclass
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EntityListeners({ AuditingEntityListener.class })
public abstract class BaseEntity implements Serializable {

    @Column
    private String createdBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column
    private Date createdAt;

    @Column
    private String updatedBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @Column
    private Date updatedAt;
}
