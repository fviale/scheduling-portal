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
package org.ow2.proactive_grid_cloud_portal.common.client.json;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;


/**
 * JSON utils to parse JSON responses from the server.
 * @author the activeeon team.
 *
 */
public class JSONUtils {

    //Repeated code from Controller. The code using ParseJSON in controller will be progressively 
    //refactored to use this new method and parseJSON in controller will then be removed.
    /**
     * Parse a JSON string. 
     * <p>
     * If the input is not valid JSON or parsing fails for some reason,
     * the exception will be logged in the UI but not thrown.
     * 
     * @param jsonStr a valid JSON string
     * @return a java representation of the JSON object hierarchy,
     * @throws JSONException if it fails to parse the JSONN
     */
    public static JSONValue parseJSON(String jsonStr) throws JSONException {
        try {
            return JSONParser.parseStrict(jsonStr);
        } catch (Throwable t) {
            String message = "JSON Parser failed " + t.getClass().getName() + ": " + t.getLocalizedMessage() +
                             "\ninput was: " + jsonStr;
            throw new JSONException(message, t);
        }
    }

    /**
     * @param throwable a serialized JSON Exception
     * @return the value of the 'errorMessage' key
     */
    public static String getJsonErrorMessage(Throwable throwable) {
        String msg = throwable.getMessage();
        return getJsonErrorMessage(msg);
    }

    /**
     * @param throwable a serialized JSON Exception
     * @return the value of the 'httpErrorCode' key, or -1
     */
    public static int getJsonErrorCode(Throwable throwable) {
        String msg = throwable.getMessage();
        return getJsonErrorCode(msg);
    }

    /**
     * @param str String representation of a serialized JSON Exception
     * @return the value of the 'errorMessage' key
     */
    public static String getJsonErrorMessage(String str) {
        try {
            JSONObject exc = JSONParser.parseStrict(str).isObject();
            if (exc != null && exc.containsKey("errorMessage")) {
                return retrieveErrorMessage(exc);
            } else if (exc != null && exc.containsKey("exception")) {
                JSONObject nestedExc = exc.get("exception").isObject();
                return retrieveErrorMessage(nestedExc);
            }
        } catch (Exception e) {
            if (str != null) {
                return str;
            } else {
                return "<no reason>";
            }
        }
        return null;
    }

    private static String retrieveErrorMessage(JSONObject exc) {
        JSONValue val = exc.get("errorMessage");
        if (val == null || val.isString() == null) {
            return "<no reason>";
        } else {
            return val.isString().stringValue();
        }
    }

    /**
     * @param str String representation of a serialized JSON Exception
     * @return the value of the 'httpErrorCode' key, or -1
     */
    public static int getJsonErrorCode(String str) {
        try {
            JSONObject exc = JSONParser.parseStrict(str).isObject();
            if (exc != null && exc.containsKey("httpErrorCode")) {
                return retrieveErrorCode(exc);
            } else if (exc != null && exc.containsKey("exception")) {
                JSONObject nestedExc = exc.get("exception").isObject();
                return retrieveErrorCode(nestedExc);
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    private static int retrieveErrorCode(JSONObject exc) {
        JSONValue val = exc.get("httpErrorCode");
        if (val == null || val.isNumber() == null) {
            return -1;
        } else {
            return (int) val.isNumber().doubleValue();
        }
    }
}
