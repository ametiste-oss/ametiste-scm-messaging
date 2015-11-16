package org.ametiste.scm.messaging.sender.client.environment;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * {@code Sanitizer} replace values of secret properties with asterisks.
 * Logic partially borrowed from Spring Actuator. See org.springframework.boot.actuate.endpoint.Sanitizer class.
 */
public class Sanitizer {

    public static final String SANITIZE_STRING = "******";

    private static final List<String> REGEX_PARTS = Arrays.asList("*", "$", "^", "+");

    private List<Pattern> keysToSanitize;

    public Sanitizer() {
        setKeysToSanitize("password", "secret", "key", "credentials");
    }

    /**
     * Keys that should be sanitized. Keys can be simple strings that the property ends
     * with or regex expressions.
     * @param keysToSanitize the keys to sanitize
     */
    public void setKeysToSanitize(String... keysToSanitize) {
        isTrue(keysToSanitize != null, "KeysToSanitize must not be null");

        this.keysToSanitize = Arrays.stream(keysToSanitize)
                .map(this::getPattern)
                .collect(Collectors.toList());
    }

    /**
     * Sanitize the given value if necessary.
     * @param key the key to sanitize
     * @param value the value
     * @return the potentially sanitized value
     */
    public Object sanitize(String key, Object value) {
        if (value != null && keysToSanitize.stream().anyMatch(p -> p.matcher(key).matches())) {
            return SANITIZE_STRING;
        }
        return value;
    }

    private Pattern getPattern(String value) {
        if (isRegex(value)) {
            return Pattern.compile(value, Pattern.CASE_INSENSITIVE);
        }
        return Pattern.compile(".*" + value + "$", Pattern.CASE_INSENSITIVE);
    }

    private boolean isRegex(String value) {
        return REGEX_PARTS.stream().anyMatch(value::contains);
    }
}