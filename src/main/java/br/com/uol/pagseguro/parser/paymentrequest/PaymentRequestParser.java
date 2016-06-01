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

package br.com.uol.pagseguro.parser.paymentrequest;

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

import br.com.uol.pagseguro.domain.AutomaticDebit;
import br.com.uol.pagseguro.domain.PaymentMethod;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.domain.Receiver;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequest;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestItem;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestReceiverFees;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestSender;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestShipping;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestShippingPackage;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestTransaction;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestType;
import br.com.uol.pagseguro.enums.PaymentMethodCode;
import br.com.uol.pagseguro.enums.PaymentMethodType;
import br.com.uol.pagseguro.enums.TransactionStatus;
import br.com.uol.pagseguro.enums.TransactionType;
import br.com.uol.pagseguro.helper.PagSeguroUtil;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.logs.Logger;
import br.com.uol.pagseguro.xmlparser.XMLParserUtils;

/**
 * Parses payment requests and responses
 */
public class PaymentRequestParser {

    /**
     * PagSeguro Log tool
     * 
     * @see Logger
     */
    private static final Log log = new Log(PaymentRequestParser.class);

    public static final String PAYMENT_REQUEST_NAME = "name";
    public static final String PAYMENT_REQUEST_REFERENCE = "reference";
    public static final String PAYMENT_REQUEST_EXPIRATION = "expiration";
    public static final String PAYMENT_REQUEST_DUE = "due";
    public static final String PAYMENT_REQUEST_SENDER_EMAIL = "sender.email";
    public static final String PAYMENT_REQUEST_SENDER_NAME = "sender.name";
    public static final String PAYMENT_REQUEST_ITEM_PREFIX = "item[";
    public static final String PAYMENT_REQUEST_ITEM_DESCRIPTION = "].description";
    public static final String PAYMENT_REQUEST_ITEM_QUANTITY = "].quantity";
    public static final String PAYMENT_REQUEST_ITEM_AMOUNT = "].amount";
    public static final String PAYMENT_REQUEST_ITEM_ID = "].id";
    public static final String PAYMENT_REQUEST_SHIPPING_PREFIX = "shipping";
    public static final String PAYMENT_REQUEST_SHIPPING_COST = PAYMENT_REQUEST_SHIPPING_PREFIX + ".cost";
    public static final String PAYMENT_REQUEST_SHIPPING_PACKAGE_PREFIX = ".package";
    public static final String PAYMENT_REQUEST_SHIPPING_PACKAGE_WIDTH = PAYMENT_REQUEST_SHIPPING_PREFIX
            + PAYMENT_REQUEST_SHIPPING_PACKAGE_PREFIX + ".width";
    public static final String PAYMENT_REQUEST_SHIPPING_PACKAGE_HEIGHT = PAYMENT_REQUEST_SHIPPING_PREFIX
            + PAYMENT_REQUEST_SHIPPING_PACKAGE_PREFIX + ".height";
    public static final String PAYMENT_REQUEST_SHIPPING_PACKAGE_LENGTH = PAYMENT_REQUEST_SHIPPING_PREFIX
            + PAYMENT_REQUEST_SHIPPING_PACKAGE_PREFIX + ".length";
    public static final String PAYMENT_REQUEST_SHIPPING_PACKAGE_WEIGHT = PAYMENT_REQUEST_SHIPPING_PREFIX
            + PAYMENT_REQUEST_SHIPPING_PACKAGE_PREFIX + ".weight";

    private PaymentRequestParser() {

    }

