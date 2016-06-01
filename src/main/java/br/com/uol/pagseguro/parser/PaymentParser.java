/*
 * ***********************************************************************
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
 * ***********************************************************************
 */

package br.com.uol.pagseguro.parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.MetaDataItem;
import br.com.uol.pagseguro.domain.ParameterItem;
import br.com.uol.pagseguro.domain.PaymentRequest;
import br.com.uol.pagseguro.domain.SenderDocument;
import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.parser.checkout.CheckoutParser;

/**
 * Parses PaymentRequests and responses
 * 
 * @deprecated use {@link CheckoutParser} instead.
 */
@Deprecated
public class PaymentParser {

    private PaymentParser() {

    }

    /**
     * 
     * @param checkout
     * @return mixed
     */
    public static Map<Object, Object> getData(Checkout checkout) {
        return CheckoutParser.getData(checkout);
    }

    /**
     * 
     * @param payment
     * @return mixed
     *
     * @deprecated use {@link #getData(Checkout)} instead.
     */
    @Deprecated
    public static Map<Object, Object> getData(PaymentRequest payment) {

        Map<Object, Object> data = new HashMap<Object, Object>();

        // SET REFERENCE
        if (payment.getReference() != null) {
            data.put("reference", payment.getReference());
        }

        /**
         * SET SENDER
         * 
         * @see Sender
         */
        if (payment.getSender() != null) {

            if (payment.getSender().getName() != null) {
                data.put("senderName", payment.getSender().getName());
            }

            if (payment.getSender().getEmail() != null) {
                data.put("senderEmail", payment.getSender().getEmail());
            }

            if (payment.getSender().getBornDate() != null) {
                data.put("senderBornDate", payment.getSender().getBornDate());
            }

            /**
             * SET PHONE
             * 
             * @see PHONE
             */
            if (payment.getSender().getPhone() != null) {

                if (payment.getSender().getPhone().getAreaCode() != null) {
                    data.put("senderAreaCode", payment.getSender().getPhone().getAreaCode());
                }
                if (payment.getSender().getPhone().getNumber() != null) {
                    data.put("senderPhone", payment.getSender().getPhone().getNumber());
                }
            }

            /**
             * SET DOCUMENTS
             * 
             * @see Documents
             */
            if (payment.getSender().getDocuments() != null) {

                List<SenderDocument> documents = payment.getSender().getDocuments();

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
        if (payment.getCurrency() != null) {
            data.put("currency", payment.getCurrency());
        }
        /**
         * SET ITEMS
         * 
         * @see ITEMS
         */

        if (payment.getItems() != null && payment.getItems().size() > 0) {

            Integer count = 0;

            for (Item item : payment.getItems()) {
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
        if (payment.getExtraAmount() != null) {
            data.put("extraAmount", payment.getExtraAmount());
        }
        /**
         * SET SHIPPING
         * 
         * @see SHIPPING
         */
        if (payment.getShipping() != null) {

            if (payment.getShipping().getType() != null && (payment.getShipping().getType().getValue() != null)) {
                data.put("shippingType", payment.getShipping().getType().getValue());
            }
            if (payment.getShipping().getCost() != null) {
                data.put("shippingCost", payment.getShipping().getCost());
            }
            /**
             * ADDRESS
             * 
             * @see Address
             */
            if (payment.getShipping().getAddress() != null) {

                if (payment.getShipping().getAddress().getStreet() != null) {
                    data.put("shippingAddressStreet", payment.getShipping().getAddress().getStreet());
                }
                if (payment.getShipping().getAddress().getNumber() != null) {
                    data.put("shippingAddressNumber", payment.getShipping().getAddress().getNumber());
                }
                if (payment.getShipping().getAddress().getComplement() != null) {
                    data.put("shippingAddressComplement", payment.getShipping().getAddress().getComplement());
                }
                if (payment.getShipping().getAddress().getCity() != null) {
                    data.put("shippingAddressCity", payment.getShipping().getAddress().getCity());
                }
                if (payment.getShipping().getAddress().getState() != null) {
                    data.put("shippingAddressState", payment.getShipping().getAddress().getState());
                }
                if (payment.getShipping().getAddress().getDistrict() != null) {
                    data.put("shippingAddressDistrict", payment.getShipping().getAddress().getDistrict());
                }
                if (payment.getShipping().getAddress().getPostalCode() != null) {
                    data.put("shippingAddressPostalCode", payment.getShipping().getAddress().getPostalCode());
                }
                if (payment.getShipping().getAddress().getCountry() != null) {
                    data.put("shippingAddressCountry", payment.getShipping().getAddress().getCountry());
                }
            }

        }

        /**
         * MAX AGE
         */
        if (payment.getMaxAge() != null) {
            data.put("maxAge", payment.getMaxAge());
        }
        /**
         * MAX USES
         */
        if (payment.getMaxUses() != null) {
            data.put("maxUses", payment.getMaxUses());
        }
        /**
         * REDIRECT URL
         */
        if (payment.getRedirectURL() != null && !"".equals(payment.getRedirectURL())) {
            data.put("redirectURL", payment.getRedirectURL());
        }
        /**
         * NOTIFICATION URL
         */
        if (payment.getNotificationURL() != null && !"".equals(payment.getNotificationURL())) {
            data.put("notificationURL", payment.getNotificationURL());
        }
        /**
         * META DATA
         * 
         * @see MetaData
         */
        if (payment.getMetaData() != null && payment.getMetaData().getItem() != null
                && !payment.getMetaData().getItem().isEmpty()) {

            Integer count = 0;

            for (MetaDataItem meta : payment.getMetaData().getItem()) {

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
        if (payment.getParameter() != null && payment.getParameter().getItems() != null
                && !payment.getParameter().getItems().isEmpty()) {

            for (ParameterItem param : payment.getParameter().getItems()) {

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
        return CheckoutParser.readSuccessXml(connection);
    }
}
