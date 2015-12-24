package org.ametiste.scm.messaging.sender.client.event;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent;

import java.net.URI;

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.SHUTDOWN;
import static org.apache.commons.lang3.Validate.notBlank;

/**
 * {@code ShutdownEventFactory} create {@code InstanceLifecycleEvent} with {@code SHUTDOWN} type.
 * <p>
 * Factory provide all instance based fields without config properties.
 * <p>
 * Factory has set of setter methods for complete event components. On set operation no validation pass. Required
 * fields and constraints check immediately before event creation.
 *
 * @since 0.1.0
 */
public class ShutdownEventFactory implements EventFactory {

    private String instanceId;
    private String version;
    private String nodeId;
    private URI uri;

    /**
     * Default constructor. Create {@code ShutdownEventFactory} object with not initialized fields.
     */
    public ShutdownEventFactory() {}

    /**
     * Create {@code ShutdownEventFactory} instance with specified instance id and version.
     *
     * @param instanceId instance identifier.
     * @param version instance version.
     */
    public ShutdownEventFactory(String instanceId, String version) {
        this.instanceId = instanceId;
        this.version = version;
    }

    /**
     * Create {@code ShutdownEventFactory} instance with all initialized fields.
     *
     * @param instanceId instance identifier.
     * @param version instance version.
     * @param nodeId instance node id.
     * @param uri instance uri for communication.
     */
    public ShutdownEventFactory(String instanceId, String version, String nodeId, URI uri) {
        this.instanceId = instanceId;
        this.version = version;
        this.nodeId = nodeId;
        this.uri = uri;
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
    public Event createEvent() {
        validateState();
        return InstanceLifecycleEvent.builder()
                .type(SHUTDOWN)
                .instanceId(instanceId)
                .version(version)
                .nodeId(nodeId)
                .uri(uri)
                .build();
    }

    /**
     * Validate method check that factory has all required fields and pass all required constrains
     * for correct event creation.
     */
    private void validateState() {
        notBlank(instanceId, "'instanceId' must be initialized and contains text");
        notBlank(version, "'version' must be initialized and contains text");
    }
}
