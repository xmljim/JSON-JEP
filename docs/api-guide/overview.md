[JEP API Guide](index.html) >> Overview

#Overview

JSON-JEP was created out an itch to address big data problems with JSON data.  Most processors require that JSON documents must be loaded into memory before you can process the data.  As with XML, big files have always been a limiting factor.  First came the Simple API for XML (SAX). The approach is to let the underlying parser send back events as it traverses the XML document, e.g., `startElement`, `startDocument`, or `endDocument`.  It's up to a `ContentHandler` implementation to process these events in manner that fits the intended solution.  

The JEP API can be found in the [`org.ghotibeaun.json.parser.jep`](https://github.com/xmljim/JSON-JEP/tree/master/src/main/java/org/ghotibeaun/json/parser/jep) package.  

In addition to JEP, there is a standard JSON Object Model that can be used to parse JSON documents, which can be found in the [`org.ghotibeaun.json`](https://github.com/xmljim/JSON-JEP/tree/master/src/main/java/org/ghotibeaun/json) package.  Here you can instantiate new parsers and documents for use in a more traditional environment.

## Features:

- Event Based parsing and loading - parse and load JSON instances of any size with high performance. For more information,
  read [Getting Started](../getting-started).
  
- POJO Object Mapping/Conversion - map JSON instances to Plain old Java objects, and convert POJOs to JSON.
  Read [Marshalling and Unmarshalling JSON and POJOs](../marshalling-unmarshalling-json-pojo) for more information.

---
**Next**: [Getting Started](getting-started.html)