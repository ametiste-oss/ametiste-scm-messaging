package org.ametiste.scm.messaging.data.mongo.event.factory;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.mongo.event.EventDocument;

import java.util.Map;
import java.util.function.Function;

/**
 *
 */
public interface EventToDocumentConverterMapFactory {

    /**
     *
     * @return
     */
    Map<Class, Function<Event, EventDocument>> getMap();
}
