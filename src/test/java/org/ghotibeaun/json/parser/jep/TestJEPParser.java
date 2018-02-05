package org.ghotibeaun.json.parser.jep;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class TestJEPParser {

    @Test
    public void testParseStringJSONEventHandler() {
        System.out.println("String Data Test");
        final String data = "{\"format\": \"yyyy-MM-dd'T'HH:mm:ss.SSSZ\", \"isTrue\": false}";
        final JSONEventParserImpl impl = new JSONEventParserImpl();
        


        final double start = System.nanoTime();
        impl.parse(data, ParserSettings.newSettings(new TestHandler()));
        final double end = System.nanoTime();

        


        final double time = ((end-start) * .000000001f);
        System.out.println(time);
        final double dataSize = data.length();

        final double mbSec = ((dataSize/time)/Math.pow(2,20));
        System.out.println("MB/sec: " + (mbSec ));
        System.out.println("GB/hr: " + ((mbSec * 3600)/Math.pow(2, 10)));
        System.out.println("Data Size: " + dataSize + " bytes");
        System.out.println("----------------------");
    }


    @Test
    public void testParseInputStreamJSONEventHandler() throws IOException {
        System.out.println("Classpath Test");
        final InputStream i = getClass().getResourceAsStream("/observation-fhir.json");
        final JSONEventParserImpl impl = new JSONEventParserImpl();


        final double start = System.nanoTime();
        impl.parse(i, ParserSettings.newSettings(new TestHandler()).setUseStrict(false));
        final double end = System.nanoTime();
        final double time = ((end-start) * .000000001f);
        System.out.println(time);
        final Long dataSize = (Long) Files.getAttribute(Paths.get("src/test/resources/observation-fhir.json"), "size");

        final double mbSec = ((dataSize/time)/Math.pow(2,20));
        System.out.println("MB/sec: " + (mbSec ));
        System.out.println("GB/hr: " + ((mbSec * 3600)/Math.pow(2, 10)));
        System.out.println("Data Size: " + dataSize + " bytes");
        System.out.println("----------------------");
    }
    
    @Test
    public void testParseFileJSONEventHandler() throws IOException {
        System.out.println("File Test - All Sets");
        final String filePath = "src/test/resources/AllSets.json";
        final Path p = Paths.get(filePath);
        final Long dataSize = (Long)Files.getAttribute(p, "size");

        final File f = new File(System.getProperty("user.dir"), filePath);
        
        final JSONEventParserImpl impl = new JSONEventParserImpl();

        final double start = System.nanoTime();
        impl.parse(f, ParserSettings.newSettings(new TestHandler()).setUseStrict(false).setBlockSize(1024));
        final double end = System.nanoTime();
        final double time = ((end-start) * .000000001f);
        System.out.println(time);
        
        final double mbSec = ((dataSize/time)/Math.pow(2,20));
        System.out.println("MB/sec: " + (mbSec ));
        System.out.println("GB/hr: " + ((mbSec * 3600)/Math.pow(2, 10)));
        System.out.println("Data Size: " + dataSize + " bytes");
        System.out.println("----------------------");
    }
    
    @Test
    public void testParsePathJSONEventHandler() throws IOException {
        System.out.println("Path Test - US Reps");
        final Path p = Paths.get("src/test/resources/us-reps.json");
        final Long dataSize = (Long)Files.getAttribute(p, "size");
        
        final JSONEventParserImpl impl = new JSONEventParserImpl();

        
        //final ParserSettings settings = new ParserSettings(ParserConfiguration.newConfiguration(new ConsoleEventHandler()));
        final ParserSettings settings = ParserSettings.newSettings(new TestHandler()).setBlockSize(16);
        settings.setUseStrict(false);
        final double start = System.nanoTime();
        impl.parse(p, settings);
        final double end = System.nanoTime();
        final double time = ((end-start) * .000000001f);
        System.out.println(time);
        
        final double mbSec = ((dataSize/time)/Math.pow(2,20));
        System.out.println("MB/sec: " + (mbSec ));
        System.out.println("GB/hr: " + ((mbSec * 3600)/Math.pow(2, 10)));
        System.out.println("Data Size: " + dataSize + " bytes");
        System.out.println("----------------------");
    }



}
