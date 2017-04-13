package com.pksprojects.minis3.scheduled;

import com.pksprojects.minis3.services.EmailService;
import com.pksprojects.minis3.services.FileMetaDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.pksprojects.minis3.models.email.EmailMessages.NEW_ITEMS_IN_PAST_HOUR;
import static com.pksprojects.minis3.models.email.EmailSubjects.HOURLY_UPDATES;
import static com.pksprojects.minis3.models.email.SystemEmailAddrs.HOURLY_NOTIFICATION;

/**
 * Component responsible for running scheduled tasks
 * Created by PKS on 4/10/17.
 */
@Component
public class ScheduledTasks {

    public static final Logger logger = LogManager.getLogger(ScheduledTasks.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private FileMetaDataService fileMetaDataService;

    /**
     * Responsible for executing tasks involved in sending hourly email notifications.
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void sendHourlyEmail() {
        logger.info("Job ** Hourly Email Notification ** fired @ {}", LocalDateTime.now());
        Map<String, List<String>> map = fileMetaDataService.getAllNewIdsAndUserEmailForNotification();
        map.forEach((k,v) -> {
            emailService.sendEmail(
                    HOURLY_NOTIFICATION.getEmail(),
                    k,
                    HOURLY_UPDATES.getSubject(),
                    NEW_ITEMS_IN_PAST_HOUR.getMsg(k, v.toString())
            );
        });
    }
}
