/*
 *
 * # Released under MIT License
 *
 * Copyright (c) 2016-2021 Jim Earley.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */
package org.ghotibeaun.json.serializer;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.exception.JSONSerializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jim Earley
 *
 */
class XMLSerializerImpl implements XMLSerializer {

    /**
     *
     */
    public XMLSerializerImpl() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.serializer.XMLSerializer#toXml(org.ghotibeaun.json.JSONNode, java.lang.String)
     */
    @Override
    public Node toXml(JSONNode jsonNode, String rootElementName) {
        Element rootElement = null;
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.newDocument();

            rootElement = document.createElement(rootElementName);
            document.appendChild(rootElement);

            if (jsonNode.isObject()) {
                handleJsonObject(jsonNode.asJSONObject(), rootElement);
            } else {
                handleJsonArray(jsonNode.asJSONArray(), rootElement);
            }
        } catch (final Exception e) {
            throw new JSONSerializationException(e.getMessage(), e);
        }

        return rootElement;
    }

    private void handleJsonObject(JSONObject obj, Element parentElement) {
        for (final String key : obj.names()) {
            final Element newElement = parentElement.getOwnerDocument().createElement(key);


            switch(obj.get(key).getType()) {
                case ARRAY:
                    handleJsonArray(obj.getJSONArray(key), newElement);
                    break;
                case OBJECT:
                    handleJsonObject(obj.getJSONObject(key), newElement);
                    break;
                default:
                    newElement.setTextContent(obj.get(key).toString());
            }

            parentElement.appendChild(newElement);
        }
    }

    private void handleJsonArray(JSONArray array, Element parentElement) {
        for (final JSONValue<?> value : array.getValues()) {
            final String elementName = parentElement.getNodeName() + "-item";
            final Element newElement = parentElement.getOwnerDocument().createElement(elementName);

            switch (value.getType()) {
                case ARRAY:
                    handleJsonArray((JSONArray)value.getValue(), newElement);
                    break;
                case OBJECT:
                    handleJsonObject((JSONObject)value.getValue(), newElement);
                    break;
                default:
                    newElement.setTextContent(value.toString());
            }

            parentElement.appendChild(newElement);
        }
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.serializer.XMLSerializer#toXml(org.ghotibeaun.json.JSONNode)
     */
    @Override
    public Node toXml(JSONNode jsonNode) {
        return toXml(jsonNode, "root");
    }

    @Override
    public String toXmlString(JSONNode jsonNode, String rootElementName) {
        final Node xml = toXml(jsonNode, rootElementName);

        return toXmlString(xml);
    }

    @Override
    public String toXmlString(JSONNode jsonNode) {
        final Node xml = toXml(jsonNode);
        return toXmlString(xml);
    }

    private String toXmlString(Node xmlNode) {
        final StringWriter writer = new StringWriter();
        handleElement(xmlNode, writer, 0);

        return writer.toString();
    }

    private void handleElement(Node element, StringWriter writer, int indent) {
        final String indentPad = "    ";

        String indentSpace = "";
        if (indent > 0) {
            final StringBuilder builder = new StringBuilder();

            for (int i = 0; i < indent; i++) {
                builder.append(indentPad);
            }

            indentSpace = builder.toString();
        }

        writer.write(indentSpace);
        writer.write("<");
        writer.write(element.getNodeName());
        writer.write(">");

        if (element.getFirstChild().getNodeType() == Node.TEXT_NODE) {
            writer.write(element.getFirstChild().getTextContent());
        } else {
            final NodeList childNodes = element.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                handleElement(childNodes.item(j), writer, indent + 1);
            }
        }

        writer.write("</");
        writer.write(element.getNodeName());
        writer.write(">");
        writer.write(System.lineSeparator());

    }

}
