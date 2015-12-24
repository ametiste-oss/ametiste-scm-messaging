package org.ametiste.scm.messaging.sender.client.config;

import org.ametiste.scm.messaging.sender.EventSender;
import org.ametiste.scm.messaging.sender.client.EventSenderClient;
import org.ametiste.scm.messaging.sender.client.event.EventFactory;
import org.ametiste.scm.messaging.sender.client.event.StartupEventFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Event clients configuration. Depends on {@code LifecycleEventsConfiguration}.
 * <p>
 * Client instances can be disabled with property {@code org.ametiste.scm.messaging.sender.client.enabled}. By default
 * configuration create two clients: one for each lifecycle event. Startup event send after context constructed
 * (method with {@code PostConstruct} annotation. Shutdown event before context destroy (method with {@code PreDestroy}
 * annotation).
 */
@Configuration
@ConditionalOnProperty(value = "org.ametiste.scm.messaging.sender.client.enabled", matchIfMissing = true)
@ConditionalOnBean(LifecycleEventsConfiguration.class)
@Import(LifecycleEventsConfiguration.class)
@EnableConfigurationProperties(ClientProperties.class)
public class MessagingClientsConfiguration {

    @Autowired
    private ClientProperties clientProps;

    @Autowired
    private EventSender eventSender;

    @Autowired
    private EventFactory startupEventFactory;

    @Autowired
    private EventFactory shutdownEventFactory;

    @Bean
    @Qualifier("startupEventSenderClient")
    public EventSenderClient startupEventSenderClient() throws URISyntaxException {
        return new EventSenderClient(
                startupEventFactory,
                eventSender,
                safeUri(clientProps.getTargetUri()),
                clientProps.isStrict()
        );
    }

    @Bean
    @Qualifier("shutdownEventSenderClient")
    @ConditionalOnProperty(value = "org.ametiste.scm.messaging.sender.client.enabled", matchIfMissing = true)
    public EventSenderClient shutdownEventSenderClient() throws URISyntaxException {
        return new EventSenderClient(
                shutdownEventFactory,
                eventSender,
                safeUri(clientProps.getTargetUri()),
                clientProps.isStrict()
        );
    }

    @PostConstruct
    public void sendStartupEvent() throws URISyntaxException {
        if (startupEventSenderClient() != null) {
            startupEventSenderClient().send();
        }
    }

    @PreDestroy
    public void sendShutdownEvent() throws URISyntaxException {
        if (shutdownEventSenderClient() != null) {
            shutdownEventSenderClient().send();
        }
    }

    /**
     * Safe processing of uri string parameter. If string is {@code null} method returns {@code null} without exceptions.
     * If string is set but not valid uri method fail with {@code java.net.MalformedURLException}.
     *
     * @param uri uri string value.
     * @return {@code URI} instance or {@code null} id input is null.
     */
    private static URI safeUri(String uri) {
        return (uri != null) ? URI.create(uri) : null;
    }
}