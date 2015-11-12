package org.ametiste.scm.messaging.data.transport

import spock.lang.Specification

class TransportMessageTest extends Specification {

    def "initialization checks"() {
        when: "try craete message with not initialized source"
        new TransportMessage(null, Collections.emptyList())

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)

        when: "try create message with not initialized exclude list"
        new TransportMessage(new Object(), null)

        then: "expect IllegalArgumentException thrown"
        thrown(IllegalArgumentException.class)

        when: "create message with initialized arguments"
        new TransportMessage(new Object(), Collections.emptyList())

        then: "expect no exception thrown"
        notThrown(Exception.class)
    }

    def "create default empty message"() {
        given: "default message"
        TransportMessage message = new TransportMessage();

        expect: "correspond property values"
        message.getSource() == null
        message.getExcludes() != null
        message.getExcludes().isEmpty()
    }

    def "create message with specified source"() {
        given: "some source object"
        String source = "test source";

        when: "create message with source only"
        TransportMessage<String> message = new TransportMessage<>(source);

        then: "expect get same source"
        message.getSource().equals(source)

        and: "empty excludes list"
        message.getExcludes().isEmpty()
    }

    def "create correct message with source and excludes"() {
        given: "some sources and excludes"
        String source = "source test"
        List<URI> excludes = [new URI("http://localhost"), new URI("http://foo.com")]

        when: "create message with source and excludes"
        TransportMessage<String> message = new TransportMessage<>(source, excludes)

        then: "expect correct properties"
        message.getSource().equals(source)
        message.getExcludes().size() == excludes.size()
        message.getExcludes().containsAll(excludes)
    }
}
