package org.ametiste.scm.messaging.data.mongo.event;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent;
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URI;
import java.util.Map;

/**
 * Document model for {@code InstanceLifecycleEvent} subtype.
 */
@Document
public class InstanceLifecycleEventDocument extends EventDocument {

    private Type type;
    private String instanceId;
    private String version;
    private Map<String, Object> properties;
    private String nodeId;
    private URI uri;

    /**
     * Empty constructor to save ability instantiate {@code InstanceStartupEventDocument} by {@code MongoConverter}.
     */
    public InstanceLifecycleEventDocument() {
    }

    /**
     * Create {@code InstanceStartupEventDocument} from {@code InstanceStartupEvent} object.
     * @param event target {@code InstanceStartupEvent} object.
     */
    public InstanceLifecycleEventDocument(InstanceLifecycleEvent event) {
        super(event);
        this.type = event.getType();
        this.instanceId = event.getInstanceId();
        this.version = event.getVersion();
        this.properties = event.getProperties();
        this.nodeId = event.getNodeId();
        this.uri = event.getUri();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
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