    /**
     * 
     * @param paymentRequest
     * @return mixed
     */
    public static Map<Object, Object> getData(PaymentRequest paymentRequest) {
        Map<Object, Object> data = new HashMap<Object, Object>();

        /**
         * Set payment request name
         */
        if (paymentRequest.getName() != null)
            data.put(PAYMENT_REQUEST_NAME, paymentRequest.getName());

        /**
         * Set payment request description
         */
        if (paymentRequest.getDescription() != null)
            data.put("description", paymentRequest.getDescription());

        /**
         * Set payment request reference
         */
        if (paymentRequest.getReference() != null)
            data.put(PAYMENT_REQUEST_REFERENCE, paymentRequest.getReference());

        /**
         * Set payment request expiration days
         */
        if (paymentRequest.getExpiration() != null)
            data.put(PAYMENT_REQUEST_EXPIRATION, paymentRequest.getExpiration());

        /**
         * Set payment request due days
         */
        if (paymentRequest.getDue() != null)
            data.put(PAYMENT_REQUEST_DUE, paymentRequest.getDue());

        /**
         * Set payment request sender information
         * 
         * @see PaymentRequestSender
         */
        if (paymentRequest.getSender() != null) {

            /**
             * Set payment request sender email
             */
            if (paymentRequest.getSender().getEmail() != null)
                data.put(PAYMENT_REQUEST_SENDER_EMAIL, paymentRequest.getSender().getEmail());

            /**
             * Set payment request sender name
             */
            if (paymentRequest.getSender().getName() != null)
                data.put(PAYMENT_REQUEST_SENDER_NAME, paymentRequest.getSender().getName());
        }

        /**
         * Set payment request items
         * 
         * @see PaymentRequestItem
         */
        if (paymentRequest.getItems() != null && !paymentRequest.getItems().isEmpty()) {
            Integer count = 1;

            for (PaymentRequestItem item : paymentRequest.getItems()) {
                if (item.getDescription() != null)
                    data.put(PAYMENT_REQUEST_ITEM_PREFIX + count.toString() + PAYMENT_REQUEST_ITEM_DESCRIPTION,
                            item.getDescription());

                if (item.getAmount() != null)
                    data.put(PAYMENT_REQUEST_ITEM_PREFIX + count.toString() + PAYMENT_REQUEST_ITEM_AMOUNT,
                            item.getAmount());

                if (item.getQuantity() != null)
                    data.put(PAYMENT_REQUEST_ITEM_PREFIX + count.toString() + PAYMENT_REQUEST_ITEM_QUANTITY,
                            item.getQuantity());

                if (item.getId() != null)
                    data.put(PAYMENT_REQUEST_ITEM_PREFIX + count.toString() + PAYMENT_REQUEST_ITEM_ID, item.getId());

                count++;
            }
        }

        /**
         * Set payment request shipping
         * 
         * @see PaymentRequestShipping
         */
        if (paymentRequest.getShipping() != null) {
            if (paymentRequest.getShipping().getCost() != null)
                data.put(PAYMENT_REQUEST_SHIPPING_COST, paymentRequest.getShipping().getCost());

            if (paymentRequest.getShipping().getPaymentRequestShippingPackage() != null) {
                if (paymentRequest.getShipping().getPaymentRequestShippingPackage().getWidth() != null)
                    data.put(PAYMENT_REQUEST_SHIPPING_PACKAGE_WIDTH, paymentRequest.getShipping()
                            .getPaymentRequestShippingPackage().getWidth());

                if (paymentRequest.getShipping().getPaymentRequestShippingPackage().getHeight() != null)
                    data.put(PAYMENT_REQUEST_SHIPPING_PACKAGE_HEIGHT, paymentRequest.getShipping()
                            .getPaymentRequestShippingPackage().getHeight());

                if (paymentRequest.getShipping().getPaymentRequestShippingPackage().getLength() != null)
                    data.put(PAYMENT_REQUEST_SHIPPING_PACKAGE_LENGTH, paymentRequest.getShipping()
                            .getPaymentRequestShippingPackage().getLength());

                if (paymentRequest.getShipping().getPaymentRequestShippingPackage().getWeight() != null)
                    data.put(PAYMENT_REQUEST_SHIPPING_PACKAGE_WEIGHT, paymentRequest.getShipping()
                            .getPaymentRequestShippingPackage().getWeight());
            }

        }

        return data;
    }

    public static PaymentRequestTransaction readPaymentRequest(InputStream xmlInputStream)
            throws ParserConfigurationException, SAXException, IOException, ParseException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputSource is = new InputSource(xmlInputStream);
        Document doc = dBuilder.parse(is);

        String tagValue = null;

        Element paymentRequestElement = doc.getDocumentElement();

        PaymentRequestTransaction paymentRequestTransaction = new PaymentRequestTransaction();

        PaymentRequestParser.log.debug("Parsing payment request");

        // parsing <paymentRequest><date>
        tagValue = XMLParserUtils.getTagValue("date", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setDate(PagSeguroUtil.parse(tagValue));
        }

        // parsing <paymentRequest><code>
        tagValue = XMLParserUtils.getTagValue("code", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setCode(tagValue);
        }

        // parsing <paymentRequest><reference>
        tagValue = XMLParserUtils.getTagValue("reference", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setReference(tagValue);
        }

