package org.ics.utils;

import org.ics.IntelligentCloudServiceApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LoggerUtil
{

    @Bean
    public Logger getLogger()
    {
        return LoggerFactory.getLogger(IntelligentCloudServiceApplication.class);
    }
}
