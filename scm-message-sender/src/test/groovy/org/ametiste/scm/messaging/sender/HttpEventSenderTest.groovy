package org.ametiste.scm.messaging.sender

import org.ametiste.scm.messaging.data.event.Event
import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import org.ametiste.scm.messaging.data.transport.TransportMessage
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.ametiste.scm.messaging.transport.http.dto.factory.DefaultEventToConverterMapFactory
import org.ametiste.scm.messaging.transport.http.dto.factory.EventDTOFactory
import org.apache.http.client.HttpClient
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import spock.lang.Specification

import java.util.stream.Collectors

import static java.util.Collections.singletonList
import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.SHUTDOWN
import static org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent.Type.STARTUP

class HttpEventSenderTest extends Specification {

    private HttpClient client;
    private HttpEventSender sender;
    private URI target;
    private List<Event> events;

    def setup() {
        client = Mock(CloseableHttpClient.class);
        sender = new HttpEventSender(client);

        target = new URI("http://localhost:9200/event-receiver");
        events = new ArrayList<>();
        events.add(InstanceLifecycleEvent.builder().type(STARTUP).instanceId("DSF").version("0.2.6").build());
        events.add(InstanceLifecycleEvent.builder().type(SHUTDOWN).instanceId("DIE").version("1.0.5").build());
    }

    def "initialization checks"() {
        when: "try create sender with not initialized http client"
        new HttpEventSender(null, )

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)

        when: "try create sender with not initialized converter map"
        new HttpEventSender(client, null)

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)

        when: "try create sender with correct parameters"
        new HttpEventSender(client, new DefaultEventToConverterMapFactory().getMap())

        then: "any exception not thrown"
        notThrown(IllegalArgumentException.class)
    }

    def "sender should send correct event request"() {
        when:
        sender.send(target, new TransportMessage<Event>(events.get(0)));

        then:
        client.execute(_, _) >> { HttpPost request, ResponseHandler<String> handler ->
            StringEntity entity = request.getEntity();

            assert target == request.getURI();
            assert entity.getContentType().getValue().contains("application/json");

            JsonNode json = new ObjectMapper().readTree(entity.getContent());
            assert json.isArray()
            assert json.elements().size() == 1
        }
    }

    def "sender should send correct event list request"() {
        when:
        sender.send(target, events.stream().map({ e -> new TransportMessage<>(e) }).collect(Collectors.toList()));

        then:
        client.execute(_, _) >> { HttpPost request, ResponseHandler<String> handler ->
            StringEntity entity = request.getEntity();

            assert target == request.getURI();
            assert entity.getContentType().getValue().contains("application/json");

            JsonNode json = new ObjectMapper().readTree(entity.getContent());
            assert json.isArray()
            assert json.elements().size() == 2
        }
    }

    def "sender shouldn't send empty pack of messages"() {
        given: "list of messages with target in excludes"
        List<TransportMessage<Event>> messages = [new TransportMessage<Event>(events[0], singletonList(target)),
                                                new TransportMessage<Event>(events[1], singletonList(target))]

        when: "send messages to target"
        sender.send(target, messages);

        then: "client not invoked"
        0 * client.execute(_, _)
    }

    def "create http client with correct parameters"() {
        given: "timeouts"
        def readTimeout = 1000;
        def connectTimeout = 300;

        when: "create client with with parameters"
        CloseableHttpClient client = HttpEventSender.createHttpClient(connectTimeout, readTimeout)

        then: "expect client is correct"
        client != null
    }

    def "sender exception on client error"() {
        when: "send messages"
        sender.send(target, new TransportMessage<Event>(events[0]))

        then: "client throw exception"
        client.execute(_, _) >> { throw new IOException("error on send"); }

        and: "expect exception intercepted and EventSendException thrown"
        thrown(EventSendException.class)
    }

    def "sender exception on message serialising"() {

    }

    def "sender exception on converter missing"() {
        given: "sender with empty converters map"
        HttpEventSender eventSender = new HttpEventSender(client, Collections.emptyMap())

        when: "try send message"
        eventSender.send(target, new TransportMessage<Event>(events[0]))

        then: "expect EventSendException thrown"
        thrown(EventSendException.class)
    }
}
