package org.ametiste.scm.messaging.sender.client.event;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent;

import java.net.URI;
import java.util.Map;

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.*;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notBlank;

/**
 * {@code StartupInstanceEventFactory} create event ({@code InstanceStartupEvent}) for sending on instance startup.
 */
public class StartupEventFactory implements EventFactory {

    private final String instanceId;
    private final String version;
    private final String nodeId;
    private final URI uri;
    private final Map<String, Object> properties;

    public StartupEventFactory(String instanceId, String version, String nodeId, URI uri, Map<String, Object> properties) {
        notBlank(instanceId, "'instanceId' must be initialized and contains text");
        notBlank(version, "'version' must be initialized and contains text");
        isTrue(properties != null, "'properties' must be initialized");

        this.instanceId = instanceId;
        this.version = version;
        this.nodeId = nodeId;
        this.uri = uri;
        this.properties = properties;
    }

    @Override
    public Event createEvent() {
        return InstanceLifecycleEvent.builder()
                .type(STARTUP)
                .instanceId(instanceId)
                .version(version)
                .properties(properties)
                .nodeId(nodeId)
                .uri(uri)
                .build();
    }
}
