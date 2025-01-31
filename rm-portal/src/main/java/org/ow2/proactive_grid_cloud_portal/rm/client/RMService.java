/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive_grid_cloud_portal.rm.client;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ow2.proactive_grid_cloud_portal.common.shared.RestServerException;
import org.ow2.proactive_grid_cloud_portal.common.shared.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * Client side stub for server calls 
 * 
 * 
 * 
 * @author mschnoor
 *
 */
@RemoteServiceRelativePath("rm")
public interface RMService extends RemoteService {

    /**
     * Default configuration is read by the server
     * @return a list of configuration properties read by 
     *  	the server used to configure the client
     */
    Map<String, String> getProperties();

    /**
     * Logout the current session
     * @param sessionId id of the current session
     * @throws ServiceException
     */
    void logout(String sessionId) throws ServiceException;

    /**
     * @param sessionId id of the current session
     * @return true if user has right to access RM portal
     */
    String portalAccess(String sessionId) throws ServiceException, RestServerException;

    /**
     *
     * @param sessionId id of the current session
     * @param portals list of portals requiring access
     * @return a list of portals with authorized access
     */
    List<String> portalsAccess(String sessionId, List<String> portals) throws ServiceException, RestServerException;

    /**
     * Limited info about the current RM State : freeNodesNumber, totalAliveNodesNumber, totalNodesNumber
     * @param sessionId the current session
     * @return freeNodesNumber, totalAliveNodesNumber, totalNodesNumber in a JSON string
     * @throws RestServerException 
     * @throws ServiceException
     */
    String getState(String sessionId) throws RestServerException, ServiceException;

    /**
     * List currently available loggers and their associated levels
     *
     * @param sessionId current session id
     * @return a map of loggers and associated levels
     * @throws RestServerException
     * @throws ServiceException
     */
    String getCurrentLoggers(String sessionId) throws ServiceException, RestServerException;

    /**
     * Change multiple loggers level.
     *
     * @param sessionId current session id
     * @param loggersConfiguration map of (logger_name, level)
     * @return true if any logger level has been changed, false otherwise
     * @throws RestServerException
     * @throws ServiceException
     */
    String setLogLevelMultiple(String sessionId, Map<String, String> loggersConfiguration)
            throws ServiceException, RestServerException;

    /**
     * Detailed info about the nodes currently held by the RM
     * presented as two arrays of nodes and nodesources referencing each others
     * @param sessionId current session
     * @param clientCounter latest counter client is aware of
     * @return a JSON object containing two arrays named nodesList and nodeSources, that contain all info about current
     * 		nodes and nodesources in the RM
     * @throws RestServerException
     * @throws ServiceException
     */
    String getMonitoring(String sessionId, Long clientCounter) throws RestServerException, ServiceException;

    /**
     * List of all supported Infrastructure Managers, and their parameters
     * @param sessionId current session
     * @return a JSON array containing all supported infrastructures
     * @throws ServiceException
     */
    String getInfrastructures(String sessionId) throws RestServerException, ServiceException;

    /**
     * List of all supported Policies, and their parameters
     * @param sessionId current session
     * @return a JSON array containing all supported policies
     * @throws RestServerException 
     * @throws ServiceException
     */
    String getPolicies(String sessionId) throws RestServerException, ServiceException;

    /**
     * @param sessionId a valid session
     * @return a mapping which for each infrastructure provides list of policies
     * that can work together with given infrastructure
     */
    String getInfrasToPoliciesMapping(String sessionId) throws RestServerException, ServiceException;

    /**
     * Retrieve the configuration of a given node source, in other words, a
     * representation of all the parameters with which a node source was
     * configured.
     *
     * @param sessionId current session
     * @param nodeSourceName name of the node source to get the configuration from
     * @return a JSON object containing the current node source configuration
     * @throws RestServerException
     * @throws ServiceException
     */
    String getNodeSourceConfiguration(String sessionId, String nodeSourceName)
            throws RestServerException, ServiceException;

    /**
     * Defines a NodeSource
     * @param sessionId current session
     * @param nodeSourceName name of the new NS
     * @param infrastructureType infrastructure manager full class name
     * @param infrastructureParameters IM String parameters, null value for files
     * @param infrastructureFileParameters file parameters
     * @param policyType policy full class name
     * @param policyParameters String parameters, null value for files
     * @param policyFileParameters file parameters
     * @param nodesRecoverable whether nodes can be recovered after a crash
     * @throws RestServerException
     * @throws ServiceException
     */
    String defineNodeSource(String sessionId, String nodeSourceName, String infrastructureType,
            String[] infrastructureParameters, String[] infrastructureFileParameters, String policyType,
            String[] policyParameters, String[] policyFileParameters, String nodesRecoverable)
            throws RestServerException, ServiceException;

