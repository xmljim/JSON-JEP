package org.ghotibeaun.json.converters.options;

public enum ScannerValidationOption {

    /**
     * Will throw errors if the underlying class does not include a field or setter for a given JSON property
     */
    STRICT, 
    /**
     * Ignore missing fields or setters for a given JSON property
     */
    LAX
}
