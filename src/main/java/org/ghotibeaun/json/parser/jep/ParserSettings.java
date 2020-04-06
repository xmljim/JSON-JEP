package org.ghotibeaun.json.parser.jep;

import java.nio.charset.Charset;

import org.ghotibeaun.json.parser.jep.eventhandler.EventHandler;

public class ParserSettings {

    private int blockSize = DefaultParserSettingValues.BLOCK_SIZE_KB;
    private boolean enableStatistics = DefaultParserSettingValues.ENABLE_STATISTICS;
    private Charset characterSet = null;
    private boolean useStrict = DefaultParserSettingValues.USE_STRICT;
    private ParserConfiguration configuration = null;
    private FloatingPointNumber floatingPoint = DefaultParserSettingValues.USE_FLOATING_POINT_TYPE;
    private NonFloatingPointNumber nonFloatingPoint = DefaultParserSettingValues.USE_NON_FLOATING_POINT_TYPE;
    
    protected ParserSettings() {
        characterSet = Charset.forName(DefaultParserSettingValues.CHARSET);
    }

    public ParserSettings(ParserConfiguration parserConfiguration) {
        this();
        setParserConfiguration(parserConfiguration);
    }
    
    public static ParserSettings newSettings(EventHandler handler) {
        return new ParserSettings(ParserConfiguration.newConfiguration(handler));
    }


    public ParserSettings setBlockSize(int sizeInKb) {
        blockSize = sizeInKb;
        return this;
    }


    public int getBlockSize() {
        return blockSize;
    }
    
    public int getBlockSizeBytes() {
        return blockSize * 1024;
    }
    
    public ParserSettings setCharSet(String charSet) {
        characterSet = Charset.forName(charSet);
        return this;
    }

    public String getCharSetName() {
        return characterSet.name();
    }
    
    public Charset getCharset() {
        return characterSet;
    }

    public ParserSettings setEnableStatistics(boolean enable) {
        this.enableStatistics = enable;
        return this;
    }

    public boolean getEnableStatistics() {
        return this.enableStatistics;
    }

    public boolean getUseStrict() {
        return useStrict;
    }

    public ParserSettings setUseStrict(boolean useStrict) {
        this.useStrict = useStrict;
        return this;
    }
    
    public ParserSettings setUseFloatingPointType(FloatingPointNumber value) {
    	this.floatingPoint = value;
    	return this;
    }
    
    public FloatingPointNumber getUseFloatingPointType() {
    	return this.floatingPoint;
    }
    
    public ParserSettings setUseNonFloatingPointType(NonFloatingPointNumber value) {
    	this.nonFloatingPoint = value;
    	return this;
    }
    
    public NonFloatingPointNumber getUseNonFloatingPointType() {
    	return this.nonFloatingPoint;
    }
    
    public ParserSettings setParserConfiguration(ParserConfiguration configuration) {
        this.configuration = configuration;
        configuration.getEventHandler().setParserSettings(this);
        configuration.getEventProcessor().setParserSettings(this);
        configuration.getEventProvider().setParserSettings(this);

        return this;
    }
    
    public ParserConfiguration getParserConfiguration() {
        return configuration;
    }
    

}
