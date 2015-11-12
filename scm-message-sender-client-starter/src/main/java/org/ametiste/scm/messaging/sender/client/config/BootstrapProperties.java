package org.ametiste.scm.messaging.sender.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Defines set of properties that would be used to define event sender bootstrap behavior.
 * <p>
 * Defined properties are included (org.ametiste.scm.messaging.sender.bootstrap.*):
 * <ul>
 * <li>enabled - boolean flag that allow include of bootstrap bean in context (default is true).</li>
 * <li>strict - boolean flag that specify exception handling policy. If it's true bootstrap throw Exception on error
 * occurred. If false bootstrap catch Exception and ignore it. Default is false.</li>
 * <li>targetUri - URI to send event. Default value not specified. If parameter will not be define bootstrap fall
 * on URI creation step.</li>
 * </ul>
 * <p>
 * In common cases user should specify only one parameter {@code targetUri}. Other parameters may be omitted.
 */
@ConfigurationProperties("org.ametiste.scm.messaging.sender.bootstrap")
public class BootstrapProperties {

    private boolean enabled = true;
    private boolean strict = false;
    private String targetUri;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isStrict() {
        return strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public String getTargetUri() {
        return targetUri;
    }

    public void setTargetUri(String targetUri) {
        this.targetUri = targetUri;
    }
}
