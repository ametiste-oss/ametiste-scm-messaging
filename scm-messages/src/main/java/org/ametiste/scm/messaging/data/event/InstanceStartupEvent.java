package org.ametiste.scm.messaging.data.event;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.validState;

/**
 * {@code InstanceStartupEvent} signal about service instance startup and contains information about
 * instance and its configuration.
 * <p>
 * To create instance of {@code InstanceStartupEvent} use {InstanceStartupEvent.Builder}.
 * Required properties is {@code instanceId} and {@code version}. All other are optional and can be {@code null}.
 * <p>
 * By default, event creates with random generated id and current time timestamp value. If necessary create copy of
 * other event or convert from DTO you must set value of both parameters {#code id} and {@code timestamp}.
 * When only one of two parameters initialized builder ignore it.
 */
public class InstanceStartupEvent extends Event {

    private final String instanceId;
    private final String version;
    private final Map<String, Object> properties;
    private final String nodeId;
    private final URI uri;

    private InstanceStartupEvent(Builder builder) {
        super();
        this.instanceId = builder.instanceId;
        this.version = builder.version;
        this.properties = Collections.unmodifiableMap(builder.properties);
        this.nodeId = builder.nodeId;
        this.uri = builder.uri;
    }

    private InstanceStartupEvent(UUID id, long timestamp, Builder builder) {
        super(id, timestamp);
        this.instanceId = builder.instanceId;
        this.version = builder.version;
        this.properties = Collections.unmodifiableMap(builder.properties);
        this.nodeId = builder.nodeId;
        this.uri = builder.uri;
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
     * Builder for constructing {@code InstanceStartupEvent} objects.
     */
    public static class Builder {

        private UUID id;
        private long timestamp;
        private String instanceId;
        private String version;
        private Map<String, Object> properties = new HashMap<>();
        private String nodeId;
        private URI uri;

        private Builder() {}

        public Builder addId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder addTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder addInstanceId(String id) {
            this.instanceId = id;
            return this;
        }

        public Builder addVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder addProperty(String key, Object value) {
            this.properties.put(key, value);
            return this;
        }

        public Builder addProperties(Map<String, Object> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder addNodeId(String id) {
            this.nodeId = id;
            return this;
        }

        public Builder addUri(URI uri) {
            this.uri = uri;
            return this;
        }

        /**
         * Build {@code InstanceStartupEvent} from contained in builder information.
         * Its required to present instanceId and version properties. If one of them absent method execution failed
         * with {@code IllegalStateException} exception.
         */
        public InstanceStartupEvent build() {
            validState(instanceId != null, "'instanceId' must be initialized before event creation");
            validState(version != null, "'version' must be initialized before event creation");
            validState(!version.isEmpty(), "'version' must be not empty string");

            if (id != null && timestamp > 0) {
                return new InstanceStartupEvent(id, timestamp, this);
            } else {
                return new InstanceStartupEvent(this);
            }
        }
    }

    /**
     * Create new instance of {@code InstanceStartupEvent.Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }
}