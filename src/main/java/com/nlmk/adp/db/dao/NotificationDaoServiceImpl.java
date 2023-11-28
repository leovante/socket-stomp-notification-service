package com.nlmk.adp.db.dao;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.db.entity.NotificationEntity;
import com.nlmk.adp.db.repository.NotificationEmailRepository;
import com.nlmk.adp.db.repository.NotificationRepository;
import com.nlmk.adp.db.repository.NotificationRoleRepository;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.mapper.NotificationToDaoMapper;

/**
 * NotificationDaoServiceImpl.
 */
@Service
@AllArgsConstructor
public class NotificationDaoServiceImpl implements NotificationDaoService {

    private final NotificationToDaoMapper notificationDaoMapper;

    private final NotificationRepository notificationRepository;

    private final NotificationRoleRepository notificationRoleRepository;

    private final NotificationEmailRepository notificationEmailRepository;

    @Override
    @Transactional
    public NotificationEntity save(NotificationDto model) {
        var entity = notificationDaoMapper.mapDtoToEntity(model);
        entity.setCreatedAt(Instant.now());
        entity.setExpiredAt(Instant.now().plus(1, ChronoUnit.DAYS));

        var snapshot = notificationRepository.save(entity);

        var roles = snapshot.getNotificationRolesEntities();
        var emails = snapshot.getNotificationUserSuccessEntities();
        roles.forEach(i -> i.setNotification(snapshot));
        emails.forEach(i -> i.setNotification(snapshot));

        notificationRoleRepository.saveAll(roles);
        notificationEmailRepository.saveAll(emails);

        return snapshot;
    }

}
