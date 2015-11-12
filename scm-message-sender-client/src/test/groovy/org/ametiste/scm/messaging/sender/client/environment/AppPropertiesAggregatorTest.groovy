package org.ametiste.scm.messaging.sender.client.environmet

import org.ametiste.scm.messaging.config.AppPropertiesAggregatorTestConfiguration
import org.ametiste.scm.messaging.sender.client.environment.AppPropertiesAggregator
import org.ametiste.scm.messaging.sender.client.environment.Sanitizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.core.env.Environment
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = AppPropertiesAggregatorTestConfiguration.class,
        loader = SpringApplicationContextLoader.class)
class AppPropertiesAggregatorTest extends Specification {

    public static final String MAP_KEY_FOR_TEST_FILE = 'class path resource [test-props-aggregator.properties]';

    @Autowired
    private Environment environment;

    @Autowired
    private AppPropertiesAggregator aggregator;

    def "aggregator should return standard env if passed is null"() {
        when: "try get properties with null environment"
        Map<String, Object> properties = aggregator.aggregateProperties(null)

        then: "expect no exception thrown"
        noExceptionThrown()

        and: "returned value initialized"
        properties != null
        !properties.isEmpty()
    }

    def "aggregator should return initialized not empty properties map"() {
        given: "result of environment aggregation"
        Map<String, Object> properties = aggregator.aggregateProperties(environment);

        expect: "map should be initialized and not empty"
        properties != null
        !properties.isEmpty()

        and: "should contains test-props-aggregator.properties"
        properties.containsKey(MAP_KEY_FOR_TEST_FILE);

        and: "should contains property from file"
        ((Map<String, Object>)properties.get(MAP_KEY_FOR_TEST_FILE)).containsKey('org.ametiste.scm.public.url');
    }

    def "aggregator should sanitize secret properties"() {
        given: "result of environment aggregation"
        Map<String, Object> properties = aggregator.aggregateProperties(environment);

        and: "extract properties map for test file"
        Map<String, Object> map = (Map<String, Object>)properties.get(MAP_KEY_FOR_TEST_FILE);

        expect: "aggregator sanitize secret properties"
        map.get(property).toString().equals(Sanitizer.SANITIZE_STRING);

        where:
        property << ['org.ametiste.scm.secret-key', 'org.ametiste.scm.credentials', 'org.ametiste.scm.password']
    }

    def "aggregator shouldn't sanitize usual properties"() {
        given: "result of environment aggregation"
        Map<String, Object> properties = aggregator.aggregateProperties(environment);

        and: "extract properties map for test file"
        Map<String, Object> map = (Map<String, Object>)properties.get(MAP_KEY_FOR_TEST_FILE);

        expect: "aggregator don't sanitize usual properties"
        !map.get(property).toString().equals(Sanitizer.SANITIZE_STRING);

        where:
        property << ['org.ametiste.scm.public.url']
    }
}
