package org.ametiste.scm.messaging.sender.client.event

import org.ametiste.scm.messaging.data.event.Event
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import spock.lang.Specification

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.SHUTDOWN

class ShutdownEventFactoryTest extends Specification {

    private ShutdownEventFactory factory;

    def "fields validation"() {
        when: "try create event with not initialized instanceId"
        factory = new ShutdownEventFactory()
        factory.setInstanceId(null);
        factory.setVersion("0.1.0");
        factory.setNodeId(null);
        factory.setUri(null);
        factory.createEvent();

        then: "expect exception will thrown"
        thrown(Exception.class)

        when: "try create event with empty instanceId"
        factory = new ShutdownEventFactory()
        factory.setInstanceId("");
        factory.setVersion("0.1.0");
        factory.setNodeId(null);
        factory.setUri(null);
        factory.createEvent();

        then: "expect exception will thrown"
        thrown(Exception.class)

        when: "try create event with not initialized version"
        factory = new ShutdownEventFactory()
        factory.setInstanceId("search");
        factory.setVersion(null);
        factory.setNodeId(null);
        factory.setUri(null);
        factory.createEvent();

        then: "expect exception will thrown"
        thrown(Exception.class)

        when: "try create event with empty version"
        factory = new ShutdownEventFactory()
        factory.setInstanceId("search");
        factory.setVersion("");
        factory.setNodeId(null);
        factory.setUri(null);
        factory.createEvent();

        then: "expect exception will thrown"
        thrown(Exception.class)

        when: "try create event with valid required parameters"
        factory = new ShutdownEventFactory()
        factory.setInstanceId("search");
        factory.setVersion("0.1.0");
        factory.setNodeId(null);
        factory.setUri(null);
        Event event = factory.createEvent();

        then: "expect no exception thrown"
        noExceptionThrown()

        and: "get not null instance of event"
        event != null
    }

    def "should create correct event"() {
        def type = SHUTDOWN;
        def instanceId = "DWS";
        def version = "0.2.6";
        def nodeId = "aws.node2";
        def uri = URI.create("http://localhost:8085")

        given: "event factory"
        def factory = new ShutdownEventFactory(instanceId, version, nodeId, uri);

        when: "factory create event"
        InstanceLifecycleEvent event = (InstanceLifecycleEvent)factory.createEvent();

        then: "expect it's correct event"
        event.getType().equals(type);
        event.getInstanceId().equals(instanceId);
        event.getVersion().equals(version);
        event.getNodeId().equals(nodeId);
        event.getUri() == uri
    }
}
