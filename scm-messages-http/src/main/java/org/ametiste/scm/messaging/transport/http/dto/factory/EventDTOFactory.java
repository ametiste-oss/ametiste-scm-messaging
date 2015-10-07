package org.ametiste.scm.messaging.transport.http.dto.factory;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.transport.http.dto.EventDTO;

/**
 * {@code EventDTOFactory} interface provides protocol to construct {@code EventDTO} from {@code Event} subtypes.
 */
public interface EventDTOFactory {

    /**
     * Create {@code EventDTO} from {@code Event} subtype object.
     * @throws IllegalArgumentException when subtype of source object not equal to expected type.
     */
    EventDTO createDTO(Event event) throws IllegalArgumentException;
}
