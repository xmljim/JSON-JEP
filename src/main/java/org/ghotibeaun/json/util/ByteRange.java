package org.ghotibeaun.json.util;

import java.util.Arrays;

/**
 * A ByteRange is a collection of bytes that can be assembled and used to determine if a given byte value matches
 * a byte value in the collection.  This is especially useful when a byte may be used to determine processor behavior.
 *
 * The ByteRange uses a Builder pattern with static <code>startWith*</code> methods to instantiate the ByteRange with one or more byte values and
 * returns the ByteRange instance.  Additional byte values can then be appended to the existing ByteRange using the <code>andAdd*</code> class
 * methods.
 *
 * Using one of the <code>contains*</code> methods will evaluate whether a given byte value is contained in the set of byte values.
 *
 * <p><b>Example</b></p>
 * Assume that we want to determine if a byte matches one of a range of byte values that represent a hexidecimal character (0-9Aa-Ff).
 *
 * <pre>
 * ByteRange hexRange = ByteRange.startWith(0x30, 0x39).addAdd(0x41, 0x46).addAdd(0x61, 0x66);
 *
 * byte b = 0x32;
 * boolean matches = hexRange.contains(b);
 * System.out.println(matches);
 *
 * byte[] byteArray = new byte[]{0x33, 0x34, 0x66, 0x62};
 * matches = hexRange.containsAll(byteArray);
 * System.out.println(matches);
 * </pre>
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public class ByteRange {

    
    private byte[] rangeData;
    private int size;
    private int cursor = 0;

    /**
     * Private constructor. Use one of the static <code>startWith</code> methods
     */
    private ByteRange() {
        rangeData = new byte[] {};
    }

    /**
     * Creates an empty ByteRange
     * @return an empty ByteRange
     */
    public static ByteRange empty() {
        final ByteRange newInstance = new ByteRange();
        return newInstance;
    }
    
    /**
     * Initializes a ByteRange with an array of byte values.
     * @param byteArray the array of bytes to add to the range
     * @return An instance of the ByteRange contain the array of bytes
     * @see #andAdd(byte[])
     */
    public static ByteRange startWith(byte[] byteArray) {
        final ByteRange newInstance = new ByteRange();
        newInstance.addToArray(byteArray);
        return newInstance;
    }

    /**
     * Initializes a ByteRange by creating a range array containing all byte values from a
     * starting byte value to an ending byte value inclusive.
     *
     * <p><b>Example</b></p>
     * Create a range starting from the 'a' byte value through the 'z' byte value
     * <pre>
     * ByteRange.startWith(0x61, 0x7a)
     * </pre>
     * returns a ByteRange containing all byte values from 0x61 through 0x7a
     *
     * @param start the starting byte value.  Must be smaller than the ending byte value
     * @param end the ending byte value.  Must be larger than the start byte value
     * @return The instantiated ByteRange containing all values from the start byte value through the end byte value
     * @throws NegativeArraySizeException if the end byte value is smaller than the start byte value
     * @see #andAddFrom(byte, byte)
     */
    public static ByteRange startWith(byte start, byte end) {
        final ByteRange newInstance = new ByteRange();
        newInstance.addToArray(start, end);
        return newInstance;
    }

    /**
     * Creates a new ByteRange with a single byte in the range value
     * @param addByte the byte value to instantiate the ByteRange with
     * @return the new ByteRange
     * @see #andAdd(byte)
     */
    public static ByteRange startWith(byte addByte) {
        final ByteRange newInstance = new ByteRange();
        newInstance.addToArray(addByte);
        return newInstance;
    }

    /**
     * Appends a ByteRange with a set of byte values starting with a start byte value to an end byte value inclusive
     * @param start the starting byte value
     * @param end the ending byte value
     * @return The existing ByteRange with the new values appended to it
     * @throws NegativeArraySizeException if the end byte value is smaller than the start byte value
     * @see #startWith(byte, byte)
     */
    public ByteRange andAddFrom(byte start, byte end) {
        addToArray(start, end);
        return this;
    }

    /**
     * Apppend a byte to an existing ByteRange
     * @param addByte the byte to add
     * @return the exsting ByteRange with the byte value appended
     * @see #startWith(byte)
     */
    public ByteRange andAdd(byte addByte) {
        addToArray(addByte);
        return this;
    }
    
    /**
     * Append an array of bytes to an existing ByteRange
     * @param byteArray the array of bytes to add
     * @return the existing ByteRange with the array of bytes appended
     * @see #startWith(byte[])
     */
    public ByteRange andAdd(byte[] byteArray) {
        addToArray(byteArray);
        return this;
    }
    
    /**
     * Evaluates if a given byte value is contained in the ByteRange
     * @param b the byte value to evaluate
     * @return <code>true</code> if the byte value is found; <code>false</code> otherwise
     */
    public boolean contains(byte b) {
        return isInRangeLocal(b);
    }

    /**
     * Evaluates if a set of byte values are contained in the ByteRange.
     * @param bytes vararg of byte values
     * @return <code>true</code> if <i>all</i> the byte values are found in the range; <code>false</code> otherwise.
     * @see #containsAll(byte[])
     */
    public boolean contains(byte... bytes) {
        return containsAll(bytes);
    }

    /**
     * Evaluates if an array of byte values are contained in the ByteRange.
     * @param byteArray the array of bytes
     * @return <code>true</code> if <i>all</i> the byte values are found in the range; <code>false</code> otherwise.
     */
    public boolean containsAll(byte[] byteArray) {
        return containsCount(byteArray) == byteArray.length;
    }

    /**
     * Evaluates if any or all of the values in a byteArray are contained in the ByteRange.
     * @param array the byte array to evaluate
     * @return <code>true</code> if any of the bytes in the array are contained the ByteRange; <code>false</code> if none of the bytes
     * in the array match.
     */
    public boolean containsSome(byte[] array) {
        return containsCount(array) != 0;
    }
    
    /**
     * Gets a count of the number of bytes that match the values in the ByteRange
     * @param array the array of bytes to evaluate
     * @return a count of the byte values that match.
     */
    public int containsCount(byte[] array) {
        int count = 0;
        
        for (final byte b : array) {
            if (contains(b)) {
                count++;
            }
        }
        
        return count;
    }

    /**
     * Creates an array of indexes that represent all of the positions in a byte array that match
     * any of the values in the ByteRange
     * @param array the array of bytes to evaluate
     * @return an array of index positions that correlate to the positions of bytes in the byte array
     */
    public int[] getMatchingByteIndexes(byte[] array) {
        final int[] posArray = new int[containsCount(array)];

        int index = 0;
        int i = 0;
        for (final byte b : array) {
            if (contains(b)) {
                posArray[index++] = i;
            }
            i++;
        }

        return posArray;
    }
    
    /**
     * Creates a byte array of values that match any of the byte values in the ByteRange
     * @param array the byte array to evaluate
     * @return the byte array of values that match any of the byte values in the ByteRange
     */
    public byte[] getMatchingByteValues(byte[] array) {
        final byte[] byteArray = new byte[containsCount(array)];
        
        int index = 0;
        
        for (final byte b : array) {
            if (contains(b)) {
                byteArray[index++] = b;
            }
        }
        
        return byteArray;
    }

    private void growArray(int addSize) {
        final int newSize = size + addSize;

        rangeData = Arrays.copyOf(rangeData, newSize);
        size = rangeData.length;
    }
    
    private void addToArray(byte start, byte end) {
        if (start > end) {
            throw new NegativeArraySizeException("The end byte value must be larger than the starting byte");
        }
        final int len = (end - start) + 1;
        growArray(len);
        for (int i = start; i <= end; i++) {
            final byte add = (byte)i;
            rangeData[cursor++] = add;
        }
        Arrays.sort(rangeData);
    }
    
    private void addToArray(byte newByte) {
        growArray(1);
        rangeData[cursor++] = newByte;
        Arrays.sort(rangeData);
    }

    private void addToArray(byte[] byteArray) {
        growArray(byteArray.length);

        for (final byte b : byteArray) {
            rangeData[cursor++] = b;
        }
        Arrays.sort(rangeData);
    }

    private boolean isInRangeLocal(byte b) {
        
        return Arrays.binarySearch(rangeData, b) >= 0;
    }

    @Override
    public String toString() {
        return new String(rangeData);
    }
}
