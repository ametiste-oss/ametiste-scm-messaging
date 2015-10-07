package org.ametiste.scm.messaging.sender

import org.ametiste.scm.messaging.data.event.Event
import org.ametiste.scm.messaging.data.event.InstanceStartupEvent
import org.ametiste.scm.messaging.data.transport.TransportMessage
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.HttpClient
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import spock.lang.Specification

import java.util.stream.Collectors

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
        events.add(InstanceStartupEvent.builder().addInstanceId("DSF").addVersion("0.2.6").build());
        events.add(InstanceStartupEvent.builder().addInstanceId("DIE").addVersion("1.0.5").build());
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
}
