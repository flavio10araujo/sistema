/*
 * ***********************************************************************
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
 * ***********************************************************************
 */

package br.com.uol.pagseguro.parser.preapproval;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.ArrayList;
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

import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequest;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestItem;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestShipping;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestShippingPackage;
import br.com.uol.pagseguro.domain.preapproval.Recurrence;
import br.com.uol.pagseguro.domain.preapproval.RecurrenceCancelTransaction;
import br.com.uol.pagseguro.domain.preapproval.RecurrenceTransaction;
import br.com.uol.pagseguro.enums.RecurrencePeriod;
import br.com.uol.pagseguro.enums.RecurrenceStatus;
import br.com.uol.pagseguro.helper.PagSeguroUtil;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.parser.paymentrequest.PaymentRequestParser;
import br.com.uol.pagseguro.xmlparser.XMLParserUtils;

/**
 * Parses recurrence requests and responses
 */
public class RecurrenceParser {

    /**
     * PagSeguro Log tool
     * 
     * @see Logger
     */
    private static final Log log = new Log(RecurrenceParser.class);

    private static final String RECURRENCE_INITIAL_DATE = "initialDate";
    private static final String RECURRENCE_PAYMENT_REQUESTS_QUANTITY = "paymentRequestsQuantity";
    private static final String RECURRENCE_PERIOD = "period";

    private RecurrenceParser() {

    }

