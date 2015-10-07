package org.ametiste.scm.messaging.mock;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceStartupEvent;
import org.ametiste.scm.messaging.data.transport.EventTransportMessage;
import org.ametiste.scm.messaging.data.transport.TransportMessage;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Subscriber contains all possible listener types and save messages by listener type for next analysis.
 */
public class Subscriber {

    private Map<ListenerType, List<Object>> messages = new HashMap<>();

    public Subscriber() {
        for (ListenerType type : ListenerType.values()) {
            messages.put(type, new ArrayList<>());
        }
    }

    @EventListener
    public void onTransportMessage(TransportMessage message) {
        messages.get(ListenerType.TRANSPORT_MESSAGE).add(message);
    }

    @EventListener
    public void onEvenTransportMessage(EventTransportMessage message) {
        messages.get(ListenerType.TRANSPORT_MESSAGE_WITH_EVENT).add(message);
    }

    @EventListener
    public void onEvent(Event event) {
        messages.get(ListenerType.EVENT).add(event);
    }

    @EventListener
    public void onInstanceStartupEvent(InstanceStartupEvent event) {
        messages.get(ListenerType.INSTANCE_STARTUP_EVENT).add(event);
    }

    public List<Object> events(ListenerType type) {
        return messages.get(type);
    }

    public void clean() {
        for (ListenerType type : messages.keySet()) {
            messages.get(type).clear();
        }
    }

    public enum ListenerType {
        TRANSPORT_MESSAGE,
        TRANSPORT_MESSAGE_WITH_EVENT,
        EVENT,
        INSTANCE_STARTUP_EVENT
    }
}
