package org.ametiste.scm.messaging.data.event

import spock.lang.Specification

class InstanceStartupEventTest extends Specification {

    def "builder should throw exception when try build with uninitialized required fields"() {
        given: "InstanceStartupEvent builder"
        InstanceStartupEvent.Builder builder = InstanceStartupEvent.builder();

        when: "try build event without required fields (instanceId and version)"
        builder.build();

        then: "should throw exception"
        thrown IllegalStateException

        when: "add only instanceId field and try build"
        builder.addInstanceId("redgreen.core1").build();

        then: "should throw exception"
        thrown IllegalStateException

        when: "add version field too and try build"
        builder.addVersion("0.2.1-RELEASE")

        then: "shouldn't throw any exception"
        notThrown Exception
    }

    def "builder create minimal event with initialized id and timestamp"() {
        given: "event builder and instance id and version info"
        InstanceStartupEvent.Builder builder = InstanceStartupEvent.builder();
        String instanceId = "redgreen.core1";
        String version = "0.2.1-RELEASE";

        when: "set required fields and build event"
        InstanceStartupEvent event = builder.addInstanceId(instanceId).addVersion(version).build();

        then: "event should have id and timestamp"
        event.id != null
        event.version != null

        and: "same instanceId and version values"
        event.instanceId.equals(instanceId)
        event.version.equals(version)
    }

    def "builder create event with correct info fields"() {
        given: "event builder and instance info"
        InstanceStartupEvent.Builder builder = InstanceStartupEvent.builder();
        UUID id = UUID.randomUUID();
        long timestamp = System.currentTimeMillis();
        String instanceId = "redgreen.core1";
        String version = "0.2.1-RELEASE";
        Map<String, Object> properties = new HashMap<>();
        properties.put("host", "http://localhost:8080");
        properties.put("timeout", 1056);
        String nodeId = "aws1";
        URI uri = new URI("http://localhost:9200");


        when: "add info to builder and build event"
        InstanceStartupEvent event = builder
                .addId(id)
                .addTimestamp(timestamp)
                .addInstanceId(instanceId)
                .addVersion(version)
                .addProperty("retryCount", 5)
                .addProperties(properties)
                .addNodeId(nodeId)
                .addUri(uri)
                .build();
        properties.put("retryCount", 5);

        then: "event should contains same info as initial fields"
        event.getId().equals(id);
        event.getTimestamp() == timestamp;
        event.getInstanceId().equals(instanceId);
        event.getVersion().equals(version);
        event.getProperties().equals(properties);
        event.getNodeId().equals(nodeId);
        event.getUri().equals(uri);
    }
}