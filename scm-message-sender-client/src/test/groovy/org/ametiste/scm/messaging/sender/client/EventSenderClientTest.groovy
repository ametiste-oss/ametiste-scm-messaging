package org.ametiste.scm.messaging.sender.client

import org.ametiste.scm.messaging.data.transport.TransportMessage
import org.ametiste.scm.messaging.sender.EventSendException
import org.ametiste.scm.messaging.sender.EventSender
import org.ametiste.scm.messaging.sender.client.event.EventFactory
import org.ametiste.scm.messaging.sender.client.event.ShutdownEventFactory
import spock.lang.Specification

class EventSenderClientTest extends Specification {

    def EventSender senderMock;
    def URI uri;
    def EventFactory shutdownFactory;

    def setup() {
        senderMock = Mock(EventSender.class)
        uri = URI.create("http://localhost:8085")
        shutdownFactory = new ShutdownEventFactory("GTH", "0.2.1", "AWS", new URI("http://localhost"))
    }

    def "initialization checks"() {
        when: "try create client with not initialized factory"
        new EventSenderClient(null, senderMock, uri, true)

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)

        when: "try create client with not initialized sender"
        new EventSenderClient(shutdownFactory, null, uri, true)

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)

        when: "try create client with not initialized target"
        new EventSenderClient(shutdownFactory, senderMock, null, true)

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)
    }

    def "common send without exceptions"() {
        given: "client sender instance"
        EventSenderClient client = new EventSenderClient(shutdownFactory, senderMock, uri, true)

        when: "send message"
        client.send()

        then: "expect send correct"
        1 * senderMock.send(_ as URI, _ as TransportMessage) >> { target, TransportMessage message ->
            assert target == uri
            assert compare(message.getSource(), shutdownFactory.createEvent())
        }

        and: "no exception thrown"
        noExceptionThrown()
    }

    def "check strict mode"() {
        given: "bootstrap with mocked sender"
        EventSenderClient strictClient = new EventSenderClient(shutdownFactory, senderMock, uri, true)

        when: "send message"
        strictClient.send()

        then: "sender throw exception"
        senderMock.send(_, _) >> { throw new EventSendException("error") }

        and: "expect exception thrown"
        thrown(Exception.class)
    }

    def "check not strict mode"() {
        given: "bootstrap with mocked sender"
        EventSenderClient notStrictClient = new EventSenderClient(shutdownFactory, senderMock, uri, false)

        when: "send message"
        notStrictClient.send()

        then: "sender throw exception"
        senderMock.send(_, _) >> { throw new EventSendException("error") }

        and: "expect no exception thrown"
        noExceptionThrown()
    }

    private static boolean compare(etalon, tested) {
        etalon.getType().equals(tested.getType());
        etalon.getId().equals(tested.getId());
        etalon.getTimestamp() == tested.getTimestamp();
        etalon.getInstanceId().equals(tested.getInstanceId());
        etalon.getVersion().equals(tested.getVersion());
        etalon.getProperties().equals(tested.getProperties());
        etalon.getNodeId().equals(tested.getNodeId());
        etalon.getUri().equals(tested.getUri());
    }
}
