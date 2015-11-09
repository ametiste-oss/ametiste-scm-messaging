package org.ametiste.scm.messaging.data.mongo.event.factory;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent;
import org.ametiste.scm.messaging.data.mongo.event.EventDocument;
import org.ametiste.scm.messaging.data.mongo.event.InstanceLifecycleEventDocument;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Default implementation of {@code EventToDocumentConverterMapFactory} that contains converters for all known event
 * subtypes.
 */
public class DefaultEventToDocumentConverterMapFactory implements EventToDocumentConverterMapFactory {

    @Override
    public Map<Class, Function<Event, EventDocument>> getMap() {
        HashMap<Class, Function<Event, EventDocument>> map = new HashMap<>(1);
        map.put(InstanceLifecycleEvent.class, p -> new InstanceLifecycleEventDocument((InstanceLifecycleEvent) p));
        return map;
    }
}
