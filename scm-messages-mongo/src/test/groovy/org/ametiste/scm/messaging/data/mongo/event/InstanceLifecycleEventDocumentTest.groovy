package org.ametiste.scm.messaging.data.mongo.event

import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import spock.lang.Specification

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.STARTUP

class InstanceLifecycleEventDocumentTest extends Specification {

    private final InstanceLifecycleEvent event = InstanceLifecycleEvent.builder()
            .type(STARTUP)
            .id(UUID.fromString("c23e289c-5543-4c7c-8d99-7a30eeb153ae"))
            .timestamp(new Date().getTime())
            .instanceId("ROLL")
            .version("0.2.6")
            .build();

    def "should correct convert both ways"() {
        when: "create document from event"
        InstanceLifecycleEventDocument document = new InstanceLifecycleEventDocument(event)

        then: "get document with same info"
        compare(document, event)

        when: "convert document to event"
        InstanceLifecycleEvent newEvent = document.convert()

        then: "converted event must be as source"
        compare(newEvent, event)
    }

    def "create correct document with default constructor and setters"() {
        given: "document constructed with default constructor and setters"
        InstanceLifecycleEventDocument document = new InstanceLifecycleEventDocument();
        document.setType(event.getType())
        document.setId(event.getId())
        document.setTimestamp(event.getTimestamp())
        document.setInstanceId(event.getInstanceId())
        document.setVersion(event.getVersion())
        document.setProperties(event.getProperties())
        document.setNodeId(event.getNodeId())
        document.setUri(event.getUri())

        expect: "take document with same information as event contains"
        compare(document, event)
    }

    private static boolean compare(test, source) {
        return  test.getId().equals(source.getId()) &&
                test.getTimestamp() == source.getTimestamp() &&
                test.getInstanceId().equals(source.getInstanceId()) &&
                test.getVersion().equals(source.getVersion()) &&
                test.getNodeId().equals(source.getNodeId()) &&
                test.getUri().equals(source.getUri()) &&
                (test.getProperties().size() == source.getProperties().size());
    }
}
