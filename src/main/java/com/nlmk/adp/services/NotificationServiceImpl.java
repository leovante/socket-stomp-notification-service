package com.nlmk.adp.services;

import com.nlmk.adp.config.ObjectMapperHelper;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.services.mapper.NotificationFromDtoMapper;
import lombok.RequiredArgsConstructor;
import nlmk.l3.mesadp.DbUserNotificationVer0;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDaoService notificationDaoService;
    private final InvalidNotificationsDaoService invalidNotificationsDaoService;
    private final SocketMessageSenderService socketMessageSenderService;
    private final RemoteNotificationService remoteNotificationService;
    private final NotificationFromDtoMapper notificationFromDtoMapper;

    @Override
    public void send(NotificationDto body) {
        notificationDaoService.save(body);
        socketMessageSenderService.send(body.href());
    }

    @Override
    public void sendToKafka(NotificationDto body) {
        var snapshot = notificationFromDtoMapper.mapDataFromDto(body);
        remoteNotificationService.send(snapshot);
    }

    @Override
    public void invalidate(DbUserNotificationVer0 body, String reason) {
        var snapshot = ObjectMapperHelper.getObjectMapper().valueToTree(body);
        invalidNotificationsDaoService.save(snapshot, reason);
    }

}
