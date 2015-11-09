package org.ametiste.scm.messaging.data.transport

import org.ametiste.scm.messaging.data.event.Event
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import spock.lang.Specification

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.STARTUP

class EventTransportMessageTest extends Specification {

    private final InstanceLifecycleEvent event = InstanceLifecycleEvent.builder()
            .type(STARTUP)
            .instanceId("foo")
            .version("0.2.5")
            .build();
    private final List<URI> excludes = [new URI("http://localhost"), new URI("http://foo.com")]

    def "create default message"() {
        when: "create default message"
        EventTransportMessage message = new EventTransportMessage();

        then: "expect take correct default transport message"
        message.getSource() == null
        message.getExcludes() != null
        message.getExcludes().isEmpty()
    }

    def "create message with source"() {
        when: "create message with source"
        TransportMessage<Event> message = new TransportMessage<>(event)

        then: "expect take correspond property values"
        message.getSource() != null
        message.getExcludes() != null
        message.getExcludes().isEmpty()
        compare(event, message.getSource())
    }

    def "create message with source and excludes"() {
        when: "create message with source and excludes"
        TransportMessage<Event> message = new TransportMessage<>(event, excludes)

        then: "expect take correspond property values"
        message.getSource() != null
        message.getExcludes() != null
        message.getExcludes().containsAll(excludes)
        compare(event, message.getSource())
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
