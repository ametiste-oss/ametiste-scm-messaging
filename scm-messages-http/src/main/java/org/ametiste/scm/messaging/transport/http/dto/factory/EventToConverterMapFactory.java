package org.ametiste.scm.messaging.transport.http.dto.factory;

import java.util.Map;

/**
 * {@code EventToConverterMapFactory} interface provides protocol to get matching map
 * {@code Event} subtype to {@code EventDTOFactory}.
 * <p>
 * Map can be used for simplification of Event to EventDTO conversion.
 */
public interface EventToConverterMapFactory {

    /**
     * Return matching map {@code Event} subtype to {@code EventDTOFactory}.
     */
    Map<Class, EventDTOFactory> getMap();
}