        // parsing <paymentRequest><recoveryCode>
        tagValue = XMLParserUtils.getTagValue("recoveryCode", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setRecoveryCode(tagValue);
        }

        // parsing <paymentRequest><type>
        tagValue = XMLParserUtils.getTagValue("type", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setType(TransactionType.fromValue((Integer.valueOf(tagValue))));
        }

        // parsing <paymentRequest><paymentType>
        tagValue = XMLParserUtils.getTagValue("paymentType", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setPaymentRequestType(PaymentRequestType.fromValue(Integer.parseInt(tagValue)));
        }

        // parsing <paymentRequest><status>
        tagValue = XMLParserUtils.getTagValue("status", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setStatus(TransactionStatus.fromValue((Integer.valueOf(tagValue))));
        }

        // parsing <paymentRequest><cancellationSource>
        tagValue = XMLParserUtils.getTagValue("cancellationSource", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setCancellationSource(tagValue);
        }

        // parsing <paymentRequest><expiration>
        tagValue = XMLParserUtils.getTagValue("expiration", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setExpiration(PagSeguroUtil.parse(tagValue));
        }

        // parsing <paymentRequest><due>
        tagValue = XMLParserUtils.getTagValue("due", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setDue(PagSeguroUtil.parse(tagValue));
        }

        // parsing <paymentRequest><lastEventDate>
        tagValue = XMLParserUtils.getTagValue("lastEventDate", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setLastEventDate(PagSeguroUtil.parse(tagValue));
        }

        // parsing <paymentRequest><paymentMethod>
        Element paymentMethodElement = XMLParserUtils.getElement("paymentMethod", paymentRequestElement);
        if (paymentMethodElement != null) {
            PaymentMethod paymentMethod = new PaymentMethod();

            // parsing <paymentRequest><paymentMethod><type>
            tagValue = XMLParserUtils.getTagValue("type", paymentMethodElement);
            if (tagValue != null) {
                paymentMethod.setType(PaymentMethodType.fromValue(Integer.valueOf(tagValue)));
            }

            // parsing <paymentRequest><paymentMethod><code>
            tagValue = XMLParserUtils.getTagValue("code", paymentMethodElement);
            if (tagValue != null) {
                paymentMethod.setCode(PaymentMethodCode.fromValue(Integer.valueOf(tagValue)));
            }

            // setting payment request transaction payment method
            paymentRequestTransaction.setPaymentMethod(paymentMethod);
        }

        // parsing <paymentRequest><automaticDebit>
        Element automaticDebitElement = XMLParserUtils.getElement("automaticDebit", paymentRequestElement);
        if (automaticDebitElement != null) {
            AutomaticDebit automaticDebit = new AutomaticDebit();

            // parsing <paymentRequest><automaticDebit><bank>
            tagValue = XMLParserUtils.getTagValue("bank", automaticDebitElement);
            if (tagValue != null) {
                automaticDebit.setBank(tagValue);
            }

            // parsing <paymentRequest><automaticDebit><scheduledDate>
            tagValue = XMLParserUtils.getTagValue("scheduledDate", automaticDebitElement);
            if (tagValue != null) {
                automaticDebit.setScheduledDate(PagSeguroUtil.parse(tagValue));
            }

            // parsing <paymentRequest><automaticDebit><debitDate>
            tagValue = XMLParserUtils.getTagValue("debitDate", automaticDebitElement);
            if (tagValue != null) {
                automaticDebit.setDebitDate(PagSeguroUtil.parse(tagValue));
            }

            // setting payment request transaction automatic debit information
            paymentRequestTransaction.setAutomaticDebit(automaticDebit);
        }

        // parsing <paymentRequest><grossAmount>
        tagValue = XMLParserUtils.getTagValue("grossAmount", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setGrossAmount(new BigDecimal(tagValue));
        }

        // parsing <paymentRequest><paidAmount>
        tagValue = XMLParserUtils.getTagValue("paidAmount", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setPaidAmount(new BigDecimal(tagValue));
        }

        // parsing <paymentRequest><discountAmount>
        tagValue = XMLParserUtils.getTagValue("discountAmount", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setDiscountAmount(new BigDecimal(tagValue));
        }

