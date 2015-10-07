package org.ametiste.scm.messaging.data.transport;

import org.ametiste.scm.messaging.data.event.Event;

import java.net.URI;
import java.util.Collection;

/**
 * Class provided for correct processing of {@code TransportMessage<Event>} (resolve "Type Erasure").
 * For example, in {@code EventListener} to handle only transport messages with {@code Event} source.
 */
public class EventTransportMessage extends TransportMessage<Event> {

    public EventTransportMessage() {
    }

    public EventTransportMessage(Event source) {
        super(source);
    }

    public EventTransportMessage(Event source, Collection<URI> excludes) {
        super(source, excludes);
    }
}
