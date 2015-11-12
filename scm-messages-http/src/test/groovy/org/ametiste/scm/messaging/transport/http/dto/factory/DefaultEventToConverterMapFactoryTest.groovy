package org.ametiste.scm.messaging.transport.http.dto.factory

import org.ametiste.scm.messaging.data.event.InstanceLifecycleEvent
import spock.lang.Specification

class DefaultEventToConverterMapFactoryTest extends Specification {

    def "should return correct map"() {
        given: "EventToConverterMapFactory instance"
        EventToConverterMapFactory factory = new DefaultEventToConverterMapFactory();

        when: "request map from factory"
        Map<Class, EventDTOFactory> map = factory.getMap()

        then: "expect take correct map"
        map != null
        !map.isEmpty()
        map.containsKey(InstanceLifecycleEvent.class)
    }
}
