package org.ametiste.scm.messaging.transport.http.dto;

import org.ametiste.scm.messaging.data.transport.TransportMessage;

import java.net.URI;
import java.util.Collection;

/**
 * {@code EventDTOMessage} is a {@code TransportMessage} with {@code EventDTO} source.
 * <p>
 * Class provided for correct processing of {@code TransportMessage<EventDTO>} (resolve "Type Erasure").
 * If we create transport message directly {@code new TransportMessage<EventDTO>(source)}, Jackson Processor don't add
 * service field "@type" to internal {@code EventDTO} object and receiver will not be able to recognize which subtype of
 * event has been sent.
 */
public class EventDTOMessage extends TransportMessage<EventDTO> {

    /**
     * Default constructor. Added to provide correct operation of Jackson Processor.
     */
    public EventDTOMessage() {
        super();
    }

    /**
     * Create {@code EventDTOMessage} with empty exclude list.
     * @param source {@code EventDTO} subclass instance.
     */
    public EventDTOMessage(EventDTO source) {
        super(source);
    }

    /**
     * Create {@code EventDTOMessage} with specified list of excludes.
     * @param source {@code EventDTO} subclass instance.
     * @param excludes list with URIs of services that must be excluded from event broadcast.
     */
    public EventDTOMessage(EventDTO source, Collection<URI> excludes) {
        super(source, excludes);
    }
}
