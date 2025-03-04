package com.nlmk.adp.db.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * NotificationEntity.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
public class NotificationEntity {

    public static final int VARCHAR_FIELD_MAX_SIZE = 500;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "expired_at")
    private Instant expiredAt;

    @Column(name = "body")
    @Length(max = VARCHAR_FIELD_MAX_SIZE)
    private String body;

    @Column(name = "header")
    @Length(max = VARCHAR_FIELD_MAX_SIZE)
    private String header;

    @Column(name = "href")
    @Length(max = VARCHAR_FIELD_MAX_SIZE)
    private String href;

    @Column(name = "ts")
    private Instant kafkaTs;

    @Column(name = "ordinal_number", insertable = false, updatable = false, columnDefinition = "serial")
    private Long ordinalNumber;

    @OneToMany(mappedBy = "notification", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NotificationRolesEntity> notificationRolesEntities;

    @OneToMany(mappedBy = "notification", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<NotificationUserSuccessEntity> notificationUserSuccessEntities;

    @Column(name = "created_at")
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant updatedAt;

}
