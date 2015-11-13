package org.ametiste.scm.messaging.sender.client;

import org.ametiste.scm.messaging.data.transport.TransportMessage;
import org.ametiste.scm.messaging.sender.EventSendException;
import org.ametiste.scm.messaging.sender.EventSender;
import org.ametiste.scm.messaging.sender.client.event.EventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.net.URI;

/**
 * {@code EventSenderBootstrap} class provides functionality to send event on service instance startup.
 * {@code isStrict} parameter defines object behavior in exception handling. If {@code isStrict} is true bootstrap
 * falls with exception when during send process any error occurred. If is's false object ignores all error, just log.
 */
// TODO think about naming after shutdown event appear. Now it's not only bootstrap worker. May be rename to EventSenderClient.
public class EventSenderBootstrap {

    private final Logger logger = LoggerFactory.getLogger(EventSenderBootstrap.class);

    private final EventFactory factory;
    private final EventSender sender;
    private final URI target;
    private final boolean isStrict;

    /**
     * Create ready for use {@code EventSenderBootstrap} obejct.
     * @param factory {@code EventFactory} that creates startup event.
     * @param sender {@code EventSender} instance.
     * @param target URI for send message.
     * @param isStrict flag that define errors processing policy.
     */
    public EventSenderBootstrap(EventFactory factory, EventSender sender, URI target, boolean isStrict) {
        Assert.notNull(factory);
        Assert.notNull(sender);
        Assert.notNull(target);

        this.factory = factory;
        this.sender = sender;
        this.target = target;
        this.isStrict = isStrict;
    }

    /**
     * Send event to target service.
     */
    public void send() {
        try {
            sender.send(target, new TransportMessage<>(factory.createEvent()));
            logger.debug("Startup event success sent");
        } catch (EventSendException e) {
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
