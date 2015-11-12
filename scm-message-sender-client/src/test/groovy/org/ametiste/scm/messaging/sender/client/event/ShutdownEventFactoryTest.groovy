package org.ametiste.scm.messaging.sender.client.event
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import spock.lang.Specification

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.SHUTDOWN

class ShutdownEventFactoryTest extends Specification {

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
