package org.ametiste.scm.messaging.sender.client.event;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent;

import java.net.URI;
import java.util.Map;

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.*;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

/**
 * {@code StartupEventFactory} create event ({@code InstanceStartupEvent}) for sending on instance startup.
 * <p>
 * Factory has set of setter methods for complete event components. On set operation no validation pass. Required
 * fields and constraints check immediately before event creation.
 *
 * @since 0.1.0
 */
public class StartupEventFactory implements EventFactory {

    private String instanceId;
    private String version;
    private String nodeId;
    private URI uri;
    private Map<String, Object> properties;

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

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public Event createEvent() {
        validateState();
        return InstanceLifecycleEvent.builder()
                .type(STARTUP)
                .instanceId(instanceId)
                .version(version)
                .properties(properties)
                .nodeId(nodeId)
                .uri(uri)
                .build();
    }

    private void validateState() {
        notBlank(instanceId, "'instanceId' must be initialized and contains text");
        notBlank(version, "'version' must be initialized and contains text");
        isTrue(properties != null, "'properties' must be initialized");
    }
}
