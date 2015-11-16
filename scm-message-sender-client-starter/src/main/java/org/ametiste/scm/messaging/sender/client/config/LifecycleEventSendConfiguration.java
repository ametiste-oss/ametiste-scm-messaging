package org.ametiste.scm.messaging.sender.client.config;

import org.ametiste.scm.messaging.sender.EventSender;
import org.ametiste.scm.messaging.sender.HttpEventSender;
import org.ametiste.scm.messaging.sender.client.EventSenderClient;
import org.ametiste.scm.messaging.sender.client.environment.AppPropertiesAggregator;
import org.ametiste.scm.messaging.sender.client.event.EventFactory;
import org.ametiste.scm.messaging.sender.client.event.ShutdownEventFactory;
import org.ametiste.scm.messaging.sender.client.event.StartupEventFactory;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configuration add to application context all needed components for sending messages to SCM system about instance
 * lifecycle events. For now it include startup and shutdown.
 * <p>
 * Startup event contains information about instance and their configuration, shutdown event include same information
 * without configuration. Events creates with {@code EventFactory} instances. Each of them has qualifier and can be
 * autowired in other part of context.
 * <p>
 * Client instances can be disabled with property {@code org.ametiste.scm.messaging.sender.client.enabled}. By default
 * configuration create two clients: one for each lifecycle event. Startup event send after context constructed
 * (method with {@code PostConstruct} annotation. Shutdown event before context destroy (method with {@code PreDestroy}
 * annotation).
 * <p>
 * All configuration can be excluded from context with property {@code org.ametiste.scm.messaging.sender.enabled} set to false.
 */
@Configuration
@ConditionalOnProperty(value = "org.ametiste.scm.messaging.sender.enabled", matchIfMissing = true)
@EnableConfigurationProperties({ HttpClientProperties.class, InstanceInfoProperties.class, ClientProperties.class })
public class LifecycleEventSendConfiguration {

    @Autowired
    private HttpClientProperties httpClientProps;

    @Autowired
    private InstanceInfoProperties instanceProps;

    @Autowired
    private ClientProperties clientProps;

    @Autowired
    private Environment env;

    @Bean
    public HttpClient eventSenderHttpClient() {
        return HttpEventSender.createHttpClient(httpClientProps.getConnectTimeout(), httpClientProps.getReadTimeout());
    }

    @Bean
    public EventSender eventSender() {
        return new HttpEventSender(eventSenderHttpClient());
    }

    @Bean
    public AppPropertiesAggregator propertiesAggregator() {
        return new AppPropertiesAggregator();
    }

    @Bean
    @Qualifier("startupEventFactory")
    public EventFactory startupEventFactory() throws URISyntaxException {
        return new StartupEventFactory(
                instanceProps.getInstanceId(),
                instanceProps.getVersion(),
                instanceProps.getNodeId(),
                new URI(instanceProps.getUri()),
                propertiesAggregator().aggregateProperties(env));
    }

    @Bean
    @Qualifier("shutdownEventFactory")
    public EventFactory shutdownEventFactory() throws URISyntaxException {
        return new ShutdownEventFactory(
                instanceProps.getInstanceId(),
                instanceProps.getVersion(),
                instanceProps.getNodeId(),
                new URI(instanceProps.getUri())
        );
    }

    @Bean
    @Qualifier("startupEventSenderClient")
    @ConditionalOnProperty(value = "org.ametiste.scm.messaging.sender.client.enabled", matchIfMissing = true)
    public EventSenderClient startupEventSenderClient() throws URISyntaxException {
        return new EventSenderClient(
                startupEventFactory(),
                eventSender(),
                new URI(clientProps.getTargetUri()),
                clientProps.isStrict()
        );
    }

    @Bean
    @Qualifier("shutdownEventSenderClient")
    @ConditionalOnProperty(value = "org.ametiste.scm.messaging.sender.client.enabled", matchIfMissing = true)
    public EventSenderClient shutdownEventSenderClient() throws URISyntaxException {
        return new EventSenderClient(
                shutdownEventFactory(),
                eventSender(),
                new URI(clientProps.getTargetUri()),
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
}
