package org.ghotibeaun.json.parser.jep.eventprovider;

import java.nio.ByteBuffer;

import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;
import org.ghotibeaun.json.parser.jep.ParserSettings;
import org.ghotibeaun.json.parser.jep.eventhandler.JSONEventHandler;
import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;

public abstract class EventProvider implements JSONEventProvider {

    private ParserSettings settings;

    public EventProvider() {

    }

    @Override
    public ParserSettings getParserSettings() {
        return settings;
    }

    @Override
    public void setParserSettings(ParserSettings settings) {
        this.settings = settings;

    }

    public JSONEventHandler getEventHandler() {
        return settings.getParserConfiguration().getEventHandler();
    }

    public static EventProvider newDefaultEventProvider() {
        //return new DefaultEventProvider();
        return FactorySettings.createFactoryClass(Setting.EVENT_PROVIDER_CLASS);
    }


    public abstract void notifyDocumentStart(ByteBuffer start);


    public abstract void notifyDocumentEnd();


    public abstract void notifyStringTokenStart();


    public abstract void notifyStringTokenEnd(ByteBuffer tokenValue);


    public abstract void notifyBooleanTokenStart();


    public abstract void notifyBooleanTokenEnd(ByteBuffer tokenValue);


    public abstract void notifyNumberTokenStart();


    public abstract void notifyNumberTokenEnd(ByteBuffer tokenValue);


    public abstract void notifyNullTokenStart();


    public abstract void notifyNullTokenEnd(ByteBuffer tokenValue);


    public abstract void notifyMapStart();


    public abstract void notifyMapEnd();


    public abstract void notifyKeyEnd();

    /**
     * Notify consumer that we encountered the start of an array boundary
     */

    public abstract void notifyArrayStart();

    /**
     * Notify consumer that we encountered the end of an array boundary
     */

    public abstract void notifyArrayEnd();

    /**
     * Notify consumer that we encountered an entity terminator (e.g.,) a comma
     */

    public abstract void notifyEntityEnd();

    @Override
    public abstract void notifyEvent(JSONEvent event);

}
