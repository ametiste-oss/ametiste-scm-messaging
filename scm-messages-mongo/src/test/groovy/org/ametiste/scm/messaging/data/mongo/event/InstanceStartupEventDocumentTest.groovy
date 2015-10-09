package org.ametiste.scm.messaging.data.mongo.event

import org.ametiste.scm.messaging.data.event.InstanceStartupEvent
import spock.lang.Specification

class InstanceStartupEventDocumentTest extends Specification {

    private InstanceStartupEvent event = InstanceStartupEvent.builder()
            .addId(UUID.fromString("c23e289c-5543-4c7c-8d99-7a30eeb153ae"))
            .addTimestamp(new Date().getTime())
            .addInstanceId("ROLL")
            .addVersion("0.2.6")
            .build();

    def "should correct convert both ways"() {
        when: "create document from event"
        InstanceStartupEventDocument document = new InstanceStartupEventDocument(event)

        then: "get document with same info"
        compare(document, event)

        when: "convert document to event"
        InstanceStartupEvent newEvent = document.convert()

        then: "converted event must be as source"
        compare(newEvent, event)
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
