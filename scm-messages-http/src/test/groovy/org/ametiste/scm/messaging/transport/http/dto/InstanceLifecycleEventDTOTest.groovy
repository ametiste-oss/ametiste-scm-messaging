package org.ametiste.scm.messaging.transport.http.dto

import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import spock.lang.Specification

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.*

class InstanceLifecycleEventDTOTest extends Specification {

    private static final InstanceLifecycleEvent EVENT = InstanceLifecycleEvent.builder()
            .type(STARTUP)
            .instanceId("ROLL")
            .version("0.2.6-1-RELEASE")
            .property("host", new URI("http://192.168.1.2:8080"))
            .property("retries", 15)
            .nodeId("enet")
            .build();

    def "create correct empty dto instance"() {
        when: "create empty dto"
        InstanceLifecycleEventDTO dto = new InstanceLifecycleEventDTO();

        then: "expect uninitialized fields take"
        dto.getId() == null
        dto.getTimestamp() == 0
        dto.getType() == null
        dto.getInstanceId() == null
        dto.getVersion() == null
        dto.getProperties() != null && dto.getProperties().isEmpty()
        dto.getNodeId() == null
        dto.getUri() == null
    }

    def "dto builder validations"() {
        given: "builder instance"
        InstanceLifecycleEventDTO.Builder builder = new InstanceLifecycleEventDTO.Builder()
            .addType(STARTUP)
            .addId(UUID.randomUUID())
            .addTimestamp(System.currentTimeMillis())
            .addInstanceId("foo")
            .addVersion("0.1.0")
            .addProperties(Collections.emptyMap())
            .addNodeId("node")
            .addUri(new URI("http://localhost"));

        when: "try build dto with valid fields"
        builder.build()

        then: "expect no exception thrown"
        noExceptionThrown()

        when: "add not initialized type and try build dto"
        builder.addType(null).build()

        then: "expect illegal state exception thrown"
        thrown(IllegalStateException.class)

        when: "add not initialized id and try build dto"
        builder.addType(STARTUP).addId(null).build()

        then: "expect illegal state exception thrown"
        thrown(IllegalStateException.class)

        when: "add negative timestamp and try build dto"
        builder.addId(UUID.randomUUID()).addTimestamp(-15).build()

        then: "expect illegal state exception thrown"
        thrown(IllegalStateException.class)

        when: "add not initialized instance id and try build dto"
        builder.addTimestamp(System.currentTimeMillis()).addInstanceId(null).build()

        then: "expect illegal state exception thrown"
        thrown(IllegalStateException.class)

        when: "add not initialized version and try build dto"
        builder.addInstanceId("foo").addVersion(null).build()

        then: "expect illegal state exception thrown"
        thrown(IllegalStateException.class)

        when: "add empty version and try build dto"
        builder.addVersion("").build()

        then: "expect illegal state exception thrown"
        thrown(IllegalStateException.class)
    }

    def "create event with builder"() {
        given: "event and builder instance"
        InstanceLifecycleEvent event = EVENT;
        InstanceLifecycleEventDTO.Builder builder = new InstanceLifecycleEventDTO.Builder();

        when: "create dto for event with builder"
        builder.addType(event.getType())
        builder.addId(event.getId())
        builder.addTimestamp(event.getTimestamp())
        builder.addInstanceId(event.getInstanceId())
        builder.addVersion(event.getVersion())
        builder.addProperties(event.getProperties())
        builder.addNodeId(event.getNodeId())
        builder.addUri(event.getUri())
        InstanceLifecycleEventDTO dto = builder.build()

        then: "expect take the same object with same information"
        compare(dto, event)
    }

    def "DTO should convert to correct event"() {
        given: "original event"
        InstanceLifecycleEvent event = EVENT;

        and: "create DTO from original event"
        InstanceLifecycleEventDTO dto = new InstanceLifecycleEventDTO(event);

        expect: "DTO converts to event equals to original event"
        InstanceLifecycleEvent convertedEvent = (InstanceLifecycleEvent)dto.convert();
        compare(event, convertedEvent)
    }

    def compare(event, convertedEvent) {
        return convertedEvent.getType().equals(event.getType()) ||
            convertedEvent.getId().equals(event.getId()) ||
            convertedEvent.getTimestamp() == event.getTimestamp() ||
            convertedEvent.getInstanceId().equals(event.getInstanceId()) ||
            convertedEvent.getVersion().equals(event.getVersion()) ||
            convertedEvent.getProperties().equals(event.getProperties()) ||
            convertedEvent.getNodeId().equals(event.getNodeId()) ||
            convertedEvent.getUri().equals(event.getUri());
    }
}
