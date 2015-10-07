package org.ametiste.scm.messaging.data.transport;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * {@code TransportMessage} used for transmitting some object between components of the system.
 * Class contains all needed service information to transport message.
 * <p>
 * {@code excludes} used to defines list of subscribers that excluded from event broadcast.
 * @param <T> type of transported source.
 */
public class TransportMessage<T> implements Serializable {

    private static final long serialVersionUID = 19L;

    private final T source;
    private final Collection<URI> excludes;

    /**
     * Create transport message without source and excludes.
     */
    public TransportMessage() {
        source = null;
        excludes = Collections.emptyList();
    }

    /**
     * Create transport message with specified source and without excludes.
     * @param source source object for transporting.
     */
    public TransportMessage(T source) {
        this(source, Collections.emptyList());
    }

    /**
     * Create transport massage with specified source and list of excludes.
     * @param source source object for transporting.
     * @param excludes list with URIs of services that must be excluded from event broadcast.
     */
    public TransportMessage(T source, Collection<URI> excludes) {
        isTrue(source != null, "'source' must be initialized");
        isTrue(excludes != null, "'excludes' must be initialized");

        this.source = source;
        this.excludes = excludes;
    }

    /**
     * Return {@code T} object included in message.
     * @return instance of {@code T} type.
     */
    public T getSource() {
        return source;
    }

    /**
     * Return list of excluded subscribers.
     * @return list with URIs of services that must be excluded from event broadcast. Never {@code null}.
     */
    public Collection<URI> getExcludes() {
        return excludes;
    }
}
