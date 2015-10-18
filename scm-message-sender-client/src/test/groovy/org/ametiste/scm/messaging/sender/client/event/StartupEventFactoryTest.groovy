package org.ametiste.scm.messaging.sender.client.event

import org.ametiste.scm.messaging.data.event.InstanceStartupEvent
import spock.lang.Specification

class StartupEventFactoryTest extends Specification {

    def "should create correct event"() {
        def instanceId = "DWS";
        def version = "0.2.6";
        def nodeId = "aws.node2";
        def uri = URI.create("http://localhost:8085")
        def properties = Collections.singletonMap("server.port", 8085);

        given: "event factory"
        def factory = new StartupEventFactory(instanceId, version, nodeId, uri, properties);

        when: "factory create event"
        InstanceStartupEvent event = (InstanceStartupEvent)factory.createEvent();

        then: "expect it's correct event"
        assert event.getInstanceId().equals(instanceId);
        assert event.getVersion().equals(version);
        assert event.getNodeId().equals(nodeId);
        assert event.getUri() == uri
        assert event.properties.size() == properties.size()
    }
}
