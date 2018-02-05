package org.ghotibeaun.json.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A sequence of {@link ByteRange} objects that represent a sequential set of
 * characters that have to appear in a certain order.  This uses a builder pattern
 * that initiates a ByteSequence from either an {@link #emptySequence()} or one
 * of several static <code>*start*</code> methods to initiate the sequence,
 * all of which return a ByteSequence instance with the set of byte values for the first sequence item.  Additional sequences of ByteRanges
 * can be applied with one of the <code>followedBy*</code> methods that append the additional bytes to a subsequent sequence item.
 * Note: it doesn't attempt to remove duplicate values.
 *
 * Once a Sequence is built, the {@link #matches(byte)} or {@link #matches(byte[])} can be used to evaluate if a byte value
 * matches the selected sequence. For example, assume that we want to evaluate whether an array of byte values is the equivalent to a true
 * boolean value.  We could build a sequence like this:
 *
 * <pre>
 * ByteSequence.startsWith({@linkplain ByteConstants#t}).followedBy({@link ByteConstants#r}).followedBy({@link ByteConstants#u}.followedBy({@link ByteConstants#e});
 * </pre>
 *
 * Alternatively, you could use {@link String#getBytes()} and use the {@link #withStartingSequence(byte[])} instead
 * <pre>
 * byte[] trueBytes = "true".getBytes();
 * ByteSequence.withStartingSequence(trueBytes);
 * </pre>
 *
 * Note that there is a difference between {@linkplain #withStartingSequence(byte[])}, and {@link #startsWithAnyOf(byte...)} or {@link #startsWithRange(ByteRange)}.
 * The first creates individual sequence items for each byte element; the latter two methods only create the initial sequence item, with
 * ByteRanges of byte values that can appear in the first sequence item.
 *
 * @author Jim Earley
 *
 *
 *
 */
public class ByteSequence {
    
    ArrayList<CardinalityByteRange> sequence = new ArrayList<>();

    /**
     * Private constructor. Use one of the static methods to instantiate the ByteSequence.
     */
    private  ByteSequence() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Creates a new ByteSequence with no sequence items
     * @return a new ByteSequence
     */
    public static ByteSequence emptySequence() {
        final ByteSequence newSequence = new ByteSequence();
        return newSequence;
    }
    
    /**
     * Initializes a ByteSequence with the first sequence item as a single byte value
     * @param b the byte value to add to the first sequence item
     * @return the ByteSequence
     */
    public static ByteSequence startsWith(byte b) {
        final ByteSequence newSequence = new ByteSequence();
        newSequence.add(ByteRange.startWith(b));
        return newSequence;
    }
    
    /**
     * Initializes a new ByteSequence with each element in the byte array as
     * separate sequence items.  This is the equivalant to
     * <pre>
     * ByteSequence.startsWith(byteValue[0]).followedBy(byteValue[1]).<i>followedBy(byteValue[n])...</i>
     * </pre>
     * @param byteSequence the byte array to build the sequence items.
     * @return the current ByteSequence initialized with a set of sequence items corresponding to the byte array elements
     */
    public static ByteSequence withStartingSequence(byte[] byteSequence) {
        final ByteSequence newSequence = new ByteSequence();
        for (final byte b : byteSequence) {
            newSequence.add(b);
        }
        
        return newSequence;
    }

    /**
     * Initializes a new ByteSequence using another ByteSequence's items
     * @param sequence The ByteSequence used to populate this instance
     * @return a new ByteSequence
     */
    public static ByteSequence startsWith(ByteSequence sequence) {
        final ByteSequence newSequence = new ByteSequence();
        newSequence.add(sequence);
        return newSequence;
    }

    /**
     * Initializes a new ByteSequence with a ByteRange of byte values. All of the values in that range will be included in the
     * first sequence item.
     * @param range The ByteRange containing the byte values to include in the first sequence item
     * @return a new ByteSequence.
     */
    public static ByteSequence startsWithRange(ByteRange range) {
        final ByteSequence newSequence = new ByteSequence();
        newSequence.add(range);
        return newSequence;
    }
    
    /**
     * Initializesa new ByteSequence by creating a first sequence item containing byte values
     * starting from one value up to an end byte value, inclusive.  This is the equivalent
     * of creating a new ByteRange using {@linkplain ByteRange#startWith(byte, byte)} and including it
     * using {@link #startsWithRange(ByteRange)}.
     *
     * Example:
     * Assume a starting byte for 'a' (0x61), and an ending byte for 'z' (0x7a). This creates a
     * ByteRange containing byte values for a through z inclusively.
     * @param start the starting byte value
     * @param end the ending byte value.  The value must be greater than the starting by.
     * @return A new ByteSequence
     */
    public static ByteSequence startsWithRangeFrom(byte start, byte end) {
        final ByteSequence newSequence = new ByteSequence();
        newSequence.add(ByteRange.startWith(start, end));
        return newSequence;
    }
    
    /**
     * Initializes a new ByteSequence containing a variable number of byte values
     * @param anyOf a variable number of byte values that will be included as a single ByteRange in
     * the first sequence
     * @return the initialized ByteSequence
     */
    public static ByteSequence startsWithAnyOf(byte ...anyOf) {
        final ByteSequence newSequence = new ByteSequence();
        newSequence.add(ByteRange.startWith(anyOf));
        return newSequence;
    }
    /*
     * We'll get to these later
    public ByteSequence whichRepeats(int numberOfRepeats) {

    }

    public ByteSequence whichRepeatsUpTo(int numberOfRepeats) {

    }

    public ByteSequence whichRepeatsUpTo(int minimum, int maximum) {

    }

    public ByteSequence whichRepeatsMoreThan(int numberStart) {

    }*/
    

    /**
     * Adds a new ByteRange sequence to an existing ByteSequence
     * @param b the byte value to be used in the ByteRange for the new sequence
     * @return the existing ByteSequence with the new sequence added
     */
    public ByteSequence followedBy(byte b) {
        add(b);
        return this;
    }

    /**
     * Adds a new ByteRange sequence to an existing ByteSequence
     * @param anyOf a variable number of byte values that will be included as a single ByteRange in the current sequence
     * @return the existing ByteSequence with the new sequence added
     */
    public ByteSequence followedByAnyOf(byte... anyOf) {
        add(anyOf);
        return this;
    }

    /**
     * Adds a new ByteRange sequence containing all bytes from a given start and end value to an existing ByteSequence
     * @param start The starting byte value
     * @param end The ending byte value. Byte value must be greater than the starting byte value
     * @return the existing ByteSequence with the new sequence added
     */
    public ByteSequence followedByRangeFrom(byte start, byte end) {
        add(start, end);
        return this;
    }

    /**
     * Appends all of the sequence items from another ByteSequence into the current ByteSequence
     * @param sequence The other ByteSequence
     * @return the existing ByteSequence with the new sequences from the other ByteSequence added
     */
    public ByteSequence followedBySequenceOf(ByteSequence sequence) {
        add(sequence);
        return this;
    }
    
    /**
     * Adds a new ByteRange sequence to the existing ByteSequence
     * @param range The ByteRange
     * @return the existing ByteSequence with the new sequence added
     */
    public ByteSequence followedByRange(ByteRange range) {
        add(range);
        return this;
    }
    
    /**
     * Matches the byte against the current sequence
     * @param b the byte to match
     * @return <code>true</code> if the sequence only has one item AND, the byte value is found in that
     * sequence's ByteRange. Otherwise, it will return false
     */
    public boolean matches(byte b) {
        return ((sequence.size() == 1) && byteIsInRange(b, 0));
    }
    
    /**
     * Internal method to evaluate whether a given sequence's ByteRange contains the specified byte value
     * @param b the byte value to compare
     * @param sequenceNumber the sequence index to use in the comparison
     * @return <code>true</code> if the byte value matches one of the ByteRange values
     */
    private boolean byteIsInRange(byte b, int sequenceNumber) {
        return sequence.get(sequenceNumber).getRange().contains(b);
    }

    /**
     * Matches a sequence of bytes in order against the ByteSequence for a match
     * @param bytes the byte array containing the sequence of bytes to match
     * @return <code>true</code> if the ByteSequence size matches the length the byte array AND,
     * if all byte array elements are contained in the corresponding sequence's ByteRange
     */
    public boolean matches(byte[] bytes) {
        int i = 0;
        boolean match = false;
        for (final byte b : bytes) {
            match = byteIsInRange(b, i);
            if (match == false) {
                break;
            }
            i++;
        }
        
        return match;
    }


    
    /**
     * Protected method to allow other ByteSequences to be added to the current ByteSequence
     * @return
     */
    protected List<CardinalityByteRange> getSequenceItems() {
        return sequence;
    }

    /**
     * Adds a new b to a ByteRange, which is added as a sequence
     * @param b the byte value
     */
    private void add(byte b) {
        add(ByteRange.startWith(b));
    }

    
    /**
     * Adds a varargs of bytes to a ByteRange, and adds the ByteRange
     * as the next sequence
     * @param anyOf
     */
    private void add(byte... anyOf) {
        final ByteRange range = ByteRange.empty();
        
        for (final byte b : anyOf) {
            range.andAdd(b);
        }
        add(range);
    }

    /**
     * Adds a new sequence containing a byte range from a starting byte to an ending byte value
     * @param start the start byte value
     * @param end the end byte value
     */
    private void add(byte start, byte end) {
        add(ByteRange.startWith(start, end));
    }

    /**
     * Adds the CardinalityByteRange instance, which is the underlying object that holds both the ByteRange
     * and its Cardinality.
     * @param cbt
     * @category Not fully implemented yet.  Cardinality needs additional code
     */
    private void add(CardinalityByteRange cbt) {
        sequence.add(cbt);
    }

    /**
     * Appends the sequence from another ByteSequence
     * @param sequence
     */
    private void add(ByteSequence sequence) {
        this.sequence.addAll(sequence.getSequenceItems());
    }
    
    /**
     * Adds a new sequence from a ByteRange
     * @param range
     */
    private void add(ByteRange range) {
        add(new CardinalityByteRange(range));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final CardinalityByteRange cbt : sequence) {
            builder.append("[");
            builder.append(cbt.getRange());
            builder.append("]");
        }

        return builder.toString();
    }

    /**
     * Class that holds the sequence's cardinality.  Not fully implemented
     * @author Jim Earley (xml.jim@gmail.com)
     *
     */
    private static class Cardinality {
        private int minimum = 0;
        private Number maximum;
        
        public Cardinality(int min, Number max) {
            this.minimum = min;
            this.maximum = max;
        }
        
        public int getMinimum() {
            return minimum;
        }

        public void setMinimum(int min) {
            minimum = min;
        }
        
        public Number getMaximum() {
            return maximum;
        }

        public void setMaximum(Number max) {
            maximum = max;
        }


        public static Cardinality defaultSingleInstance() {
            return new Cardinality(1, 1);
        }
        
        public static Cardinality oneOrMore() {
            return new Cardinality(1, Double.POSITIVE_INFINITY);
        }
    }
    
    /**
     * Wrapper class that represents a sequence item. It contains the ByteRange and Cardinality
     * @author Jim Earley (xml.jim@gmail.com)
     *
     */
    private static class CardinalityByteRange {
        private final ByteRange range;
        private Cardinality cardinality;

        public CardinalityByteRange(ByteRange range) {
            this.range = range;
            this.cardinality = Cardinality.defaultSingleInstance();
        }

        public CardinalityByteRange(ByteRange range, Cardinality cardinality) {
            this.range = range;
            this.cardinality = cardinality;
        }

        public ByteRange getRange() {
            return range;
        }

        public Cardinality getCardinality() {
            return cardinality;
        }

        public void setCardinality(Cardinality cardinality) {
            this.cardinality = cardinality;
        }
    }
    
    
}
