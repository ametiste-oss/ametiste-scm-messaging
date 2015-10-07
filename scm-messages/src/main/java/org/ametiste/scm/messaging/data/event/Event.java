package org.ametiste.scm.messaging.data.event;

import java.io.Serializable;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * {@code Event} is a abstract class that generalize all events in system.
 * All event must be extended from {@code Event} and have identifier and time point when event created.
 */
public abstract class Event implements Serializable {

    private static final long serialVersionUID = 19L;

    private final UUID id;
    private final long timestamp;

    /**
     * Create event with random id (UUID) and current time as creation time.
     */
    public Event() {
        this(UUID.randomUUID());
    }

    /**
     * Create event with specified id and current time as creation time.
     * @param id {@code UUID} identifier of event.
     */
    public Event(UUID id) {
        this(id, System.currentTimeMillis());
    }

    /**
     * Create event with custom id and creation time. Commonly used by Jackson Processor for converting messages to DTO.
     * @param id {@code UUID} identifier of event.
     * @param timestamp timestamp of event creation time in milliseconds.
     */
    public Event(UUID id, long timestamp) {
        isTrue(id != null, "'id' must be initialized");
        isTrue(timestamp > 0, "'timestamp' must be a positive number: %d", timestamp);

        this.id = id;
        this.timestamp = timestamp;
    }

    /**
     * Returns identifier of event.
     * @return {@code UUID} identifier.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns event creation time (timestamp).
     * @return timestamp in milliseconds.
     */
    public long getTimestamp() {
        return timestamp;
    }
}