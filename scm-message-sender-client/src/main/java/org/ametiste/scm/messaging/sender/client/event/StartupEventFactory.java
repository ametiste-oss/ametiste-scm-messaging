package org.ametiste.scm.messaging.sender.client.event;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent;
import org.springframework.util.Assert;

import java.net.URI;
import java.util.Map;

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.*;

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
        Assert.hasText(instanceId, "'instanceId' must be initialized");
        Assert.hasText(version, "'version' must be initialized");
        Assert.notNull(properties, "'properties' must be initialized");

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
