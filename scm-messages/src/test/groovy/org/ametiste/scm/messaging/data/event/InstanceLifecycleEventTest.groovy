package org.ametiste.scm.messaging.data.event

import spock.lang.Specification

import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.*
import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.*

class InstanceLifecycleEventTest extends Specification {

    def "should fail on build with uninitialized required fields"() {
        given: "InstanceStartupEvent builder"
        Builder builder = builder();

        when: "try build event without required fields"
        builder.build();

        then: "expect exception thrown"
        thrown IllegalStateException

        when: "add only type of event and try build"
        builder.type(STARTUP).build()

        then: "expect exception thrown"
        thrown IllegalStateException

        when: "add instanceId field and try build"
        builder.instanceId("redgreen.core1").build();

        then: "expect exception thrown"
        thrown IllegalStateException

        when: "add version field too and try build"
        builder.version("0.2.1-RELEASE")

        then: "shouldn't throw any exception"
        notThrown Exception
    }

    def "should ignore on incorrect parameters for Event"() {

        when: "create event with null id"
        InstanceLifecycleEvent event = builder().type(SHUTDOWN)
            .id(null)
            .timestamp(new Date().getTime())
            .instanceId("rabbot")
            .version("0.2.1")
            .build();

        then: "expect ignore invalid fields"
        notThrown Exception

        and: "build event with default values"
        event.instanceId.equals("rabbot")
        event.version.equals("0.2.1")
        event.id != null
        event.timestamp > 0

        when: "create event with invalid timestamp"
        event = builder().type(SHUTDOWN)
                .id(UUID.randomUUID())
                .timestamp(-2)
                .instanceId("rabbot")
                .version("0.2.1")
                .build();

        then: "expect ignore invalid fields"
        notThrown Exception

        and: "build event with default values"
        event.instanceId.equals("rabbot")
        event.version.equals("0.2.1")
        event.id != null
        event.timestamp > 0
    }

    def "builder create minimal event with initialized id and timestamp"() {
        given: "event builder and instance id and version info"
        Builder builder = builder();
        Type type = SHUTDOWN;
        String instanceId = "redgreen.core1";
        String version = "0.2.1-RELEASE";

        when: "set required fields and build event"
        InstanceLifecycleEvent event = builder.type(type).instanceId(instanceId).version(version).build();

        then: "event should have type, id and timestamp"
        event.type != null
        event.id != null
        event.version != null

        and: "same values for required fields"
        event.type.equals(type)
        event.instanceId.equals(instanceId)
        event.version.equals(version)
    }

    def "builder create event with correct info fields"() {
        given: "event builder and instance info"
        Builder builder = builder();
        UUID id = UUID.randomUUID();
        long timestamp = System.currentTimeMillis();
        Type type = STARTUP;
        String instanceId = "redgreen.core1";
        String version = "0.2.1-RELEASE";
        Map<String, Object> properties = new HashMap<>();
        properties.put("host", "http://localhost:8080");
        properties.put("timeout", 1056);
        String nodeId = "aws1";
        URI uri = new URI("http://localhost:9200");


        when: "add info to builder and build event"
        InstanceLifecycleEvent event = builder
                .type(type)
                .id(id)
                .timestamp(timestamp)
                .instanceId(instanceId)
                .version(version)
                .property("retryCount", 5)
                .properties(properties)
                .nodeId(nodeId)
                .uri(uri)
                .build();
        properties.put("retryCount", 5);

        then: "event should contains same info as initial fields"
        event.getType().equals(type);
        event.getId().equals(id);
        event.getTimestamp() == timestamp;
        event.getInstanceId().equals(instanceId);
        event.getVersion().equals(version);
        event.getProperties().equals(properties);
        event.getNodeId().equals(nodeId);
        event.getUri().equals(uri);
    }
}