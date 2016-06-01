/*
 ************************************************************************
 Copyright [2014] [PagSeguro Internet Ltda.]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ************************************************************************
 */

package br.com.uol.pagseguro.parser.authorization;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.authorization.AuthorizationRequest;
import br.com.uol.pagseguro.xmlparser.XMLParserUtils;

/**
 * Parses Authorization and responses
 */
public class AuthorizationRequestParser {
	
	private AuthorizationRequestParser() {

    }
	
	/**
     * 
     * @param authorization
     * @return mixed
     */
    public static Map<Object, Object> getData(AuthorizationRequest authorization) {

        Map<Object, Object> data = new HashMap<Object, Object>();

        /**
         * REFERENCE
         */
        if (authorization.getReference() != null) {
            data.put("reference", authorization.getReference());
        }

        /**
         * REDIRECT URL
         */
        if (authorization.getRedirectURL() != null && !"".equals(authorization.getRedirectURL())) {
            data.put("redirectURL", authorization.getRedirectURL());
        }
        /**
         * NOTIFICATION URL
         */
        if (authorization.getNotificationURL() != null && !"".equals(authorization.getNotificationURL())) {
            data.put("notificationURL", authorization.getNotificationURL());
        }
        
        /**
         * PERMISSIONS
         */
        if (authorization.getPermissions() != null && authorization.getPermissions().size() > 0) {
        	
        	String permissionList = "";
        	boolean first = true;
            for (String permission: authorization.getPermissions()) {

                if (permission != null && !"".equals(permission)) {
                    
                	if(first == true){
                		permissionList += permission;
                	} else {
                		permissionList += "," + permission;
                	}
                	first = false;
                }
            }
            
            data.put("permissions", permissionList);

        }

        return data;

    }
    
    public static String readSuccessXml(HttpURLConnection connection) throws ParserConfigurationException,
	    SAXException, IOException {
	
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = dBuilder.parse(connection.getInputStream());
		Element authorizationReturnElement = doc.getDocumentElement();
		
		return XMLParserUtils.getTagValue("code", authorizationReturnElement);
	
	}

}
