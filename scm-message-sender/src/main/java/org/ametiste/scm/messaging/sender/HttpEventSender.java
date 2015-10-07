package org.ametiste.scm.messaging.sender;

import org.ametiste.scm.messaging.data.event.Event;
import org.ametiste.scm.messaging.data.transport.TransportMessage;
import org.ametiste.scm.messaging.transport.http.dto.EventDTO;
import org.ametiste.scm.messaging.transport.http.dto.EventDTOMessage;
import org.ametiste.scm.messaging.transport.http.dto.factory.DefaultEventToConverterMapFactory;
import org.ametiste.scm.messaging.transport.http.dto.factory.EventDTOFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * {@code HttpEventSender} is a default implementation of {EventSender} interface that send messages via HTTP protocol.
 * <p>
 * Sender sent messages as array of objects. If we send one event, sender automatically wrap it.
 */
public class HttpEventSender implements EventSender {

    private static final ResponseHandler RESPONSE_HANDLER = new BasicResponseHandler();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final HttpClient client;
    private final Map<Class, EventDTOFactory> eventConverterMap;

    /**
     * Create {@code HttpEventSender} with specified {@code HttpClient} instance and default event class to
     * {@code EventDTOFactory} map.
     * @param client Apache HttpClient instance.
     */
    public HttpEventSender(HttpClient client) {
        this(client, new DefaultEventToConverterMapFactory().getMap());
    }

    /**
     * Create {@code HttpEventSender} with specified {@code HttpClient} instance and event class to
     * {@code EventDTOFactory} map.
     * <p>
     * For creating HttpClient you can use static method {@code createHttpClient}.
     * @param client Apache HttpClient instance.
     * @param eventConverterMap event class to {@code EventDTOFactory} map.
     */
    public HttpEventSender(HttpClient client, Map<Class, EventDTOFactory> eventConverterMap) {
        isTrue(client != null, "'client' must be initialized");
        isTrue(eventConverterMap != null, "'convertersMapFactory' must be initialized");

        this.client = client;
        this.eventConverterMap = eventConverterMap;
    }

    @Override
    public void send(URI receiver, TransportMessage<Event> message) throws EventSendException {
        send(receiver, Collections.singleton(message));
    }

    @Override
    public void send(URI receiver, Collection<TransportMessage<Event>> messages) throws EventSendException {
        List<EventDTOMessage> bulk = messages.stream()
                .filter(eventMessage -> !eventMessage.getExcludes().contains(receiver))
                .map(m -> new EventDTOMessage(mapToDto(m.getSource()), m.getExcludes()))
                .collect(Collectors.toList());

        if (!bulk.isEmpty()) {
            execute(receiver, bulk);
        }
    }

    /**
     * Create {@code CloseableHttpClient} with custom request configuration (timeouts).
     * @param connectionTimeout connection timeout in milliseconds.
     * @param readTimeout read timeout in milliseconds.
     */
    public static CloseableHttpClient createHttpClient(int connectionTimeout, int readTimeout) {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setSocketTimeout(readTimeout)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();
    }

    private void execute(URI receiver, Object message) throws EventSendException {
        HttpPost request = new HttpPost(receiver);
        request.setEntity(new StringEntity(serialize(message), ContentType.APPLICATION_JSON));
        try {
            client.execute(request, RESPONSE_HANDLER);
        } catch (IOException e) {
            throw new EventSendException(e.getMessage(), e);
        }
    }

    private String serialize(Object message) throws EventSendException {
        try {
            return MAPPER.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new EventSendException(e.getMessage(), e);
        }
    }

    private EventDTO mapToDto(Event event) throws EventSendException {
        if (!eventConverterMap.containsKey(event.getClass())) {
            throw new EventSendException("Can't map event to DTO. Not registered event type.");
        }

        return eventConverterMap.get(event.getClass()).createDTO(event);
    }
}
