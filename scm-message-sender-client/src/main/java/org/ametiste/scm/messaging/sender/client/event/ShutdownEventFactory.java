package org.ametiste.scm.messaging.sender.client.event;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent;
import org.springframework.util.Assert;

import java.net.URI;

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.SHUTDOWN;

public class ShutdownEventFactory implements EventFactory {

    private final String instanceId;
    private final String version;
    private final String nodeId;
    private final URI uri;

    public ShutdownEventFactory(String instanceId, String version, String nodeId, URI uri) {
        Assert.hasText(instanceId, "'instanceId' must be initialized");
        Assert.hasText(version, "'version' must be initialized");

        this.instanceId = instanceId;
        this.version = version;
        this.nodeId = nodeId;
        this.uri = uri;
    }

    @Override
    public Event createEvent() {
        return InstanceLifecycleEvent.builder()
                .type(SHUTDOWN)
                .instanceId(instanceId)
                .version(version)
                .nodeId(nodeId)
                .uri(uri)
                .build();
    }
}
