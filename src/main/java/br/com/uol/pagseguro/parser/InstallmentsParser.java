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
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.installment.Installment;
import br.com.uol.pagseguro.domain.installment.Installments;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.utils.collections.CollectionsUtil;
import br.com.uol.pagseguro.utils.collections.Translator;
import br.com.uol.pagseguro.xmlparser.XMLParserUtils;

/**
 * Parses a installments XML in a <b>Installments</b> object
 * 
 * @see Installments
 */
public class InstallmentsParser {

    /**
     * PagSeguro Log tool
     * 
     * @see Logger
     */
    private static Log log = new Log(InstallmentsParser.class);

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
    public static Installments readInstallments(InputStream xmlInputStream) //
            throws IOException, //
            ParserConfigurationException, //
            ParseException, //
            SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        InputSource inputSource = new InputSource(xmlInputStream);
        Document document = documentBuilder.parse(inputSource);

        Element installmentsElement = document.getDocumentElement();
        List<Installment> installments = new ArrayList<Installment>();

        InstallmentsParser.log.debug("Parsing installments");

        List<Element> installmentElements = XMLParserUtils.getElements("installment", installmentsElement);
        for (int i = 0; i < installmentElements.size(); i++) {
            Element element = installmentElements.get(i);

            // setting <installments><installment><cardBrand>
            String cardBrand = XMLParserUtils.getTagValue("cardBrand", element);

            // setting <installments><installment><quantity>
            String quantity = XMLParserUtils.getTagValue("quantity", element);

            // setting <installments><installment><amount>
            String amount = XMLParserUtils.getTagValue("amount", element);

            // setting <installments><installment><totalAmount>
            String totalAmount = XMLParserUtils.getTagValue("totalAmount", element);

            // setting <installments><installment><interestFree>
            String interestFree = XMLParserUtils.getTagValue("interestFree", element);

            // adding item for items list
            installments.add(new Installment(cardBrand, //
                    Integer.parseInt(quantity), //
                    new BigDecimal(amount), //
                    new BigDecimal(totalAmount), //
                    Boolean.parseBoolean(interestFree)));
        }

        return new Installments(CollectionsUtil.createMultiMap(installments, new Translator<Installment, String>() {

            public String translate(Installment installment) {
                return installment.getCardBrand();
            }

        }));
    }
}
