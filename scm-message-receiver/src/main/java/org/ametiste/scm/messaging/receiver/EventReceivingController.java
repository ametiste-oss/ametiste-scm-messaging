package org.ametiste.scm.messaging.receiver;

import org.ametiste.scm.messaging.data.transport.EventTransportMessage;
import org.ametiste.scm.messaging.data.transport.TransportMessage;
import org.ametiste.scm.messaging.transport.http.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * {@code EventReceivingController} receive transport messages with event DTOs and transform them to {@code Event} and
 * {@code TransportMessage<Event>} objects.
 * <p>
 * Transformed objects publish with Spring {@code ApplicationEventPublisher}. Listeners can handle all {@code Event}
 * objects, concrete subtype of Event or TransportMessage. Last case useful for routers, brokers and other transport
 * services.
 * <p>
 * Controller process POST requests to "event-receiver" path with {@literal application-json} content type. When
 * messages processing success controller return {@literal ACCEPTED 202} status code.
 */
@RestController
public class EventReceivingController {

    private final ApplicationEventPublisher publisher;

    @Autowired
    public EventReceivingController(ApplicationEventPublisher publisher) {
        Assert.notNull(publisher, "'publisher' must be initialized");

        this.publisher = publisher;
    }

    @RequestMapping(value = "/event-receiver", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void receiveEvents(@RequestBody Collection<TransportMessage<EventDTO>> events) {
        events.stream()
                .map(m -> new EventTransportMessage(m.getSource().convert(), m.getExcludes()))
                .forEach(publisher::publishEvent);

        events.stream().map(TransportMessage::getSource).map(EventDTO::convert).forEach(publisher::publishEvent);
    }
}
