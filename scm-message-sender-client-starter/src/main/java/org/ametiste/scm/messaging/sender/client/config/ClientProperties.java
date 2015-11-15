package org.ametiste.scm.messaging.sender.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Defines set of properties that would be used to define event sender clients behavior.
 * <p>
 * Defined properties are included (org.ametiste.scm.messaging.sender.client.*):
 * <table summary="parameters description">
 *     <tr><td>Name</td><td>Type</td><td>Description</td><td>Default</td></tr>
 *     <tr>
 *         <td>enabled</td>
 *         <td>boolean</td>
 *         <td>Flag that allow include of bootstrap bean in context.</td>
 *         <td>true</td>
 *     </tr>
 *     <tr>
 *         <td>strict</td>
 *         <td>boolean</td>
 *         <td>Flag that specify exception handling policy. If it's true bootstrap throw Exception on error occurred.
 *         If false bootstrap catch Exception and ignore it.</td>
 *         <td>false</td>
 *     </tr>
 *     <tr>
 *         <td>targetUri</td>
 *         <td>URI</td>
 *         <td>URI to send event. If parameter will not be define bootstrap fall on URI creation step.</td>
 *         <td>(not defined)</td>
 *     </tr>
 * </table>
 * <p>
 * In common cases user should specify only one parameter {@code targetUri}. Other parameters may be omitted.
 */
@ConfigurationProperties("org.ametiste.scm.messaging.sender.client")
public class ClientProperties {

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
