JSON-JEP - An Event-Driven JSON Processor
=========================================

JSON is a well established [international standard](http://www.ecma-international.org/publications/files/ECMA-ST/ECMA-404.pdf)
for data interchange using Javascript Object Notation. There are numerous 
reliable parsers available from both commercial and open-source market.

In most use cases, JSON is reasonably small -- typically under a few MBs in size. However,
in today's Big Data world, JSON is poised to be the go-to interchange format for its
relative sparsity (compared to XML) and flexibility to represent most data structures
including relatively flat tabular models to deeply nested models used in legacy COBOL/Mainframe 
systems. 

The problem with most JSON libraries is that it relies on the JSON data to be fully loaded into
a JSON object model (which I will *creatively* call the **JOM**). For large datasets, this also means potentially significant memory overhead and consumption to load the data before it can be used. 

XML has two approaches for dealing with large/very large data documents:  [Simple API for XML]https://docs.oracle.com/javase/tutorial/jaxp/sax/index.html) (SAX),
and [Streaming API for XML](https://docs.oracle.com/cd/E17802_01/webservices/webservices/docs/1.6/tutorial/doc/SJSXP2.html) (StAX). You 
can read about the differences between these APIs with the links provided. Hence, I've developed **JEP**, or the JSON Event Processor.  

It uses a paradigm similar to SAX, where content is parsed (pushed), and events are fired to an Event Handler
that consumes the data. It's up to the Event Hander instance to determine what to do with the data and event provided. This provides a great deal of flexibility so that developers can parse large JSON datasets, and only process the data they need, or provide different processing patterns to handle different scenarios including ETL of very large to huge datasets.

Full JOM Implementation
-----------------------
In addition to JEP processing, the library also includes a full JSON Object Model that allows developers to load a JSON instance into memory if so desired.  Similar to the W3C DOM, the JOM relies on interfaces for JSON nodes leaving the underlying implementation separate from the API. Under the hood, the JOM Parser also takes advantage of the JEP model to load JSON into it.

Encoding Independent
--------------------
The JEP processing model is designed to be Encoding agnostic.  Under the hood, processing occurs at the byte level rather than at the character or string level.  This works because of the specific requirements for JSON data models, allowing the processor to target specific
byte values for firing events.  Here, the Event Hander implementation can be used to decode the byte array into the proper Character Set.

Fast Processing
---------------
Several considerations have been taken into account to help improve performance, including configuration of the size of the byte buffer to read into blocks of memory and minimize I/O overhead. As a result overall performance is bound to the clock speed of the CPU. In some of the tests I've run with an i7-6600U 2.60GHz, results consistently show full loads into the JOM at 15MB/sec for a 30MB file (or 2 seconds). For smaller files, the rates are lower, but loads of .001 second or faster have been consistent with the same CPU for a 2K file, and .2 seconds for a 750K file.

The main advantage is that it allows you to decode the data only when you need to rather than during process time. This minimizes both memory and clock cycles needed to read the JSON data. And allows an Event Handler to work with the data selectively

Roadmap
-------
These are early days into development.  Development of this library came out of an itch to deal with very large data (GB's, and even TB's of data) efficiently.  There are several features that need further work

* Statistics: For very large data sets, having frequent updates on processing speed and and time to completion is always important
* Event-based serialization:  There are two models that I want to consider - one is based on serialization of JOM instances based on traversal of the tree, allowing developers to filter the data they wish to serialize.  The second is based on the idea of Pipes, where an input pipe, such as an EventHandler can then integrated with an output stream to serialize the data.  

Both of these require additional design and thought about how the data can be used and possibly transformed into other formats.   

