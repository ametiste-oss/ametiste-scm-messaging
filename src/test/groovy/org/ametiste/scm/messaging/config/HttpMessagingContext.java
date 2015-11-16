package org.ametiste.scm.messaging.config;

import org.ametiste.scm.messaging.mock.Subscriber;
import org.ametiste.scm.messaging.sender.EventSender;
import org.ametiste.scm.messaging.sender.HttpEventSender;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Minimal configuration for testing messaging via HTTP protocol.
 * Context includes EventReceiverController with scm-messaging-receiver-starter module.
 */
@SpringBootApplication
@EnableWebMvc
public class HttpMessagingContext {

    @Bean
    public EventSender sender() {
        return new HttpEventSender(HttpEventSender.createHttpClient(1000, 1000));
    }

    @Bean
    public Subscriber subscriber() {
        return new Subscriber();
    }
}
