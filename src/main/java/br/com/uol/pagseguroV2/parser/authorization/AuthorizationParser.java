package br.com.uol.pagseguroV2.parser.authorization;

import br.com.uol.pagseguroV2.domain.Authorization;
import br.com.uol.pagseguroV2.domain.Permission;
import br.com.uol.pagseguroV2.enums.PermissionStatus;
import br.com.uol.pagseguroV2.enums.PermissionType;
import br.com.uol.pagseguroV2.helper.PagSeguroUtil;
import br.com.uol.pagseguroV2.logs.Log;
import br.com.uol.pagseguroV2.logs.Logger;
import br.com.uol.pagseguroV2.parser.TransactionParser;
import br.com.uol.pagseguroV2.xmlparser.XMLParserUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationParser {

    private AuthorizationParser() {
    }

    /**
     * PagSeguro Log tool
     *
     * @see Logger
     */
    private static Log log = new Log(TransactionParser.class);

    /**
     * Parses the XML response form PagSeguro web services
     *
     * @param xmlInputStream
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ParseException
     */
    public static Authorization readAuthorization(InputStream xmlInputStream) throws ParserConfigurationException,
            SAXException, IOException, ParseException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputSource is = new InputSource(xmlInputStream);
        Document doc = dBuilder.parse(is);

        Element authorizationElement = doc.getDocumentElement();

        return parseAuthorization(authorizationElement);

    }

    public static List<Authorization> readAuthorizations(InputStream xmlInputStream) throws ParserConfigurationException,
            SAXException, IOException, ParseException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputSource is = new InputSource(xmlInputStream);
        Document doc = dBuilder.parse(is);

        Element authorizationElement = doc.getDocumentElement();

        List<Authorization> authorizationList = new ArrayList<Authorization>();

        Element authorizations = XMLParserUtils.getElement("authorizations", authorizationElement);
        if (authorizations != null) {
            List<Element> authorizationElements = XMLParserUtils.getElements("authorization", authorizations);

            for (int i = 0; i < authorizationElements.size(); i++) {
                Element element = authorizationElements.get(i);
                authorizationList.add(parseAuthorization(element));
            }
        }

        return authorizationList;
    }

    private static Authorization parseAuthorization(Element authorizationElement) throws ParseException {

        String tagValue = null;

        Authorization authorization = new Authorization();

        AuthorizationParser.log.debug("Parsing authorization");

        // parsing <authorization><date>
        tagValue = XMLParserUtils.getTagValue("date", authorizationElement);
        if (tagValue != null) {
            authorization.setDate(PagSeguroUtil.parse(tagValue));
        }

        // parsing <authorization><code>
        tagValue = XMLParserUtils.getTagValue("code", authorizationElement);
        if (tagValue != null) {
            authorization.setCode(tagValue);
        }

        // parsing <authorization><reference>
        tagValue = XMLParserUtils.getTagValue("reference", authorizationElement);
        if (tagValue != null) {
            authorization.setReference(tagValue);
        }

        // setting <authorization><permissions>
        Element permissionsElement = XMLParserUtils.getElement("permissions", authorizationElement);
        if (permissionsElement != null) {
            List<Element> itElements = XMLParserUtils.getElements("permission", permissionsElement);

            for (int i = 0; i < itElements.size(); i++) {
                Element itElement = itElements.get(i);

                // setting <authorization><permissions><permission>
                Permission permission = new Permission();

                // setting <authorization><permissions><permission><permission>
                tagValue = XMLParserUtils.getTagValue("code", itElement);
                if (tagValue != null) {
                    permission.setPermission(PermissionType.fromValue(tagValue));
                }

                // setting <authorization><permissions><permission><status>
                tagValue = XMLParserUtils.getTagValue("status", itElement);
                if (tagValue != null) {
                    permission.setStatus(PermissionStatus.fromValue(tagValue));
                }

                // setting <authorization><permissions><permission><lastEvent>
                tagValue = XMLParserUtils.getTagValue("lastEvent", itElement);
                if (tagValue != null) {
                    permission.setLastUpdate(PagSeguroUtil.parse(tagValue));
                }

                // adding item for items list
                authorization.addPermission(permission);
            }

        }

        AuthorizationParser.log.debug("Parsing authorization success: " + authorization.getCode());

        return authorization;

    }

}
