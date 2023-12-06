package com.nlmk.adp.services.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.nlmk.adp.db.entity.NotificationEmailPk;
import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.db.entity.NotificationRolesEntity;
import com.nlmk.adp.db.entity.NotificationRolesPk;
import com.nlmk.adp.db.entity.NotificationUserSuccessEntity;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.kafka.dto.RoleDto;
import com.nlmk.adp.kafka.dto.UserEmailDto;
import com.nlmk.adp.util.SpringMapperConfig;

/**
 * Маппер между дто и jpa.
 */
@Mapper(config = SpringMapperConfig.class)
public interface NotificationToDaoMapper {

    /**
     * map to dto.
     *
     * @param entity
     *         entity.
     *
     * @return dto.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "header", source = "header")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "href", source = "href")
    @Mapping(target = "roles", source = "notificationRolesEntities")
    @Mapping(target = "emails", source = "notificationUserSuccessEntities")
    @Mapping(target = "happenedAt", source = "kafkaDt")
    @Mapping(target = "expiredAt", source = "expiredAt")
    NotificationDto mapToDto(NotificationEntity entity);

    /**
     * role.
     *
     * @param role
     *
     * @return role.
     */
    @Mapping(target = "role", source = "primaryKey.role")
    @Mapping(target = "roleType", source = "roleType")
    RoleDto mapRole(NotificationRolesEntity role);

    /**
     * email.
     *
     * @param email
     *
     * @return email.
     */
    @Mapping(target = "email", source = "primaryKey.email")
    @Mapping(target = "readAt", source = "readAt")
    UserEmailDto mapEmail(NotificationUserSuccessEntity email);

    /**
     * mapDtoToEntity.
     *
     * @param dto
     *         dto
     *
     * @return NotificationEntity
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "kafkaDt", source = "happenedAt")
    @Mapping(target = "notificationRolesEntities", expression = "java(mapRolesToEntity(dto))")
    @Mapping(target = "notificationUserSuccessEntities", expression = "java(mapEmailsToEntity(dto))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    NotificationEntity mapDtoToEntity(NotificationDto dto);

    /**
     * mapRolesToEntity.
     *
     * @param notificationDto
     *         notificationDto
     *
     * @return Set
     */
    default List<NotificationRolesEntity> mapRolesToEntity(NotificationDto notificationDto) {
        return notificationDto.roles().stream()
                              .map(dto -> {
                                  var entity = new NotificationRolesEntity();
                                  var role = new NotificationRolesPk();
                                  role.setRole(dto.role());
                                  role.setNotificationId(notificationDto.id());

                                  entity.setPrimaryKey(role);
                                  entity.setRoleType(dto.roleType());
                                  return entity;
                              })
                              .toList();
    }

    /**
     * mapEmailsToEntity.
     *
     * @param notificationDto
     *         notificationDto
     *
     * @return Set
     */
    default List<NotificationUserSuccessEntity> mapEmailsToEntity(NotificationDto notificationDto) {
        return notificationDto.emails().stream()
                              .map(dto -> {
                                  var entity = new NotificationUserSuccessEntity();
                                  var emailPk = new NotificationEmailPk();
                                  emailPk.setEmail(dto.email());
                                  emailPk.setNotificationId(notificationDto.id());

                                  entity.setPrimaryKey(emailPk);
                                  return entity;
                              })
                              .toList();
    }

}
