package org.ametiste.scm.messaging.sender.client.event;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent;

import java.net.URI;

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.SHUTDOWN;
import static org.apache.commons.lang3.Validate.notBlank;

public class ShutdownEventFactory implements EventFactory {

    private final String instanceId;
    private final String version;
    private final String nodeId;
    private final URI uri;

    public ShutdownEventFactory(String instanceId, String version, String nodeId, URI uri) {
        notBlank(instanceId, "'instanceId' must be initialized and contains text");
        notBlank(version, "'version' must be initialized and contains text");

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
