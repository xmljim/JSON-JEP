/*
 * Copyright (c) 2019, Flatirons Digital Innovations. All Rights Reserved.
 * This code may not be used without the express written permission
 * of the copyright holder, Flatirons Digital Innovations.
 */
package org.ghotibeaun.json.serializer;

import org.ghotibeaun.json.JSONNode;
import org.w3c.dom.Node;

/**
 * @author Jim Earley
 *
 */
public interface XMLSerializer {

    Node toXml(JSONNode jsonNode, String rootElementName);

    Node toXml(JSONNode jsonNode);

    String toXmlString(JSONNode jsonNode, String rootElementName);

    String toXmlString(JSONNode jsonNode);
}