    /**
     * Edit a NodeSource
     * @param sessionId current session
     * @param nodeSourceName name of the NS to edit
     * @param infrastructureType infrastructure manager full class name
     * @param infrastructureParameters IM String parameters, null value for files
     * @param infrastructureFileParameters file parameters
     * @param policyType policy full class name
     * @param policyParameters String parameters, null value for files
     * @param policyFileParameters file parameters
     * @param nodesRecoverable whether nodes can be recovered after a crash
     * @throws RestServerException
     * @throws ServiceException
     */
    String editNodeSource(String sessionId, String nodeSourceName, String infrastructureType,
            String[] infrastructureParameters, String[] infrastructureFileParameters, String policyType,
            String[] policyParameters, String[] policyFileParameters, String nodesRecoverable)
            throws RestServerException, ServiceException;

    /**
     * Override the dynamic parameters of a NodeSource
     * @param sessionId current session
     * @param nodeSourceName name of the NS to edit
     * @param infrastructureType infrastructure manager full class name
     * @param infrastructureParameters IM String parameters, null value for files
     * @param infrastructureFileParameters file parameters
     * @param policyType policy full class name
     * @param policyParameters String parameters, null value for files
     * @param policyFileParameters file parameters
     * @throws RestServerException
     * @throws ServiceException
     */
    String updateDynamicParameters(String sessionId, String nodeSourceName, String infrastructureType,
            String[] infrastructureParameters, String[] infrastructureFileParameters, String policyType,
            String[] policyParameters, String[] policyFileParameters) throws RestServerException, ServiceException;

    /**
     * Deploys a node source and starts acquiring its nodes
     *
     * @param sessionId current session
     * @param nodeSourceName name of the node source to deploy
    
     * @throws RestServerException
     * @throws ServiceException
     */
    String deployNodeSource(String sessionId, String nodeSourceName) throws RestServerException, ServiceException;

    /**
     * Undeploys a node source and removes its nodes
     *
     * @param sessionId current session
     * @param nodeSourceName name of the node source to undeploy
    
     * @throws RestServerException
     * @throws ServiceException
     */
    String undeployNodeSource(String sessionId, String nodeSourceName, boolean force)
            throws RestServerException, ServiceException;

    /**
     * Redeploys a node source
     *
     * @param sessionId current session
     * @param nodeSourceName name of the node source to redeploy
    
     * @throws RestServerException
     * @throws ServiceException
     */
    String redeployNodeSource(String sessionId, String nodeSourceName) throws RestServerException, ServiceException;

    /**
     * lock a set of nodes
     * @param sessionId current session
     * @param nodeUrls nodes to lock
     * @return true upon success
     * @throws RestServerException 
     * @throws ServiceException
     */
    String lockNodes(String sessionId, Set<String> nodeUrls) throws RestServerException, ServiceException;

    /**
     * Unlock a set of nodes
     * @param sessionId current session
     * @param nodeUrls nodes to unlock
     * @return true upon success
     * @throws RestServerException 
     * @throws ServiceException
     */
    String unlockNodes(String sessionId, Set<String> nodeUrls) throws RestServerException, ServiceException;

    /**
     * Release a node
     * @param sessionId currend session
     * @param url complete unique url of the node
     * @return true when ok
     * @throws RestServerException 
     * @throws ServiceException
     */
    String releaseNode(String sessionId, String url) throws RestServerException, ServiceException;

    /**
     * Remove a node
     * @param sessionId currend session
     * @param url complete unique url of the node
     * @param force do not wait for task completion
     * @return true when ok
     * @throws RestServerException 
     * @throws ServiceException
     */
    String removeNode(String sessionId, String url, boolean force) throws RestServerException, ServiceException;

    /**
     * Remove a node
     * @param sessionId currend session
     * @param name complete unique name of the nodesource
     * @param preempt don't wait for tasks if true
     * @return true when ok
     * @throws RestServerException 
     * @throws ServiceException
     */
    String removeNodesource(String sessionId, String name, boolean preempt)
            throws RestServerException, ServiceException;

    /**
     * @return version string of the REST api
     */
    String getVersion() throws RestServerException, ServiceException;

    /**
     * Query a list of attributes from a specific RM MBean
     * @param sessionId current session
     * @param name of the JMX management bean
     * @param attrs attributes to fetch in the specified MBean
     * @return JSON object with attribute names as key
     * @throws RestServerException
     * @throws ServiceException
     */
    String getMBeanInfo(String sessionId, String name, List<String> attrs) throws RestServerException, ServiceException;

