package org.ametiste.scm.messaging.data.event

import spock.lang.Specification

class EventTest extends Specification {

    def "should correct initialize"() {
        when: "try create event with uninitialized id"
        new DummyEvent(null, System.currentTimeMillis())

        then: "expect throw IllegalArgumentException"
        thrown(IllegalArgumentException.class)

        when: "try create event with uninitialized timestamp"
        new DummyEvent(UUID.randomUUID(), 0)

        then: "expect throw IllegalArgumentException"
        thrown(IllegalArgumentException.class)

        when: "try create event with negative timestamp"
        new DummyEvent(UUID.randomUUID(), -2)

        then: "expect throw IllegalArgumentException"
        thrown(IllegalArgumentException.class)
    }

    def "create correct event with default constructor"() {
        when: "create event with default constructor"
        DummyEvent event = new DummyEvent()

        then: "take correct event instance"
        event.getId() != null
        event.getTimestamp() > 0
    }

    def "create event with specified id"() {
        given: "id"
        UUID id = UUID.randomUUID()

        when: "create event with specified id"
        DummyEvent event = new DummyEvent(id);

        then: "expect get event with correct id"
        id == event.getId()

        and: "initialized timestamp"
        event.getTimestamp() > 0
    }

    def "create event with id and timestamp"() {
        given: "id and timestamp"
        UUID id = UUID.randomUUID()
        long timestamp = System.currentTimeMillis()

        when: "create event with specified data"
        DummyEvent event = new DummyEvent(id, timestamp)

        then: "expect take instance with specified fields"
        event.getId() == id
        event.getTimestamp() == timestamp
    }


    /**
     * {@code Event} is abstract class so for testing we need to extend it.
     */
    public static class DummyEvent extends Event {
        DummyEvent() {
        }

        DummyEvent(UUID id) {
            super(id)
        }

        DummyEvent(UUID id, long timestamp) {
            super(id, timestamp)
        }
    }
}