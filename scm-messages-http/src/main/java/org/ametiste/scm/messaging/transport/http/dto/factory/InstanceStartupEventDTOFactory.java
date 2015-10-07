package org.ametiste.scm.messaging.transport.http.dto.factory;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceStartupEvent;
import org.ametiste.scm.messaging.transport.http.dto.EventDTO;
import org.ametiste.scm.messaging.transport.http.dto.InstanceStartupEventDTO;

/**
 * {@code EventDTOFactory} for {@code InstanceStartupEvent} event type.
 */
public class InstanceStartupEventDTOFactory implements EventDTOFactory {

    private static final Class TARGET_EVENT_CLASS = InstanceStartupEvent.class;

    @Override
    public EventDTO createDTO(Event event) throws IllegalArgumentException {
        if (!TARGET_EVENT_CLASS.isInstance(event)) {
            throw new IllegalArgumentException("Can't create DTO. Unexpected event type.");
        }

        return new InstanceStartupEventDTO((InstanceStartupEvent)event);
    }
}
