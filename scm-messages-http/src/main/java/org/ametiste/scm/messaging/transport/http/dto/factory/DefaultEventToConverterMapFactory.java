package org.ametiste.scm.messaging.transport.http.dto.factory;

import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Default {@code EventToConverterMapFactory} factory that contains mapping for all available {@code Event} subtypes.
 */
public class DefaultEventToConverterMapFactory implements EventToConverterMapFactory {

    @Override
    public Map<Class, EventDTOFactory> getMap() {
        Map<Class, EventDTOFactory> map = new HashMap<>(1);
        map.put(InstanceLifecycleEvent.class, new InstanceLifecycleEventDTOFactory());

        return Collections.unmodifiableMap(map);
    }
}
