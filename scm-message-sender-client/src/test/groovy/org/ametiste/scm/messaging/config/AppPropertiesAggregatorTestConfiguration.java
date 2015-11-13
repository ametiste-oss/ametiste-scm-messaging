package org.ametiste.scm.messaging.config;

import org.ametiste.scm.messaging.sender.client.environment.AppPropertiesAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:test-props-aggregator.properties")
public class AppPropertiesAggregatorTestConfiguration {

    @Bean
    public AppPropertiesAggregator aggregator() {
        return new AppPropertiesAggregator();
    }
}
