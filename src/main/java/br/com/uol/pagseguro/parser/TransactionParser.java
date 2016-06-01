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

import br.com.uol.pagseguro.domain.Address;
import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.PaymentMethod;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.Shipping;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.enums.PaymentMethodCode;
import br.com.uol.pagseguro.enums.PaymentMethodType;
import br.com.uol.pagseguro.enums.ShippingType;
import br.com.uol.pagseguro.enums.TransactionStatus;
import br.com.uol.pagseguro.enums.TransactionType;
import br.com.uol.pagseguro.helper.PagSeguroUtil;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.xmlparser.XMLParserUtils;

/**
 * Parses a transaction XML in a <b>Transaction</b> object
 * 
 * @see Transaction
 */
public class TransactionParser {

    private TransactionParser() {
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
    public static Transaction readTransaction(InputStream xmlInputStream) throws ParserConfigurationException,
            SAXException, IOException, ParseException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputSource is = new InputSource(xmlInputStream);
        Document doc = dBuilder.parse(is);

        String tagValue = null;

        Element transactionElement = doc.getDocumentElement();
        Transaction transaction = new Transaction();

        TransactionParser.log.debug("Parsing transaction");

        // parsing <transaction><lastEventDate>
        tagValue = XMLParserUtils.getTagValue("lastEventDate", transactionElement);
        if (tagValue != null) {
            transaction.setLastEventDate(PagSeguroUtil.parse(tagValue));
        }

        // parsing <transaction><date>
        tagValue = XMLParserUtils.getTagValue("date", transactionElement);
        if (tagValue != null) {
            transaction.setDate(PagSeguroUtil.parse(tagValue));
        }

        // parsing <transaction><code>
        tagValue = XMLParserUtils.getTagValue("code", transactionElement);
        if (tagValue != null) {
            transaction.setCode(tagValue);
        }

        // parsing <transaction><reference>
        tagValue = XMLParserUtils.getTagValue("reference", transactionElement);
        if (tagValue != null) {
            transaction.setReference(tagValue);
        }

        // parsing <transaction><type>
        tagValue = XMLParserUtils.getTagValue("type", transactionElement);
        if (tagValue != null) {
            // transaction.setType(new TransactionType(Integer.valueOf(tagValue)));
            transaction.setType(TransactionType.fromValue((Integer.valueOf(tagValue))));
        }

        // parsing <transaction><status>
        tagValue = XMLParserUtils.getTagValue("status", transactionElement);
        if (tagValue != null) {
            transaction.setStatus(TransactionStatus.fromValue(Integer.valueOf(tagValue)));
        }

        // parsing <transaction><paymentMethod>
        Element paymentMethodElement = XMLParserUtils.getElement("paymentMethod", transactionElement);
        if (paymentMethodElement != null) {
            PaymentMethod paymentMethod = new PaymentMethod();

            // parsing <transaction><paymentMethod><type>
            tagValue = XMLParserUtils.getTagValue("type", paymentMethodElement);
            if (tagValue != null) {
                paymentMethod.setType(PaymentMethodType.fromValue(Integer.valueOf(tagValue)));
            }

            // parsing <transaction><paymentMethod><code>
            tagValue = XMLParserUtils.getTagValue("code", paymentMethodElement);
            if (tagValue != null) {
                paymentMethod.setCode(PaymentMethodCode.fromValue(Integer.valueOf(tagValue)));
            }

            // setting transaction payment method
            transaction.setPaymentMethod(paymentMethod);

        }

        // setting <transaction><paymentLink>
        tagValue = XMLParserUtils.getTagValue("paymentLink", transactionElement);
        if (tagValue != null) {
            transaction.setPaymentLink(tagValue);
        }

        // setting <transaction><grossAmount>
        tagValue = XMLParserUtils.getTagValue("grossAmount", transactionElement);
        if (tagValue != null) {
            transaction.setGrossAmount(new BigDecimal(tagValue));
        }

        // setting <transaction><discountAmount>
        tagValue = XMLParserUtils.getTagValue("discountAmount", transactionElement);
        if (tagValue != null) {
            transaction.setDiscountAmount(new BigDecimal(tagValue));
        }

        // setting <transaction><feeAmount>
        tagValue = XMLParserUtils.getTagValue("feeAmount", transactionElement);
        if (tagValue != null) {
            transaction.setFeeAmount(new BigDecimal(tagValue));
        }

        // setting <transaction><netAmount>
        tagValue = XMLParserUtils.getTagValue("netAmount", transactionElement);
        if (tagValue != null) {
            transaction.setNetAmount(new BigDecimal(tagValue));
        }

        // setting <transaction><extraAmount>
        tagValue = XMLParserUtils.getTagValue("extraAmount", transactionElement);
        if (tagValue != null) {
            transaction.setExtraAmount(new BigDecimal(tagValue));
        }

        // parsing <transaction><escrowEndDate>
        tagValue = XMLParserUtils.getTagValue("escrowEndDate", transactionElement);
        if (tagValue != null) {
            transaction.setEscrowEndDate(PagSeguroUtil.parse(tagValue));
        }

        // parsing <transaction><cancellationSource>
        tagValue = XMLParserUtils.getTagValue("cancellationSource", transactionElement);
        if (tagValue != null) {
            transaction.setCancellationSource(tagValue);
        }

        // setting <transaction><installmentCount>
        tagValue = XMLParserUtils.getTagValue("installmentCount", transactionElement);
        if (tagValue != null) {
            transaction.setInstallmentCount(Integer.valueOf(tagValue));
        }

        // setting <transaction><itemCount>
        tagValue = XMLParserUtils.getTagValue("itemCount", transactionElement);
        if (tagValue != null) {
            transaction.setItemCount(Integer.valueOf(tagValue));
        }

        // setting <transaction><items>
        Element itemsElement = XMLParserUtils.getElement("items", transactionElement);
        if (itemsElement != null) {
            List<Element> itElements = XMLParserUtils.getElements("item", itemsElement);
            List<Item> items = new ArrayList<Item>();

            for (int i = 0; i < itElements.size(); i++) {
                Element itElement = itElements.get(i);

                // setting <transaction><items><item>
                Item item = new Item();

                // setting <transaction><items><item><id>
                tagValue = XMLParserUtils.getTagValue("id", itElement);
                if (tagValue != null) {
                    item.setId(tagValue);
                }

                // setting <transaction><items><item><description>
                tagValue = XMLParserUtils.getTagValue("description", itElement);
                if (tagValue != null) {
                    item.setDescription(tagValue);
                }

                // setting <transaction><items><item><quantity>
                tagValue = XMLParserUtils.getTagValue("quantity", itElement);
                if (tagValue != null) {
                    item.setQuantity(Integer.valueOf(tagValue));
                }

                // setting <transaction><items><item><amount>
                tagValue = XMLParserUtils.getTagValue("amount", itElement);
                if (tagValue != null) {
                    item.setAmount(new BigDecimal(tagValue));
                }

                // adding item for items list
                items.add(item);
            }

            transaction.setItems(items);
        }

        // setting <transaction><sender>
        Element senderElement = XMLParserUtils.getElement("sender", transactionElement);
        if (senderElement != null) {

            Sender sender = new Sender();

            // setting <transaction><sender><name>
            tagValue = XMLParserUtils.getTagValue("name", senderElement);
            if (tagValue != null) {
                sender.setName(tagValue);
            }

            // setting <transaction><sender><email>
            tagValue = XMLParserUtils.getTagValue("email", senderElement);
            if (tagValue != null) {
                sender.setEmail(tagValue);
            }

            // setting <transaction><sender><phone>
            Element phoneElement = XMLParserUtils.getElement("phone", senderElement);
            if (phoneElement != null) {
                Phone phone = new Phone();

                tagValue = XMLParserUtils.getTagValue("areaCode", phoneElement);
                if (tagValue != null) {
                    phone.setAreaCode(tagValue);
                }

                tagValue = XMLParserUtils.getTagValue("number", phoneElement);
                if (tagValue != null) {
                    phone.setNumber(tagValue);
                }

                sender.setPhone(phone);
            }

            transaction.setSender(sender);
        }

        // setting <transaction><shipping>
        Element shippingElement = XMLParserUtils.getElement("shipping", transactionElement);
        if (shippingElement != null) {

            // creating new Shipping object
            Shipping shipping = new Shipping();

            // setting <transaction><shipping><type>
            tagValue = XMLParserUtils.getTagValue("type", shippingElement);
            if (tagValue != null) {
                shipping.setType(ShippingType.fromValue(Integer.valueOf(tagValue)));
            }

            // setting <transaction><shipping><cost>
            tagValue = XMLParserUtils.getTagValue("cost", shippingElement);
            if (tagValue != null) {
                shipping.setCost(new BigDecimal(tagValue));
            }

            // setting <transaction><shipping><address>
            Element addressElement = XMLParserUtils.getElement("address", shippingElement);
            if (addressElement != null) {

                // creating new Address object
                Address address = new Address();

                // setting <transaction><shipping><address><street>
                tagValue = XMLParserUtils.getTagValue("street", addressElement);
                if (tagValue != null) {
                    address.setStreet(tagValue);
                }

                // setting <transaction><shipping><address><number>
                tagValue = XMLParserUtils.getTagValue("number", addressElement);
                if (tagValue != null) {
                    address.setNumber(tagValue);
                }

                // setting <transaction><shipping><address><complement>
                tagValue = XMLParserUtils.getTagValue("complement", addressElement);
                if (tagValue != null) {
                    address.setComplement(tagValue);
                }

                // setting <transaction><shipping><address><district>
                tagValue = XMLParserUtils.getTagValue("district", addressElement);
                if (tagValue != null) {
                    address.setDistrict(tagValue);
                }

                // setting <transaction><shipping><address><postalCode>
                tagValue = XMLParserUtils.getTagValue("postalCode", addressElement);
                if (tagValue != null) {
                    address.setPostalCode(tagValue);
                }

                // setting <transaction><shipping><address><city>
                tagValue = XMLParserUtils.getTagValue("city", addressElement);
                if (tagValue != null) {
                    address.setCity(tagValue);
                }

                // setting <transaction><shipping><address><state>
                tagValue = XMLParserUtils.getTagValue("state", addressElement);
                if (tagValue != null) {
                    address.setState(tagValue);
                }

                // setting <transaction><shipping><address><country>
                tagValue = XMLParserUtils.getTagValue("country", addressElement);
                if (tagValue != null) {
                    address.setCountry(tagValue);
                }

                // setting address for shipping object
                shipping.setAddress(address);
            }

            // setting <transaction><shipping>
            transaction.setShipping(shipping);
        }

        TransactionParser.log.debug("Parsing transaction success: " + transaction.getCode());

        return transaction;

    }
}
