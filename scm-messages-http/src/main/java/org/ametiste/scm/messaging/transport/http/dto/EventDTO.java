package org.ametiste.scm.messaging.transport.http.dto;

import org.ametiste.scm.messaging.data.event.Event;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

/**
 * {@code EventDTO} is a abstract class that generalize all events dto for http protocol.
 * <p>
 * Jackson Processor serialize {@code Event} subclasses for transmitting. To recognize which subtype of {@code Event}
 * received parser add to json event object property "@type" with name of {@code Event} subtype.
 * Additional operations and subtypes names defined by Jackson annotations.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes(@JsonSubTypes.Type(value = InstanceLifecycleEventDTO.class, name = "InstanceStartupEventDTO"))
public abstract class EventDTO {

    private final UUID id;
    private final long timestamp;

    public EventDTO() {
        this(null, 0);
    }

    public EventDTO(UUID id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public EventDTO(Event event) {
        this.id = event.getId();
        this.timestamp = event.getTimestamp();
    }

    public UUID getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Convert entire data to corresponding {@code Event} subtype object.
     */
    public abstract Event convert();
}
