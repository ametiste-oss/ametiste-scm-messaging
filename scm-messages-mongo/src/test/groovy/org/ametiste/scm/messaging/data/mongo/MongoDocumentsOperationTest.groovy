package org.ametiste.scm.messaging.data.mongo

import org.ametiste.scm.messaging.data.event.Event
import org.ametiste.scm.messaging.data.event.InstanceStartupEvent
import org.ametiste.scm.messaging.data.mongo.event.EventDocument
import org.ametiste.scm.messaging.data.mongo.event.factory.DefaultEventToDocumentConverterMapFactory
import org.ametiste.scm.messaging.data.mongo.event.factory.EventToDocumentConverterMapFactory
import spock.lang.Specification

import java.util.function.Function

/**
 * Test all together: documents creation and mapping with default map from
 * {@code DefaultEventToDocumentConverterMapFactory}.
 */
class MongoDocumentsOperationTest extends Specification {

    private EventToDocumentConverterMapFactory mapFactory = new DefaultEventToDocumentConverterMapFactory();

    private Event event = InstanceStartupEvent.builder()
            .addId(UUID.fromString("c23e289c-5543-4c7c-8d99-7a30eeb153ae"))
            .addTimestamp(new Date().getTime())
            .addInstanceId("ROLL")
            .addVersion("0.2.6")
            .build();

    def "should create correct document without exception"() {
        given: "map with converters"
        Map<Class, Function<Event, EventDocument>> map = mapFactory.getMap();

        when: "convert event to document"
        EventDocument document = map.get(event.getClass()).apply(event);

        then: "get document with same info"
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