        // parsing <paymentRequest><receiver>
        Element receiverElement = XMLParserUtils.getElement("receiver", paymentRequestElement);
        if (receiverElement != null) {
            Receiver receiver = new Receiver();

            // parsing <paymentRequest><receiver><name>
            tagValue = XMLParserUtils.getTagValue("name", receiverElement);
            if (tagValue != null) {
                receiver.setName(tagValue);
            }

            // parsing <paymentRequest><receiver><email>
            tagValue = XMLParserUtils.getTagValue("email", receiverElement);
            if (tagValue != null) {
                receiver.setEmail(tagValue);
            }

            // parsing <paymentRequest><receiver><phone>
            Element receiverPhoneElement = XMLParserUtils.getElement("phone", receiverElement);
            if (receiverPhoneElement != null) {
                Phone phone = new Phone();

                // parsing <paymentRequest><receiver><phone><areaCode>
                tagValue = XMLParserUtils.getTagValue("areaCode", receiverPhoneElement);
                if (tagValue != null) {
                    phone.setAreaCode(tagValue);
                }

                // parsing <paymentRequest><receiver><phone><number>
                tagValue = XMLParserUtils.getTagValue("number", receiverPhoneElement);
                if (tagValue != null) {
                    phone.setNumber(tagValue);
                }

                receiver.setPhone(phone);
            }

            // setting payment request transaction receiver information
            paymentRequestTransaction.setReceiver(receiver);
        }

        // parsing <paymentRequest><receiverFees>
        Element receiverFeesElement = XMLParserUtils.getElement("receiverFees", paymentRequestElement);
        if (receiverFeesElement != null) {
            PaymentRequestReceiverFees receiverFees = new PaymentRequestReceiverFees();

            // parsing <paymentRequest><receiverFees><installmentFeeAmount>
            tagValue = XMLParserUtils.getTagValue("installmentFeeAmount", receiverFeesElement);
            if (tagValue != null) {
                receiverFees.setInstallmentFeeAmount(new BigDecimal(tagValue));
            }

            // parsing <paymentRequest><receiverFees><operationalFeeAmount>
            tagValue = XMLParserUtils.getTagValue("operationalFeeAmount", receiverFeesElement);
            if (tagValue != null) {
                receiverFees.setOperationalFeeAmount(new BigDecimal(tagValue));
            }

            // parsing <paymentRequest><receiverFees><intermediationRateAmount>
            tagValue = XMLParserUtils.getTagValue("intermediationRateAmount", receiverFeesElement);
            if (tagValue != null) {
                receiverFees.setIntermediationRateAmount(new BigDecimal(tagValue));
            }

            // parsing <paymentRequest><receiverFees><intermediationFeeAmount>
            tagValue = XMLParserUtils.getTagValue("intermediationFeeAmount", receiverFeesElement);
            if (tagValue != null) {
                receiverFees.setIntermediationFeeAmount(new BigDecimal(tagValue));
            }

            // parsing <paymentRequest><receiverFees><commissionFeeAmount>
            tagValue = XMLParserUtils.getTagValue("commissionFeeAmount", receiverFeesElement);
            if (tagValue != null) {
                receiverFees.setCommissionFeeAmount(new BigDecimal(tagValue));
            }

            // parsing <paymentRequest><receiverFees><efrete>
            tagValue = XMLParserUtils.getTagValue("efrete", receiverFeesElement);
            if (tagValue != null) {
                receiverFees.setEfrete(new BigDecimal(tagValue));
            }

            // setting payment request transaction receiver fees
            paymentRequestTransaction.setReceiverFees(receiverFees);
        }

        // parsing <paymentRequest><netAmount>
        tagValue = XMLParserUtils.getTagValue("netAmount", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setNetAmount(new BigDecimal(tagValue));
        }

        // parsing <paymentRequest><description>
        tagValue = XMLParserUtils.getTagValue("description", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setDescription(tagValue);
        }

        // parsing <paymentRequest><escrowEndDate>
        tagValue = XMLParserUtils.getTagValue("escrowEndDate", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setEscrowEndDate(PagSeguroUtil.parse(tagValue));
        }

        // parsing <paymentRequest><installmentCount>
        tagValue = XMLParserUtils.getTagValue("installmentCount", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setInstallmentCount(Integer.valueOf(tagValue));
        }

        // parsing <paymentRequest><itemCount>
        tagValue = XMLParserUtils.getTagValue("itemCount", paymentRequestElement);
        if (tagValue != null) {
            paymentRequestTransaction.setItemCount(Integer.valueOf(tagValue));
        }

