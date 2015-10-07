package org.ametiste.scm.messaging.sender.client.client.environment;

import org.springframework.core.env.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@code AppPropertiesAggregator} provides functionality to create map of properties from {@code Environment}.
 * Logic borrowed from Spring Actuator. See org.springframework.boot.actuate.endpoint.EnvironmentEndpoint class.
 */
public class AppPropertiesAggregator {

    private final Sanitizer sanitizer = new Sanitizer();

    /**
     * Get properties from all available sources.
     * Also add information about active profiles ("profiles" key).
     * @param environment {@code Environment} instance.
     * @return result map of properties.
     */
    public Map<String, Object> aggregateProperties(Environment environment) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("profiles", environment.getActiveProfiles());

        getPropertySources(environment).entrySet().stream()
                .filter(entry -> entry.getValue() instanceof EnumerablePropertySource)
                .forEach(entry -> {
                    EnumerablePropertySource<?> enumerable = (EnumerablePropertySource<?>)entry.getValue();
                    Map<String, Object> map = Arrays.stream(enumerable.getPropertyNames())
                            .collect(Collectors.toMap(
                                    name -> name,
                                    name -> sanitizer.sanitize(name, enumerable.getProperty(name)))
                            );
                    result.put(entry.getKey(), map);
                });

        return result;
    }

    private Map<String, PropertySource<?>> getPropertySources(Environment environment) {
        Map<String, PropertySource<?>> map = new LinkedHashMap<>();
        MutablePropertySources sources = null;

        if (environment != null && environment instanceof ConfigurableEnvironment) {
            sources = ((ConfigurableEnvironment) environment).getPropertySources();
        } else {
            sources = new StandardEnvironment().getPropertySources();
        }

        for (PropertySource<?> source : sources) {
            extract("", map, source);
        }
        return map;
    }

    private void extract(String root, Map<String, PropertySource<?>> map, PropertySource<?> source) {
        if (source instanceof CompositePropertySource) {
            for (PropertySource<?> nest : ((CompositePropertySource) source).getPropertySources()) {
                extract(source.getName() + ":", map, nest);
            }
        } else {
            map.put(root + source.getName(), source);
        }
    }
}