    /**
     * 
     * @param recurrence
     * @return mixed
     */
    public static Map<Object, Object> getData(Recurrence recurrence) {
        Map<Object, Object> data = new HashMap<Object, Object>();

        /**
         * Set recurrence initial date
         */
        if (recurrence.getInitialDate() != null)
            data.put(RECURRENCE_INITIAL_DATE, recurrence.getInitialDate());

        /**
         * Set payment request
         */
        if (recurrence.getPaymentRequest() != null) {
            /**
             * Set payment request name
             */
            if (recurrence.getPaymentRequest().getName() != null)
                data.put(PaymentRequestParser.PAYMENT_REQUEST_NAME, recurrence.getPaymentRequest().getName());

            /**
             * Set payment request reference
             */
            if (recurrence.getPaymentRequest().getReference() != null)
                data.put(PaymentRequestParser.PAYMENT_REQUEST_REFERENCE, recurrence.getPaymentRequest().getReference());

            /**
             * Set payment request expiration days
             */
            if (recurrence.getPaymentRequest().getExpiration() != null)
                data.put(PaymentRequestParser.PAYMENT_REQUEST_EXPIRATION, recurrence.getPaymentRequest()
                        .getExpiration());

            /**
             * Set payment request due days
             */
            if (recurrence.getPaymentRequest().getDue() != null)
                data.put(PaymentRequestParser.PAYMENT_REQUEST_DUE, recurrence.getPaymentRequest().getDue());

            /**
             * Set payment request sender information
             * 
             * @see PaymentRequestSender
             */
            if (recurrence.getPaymentRequest().getSender() != null) {

                /**
                 * Set payment request sender email
                 */
                if (recurrence.getPaymentRequest().getSender().getEmail() != null)
                    data.put(PaymentRequestParser.PAYMENT_REQUEST_SENDER_EMAIL, recurrence.getPaymentRequest()
                            .getSender().getEmail());

                /**
                 * Set payment request sender name
                 */
                if (recurrence.getPaymentRequest().getSender().getName() != null)
                    data.put(PaymentRequestParser.PAYMENT_REQUEST_SENDER_NAME, recurrence.getPaymentRequest()
                            .getSender().getName());
            }

            /**
             * Set payment request items
             * 
             * @see PaymentRequestItem
             */
            if (recurrence.getPaymentRequest().getItems() != null
                    && !recurrence.getPaymentRequest().getItems().isEmpty()) {
                Integer count = 1;

                for (PaymentRequestItem item : recurrence.getPaymentRequest().getItems()) {
                    if (item.getDescription() != null)
                        data.put(PaymentRequestParser.PAYMENT_REQUEST_ITEM_PREFIX + count.toString()
                                + PaymentRequestParser.PAYMENT_REQUEST_ITEM_DESCRIPTION, item.getDescription());

                    if (item.getAmount() != null)
                        data.put(PaymentRequestParser.PAYMENT_REQUEST_ITEM_PREFIX + count.toString()
                                + PaymentRequestParser.PAYMENT_REQUEST_ITEM_AMOUNT, item.getAmount());

                    if (item.getQuantity() != null)
                        data.put(PaymentRequestParser.PAYMENT_REQUEST_ITEM_PREFIX + count.toString()
                                + PaymentRequestParser.PAYMENT_REQUEST_ITEM_QUANTITY, item.getQuantity());

                    if (item.getId() != null)
                        data.put(PaymentRequestParser.PAYMENT_REQUEST_ITEM_PREFIX + count.toString()
                                + PaymentRequestParser.PAYMENT_REQUEST_ITEM_ID, item.getId());

                    count++;
                }
            }

            /**
             * Set payment request shipping
             * 
             * @see PaymentRequestShipping
             */
            if (recurrence.getPaymentRequest().getShipping() != null) {
                if (recurrence.getPaymentRequest().getShipping().getCost() != null)
                    data.put(PaymentRequestParser.PAYMENT_REQUEST_SHIPPING_COST, recurrence.getPaymentRequest()
                            .getShipping().getCost());

                if (recurrence.getPaymentRequest().getShipping().getPaymentRequestShippingPackage() != null) {
                    if (recurrence.getPaymentRequest().getShipping().getPaymentRequestShippingPackage().getWidth() != null)
                        data.put(PaymentRequestParser.PAYMENT_REQUEST_SHIPPING_PACKAGE_WIDTH, recurrence
                                .getPaymentRequest().getShipping().getPaymentRequestShippingPackage().getWidth());

                    if (recurrence.getPaymentRequest().getShipping().getPaymentRequestShippingPackage().getHeight() != null)
                        data.put(PaymentRequestParser.PAYMENT_REQUEST_SHIPPING_PACKAGE_HEIGHT, recurrence
                                .getPaymentRequest().getShipping().getPaymentRequestShippingPackage().getWidth());

                    if (recurrence.getPaymentRequest().getShipping().getPaymentRequestShippingPackage().getLength() != null)
                        data.put(PaymentRequestParser.PAYMENT_REQUEST_SHIPPING_PACKAGE_LENGTH, recurrence
                                .getPaymentRequest().getShipping().getPaymentRequestShippingPackage().getWidth());

                    if (recurrence.getPaymentRequest().getShipping().getPaymentRequestShippingPackage().getWeight() != null)
                        data.put(PaymentRequestParser.PAYMENT_REQUEST_SHIPPING_PACKAGE_WEIGHT, recurrence
                                .getPaymentRequest().getShipping().getPaymentRequestShippingPackage().getWidth());
                }
            }
        }

        /**
         * Set payment requests quantity
         */
        if (recurrence.getPaymentRequestsQuantity() != null)
            data.put(RECURRENCE_PAYMENT_REQUESTS_QUANTITY, recurrence.getPaymentRequestsQuantity());

        /**
         * Set period
         */
        if (recurrence.getPeriod() != null)
            data.put(RECURRENCE_PERIOD, recurrence.getPeriod().toString());

        return data;
    }

