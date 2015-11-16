package org.ametiste.scm.messaging.sender.client.event

import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import spock.lang.Specification

import static java.util.Collections.emptyMap
import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.STARTUP

class StartupEventFactoryTest extends Specification {

    def "arguments validation"() {
        when: "try create event factory with not initialized instanceId"
        new StartupEventFactory(null, "0.1.0", null, null, emptyMap())

        then: "expect exception will thrown"
        thrown(Exception.class)

        when: "try create event factory with empty instanceId"
        new StartupEventFactory("", "0.1.0", null, null, emptyMap())

        then: "expect exception will thrown"
        thrown(Exception.class)

        when: "try create event factory with not initialized version"
        new StartupEventFactory("search", null, null, null, emptyMap())

        then: "expect exception will thrown"
        thrown(Exception.class)

        when: "try create event factory with empty version"
        new StartupEventFactory("search", "", null, null, emptyMap())

        then: "expect exception will thrown"
        thrown(Exception.class)

        when: "try create event factory with not initialized properties"
        new StartupEventFactory("search", "0.1.0", null, null, null)

        then: "expect Exception will thrown"
        thrown(Exception.class)

        when: "try create event factory with valid required parameters"
        StartupEventFactory factory = new StartupEventFactory("search", "0.1.0", null, null, emptyMap())

        then: "expect no exception thrown"
        noExceptionThrown()

        and: "get not null instance of event factory"
        factory != null
    }

    def "should create correct event"() {
        def type = STARTUP;
        def instanceId = "DWS";
        def version = "0.2.6";
        def nodeId = "aws.node2";
        def uri = URI.create("http://localhost:8085")
        def properties = Collections.singletonMap("server.port", 8085);

        given: "event factory"
        def factory = new StartupEventFactory(instanceId, version, nodeId, uri, properties);

        when: "factory create event"
        InstanceLifecycleEvent event = (InstanceLifecycleEvent)factory.createEvent();

        then: "expect it's correct event"
        event.getType().equals(type);
        event.getInstanceId().equals(instanceId);
        event.getVersion().equals(version);
        event.getNodeId().equals(nodeId);
        event.getUri() == uri
        event.properties.size() == properties.size()
    }
}
