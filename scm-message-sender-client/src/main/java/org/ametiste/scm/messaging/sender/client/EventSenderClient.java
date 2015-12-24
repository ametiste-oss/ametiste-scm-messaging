package org.ametiste.scm.messaging.sender.client;

import org.ametiste.scm.messaging.data.transport.TransportMessage;
import org.ametiste.scm.messaging.sender.EventSendException;
import org.ametiste.scm.messaging.sender.EventSender;
import org.ametiste.scm.messaging.sender.client.event.EventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * {@code EventSenderClient} class provides functionality to send event from service instance. For example, when instance
 * startup, shutdown or configuration of service refreshed.
 * <p>
 * Client takes event from {@code EventFactory} instance that cover all details of event construction. Also client
 * receive target URI and sender instance on construction and this parameters can't be changed later.
 * <p>
 * Client also cover all exceptions during send process. {@code isStrict} parameter defines client behavior in exception
 * handling. If {@code isStrict} is true client falls with exception when during send process any error occurred.
 * If it's false client ignores all error, just log (messages allowed on in debug level).
 */
public class EventSenderClient {

    private final Logger logger = LoggerFactory.getLogger(EventSenderClient.class);

    private final EventFactory factory;
    private final EventSender sender;
    private final URI target;
    private final boolean isStrict;

    /**
     * Create ready for use {@code EventSenderClient} object.
     * @param factory {@code EventFactory} that creates event.
     * @param sender {@code EventSender} instance.
     * @param target URI for send message.
     * @param isStrict flag that define errors processing policy.
     */
    public EventSenderClient(EventFactory factory, EventSender sender, URI target, boolean isStrict) {
        isTrue(factory != null, "'factory' must be initialized");
        isTrue(sender != null, "'sender' must be initialized");
        isTrue(target != null, "'target' must be initialized");

        this.factory = factory;
        this.sender = sender;
        this.target = target;
        this.isStrict = isStrict;
    }

    /**
     * Send event to target service.
     * <p>
     * All operations is logged on debug level. If client work in strict mode and exception catch during send it throw
     * exception in higher level.
     */
    public void send() {
        try {
            sender.send(target, new TransportMessage<>(factory.createEvent()));
        } catch (Exception e) {
            if (isStrict) {
                throw e;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }
}
