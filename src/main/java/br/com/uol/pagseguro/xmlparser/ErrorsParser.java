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
package br.com.uol.pagseguro.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.properties.PagSeguroSystem;

/**
 * Class that parses error messages in PagSeguro web service responses
 */
public class ErrorsParser {

    private ErrorsParser() {
    }

    /**
     * read a response from PagSeguro web service and fill the error list
     * 
     * @param xml
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static List<Error> readErrosXml(InputStream xml) throws ParserConfigurationException, SAXException,
            IOException {

        List<Error> errors = new ArrayList<Error>();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(xml);
        is.setEncoding(PagSeguroSystem.getPagSeguroEncoding());
        Document doc = dBuilder.parse(is);

        Element errorsElement = doc.getDocumentElement();
        List<Element> errorElements = XMLParserUtils.getElements("error", errorsElement);

        Error error = null;
        for (int i = 0; i < errorElements.size(); i++) {
            error = new Error();
            Element errorElement = errorElements.get(i);
            error.setCode(XMLParserUtils.getTagValue("code", errorElement));
            error.setMessage(XMLParserUtils.getTagValue("message", errorElement));
            errors.add(error);
        }

        return errors;
    }

    public static List<Error> readErrosException(Exception exception) {

        List<Error> errors = new ArrayList<Error>();
        Error error = new Error("", exception.getMessage());
        errors.add(error);

        return errors;

    }

}
