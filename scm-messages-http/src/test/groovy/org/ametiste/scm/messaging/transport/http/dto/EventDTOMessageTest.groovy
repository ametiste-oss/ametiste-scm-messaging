package org.ametiste.scm.messaging.transport.http.dto

import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import spock.lang.Specification

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.STARTUP

class EventDTOMessageTest extends Specification {

    private final InstanceLifecycleEventDTO dto = new InstanceLifecycleEventDTO(
            InstanceLifecycleEvent.builder()
                .type(STARTUP)
                .instanceId("foo")
                .version("0.2.5")
                .build()
    );

    private final List<URI> excludes = [new URI("http://localhost"), new URI("http://foo.com")]

    def "create default dto message"() {
        when: "create default message"
        EventDTOMessage message = new EventDTOMessage();

        then: "expect take correct default transport message"
        message.getSource() == null
        message.getExcludes() != null
        message.getExcludes().isEmpty()
    }

    def "create dto message with source"() {
        when: "create message with source"
        EventDTOMessage message = new EventDTOMessage(dto)

        then: "expect take correspond property values"
        message.getSource() != null
        message.getExcludes() != null
        message.getExcludes().isEmpty()
        compare(dto, message.getSource())
    }

    def "create dto message with source and excludes"() {
        when: "create message with source and excludes"
        EventDTOMessage message = new EventDTOMessage(dto, excludes)

        then: "expect take correspond property values"
        message.getSource() != null
        message.getExcludes() != null
        message.getExcludes().containsAll(excludes)
        compare(dto, message.getSource())
    }

    private static boolean compare(etalon, tested) {
        etalon.getType().equals(tested.getType());
        etalon.getId().equals(tested.getId());
        etalon.getTimestamp() == tested.getTimestamp();
        etalon.getInstanceId().equals(tested.getInstanceId());
        etalon.getVersion().equals(tested.getVersion());
        etalon.getProperties().equals(tested.getProperties());
        etalon.getNodeId().equals(tested.getNodeId());
        etalon.getUri().equals(tested.getUri());
    }
}
