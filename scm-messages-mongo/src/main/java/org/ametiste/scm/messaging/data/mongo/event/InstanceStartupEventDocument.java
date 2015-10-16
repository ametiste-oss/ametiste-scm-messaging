package org.ametiste.scm.messaging.data.mongo.event;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceStartupEvent;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URI;
import java.util.Map;

@Document
public class InstanceStartupEventDocument extends EventDocument {

    private String instanceId;
    private String version;
    private Map<String, Object> properties;
    private String nodeId;
    private URI uri;

    /**
     * Empty constructor to save ability instantiate {@code InstanceStartupEventDocument} by {@code MongoConverter}.
     */
    public InstanceStartupEventDocument() {
    }

    /**
     * Create {@code InstanceStartupEventDocument} from {@code InstanceStartupEvent} object.
     * @param event target {@code InstanceStartupEvent} object.
     */
    public InstanceStartupEventDocument(InstanceStartupEvent event) {
        super(event);
        this.instanceId = event.getInstanceId();
        this.version = event.getVersion();
        this.properties = event.getProperties();
        this.nodeId = event.getNodeId();
        this.uri = event.getUri();
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
        return InstanceStartupEvent.builder()
                .addId(super.getId())
                .addTimestamp(super.getTimestamp())
                .addInstanceId(instanceId)
                .addVersion(version)
                .addProperties(properties)
                .addNodeId(nodeId)
                .addUri(uri)
                .build();
    }
}
