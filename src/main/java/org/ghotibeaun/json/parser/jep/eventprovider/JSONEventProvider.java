package org.ghotibeaun.json.parser.jep.eventprovider;

import org.ghotibeaun.json.parser.jep.Configurable;
import org.ghotibeaun.json.parser.jep.eventhandler.event.JSONEvent;

public interface JSONEventProvider extends Configurable {

    void notifyEvent(JSONEvent event);
}
