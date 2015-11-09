package org.ametiste.scm.messaging.data.event;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.validState;

/**
 * {@code InstanceLifecycleEvent} signal about changes in instance life (startup, shutdown, refresh, etc.) cycle and
 * contains all necessary information about instance: id, version, location in system and configuration.
 * <p>
 * To create instance of {@code InstanceLifecycleEvent} use {InstanceLifecycleEvent.Builder}.
 * Required properties is {@code type}, {@code instanceId} and {@code version}. All other are optional and can be {@code null}.
 * <p>
 * By default, event creates with random generated id and current time timestamp value. If necessary create copy of
 * other event or convert from DTO you must set value of both parameters {@code id} and {@code timestamp}.
 * When only one of two parameters initialized builder ignore it.
 */
public class InstanceLifecycleEvent extends Event {

    private final Type type;
    private final String instanceId;
    private final String version;
    private final Map<String, Object> properties;
    private final String nodeId;
    private final URI uri;

    private InstanceLifecycleEvent(Builder builder) {
        super();
        this.type = builder.type;
        this.instanceId = builder.instanceId;
        this.version = builder.version;
        this.properties = Collections.unmodifiableMap(builder.properties);
        this.nodeId = builder.nodeId;
        this.uri = builder.uri;
    }

    private InstanceLifecycleEvent(UUID id, long timestamp, Builder builder) {
        super(id, timestamp);
        this.type = builder.type;
        this.instanceId = builder.instanceId;
        this.version = builder.version;
        this.properties = Collections.unmodifiableMap(builder.properties);
        this.nodeId = builder.nodeId;
        this.uri = builder.uri;
    }

    /**
     * Return instance lifecycle state about that event signals.
     * @return {@code Type} of life cycle event.
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns id of service instance.
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * Returns version of service instance.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns map with configuration properties of service instance.
     * @return {@code Map<String, Object} object. If no properties present return empty map, never {@code null}.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Returns node id of service instance.
     * @return string with node is value. If node id not present returns {@code null}.
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * Returns URI to root path of service instance.
     * @return {@code URI} object. If URI not present returns {@code null}.
     */
    public URI getUri() {
        return uri;
    }

    /**
     * Builder for constructing {@code InstanceLifecycleEvent} objects.
     */
    public static class Builder {

        private Type type;
        private UUID id;
        private long timestamp;
        private String instanceId;
        private String version;
        private Map<String, Object> properties = new HashMap<>();
        private String nodeId;
        private URI uri;

        private Builder() {}

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder instanceId(String id) {
            this.instanceId = id;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder property(String key, Object value) {
            this.properties.put(key, value);
            return this;
        }

        public Builder properties(Map<String, Object> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder nodeId(String id) {
            this.nodeId = id;
            return this;
        }

        public Builder uri(URI uri) {
            this.uri = uri;
            return this;
        }

        /**
         * Build {@code InstanceLifecycleEvent} from contained in builder information.
         * Its required to present type, instanceId and version properties. If one of them absent method execution failed
         * with {@code IllegalStateException} exception.
         */
        public InstanceLifecycleEvent build() {
            validState(type != null, "'type' must be initialized before event creation");
            validState(instanceId != null, "'instanceId' must be initialized before event creation");
            validState(version != null, "'version' must be initialized before event creation");
            validState(!version.isEmpty(), "'version' must be not empty string");

            if (id != null && timestamp > 0) {
                return new InstanceLifecycleEvent(id, timestamp, this);
            } else {
                return new InstanceLifecycleEvent(this);
            }
        }
    }

    /**
     * Create new instance of {@code InstanceStartupEvent.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Enumeration of instance lifecycle states. {@code InstanceLifecycleEvent} describe transition to one of them.
     * <p>
     * Enum contains two values:
     * <ul>
     *     <li>STARTUP - instance of service start;</li>
     *     <li>SHUTDOWN - instance shutdown in some reason.</li>
     * </ul>
     */
    public enum Type { STARTUP, SHUTDOWN }
}