        // parsing <paymentRequest><items>
        Element itemsElement = XMLParserUtils.getElement("items", paymentRequestElement);
        if (itemsElement != null) {
            List<Element> itElements = XMLParserUtils.getElements("item", itemsElement);
            List<PaymentRequestItem> items = new ArrayList<PaymentRequestItem>();

            for (Element itElement : itElements) {

                // setting <paymentRequest><items><item>
                PaymentRequestItem item = new PaymentRequestItem();

                // setting <paymentRequest><items><item><id>
                tagValue = XMLParserUtils.getTagValue("id", itElement);
                if (tagValue != null) {
                    item.setId(tagValue);
                }

                // setting <paymentRequest><items><item><description>
                tagValue = XMLParserUtils.getTagValue("description", itElement);
                if (tagValue != null) {
                    item.setDescription(tagValue);
                }

                // setting <paymentRequest><items><item><quantity>
                tagValue = XMLParserUtils.getTagValue("quantity", itElement);
                if (tagValue != null) {
                    item.setQuantity(Integer.valueOf(tagValue));
                }

                // setting <paymentRequest><items><item><amount>
                tagValue = XMLParserUtils.getTagValue("amount", itElement);
                if (tagValue != null) {
                    item.setAmount(new BigDecimal(tagValue));
                }

                // adding item for items list
                items.add(item);
            }

            paymentRequestTransaction.setItems(items);
        }

        // parsing <paymentRequest><sender>
        Element senderElement = XMLParserUtils.getElement("sender", paymentRequestElement);
        if (senderElement != null) {

            PaymentRequestSender sender = new PaymentRequestSender();

            // setting <paymentRequest><sender><email>
            tagValue = XMLParserUtils.getTagValue("email", senderElement);
            if (tagValue != null) {
                sender.setEmail(tagValue);
            }

            // setting <paymentRequest><sender><name>
            tagValue = XMLParserUtils.getTagValue("name", senderElement);
            if (tagValue != null) {
                sender.setName(tagValue);
            }

            // setting <paymentRequest><sender><phone>
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
            }

            paymentRequestTransaction.setSender(sender);
        }

        // parsing <paymentRequest><shipping>
        Element shippingElement = XMLParserUtils.getElement("shipping", paymentRequestElement);
        if (shippingElement != null) {

            // creating new PaymentRequestShipping object
            PaymentRequestShipping shipping = new PaymentRequestShipping();

            // setting <paymentRequest><shipping><cost>
            tagValue = XMLParserUtils.getTagValue("cost", shippingElement);
            if (tagValue != null) {
                shipping.setCost(new BigDecimal(tagValue));
            }

            // parsing <paymentRequest><shipping><package>
            Element packageElement = XMLParserUtils.getElement("package", shippingElement);
            if (packageElement != null) {

                // creating new PaymentRequestShippingPackage object
                PaymentRequestShippingPackage shippingPackage = new PaymentRequestShippingPackage();

                // setting <paymentRequest><shipping><package><weight>
                tagValue = XMLParserUtils.getTagValue("weight", packageElement);
                if (tagValue != null) {
                    shippingPackage.setWeight(new BigDecimal(tagValue));
                }

                // setting <paymentRequest><shipping><package><width>
                tagValue = XMLParserUtils.getTagValue("width", packageElement);
                if (tagValue != null) {
                    shippingPackage.setWidth(Integer.valueOf(tagValue));
                }

                // setting <paymentRequest><shipping><package><height>
                tagValue = XMLParserUtils.getTagValue("height", packageElement);
                if (tagValue != null) {
                    shippingPackage.setHeight(Integer.valueOf(tagValue));
                }

                // setting <paymentRequest><shipping><package><length>
                tagValue = XMLParserUtils.getTagValue("length", packageElement);
                if (tagValue != null) {
                    shippingPackage.setLength(Integer.valueOf(tagValue));
                }

                shipping.setPaymentRequestPackage(shippingPackage);
            }

            // setting <paymentRequest><shipping>
            paymentRequestTransaction.setShipping(shipping);
        }

        PaymentRequestParser.log.debug("Parsing payment request success: " + paymentRequestTransaction.getCode());

        return paymentRequestTransaction;
    }

    /**
     * Reads the payment request code when the request is successful
     * 
     * @param connection
     * @return payment request code
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static String readSuccessXml(HttpURLConnection connection) throws ParserConfigurationException,
            SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(connection.getInputStream());
        Element paymentRequestReturnElement = doc.getDocumentElement();

        return XMLParserUtils.getTagValue("code", paymentRequestReturnElement);
    }
}
