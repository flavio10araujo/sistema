/*
 ************************************************************************
 Copyright [2011] [PagSeguro Internet Ltda.]

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
package br.com.uol.pagseguro.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.paymentmethod.PaymentMethod;
import br.com.uol.pagseguro.domain.paymentmethod.PaymentMethods;
import br.com.uol.pagseguro.enums.PaymentMethodStatus;
import br.com.uol.pagseguro.enums.PaymentMethodType;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.xmlparser.XMLParserUtils;

/**
 * Parses a PaymentMethods XML in a List of Map object
 * 
 * @see InstallmentXml
 */
public class PaymentMethodsParser {

    /**
     * PagSeguro Log tool
     * 
     * @see Logger
     */
    private static Log log = new Log(PaymentMethodsParser.class);

    /**
     * Parses the XML response form PagSeguro web services
     * 
     * @param xmlInputStream
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws ParseException
     * @throws SAXException
     */
    public static PaymentMethods readPaymentMethods(InputStream xmlInputStream) //
            throws IOException, //
            ParserConfigurationException, //
            ParseException, //
            SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        InputSource inputSource = new InputSource(xmlInputStream);
        Document document = documentBuilder.parse(inputSource);

        Element paymentMethodsElement = document.getDocumentElement();
        Map<PaymentMethodType, Map<Integer, PaymentMethod>> paymentMethods = new HashMap<PaymentMethodType, Map<Integer, PaymentMethod>>();

        PaymentMethodsParser.log.debug("Parsing paymentMethods");

        List<Element> paymentMethodElements = XMLParserUtils.getElements("paymentMethod", paymentMethodsElement);
        for (int i = 0; i < paymentMethodElements.size(); i++) {
            Element element = paymentMethodElements.get(i);

            // setting <paymentMethods><paymentMethod><code>
            String type = XMLParserUtils.getTagValue("code", element);

            PaymentMethodType paymentMethodType = PaymentMethodType.fromValue(Integer.parseInt(type));

            List<Element> optionElements = XMLParserUtils.getElements("option", element);
            for (int j = 0; j < optionElements.size(); j++) {
                Element option = optionElements.get(j);

                // setting <paymentMethods><paymentMethod><options><option><code>
                String code = XMLParserUtils.getTagValue("code", option);

                // setting <paymentMethods><paymentMethod><options><option><name>
                String name = XMLParserUtils.getTagValue("name", option);

                // setting <paymentMethods><paymentMethod><options><option><displayName>
                String displayName = XMLParserUtils.getTagValue("displayName", option);

                // setting <paymentMethods><paymentMethod><options><option><status>
                String status = XMLParserUtils.getTagValue("status", option);

                PaymentMethod paymentMethod = new PaymentMethod(Integer.parseInt(code), //
                        name, //
                        displayName, //
                        PaymentMethodStatus.valueOf(status));

                Map<Integer, PaymentMethod> paymentMethodsByType = paymentMethods.get(paymentMethodType);
                if (paymentMethodsByType == null) {
                    paymentMethodsByType = new HashMap<Integer, PaymentMethod>();
                    paymentMethods.put(paymentMethodType, paymentMethodsByType);
                }

                paymentMethodsByType.put(paymentMethod.getCode(), paymentMethod);
            }
        }

        return new PaymentMethods(paymentMethods);
    }

}
