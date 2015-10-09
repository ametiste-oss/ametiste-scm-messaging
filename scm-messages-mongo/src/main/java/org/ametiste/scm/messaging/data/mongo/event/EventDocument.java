package org.ametiste.scm.messaging.data.mongo.event;

import org.ametiste.scm.messaging.data.event.Event;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

/**
 * Document entity for {@code Event} to easier work with MongoDB.
 * <p>
 * {@code EventDocument} contains same fields as correspond Event class.
 */
@Document
public abstract class EventDocument {

    @Id
    private UUID id;
    private long timestamp;

    /**
     * Empty constructor to save ability instantiate {@code EventDocument} by {@code MongoConverter}.
     */
    public EventDocument() {
    }

    /**
     * Create {@code EventDocument} from {@code Event} object.
     * @param event target {@code Event} object.
     */
    public EventDocument(Event event) {
        this.id = event.getId();
        this.timestamp = event.getTimestamp();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Convert document instance to Event object.
     * @return {@literal Event} instance with information from current document entity.
     */
    public abstract Event convert();
}
