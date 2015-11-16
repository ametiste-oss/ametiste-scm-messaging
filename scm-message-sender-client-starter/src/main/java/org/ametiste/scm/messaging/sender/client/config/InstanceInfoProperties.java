package org.ametiste.scm.messaging.sender.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Defines set of properties that would be used to define instance information.
 * <p>
 * Defined properties are included (org.ametiste.scm.instance.*):
 * <table summary="parameters description">
 *     <tr><td>Name</td><td>Type</td><td>Description</td><td>Default</td></tr>
 *     <tr>
 *         <td>instanceId</td>
 *         <td>String</td>
 *         <td>Unique identifier for service instance (required).</td>
 *         <td>(not defined)</td>
 *     </tr>
 *     <tr>
 *         <td>version</td>
 *         <td>String</td>
 *         <td>Version of service (required).</td>
 *         <td>(not defined)</td>
 *     </tr>
 *     <tr>
 *         <td>nodeId</td>
 *         <td>String</td>
 *         <td>Identifier of node where instance located.</td>
 *         <td>(not defined)</td>
 *     </tr>
 *     <tr>
 *         <td>uri</td>
 *         <td>URI</td>
 *         <td>URI to communicate with instance.</td>
 *         <td>(not defined)</td>
 *     </tr>
 * </table>
 * <p>
 * This properties doesn't contain default values. If user not define required properties (InstanceId and version) it
 * can't lead to unpredictable behavior.
 */
@ConfigurationProperties("org.ametiste.scm.instance")
public class InstanceInfoProperties {

    private String instanceId;
    private String version;
    private String nodeId;
    private String uri;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
