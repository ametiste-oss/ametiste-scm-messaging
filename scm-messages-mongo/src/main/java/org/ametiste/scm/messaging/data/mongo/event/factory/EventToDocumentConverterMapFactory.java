package org.ametiste.scm.messaging.data.mongo.event.factory;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.mongo.event.EventDocument;

import java.util.Map;
import java.util.function.Function;

/**
 * {@code EventToDocumentConverterMapFactory} interface provides protocol to get map of converters from {@code Event}
 * to document model.
 */
public interface EventToDocumentConverterMapFactory {

    /**
     * Create map with entries where key is Event class and value is converter from this class to corresponded
     * {@code EventDocument} instance.
     * @return initialized map, never return {@literal null}.
     */
    Map<Class, Function<Event, EventDocument>> getMap();
}
