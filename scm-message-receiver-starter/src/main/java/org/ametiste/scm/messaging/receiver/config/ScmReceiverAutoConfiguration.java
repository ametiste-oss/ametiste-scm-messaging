package org.ametiste.scm.messaging.receiver.config;

import org.ametiste.scm.messaging.receiver.EventReceivingController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Configuration add controller to application context for event receiving.
 */
@Configuration
@ComponentScan( value = "org.ametiste.scm.messaging.receiver",
                useDefaultFilters = false,
                includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = EventReceivingController.class))
public class ScmReceiverAutoConfiguration {
}
