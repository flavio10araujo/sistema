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

package br.com.uol.pagseguro.parser.checkout;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.MetaDataItem;
import br.com.uol.pagseguro.domain.ParameterItem;
import br.com.uol.pagseguro.domain.SenderDocument;
import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.xmlparser.XMLParserUtils;

/**
 * Parses Checkout and responses
 */
public class CheckoutParser {

    private CheckoutParser() {

    }

    /**
     * 
     * @param checkout
     * @return mixed
     */
    public static Map<Object, Object> getData(Checkout checkout) {

        Map<Object, Object> data = new HashMap<Object, Object>();

        // SET REFERENCE
        if (checkout.getReference() != null) {
            data.put("reference", checkout.getReference());
        }

        /**
         * SET SENDER
         * 
         * @see Sender
         */
        if (checkout.getSender() != null) {

            if (checkout.getSender().getName() != null) {
                data.put("senderName", checkout.getSender().getName());
            }

            if (checkout.getSender().getEmail() != null) {
                data.put("senderEmail", checkout.getSender().getEmail());
            }

            if (checkout.getSender().getBornDate() != null) {
                data.put("senderBornDate", checkout.getSender().getBornDate());
            }

            /**
             * SET PHONE
             * 
             * @see PHONE
             */
            if (checkout.getSender().getPhone() != null) {

                if (checkout.getSender().getPhone().getAreaCode() != null) {
                    data.put("senderAreaCode", checkout.getSender().getPhone().getAreaCode());
                }
                if (checkout.getSender().getPhone().getNumber() != null) {
                    data.put("senderPhone", checkout.getSender().getPhone().getNumber());
                }
            }

            /**
             * SET DOCUMENTS
             * 
             * @see Documents
             */
            if (checkout.getSender().getDocuments() != null) {

                List<SenderDocument> documents = checkout.getSender().getDocuments();

                if (!documents.isEmpty()) {
                    for (SenderDocument document : documents) {
                        if (document.getValue() != null) {
                            data.put("senderCPF", document.getValue());
                        }
                    }
                }

            }

        }

        /**
         * SET CURRENCY
         * 
         * @see Cureencies
         */
        if (checkout.getCurrency() != null) {
            data.put("currency", checkout.getCurrency());
        }
        /**
         * SET ITEMS
         * 
         * @see ITEMS
         */

        if (checkout.getItems() != null && checkout.getItems().size() > 0) {

            Integer count = 0;

            for (Item item : checkout.getItems()) {
                count++;

                if (item.getId() != null && !"".equals(item.getId())) {
                    data.put("itemId" + count.toString(), item.getId());
                }

                if (item.getDescription() != null && !"".equals(item.getDescription())) {
                    data.put("itemDescription" + count.toString(), item.getDescription());
                }

                if (item.getQuantity() != null && item.getQuantity() > 0) {
                    data.put("itemQuantity" + count.toString(), item.getQuantity());
                }

                if (item.getAmount() != null) {
                    data.put("itemAmount" + count.toString(), item.getAmount());
                }

                if (item.getWeight() != null) {
                    data.put("itemWeight" + count.toString(), item.getWeight());
                }

                if (item.getShippingCost() != null) {
                    data.put("itemShippingCost" + count.toString(), item.getShippingCost());
                }
            }

        }

        /**
         * SET EXTRA AMOUNT
         */
        if (checkout.getExtraAmount() != null) {
            data.put("extraAmount", checkout.getExtraAmount());
        }
        /**
         * SET SHIPPING
         * 
         * @see SHIPPING
         */
        if (checkout.getShipping() != null) {

            if (checkout.getShipping().getType() != null && (checkout.getShipping().getType().getValue() != null)) {
                data.put("shippingType", checkout.getShipping().getType().getValue());
            }
            if (checkout.getShipping().getCost() != null) {
                data.put("shippingCost", checkout.getShipping().getCost());
            }
            /**
             * ADDRESS
             * 
             * @see Address
             */
            if (checkout.getShipping().getAddress() != null) {

                if (checkout.getShipping().getAddress().getStreet() != null) {
                    data.put("shippingAddressStreet", checkout.getShipping().getAddress().getStreet());
                }
                if (checkout.getShipping().getAddress().getNumber() != null) {
                    data.put("shippingAddressNumber", checkout.getShipping().getAddress().getNumber());
                }
                if (checkout.getShipping().getAddress().getComplement() != null) {
                    data.put("shippingAddressComplement", checkout.getShipping().getAddress().getComplement());
                }
                if (checkout.getShipping().getAddress().getCity() != null) {
                    data.put("shippingAddressCity", checkout.getShipping().getAddress().getCity());
                }
                if (checkout.getShipping().getAddress().getState() != null) {
                    data.put("shippingAddressState", checkout.getShipping().getAddress().getState());
                }
                if (checkout.getShipping().getAddress().getDistrict() != null) {
                    data.put("shippingAddressDistrict", checkout.getShipping().getAddress().getDistrict());
                }
                if (checkout.getShipping().getAddress().getPostalCode() != null) {
                    data.put("shippingAddressPostalCode", checkout.getShipping().getAddress().getPostalCode());
                }
                if (checkout.getShipping().getAddress().getCountry() != null) {
                    data.put("shippingAddressCountry", checkout.getShipping().getAddress().getCountry());
                }
            }

        }

        /**
         * MAX AGE
         */
        if (checkout.getMaxAge() != null) {
            data.put("maxAge", checkout.getMaxAge());
        }
        /**
         * MAX USES
         */
        if (checkout.getMaxUses() != null) {
            data.put("maxUses", checkout.getMaxUses());
        }
        /**
         * REDIRECT URL
         */
        if (checkout.getRedirectURL() != null && !"".equals(checkout.getRedirectURL())) {
            data.put("redirectURL", checkout.getRedirectURL());
        }
        /**
         * NOTIFICATION URL
         */
        if (checkout.getNotificationURL() != null && !"".equals(checkout.getNotificationURL())) {
            data.put("notificationURL", checkout.getNotificationURL());
        }
        /**
         * META DATA
         * 
         * @see MetaData
         */
        if (checkout.getMetaData() != null && checkout.getMetaData().getItem() != null
                && !checkout.getMetaData().getItem().isEmpty()) {

            Integer count = 0;

            for (MetaDataItem meta : checkout.getMetaData().getItem()) {

                if ((meta.getKey() != null && !"".equals(meta.getKey()))
                        && (meta.getValue() != null && !"".equals(meta.getValue()))) {

                    count++;
                    data.put("metadataItemKey" + count.toString(), meta.getKey());
                    data.put("metadataItemValue" + count.toString(), meta.getValue());

                    if (meta.getGroup() != null) {
                        data.put("metadataItemGroup" + count.toString(), meta.getGroup());
                    }

                }
            }

        }

        /**
         * PARAMETER
         * 
         * @see Parameter
         */
        if (checkout.getParameter() != null && checkout.getParameter().getItems() != null
                && !checkout.getParameter().getItems().isEmpty()) {

            for (ParameterItem param : checkout.getParameter().getItems()) {

                if ((param.getName() != null && !"".equals(param.getName()))
                        && (param.getValue() != null && !"".equals(param.getValue()))) {
                    if (param.getIndex() != null) {
                        data.put(param.getName() + "" + param.getIndex().toString(), param.getValue());
                    } else {
                        data.put(param.getName(), param.getValue());
                    }
                }
            }

        }

        return data;

    }

    public static String readSuccessXml(HttpURLConnection connection) throws ParserConfigurationException,
            SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(connection.getInputStream());
        Element paymentReturnElement = doc.getDocumentElement();

        return XMLParserUtils.getTagValue("code", paymentReturnElement);

    }
}
