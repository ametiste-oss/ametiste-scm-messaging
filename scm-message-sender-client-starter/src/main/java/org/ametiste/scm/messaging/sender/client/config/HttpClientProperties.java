package org.ametiste.scm.messaging.sender.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Defines set of properties that would be used to configure {@code HttpClient} instance for {@code EventSender}.
 * <p>
 * Defined properties are included (org.ametiste.scm.messaging.sender.http-client.*):
 * <table summary="parameters description">
 *     <tr><td>Name</td><td>Type</td><td>Description</td><td>Default</td></tr>
 *     <tr>
 *         <td>connect-timeout</td>
 *         <td>int</td>
 *         <td>Connection timeout for send event request (in milliseconds).</td>
 *         <td>1000</td>
 *     </tr>
 *     <tr>
 *         <td>read-timeout</td>
 *         <td>int</td>
 *         <td>Read timeout for send event request (in milliseconds).</td>
 *         <td>1000</td>
 *     </tr>
 * </table>
 * <p>
 * This properties is designed provide usable default configuration that provides correct timeout values.
 */
@ConfigurationProperties("org.ametiste.scm.messaging.sender.http-client")
public class HttpClientProperties {

    private int connectTimeout = 1000;
    private int readTimeout = 1000;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}