    /**
     * Reads the recurrence request code when the request is successful
     * 
     * @param connection
     * @return recurrence code
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static String readSuccessXml(HttpURLConnection connection) throws ParserConfigurationException,
            SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(connection.getInputStream());
        Element recurrenceReturnElement = doc.getDocumentElement();

        return XMLParserUtils.getTagValue("code", recurrenceReturnElement);
    }

    /**
     * Reads the recurrence cancel information
     * 
     * @return payment request code
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static RecurrenceCancelTransaction readCancelXml(InputStream xmlInputStream)
            throws ParserConfigurationException, SAXException, IOException, ParseException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputSource is = new InputSource(xmlInputStream);
        Document doc = dBuilder.parse(is);

        Element recurrenceReturnElement = doc.getDocumentElement();
        RecurrenceCancelTransaction cancelTransaction = new RecurrenceCancelTransaction();
        String tagValue = null;

        // parsing <recurrenceCancel><code>
        tagValue = XMLParserUtils.getTagValue("code", recurrenceReturnElement);
        if (tagValue != null)
            cancelTransaction.setCode(tagValue);

        // parsing <recurrenceCancel><date>
        tagValue = XMLParserUtils.getTagValue("date", recurrenceReturnElement);
        if (tagValue != null)
            cancelTransaction.setDate(PagSeguroUtil.parse(tagValue));

        return cancelTransaction;
    }

    public static RecurrenceTransaction readRecurrence(InputStream xmlInputStream) throws ParserConfigurationException,
            SAXException, IOException, ParseException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputSource is = new InputSource(xmlInputStream);
        Document doc = dBuilder.parse(is);

        String tagValue = null;

        Element recurrenceElement = doc.getDocumentElement();

        RecurrenceTransaction recurrenceTransaction = new RecurrenceTransaction();

        RecurrenceParser.log.debug("Parsing recurrence");

        // parsing <recurrence><code>
        tagValue = XMLParserUtils.getTagValue("code", recurrenceElement);
        if (tagValue != null) {
            recurrenceTransaction.setCode(tagValue);
        }

        // parsing <recurrence><status>
        tagValue = XMLParserUtils.getTagValue("status", recurrenceElement);
        if (tagValue != null) {
            recurrenceTransaction.setStatus(RecurrenceStatus.fromValue(tagValue.charAt(0)));
        }

        // parsing <recurrence><period>
        tagValue = XMLParserUtils.getTagValue("period", recurrenceElement);
        if (tagValue != null) {
            recurrenceTransaction.setPeriod(RecurrencePeriod.valueOf(tagValue));
        }

        // parsing <recurrence><paymentRequestsQuantity>
        tagValue = XMLParserUtils.getTagValue("paymentRequestsQuantity", recurrenceElement);
        if (tagValue != null) {
            recurrenceTransaction.setPaymentRequestsQuantity(Integer.valueOf(tagValue));
        }

        // parsing <recurrence><initialDate>
        tagValue = XMLParserUtils.getTagValue("initialDate", recurrenceElement);
        if (tagValue != null) {
            recurrenceTransaction.setInitialDate(PagSeguroUtil.parse(tagValue));
        }

        // parsing <recurrence><paymentRequest>
        Element paymentRequestElement = XMLParserUtils.getElement("paymentRequest", recurrenceElement);
        if (paymentRequestElement != null) {
            PaymentRequest paymentRequest = new PaymentRequest();

            // parsing <recurrence><paymentRequest><sender>
            Element senderElement = XMLParserUtils.getElement("sender", paymentRequestElement);
            if (senderElement != null) {

                Sender sender = new Sender();

                // setting <recurrence><paymentRequest><sender><email>
                tagValue = XMLParserUtils.getTagValue("email", senderElement);
                if (tagValue != null) {
                    sender.setEmail(tagValue);
                }

                // setting <recurrence><paymentRequest><sender><name>
                tagValue = XMLParserUtils.getTagValue("name", senderElement);
                if (tagValue != null) {
                    sender.setName(tagValue);
                }

                paymentRequest.setSender(sender);
            }

            // parsing <recurrence><paymentRequest><items>
            Element itemsElement = XMLParserUtils.getElement("items", paymentRequestElement);
            if (itemsElement != null) {
                List<Element> itElements = XMLParserUtils.getElements("item", itemsElement);
                List<PaymentRequestItem> items = new ArrayList<PaymentRequestItem>();

                for (Element itElement : itElements) {

                    // setting <recurrence><paymentRequest><items><item>
                    PaymentRequestItem item = new PaymentRequestItem();

                    // setting <recurrence><paymentRequest><items><item><description>
                    tagValue = XMLParserUtils.getTagValue("description", itElement);
                    if (tagValue != null) {
                        item.setDescription(tagValue);
                    }

                    // setting <recurrence><paymentRequest><items><item><amount>
                    tagValue = XMLParserUtils.getTagValue("amount", itElement);
                    if (tagValue != null) {
                        item.setAmount(new BigDecimal(tagValue));
                    }

                    // setting <recurrence><paymentRequest><items><item><quantity>
                    tagValue = XMLParserUtils.getTagValue("quantity", itElement);
                    if (tagValue != null) {
                        item.setQuantity(Integer.valueOf(tagValue));
                    }

                    // setting <recurrence<paymentRequest><items><item><id>
                    tagValue = XMLParserUtils.getTagValue("id", itElement);
                    if (tagValue != null) {
                        item.setId(tagValue);
                    }

                    // adding item for items list
                    items.add(item);
                }

                paymentRequest.setItems(items);
            }

            // parsing <recurrence><paymentRequest><shipping>
            Element shippingElement = XMLParserUtils.getElement("shipping", paymentRequestElement);
            if (shippingElement != null) {

                // creating new PaymentRequestShipping object
                PaymentRequestShipping shipping = new PaymentRequestShipping();

                // setting <recurrence><paymentRequest><shipping><cost>
                tagValue = XMLParserUtils.getTagValue("cost", shippingElement);
                if (tagValue != null) {
                    shipping.setCost(new BigDecimal(tagValue));
                }

                // parsing <recurrence><paymentRequest><shipping><package>
                Element packageElement = XMLParserUtils.getElement("package", shippingElement);
                if (packageElement != null) {

                    // creating new PaymentRequestShippingPackage object
                    PaymentRequestShippingPackage shippingPackage = new PaymentRequestShippingPackage();

                    // setting <recurrence><paymentRequest><shipping><package><weight>
                    tagValue = XMLParserUtils.getTagValue("weight", packageElement);
                    if (tagValue != null) {
                        shippingPackage.setWeight(new BigDecimal(tagValue));
                    }

                    // setting <recurrence><paymentRequest><shipping><package><width>
                    tagValue = XMLParserUtils.getTagValue("width", packageElement);
                    if (tagValue != null) {
                        shippingPackage.setWidth(Integer.valueOf(tagValue));
                    }

                    // setting <recurrence><paymentRequest><shipping><package><height>
                    tagValue = XMLParserUtils.getTagValue("height", packageElement);
                    if (tagValue != null) {
                        shippingPackage.setHeight(Integer.valueOf(tagValue));
                    }

                    // setting <recurrence><paymentRequest><shipping><package><length>
                    tagValue = XMLParserUtils.getTagValue("length", packageElement);
                    if (tagValue != null) {
                        shippingPackage.setLength(Integer.valueOf(tagValue));
                    }

                    shipping.setPaymentRequestPackage(shippingPackage);
                }

                // setting <recurrence><paymentRequest><shipping>
                paymentRequest.setShipping(shipping);
            }

            // parsing <recurrence><paymentRequest><name>
            tagValue = XMLParserUtils.getTagValue("name", paymentRequestElement);
            if (tagValue != null) {
                paymentRequest.setName(tagValue);
            }

            // parsing <recurrence><paymentRequest><description>
            tagValue = XMLParserUtils.getTagValue("description", paymentRequestElement);
            if (tagValue != null) {
                paymentRequest.setDescription(tagValue);
            }

            // parsing <recurrence><paymentRequest><expiration>
            tagValue = XMLParserUtils.getTagValue("expiration", paymentRequestElement);
            if (tagValue != null) {
                paymentRequest.setExpiration(Integer.valueOf(tagValue));
            }

            // parsing <recurrence><paymentRequest><reference>
            tagValue = XMLParserUtils.getTagValue("reference", paymentRequestElement);
            if (tagValue != null) {
                paymentRequest.setReference(tagValue);
            }

            // parsing <recurrence><paymentRequest><due>
            tagValue = XMLParserUtils.getTagValue("due", paymentRequestElement);
            if (tagValue != null) {
                paymentRequest.setDue(Integer.valueOf(tagValue));
            }

            recurrenceTransaction.setPaymentRequest(paymentRequest);
        }

        RecurrenceParser.log.debug("Parsing recurrence success: " + recurrenceTransaction.getCode());

        return recurrenceTransaction;
    }
}