    /**
     * Retrieves attributes of the specified mbean.
     * 
     * @param sessionId current session
     * @param name of mbean
     * @param nodeJmxUrl mbean server url
     * @param attrs set of mbean attributes
     *
     * @return mbean attributes values
     */
    String getNodeMBeanInfo(String sessionId, String nodeJmxUrl, String objectName, List<String> attrs)
            throws RestServerException, ServiceException;

    String getNodeMBeanHistory(String sessionId, String jmxServerUrl, String mbeanName, List<String> strings,
            String timeRange) throws RestServerException, ServiceException;

    String getNodeMBeansHistory(String sessionId, String nodeJmxUrl, String objectName, List<String> attrs,
            String timeRange) throws RestServerException, ServiceException;

    /**
     * Retrieves attributes of the specified mbeans.
     * 
     * @param sessionId current session
     * @param objectNames mbean names (@see ObjectName format)
     * @param nodeJmxUrl mbean server url
     * @param attrs set of mbean attributes
     * 
     * @return mbean attributes values
     */
    String getNodeMBeansInfo(String sessionId, String nodeJmxUrl, String objectNames, List<String> attrs)
            throws RestServerException, ServiceException;

    /**
     * Statistic history for the following values:<pre>
     * 	{ "BusyNodesCount",
     *    "FreeNodesCount",
     *    "DownNodesCount",
     *    "AvailableNodesCount",
     *    "AverageActivity" }</pre>
     *    
     * @param sessionId current session 
     * @param range a String of 5 chars, one for each stat history source, indicating the time range to fetch
     *      for each source. Each char can be:<ul>
     *            <li>'a' 1 minute
     *            <li>'m' 10 minutes
     *            <li>'h' 1 hour
     *            <li>'H' 8 hours
     *            <li>'d' 1 day
     *            <li>'w' 1 week
     *            <li>'M' 1 month
     *            <li>'y' 1 year</ul>
     * @return will contain the server response, a JSON object containing a key for each source
     */
    String getStatHistory(String sessionId, String range) throws RestServerException, ServiceException;

    /**
     * Executes a script on a node
     * 
     * @param sessionId current session
     * @param script to execute 
     * @param a script engine to use for the script execution 
     * @param nodeUrl a target for the script execution
     * 
     * @return script output
     * @throws RestServerException
     * @throws ServiceException
     */
    String executeNodeScript(String sessionId, String script, String engine, String nodeUrl)
            throws RestServerException, ServiceException;

    /**
     * Executes a script on a node source
     *
     * @param sessionId current session
     * @param script to execute
     * @param engine a script engine to use for the script execution
     * @param nodeSourceName a target for the script execution
     *
     * @return script output
     */
    String executeNodeSourceScript(String sessionId, String script, String engine, String nodeSourceName)
            throws RestServerException, ServiceException;

    /**
     * Executes a script on a host
     *
     * @param sessionId current session
     * @param script to execute
     * @param engine a script engine to use for the script execution
     * @param host a target for the script execution
     *
     * @return script output
     */
    String executeHostScript(String sessionId, String script, String engine, String host)
            throws RestServerException, ServiceException;

    /**
     * Retrieves the thread dump of the Resource Manager
     *
     * @param sessionId current session
     */
    String getRMThreadDump(String sessionId) throws ServiceException, RestServerException;

    /**
     * Retrieves the thread dump of the node identified by the given URL
     *
     * @param sessionId current session
     * @param nodeUrl node to ask the thread dump from
     */
    String getNodeThreadDump(String sessionId, String nodeUrl) throws ServiceException, RestServerException;

    void setNodeTokens(String sessionId, String nodeurl, List<String> tokens);

    String checkNodePermission(String sessionId, String nodeUrl, boolean provider)
            throws RestServerException, ServiceException;

    String checkNodeSourcePermission(String sessionId, String nodeSourceName, boolean provider)
            throws RestServerException, ServiceException;

    Map<String, Boolean> checkMethodsPermissions(final String sessionId, List<String> methods)
            throws RestServerException, ServiceException;

    /**
     * Returns a UserData object associated to a session.
     *
     * <br> NOTE: <br>
     * In case the given sessionId doesn't have an associated login (session id expired, or invalid),
     * this endpoint will return null
     *
     * @param sessionId id of a session
     * @return a UserData object or null
     */
    String getCurrentUserData(String sessionId) throws RestServerException, ServiceException;

    /**
     * Returns the domains configured on the scheduler server.
     *
     */
    List<String> getDomains() throws RestServerException, ServiceException;
}
