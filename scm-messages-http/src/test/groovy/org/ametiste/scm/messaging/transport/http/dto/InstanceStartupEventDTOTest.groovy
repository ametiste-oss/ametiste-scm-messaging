package org.ametiste.scm.messaging.transport.http.dto

import org.ametiste.scm.messaging.data.event.InstanceStartupEvent
import spock.lang.Specification

class InstanceStartupEventDTOTest extends Specification {

    def "DTO should convert to correct event"() {
        given: "original event"
        InstanceStartupEvent event = InstanceStartupEvent.builder()
            .addInstanceId("ROLL")
            .addVersion("0.2.6-1-RELEASE")
            .addProperty("host", new URI("http://192.168.1.2:8080"))
            .addProperty("retries", 15)
            .addNodeId("enet")
            .build();

        and: "create DTO from original event"
        InstanceStartupEventDTO dto = new InstanceStartupEventDTO(event);

        expect: "DTO converts to event equals to original event"
        InstanceStartupEvent convertedEvent = (InstanceStartupEvent)dto.convert();
        convertedEvent.getId().equals(event.getId());
        convertedEvent.getTimestamp() == event.getTimestamp();
        convertedEvent.getInstanceId().equals(event.getInstanceId());
        convertedEvent.getVersion().equals(event.getVersion());
        convertedEvent.getProperties().equals(event.getProperties());
        convertedEvent.getNodeId().equals(event.getNodeId());
        convertedEvent.getUri().equals(event.getUri());
    }
}
