package org.ametiste.scm.messaging.sender.client

import org.ametiste.scm.messaging.sender.EventSendException
import org.ametiste.scm.messaging.sender.EventSender
import org.ametiste.scm.messaging.sender.HttpEventSender
import org.ametiste.scm.messaging.sender.client.event.EventFactory
import org.ametiste.scm.messaging.sender.client.event.ShutdownEventFactory
import spock.lang.Specification

class EventSenderBootstrapTest extends Specification {

    def EventSender senderMock;
    def URI uri;
    def EventFactory shutdownFactory;

    def setup() {
        senderMock = Mock(EventSender.class)
        uri = URI.create("http://localhost:8085")
        shutdownFactory = new ShutdownEventFactory("GTH", "0.2.1", "AWS", new URI("http://localhost"))
    }

    def "initialization checks"() {
        when: "try create bootstrap with not initialized factory"
        new EventSenderBootstrap(null, Mock(EventSender.class),uri, true)

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)

        when: "try create bootstrap with not initialized sender"
        new EventSenderBootstrap(Mock(EventFactory.class), null, uri, true)

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)

        when: "try create bootstrap with not initialized target"
        new EventSenderBootstrap(Mock(EventFactory.class), Mock(EventSender.class), null, true)

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)
    }

    def "check strict mode"() {
        given: "bootstrap with mocked sender"
        EventSenderBootstrap strictBootstrap = new EventSenderBootstrap(shutdownFactory, senderMock, uri, true)

        when: "send message"
        strictBootstrap.send()

        then: "sender throw exception"
        senderMock.send(_, _) >> { throw new EventSendException("error") }

        and: "expect exception thrown"
        thrown(Exception.class)
    }

    def "check not strict mode"() {
        given: "bootstrap with mocked sender"
        EventSenderBootstrap notStrictBootstrap = new EventSenderBootstrap(shutdownFactory, senderMock, uri, false)

        when: "send message"
        notStrictBootstrap.send()

        then: "sender throw exception"
        senderMock.send(_, _) >> { throw new EventSendException("error") }

        and: "expect no exception thrown"
        noExceptionThrown()
    }
}
