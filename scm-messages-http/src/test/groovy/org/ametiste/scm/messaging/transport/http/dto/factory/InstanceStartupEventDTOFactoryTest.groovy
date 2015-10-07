package org.ametiste.scm.messaging.transport.http.dto.factory

import org.ametiste.scm.messaging.data.event.Event
import org.ametiste.scm.messaging.data.event.InstanceStartupEvent
import org.ametiste.scm.messaging.transport.http.dto.EventDTO
import spock.lang.Specification

class InstanceStartupEventDTOFactoryTest extends Specification {

    private InstanceStartupEventDTOFactory factory = new InstanceStartupEventDTOFactory();

    private InstanceStartupEvent event = InstanceStartupEvent.builder()
            .addInstanceId("ROLL")
            .addVersion("0.2.6")
            .build();

    def "factory must create DTO from event"() {
        when: "try create DTO from InstanceStartupEvent object"
        EventDTO dto = factory.createDTO(event);

        then: "DTO object should be created"
        dto != null
    }

    def "factory throw exception when take wrong event type"() {
        when: "try create DTO from wrong object"
        factory.createDTO(new TestEvent());

        then: "should throw IllegalArgumentException"
        thrown IllegalArgumentException
    }

    private class TestEvent extends Event {}
}
