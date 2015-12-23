package org.ametiste.scm.messaging.sender.client.config;

import org.ametiste.scm.messaging.sender.EventSender;
import org.ametiste.scm.messaging.sender.HttpEventSender;
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
 * All configuration can be excluded from context with property {@code org.ametiste.scm.messaging.sender.enabled} set to false.
 *
 * @since 0.1.0
 */
@Configuration
@ConditionalOnProperty(value = "org.ametiste.scm.messaging.sender.enabled", matchIfMissing = true)
@EnableConfigurationProperties({ HttpClientProperties.class, InstanceInfoProperties.class })
public class LifecycleEventsConfiguration {

    @Autowired
    private HttpClientProperties httpClientProps;

    @Autowired
    private InstanceInfoProperties instanceProps;

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
        StartupEventFactory factory = new StartupEventFactory();
        factory.setInstanceId(instanceProps.getInstanceId());
        factory.setVersion(instanceProps.getVersion());
        factory.setNodeId(instanceProps.getNodeId());
        factory.setUri(safeUri(instanceProps.getUri()));
        factory.setProperties(propertiesAggregator().aggregateProperties(env));
        return factory;
    }

    @Bean
    @Qualifier("shutdownEventFactory")
    public EventFactory shutdownEventFactory() throws URISyntaxException {
        ShutdownEventFactory factory = new ShutdownEventFactory();
        factory.setInstanceId(instanceProps.getInstanceId());
        factory.setVersion(instanceProps.getVersion());
        factory.setNodeId(instanceProps.getNodeId());
        factory.setUri(safeUri(instanceProps.getUri()));
        return factory;
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
