package org.ametiste.scm.messaging.sender;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.transport.TransportMessage;

import java.net.URI;
import java.util.Collection;

/**
 * {@code EventSender} interface provide protocol for sending events.
 * <p>
 * {@code EventSender} doesn't transform incoming URI and works with it as with full path to event receiver endpoint.
 * Sender implementations must ignore incoming messages that contains receiver URI in exclude list.
 */
public interface EventSender {

    /**
     * Send {@code TransportMessage<Event>} to receiver.
     * @param receiver URI to event receiver endpoint.
     * @param message message to send.
     * @throws EventSendException when any error occurred during transmit process.
     */
    void send(URI receiver, TransportMessage<Event> message) throws EventSendException;

    /**
     * Send {@code TransportMessage<Event>} bulk to receiver.
     * @param receiver URI to event receiver endpoint.
     * @param messages list with messages to send.
     * @throws EventSendException when any error occurred during transmit process.
     */
    void send(URI receiver, Collection<TransportMessage<Event>> messages) throws EventSendException;
}
