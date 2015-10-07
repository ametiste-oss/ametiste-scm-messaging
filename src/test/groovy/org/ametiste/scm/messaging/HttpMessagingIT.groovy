package org.ametiste.scm.messaging

import org.ametiste.scm.messaging.config.HttpMessagingContext
import org.ametiste.scm.messaging.data.event.Event
import org.ametiste.scm.messaging.data.event.InstanceStartupEvent
import org.ametiste.scm.messaging.data.transport.TransportMessage
import org.ametiste.scm.messaging.mock.Subscriber
import org.ametiste.scm.messaging.sender.EventSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static org.ametiste.scm.messaging.mock.Subscriber.ListenerType.*
import static org.junit.Assert.assertEquals

@ContextConfiguration(classes = HttpMessagingContext.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest("server.port=0")
class HttpMessagingIT extends Specification {

    @Autowired
    private Subscriber subscriber;

    @Autowired
    private EventSender sender;

    @Value('${local.server.port}')
    private int port;

    private URI receiverEndpointURI;

    def setup() {
        receiverEndpointURI = new URI("http://localhost:" + port + "/event-receiver");
    }

    def "service instance send event with configuration info to broker"() {
        given: "event with information about service instance"
        InstanceStartupEvent event = createInstanceStartupEvent();

        when: "pack event to message and send to broker"
        TransportMessage<Event> message = new TransportMessage<>(event);
        sender.send(receiverEndpointURI, message);

        then: "broker receiver message and should publish them"
        assert subscriber.events(TRANSPORT_MESSAGE).size() == 1
        assert subscriber.events(TRANSPORT_MESSAGE_WITH_EVENT).size() == 1

        and: "should publish internal event"
        assert subscriber.events(EVENT).size() == 1
        assert subscriber.events(INSTANCE_STARTUP_EVENT).size() == 1

        and: "received event should be same as original"
        checkEvents(event, (InstanceStartupEvent)subscriber.events(EVENT).get(0))

        cleanup:
        subscriber.clean()
    }

    def "event broker send few events in one message to subscriber"() {
        given: "event with information about service instance"
        InstanceStartupEvent event = createInstanceStartupEvent();

        when: "pack events to messages and send to broker"
        TransportMessage<Event> message = new TransportMessage<>(event);
        sender.send(receiverEndpointURI, Arrays.asList(message, message));

        then: "broker receiver message and should publish them"
        assert subscriber.events(TRANSPORT_MESSAGE).size() == 2
        assert subscriber.events(TRANSPORT_MESSAGE_WITH_EVENT).size() == 2

        and: "internal events should publish too"
        assert subscriber.events(EVENT).size() == 2
        assert subscriber.events(INSTANCE_STARTUP_EVENT).size() == 2

        cleanup:
        subscriber.clean()
    }

    def "sender exclude target from event broadcast"() {
        given: "event with information about service instance"
        InstanceStartupEvent event = createInstanceStartupEvent();

        when: "pack event to message with target exclude and send to broker"
        TransportMessage<Event> message = new TransportMessage<>(event, Collections.singleton(receiverEndpointURI));
        sender.send(receiverEndpointURI, message);

        then: "no event shouldn't receive"
        assert subscriber.events(TRANSPORT_MESSAGE).size() == 0

        cleanup:
        subscriber.clean()
    }

    private static InstanceStartupEvent createInstanceStartupEvent() {
        String instanceId = "DEBS";
        String version = "0.1.5-RELEASE";
        String nodeId = "AWS1.RAIN";
        URI uri = new URI("http://192.168.0.2/endpoint");

        Map<String, Object> properties = new HashMap<>();
        properties.put("dbUrl", new URI("http://localhost:9300/schema"));
        properties.put("retry", 15);

        return InstanceStartupEvent.builder()
                .addInstanceId(instanceId)
                .addVersion(version)
                .addProperties(properties)
                .addNodeId(nodeId)
                .addUri(uri)
                .build();
    }

    private static void checkEvents(InstanceStartupEvent expect, InstanceStartupEvent received) {
        assertEquals(expect.getId(), received.getId())
        assertEquals(expect.getTimestamp(), expect.getTimestamp())
        assertEquals(expect.getInstanceId(), received.getInstanceId())
        assertEquals(expect.getVersion(), received.getVersion())
        assertEquals(expect.getProperties().size(), expect.getProperties().size())
        assertEquals(expect.getNodeId(), received.getNodeId())
        assertEquals(expect.getUri(), received.getUri())
    }
}
