package org.ametiste.scm.messaging.data.mongo.event.factory;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.mongo.event.EventDocument;
import org.ametiste.scm.messaging.data.mongo.event.InstanceStartupEventDocument;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultEventToDocumentConverterMapFactory implements EventToDocumentConverterMapFactory {

    @Override
    public Map<Class, Function<Event, EventDocument>> getMap() {
        HashMap<Class, Function<Event, EventDocument>> map = new HashMap<>(1);
        map.put(InstanceStartupEvent.class, p -> new InstanceStartupEventDocument((InstanceStartupEvent)p));
        return map;
    }
}
