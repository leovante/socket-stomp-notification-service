package com.nlmk.adp.db.entity;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.nlmk.adp.db.entity.convert.RawMessageConverter;

/**
 * InvalidNotificationsEntity.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invalid_notifications")
@EntityListeners(AuditingEntityListener.class)
public class InvalidNotificationsEntity {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "raw_message")
    @Convert(converter = RawMessageConverter.class)
    private JsonNode rawMessage;

    @Column(name = "error_message")
    @Length(max = 8092)
    private String errorMessage;

    @Column(name = "created_at")
    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant updatedAt;

}
