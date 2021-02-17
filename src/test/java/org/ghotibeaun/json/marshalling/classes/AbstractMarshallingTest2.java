package org.ghotibeaun.json.marshalling.classes;

import java.util.List;

public abstract class AbstractMarshallingTest2 {

    private String stringValue;
    private boolean booleanValue;
    private long numberValue;
    private double doubleValue;
    private Object nullValue;
    private List<String> primitiveArray;
    private Person simpleObject;

    public AbstractMarshallingTest2() {

    }

    public String getStringValue() {
        return stringValue;
    }

    //@JSONValueConverter(converter = StringJSONValueConverter.class, args= {"Value Converter Wrapper test: %s"})
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public long getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(long numberValue) {
        this.numberValue = numberValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Object getNullValue() {
        return nullValue;
    }

    public void setNullValue(Object nullValue) {
        this.nullValue = nullValue;
    }

    public List<String> getPrimitiveArray() {
        return primitiveArray;
    }

    public void setPrimitiveArray(List<String> primitiveArray) {
        this.primitiveArray = primitiveArray;
    }

    public Person getSimpleObject() {
        return simpleObject;
    }

    public void setSimpleObject(Person simpleObject) {
        this.simpleObject = simpleObject;
    }


}
