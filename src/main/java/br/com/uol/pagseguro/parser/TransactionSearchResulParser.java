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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.TransactionSearchResult;
import br.com.uol.pagseguro.domain.TransactionSummary;
import br.com.uol.pagseguro.helper.PagSeguroUtil;

/**
 * Utility class for handle transactions search in PagSeguro's web service
 */
public class TransactionSearchResulParser extends DefaultHandler {

    private List<String> handledElements = new ArrayList<String>();

    private static final String ROOT_ELEMENT = "transactionSearchResult";
    private static final String TRANSACTIONS_ELEMENT = "transactions";

    private String parentElement = ROOT_ELEMENT;
    private String currentElement = ROOT_ELEMENT;

    private StringBuilder xmlTransaction;

    private TransactionSearchResult transactionSearchResult;
    private List<TransactionSummary> transactions = new ArrayList<TransactionSummary>();

    public TransactionSearchResulParser(TransactionSearchResult transactionSearchResult) {

        handledElements = new ArrayList<String>();
        handledElements.add("date");
        handledElements.add("currentPage");
        handledElements.add("resultsInThisPage");
        handledElements.add("totalPages");

        this.transactionSearchResult = transactionSearchResult;

    }

    public TransactionSearchResult getTransactionSearchResult() {
        return transactionSearchResult;
    }

    public void setTransactionSearchResult(TransactionSearchResult transactionSearchResult) {
        this.transactionSearchResult = transactionSearchResult;
    }

    @Override
    public void characters(char[] buffer, int start, int length) throws SAXException {

        StringBuilder buf = new StringBuilder();

        if (parentElement.equals(ROOT_ELEMENT)) {

            if ("date".equals(currentElement)) {

                try {
                    transactionSearchResult.setDate(PagSeguroUtil.parse(buf.append(buffer, start, length).toString()));
                } catch (ParseException e) {
                    throw new SAXException(e);
                }

            } else if ("currentPage".equals(currentElement)) {
                transactionSearchResult.setPage(Integer.parseInt(buf.append(buffer, start, length).toString()));
            } else if ("resultsInThisPage".equals(currentElement)) {
                transactionSearchResult.setResultsInThisPage(Integer.parseInt(buf.append(buffer, start, length)
                        .toString()));
            } else if ("totalPages".equals(currentElement)) {
                transactionSearchResult.setTotalPages(Integer.parseInt(buf.append(buffer, start, length).toString()));
            }
        } else if ("transaction".equals(currentElement)) {
            xmlTransaction.append(buffer, start, length);
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (handledElements.contains(qName) && parentElement.equals(ROOT_ELEMENT)) {
            currentElement = qName;
        }

        if (qName.equals(TRANSACTIONS_ELEMENT)) {
            parentElement = TRANSACTIONS_ELEMENT;
        }

        if ("transaction".equals(qName)) {
            currentElement = "transaction";
            xmlTransaction = new StringBuilder();
        }

        if ("transaction".equals(currentElement)) {
            xmlTransaction.append("<" + qName + ">");
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equals(TRANSACTIONS_ELEMENT)) {
            parentElement = ROOT_ELEMENT;
        }

        if ("transaction".equals(currentElement)) {
            xmlTransaction.append("</" + qName + ">");
        }

        if ("transaction".equals(qName)) {

            try {

                Transaction transaction = TransactionParser.readTransaction(new ByteArrayInputStream(xmlTransaction
                        .toString().getBytes()));
                TransactionSummary transactionSummary = buildTransactionSummary(transaction);
                transactions.add(transactionSummary);

            } catch (ParserConfigurationException e) {
                throw new SAXException(e);
            } catch (ParseException e) {
                throw new SAXException(e);
            } catch (IOException e) {
                throw new SAXException(e);
            }

        }
    }

    private TransactionSummary buildTransactionSummary(Transaction transaction) {

        TransactionSummary transactionSummary = new TransactionSummary();

        transactionSummary.setDate(transaction.getDate());
        transactionSummary.setLastEvent(transaction.getLastEventDate());
        transactionSummary.setCode(transaction.getCode());
        transactionSummary.setReference(transaction.getReference());
        transactionSummary.setType(transaction.getType());
        transactionSummary.setStatus(transaction.getStatus());
        transactionSummary.setPaymentMethod(transaction.getPaymentMethod());
        transactionSummary.setGrossAmount(transaction.getGrossAmount());
        transactionSummary.setDiscountAmount(transaction.getDiscountAmount());
        transactionSummary.setFeeAmount(transaction.getFeeAmount());
        transactionSummary.setNetAmount(transaction.getNetAmount());
        transactionSummary.setExtraAmount(transaction.getExtraAmount());

        return transactionSummary;

    }

    @Override
    public void endDocument() throws SAXException {
        transactionSearchResult.setTransactions(transactions);
    }

    public static TransactionSearchResulParser getHandler(InputStream xml,
            TransactionSearchResult transactionSearchResult) throws ParserConfigurationException, SAXException,
            IOException {

        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        InputSource input = new InputSource(xml);
        TransactionSearchResulParser handler = new TransactionSearchResulParser(transactionSearchResult);

        parser.parse(input, handler);

        return handler;

    }
}
