package org.ghotibeaun.json;

import java.io.Serializable;

/**
 * Tagging class for Null objects
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public class NullObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8525715812532856941L;

    public NullObject() {
        // no-op
    }

    @Override
    public String toString() {
        return "null";
    }

}
