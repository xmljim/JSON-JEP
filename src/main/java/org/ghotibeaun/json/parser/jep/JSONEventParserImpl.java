package org.ghotibeaun.json.parser.jep;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import org.ghotibeaun.json.exception.JSONEventParserException;
import org.ghotibeaun.json.exception.JSONParserException;

class JSONEventParserImpl extends EventParser {
    
    public JSONEventParserImpl() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void parse(String data, ParserSettings settings) throws JSONParserException {
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes())) {
            parse(bais, settings);
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }
    }
    
    @Override
    public void parse(InputStream inputStream, ParserSettings settings) throws JSONParserException {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            settings.getParserConfiguration().init();
            settings.getParserConfiguration().getEventProcessor().start(bis);
            /*            final JSONByteEventProcessor processor = new JSONByteEventProcessor(bis, new DirectEventProviderImpl(eventHandler), new ParserSettings());

            processor.start();*/
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }
    }
    
    @Override
    public void parse(Path path, ParserSettings settings) throws JSONParserException {
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            parse(fis, settings);
        } catch (final FileNotFoundException e) {
            throw new JSONParserException(e);
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }
        
    }
    
    @Override
    public void parse(File file, ParserSettings settings) throws JSONParserException {
        try (FileInputStream fis = new FileInputStream(file)) {
            parse(fis, settings);
        } catch (final FileNotFoundException e) {
            throw new JSONParserException(e);
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }
        
    }

    @Override
    public void parse(URL url, ParserSettings settings) throws JSONEventParserException {
        // TODO Auto-generated method stub
        
    }
    
    
}
