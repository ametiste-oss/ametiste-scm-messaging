package org.ametiste.scm.messaging.transport.http.dto;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.lang3.Validate.validState;

/**
 * DTO for {@code InstanceLifecycleEvent} class.
 */
public class InstanceLifecycleEventDTO extends EventDTO {

    private final InstanceLifecycleEvent.Type type;
    private final String instanceId;
    private final String version;
    private final Map<String, Object> properties;
    private final String nodeId;
    private final URI uri;

    private InstanceLifecycleEventDTO(Builder builder) {
        super(builder.id, builder.timestamp);
        this.type = builder.type;
        this.instanceId = builder.instanceId;
        this.version = builder.version;
        this.properties = Collections.unmodifiableMap(builder.properties);
        this.nodeId = builder.nodeId;
        this.uri = builder.uri;
    }

    /**
     * Default constructor that creates empty event. Adding for Jackson Processor works.
     */
    public InstanceLifecycleEventDTO() {
        super();
        type = null;
        instanceId = null;
        version = null;
        properties = Collections.emptyMap();
        nodeId = null;
        uri = null;
    }

    /**
     * Create DTO from {@code InstanceStartupEvent} object.
     */
    public InstanceLifecycleEventDTO(InstanceLifecycleEvent event) {
        super(event);
        this.type = event.getType();
        this.instanceId = event.getInstanceId();
        this.version = event.getVersion();
        this.properties = event.getProperties();
        this.nodeId = event.getNodeId();
        this.uri = event.getUri();
    }

    public InstanceLifecycleEvent.Type getType() {
        return type;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getNodeId() {
        return nodeId;
    }

    public URI getUri() {
        return uri;
    }

    /**
     * Builder for constructing {@code InstanceStartupEventDTO} objects.
     * Used by Jackson Processor to deserialize DTO.
     */
    @JsonPOJOBuilder(withPrefix = "add")
    public static class Builder {

        private InstanceLifecycleEvent.Type type;
        private UUID id;
        private long timestamp;
        private String instanceId;
        private String version;
        private Map<String, Object> properties = new HashMap<>();
        private String nodeId;
        private URI uri;

        public Builder addType(InstanceLifecycleEvent.Type type) {
            this.type = type;
            return this;
        }

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

        public InstanceLifecycleEventDTO build() {
            validState(type != null, "'type' must be initialized before DTO creation");
            validState(id != null, "'id' must be initialized before DTO creation");
            validState(timestamp > 0, "'timestamp' must be initialized before DTO creation");
            validState(instanceId != null, "'instanceId' must be initialized before DTO creation");
            validState(version != null, "'version' must be initialized before DTO creation");
            validState(!version.isEmpty(), "'version' must be not empty string");

            return new InstanceLifecycleEventDTO(this);
        }
    }

    @Override
    public Event convert() {
        return InstanceLifecycleEvent.builder()
                .type(type)
                .id(super.getId())
                .timestamp(super.getTimestamp())
                .instanceId(instanceId)
                .version(version)
                .properties(properties)
                .nodeId(nodeId)
                .uri(uri)
                .build();
    }
}
