package org.ametiste.scm.messaging.transport.http.dto

import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import spock.lang.Specification

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.*

class InstanceLifecycleEventDTOTest extends Specification {

    def "DTO should convert to correct event"() {
        given: "original event"
        InstanceLifecycleEvent event = InstanceLifecycleEvent.builder()
            .type(STARTUP)
            .instanceId("ROLL")
            .version("0.2.6-1-RELEASE")
            .property("host", new URI("http://192.168.1.2:8080"))
            .property("retries", 15)
            .nodeId("enet")
            .build();

        and: "create DTO from original event"
        InstanceLifecycleEventDTO dto = new InstanceLifecycleEventDTO(event);

        expect: "DTO converts to event equals to original event"
        InstanceLifecycleEvent convertedEvent = (InstanceLifecycleEvent)dto.convert();
        convertedEvent.getType().equals(event.getType());
        convertedEvent.getId().equals(event.getId());
        convertedEvent.getTimestamp() == event.getTimestamp();
        convertedEvent.getInstanceId().equals(event.getInstanceId());
        convertedEvent.getVersion().equals(event.getVersion());
        convertedEvent.getProperties().equals(event.getProperties());
        convertedEvent.getNodeId().equals(event.getNodeId());
        convertedEvent.getUri().equals(event.getUri());
    }
}
