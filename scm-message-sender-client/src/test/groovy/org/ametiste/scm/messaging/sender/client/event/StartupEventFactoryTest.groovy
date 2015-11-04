package org.ametiste.scm.messaging.sender.client.event

import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import spock.lang.Specification

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.*

class StartupEventFactoryTest extends Specification {

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
        assert event.getType().equals(type);
        assert event.getInstanceId().equals(instanceId);
        assert event.getVersion().equals(version);
        assert event.getNodeId().equals(nodeId);
        assert event.getUri() == uri
        assert event.properties.size() == properties.size()
    }
}
