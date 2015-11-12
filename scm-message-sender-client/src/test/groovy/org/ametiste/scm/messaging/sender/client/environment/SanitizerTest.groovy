package org.ametiste.scm.messaging.sender.client.environment

import spock.lang.Specification

class SanitizerTest extends Specification {

    private Sanitizer sanitizer;

    def setup() {
        sanitizer = new Sanitizer();
    }

    def "pattern as sanitize key correct work"() {
        given: "sanitizer with pattern key"
        sanitizer.setKeysToSanitize("^[0-9]*")

        expect:
        sanitized == sanitizer.sanitize(key, value)

        where:
        key     | value         | sanitized
        "4"     | "localhost"   | Sanitizer.SANITIZE_STRING
        "566"   | "pass"        | Sanitizer.SANITIZE_STRING
        "host"  | "localhost"   | "localhost"
        "555"   | null          | null
    }

    def "sanitizer with default set of keys correct work"() {
        expect:
        sanitized == sanitizer.sanitize(key, value)

        where:
        key             | value         | sanitized
        "password"      | "localhost"   | Sanitizer.SANITIZE_STRING
        "secret"        | "pass"        | Sanitizer.SANITIZE_STRING
        "key"           | "localhost"   | Sanitizer.SANITIZE_STRING
        "credentials"   | "asfgds"      | Sanitizer.SANITIZE_STRING
        "credentials"   | null          | null
        "foo"           | "goofs"       | "goofs"
    }
}
