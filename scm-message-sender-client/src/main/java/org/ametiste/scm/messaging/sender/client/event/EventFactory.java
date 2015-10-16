package org.ametiste.scm.messaging.sender.client.event;

import org.ametiste.scm.messaging.data.event.Event;

/**
 * Factory provides protocol to get event based on instance specific information (like id, version, properties, etc).
 * Created object is ready to send event.
 */
public interface EventFactory {

    /**
     * Create object of some {@code Event} subtype with all needed information about instance.
     */
    Event createEvent();
}