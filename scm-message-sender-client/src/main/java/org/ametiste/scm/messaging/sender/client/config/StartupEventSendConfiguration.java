package org.ametiste.scm.messaging.sender.client.config;

import org.ametiste.scm.messaging.sender.EventSender;
import org.ametiste.scm.messaging.sender.HttpEventSender;
import org.ametiste.scm.messaging.sender.client.EventSenderBootstrap;
import org.ametiste.scm.messaging.sender.client.environment.AppPropertiesAggregator;
import org.ametiste.scm.messaging.sender.client.event.EventFactory;
import org.ametiste.scm.messaging.sender.client.event.StartupEventFactory;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configuration for integrate bootstrap event sending process.
 * It can be excluded from context with property {@code org.ametiste.scm.messaging.sender.enabled} set to false.
 */
@Configuration
@ConditionalOnProperty(value = "org.ametiste.scm.messaging.sender.enabled", matchIfMissing = true)
@EnableConfigurationProperties({ HttpClientProperties.class, InstanceInfoProperties.class, BootstrapProperties.class })
public class StartupEventSendConfiguration {

    @Autowired
    private HttpClientProperties httpClientProps;

    @Autowired
    private InstanceInfoProperties instanceProps;

    @Autowired
    private BootstrapProperties bootstrapProps;

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
    public EventFactory startupEventFactory() throws URISyntaxException {
        return new StartupEventFactory(
                instanceProps.getInstanceId(),
                instanceProps.getVersion(),
                instanceProps.getNodeId(),
                new URI(instanceProps.getUri()),
                propertiesAggregator().aggregateProperties(env));
    }

    @Bean
    @ConditionalOnProperty(value = "org.ametiste.scm.messaging.sender.bootstrap.enabled", matchIfMissing = true)
    public EventSenderBootstrap eventSenderBootstrap() throws URISyntaxException {
        EventSenderBootstrap bootstrap = new EventSenderBootstrap(
                startupEventFactory(),
                eventSender(),
                new URI(bootstrapProps.getTargetUri()),
                bootstrapProps.isStrict()
        );
        bootstrap.send();
        return bootstrap;
    }
}
