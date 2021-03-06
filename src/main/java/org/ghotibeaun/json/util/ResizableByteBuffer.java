/*
 *
 * # Released under MIT License
 *
 * Copyright (c) 2016-2021 Jim Earley.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */
package org.ghotibeaun.json.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class that mimics similar behavior in a ByteBuffer.
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public class ResizableByteBuffer {
    private static Logger LOGGER = LoggerFactory.getLogger(ResizableByteBuffer.class);

    private static final int DEFAULT_CAPACITY = 10;

    private int size;

    private int position = -1;

    private transient byte[] byteArray = new byte[] {};


    /**
     * Default constructor
     */
    public ResizableByteBuffer() {
        grow(DEFAULT_CAPACITY);
        size = 0;

    }

    /**
     * Constructor
     * @param allocateSize the size to allocate to the backing array
     */
    public ResizableByteBuffer(int allocateSize) {
        grow(allocateSize);
    }

    /**
     * Constructor that is initialized with a starting array of bytes
     * @param initialByteArray the initial array of bytes
     */
    public ResizableByteBuffer(byte[] initialByteArray) {
        add(initialByteArray);
    }

    public static ResizableByteBuffer fromStream(InputStream inputStream) {
        final ResizableByteBuffer buffer = new ResizableByteBuffer();
        final byte[] bytes = new byte[DEFAULT_CAPACITY];
        try {
            inputStream.read(bytes);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        buffer.add(bytes);
        return buffer;
    }

    public static ResizableByteBuffer fromStream(InputStream inputStream, int allocateSize) {
        final ResizableByteBuffer buffer = new ResizableByteBuffer(DEFAULT_CAPACITY);
        final byte[] bytes = new byte[allocateSize];
        try {
            final int result = inputStream.read(bytes);
            LOGGER.debug("Byte Count: {}", result);
            if (result != -1) {
                buffer.add(bytes);
                buffer.trim();
            }
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return buffer;
    }

    /**
     * Add a new byte to the buffer
     * @param b the byte
     */
    public void add(byte b) {
        ensureByteCapacity();
        byteArray[size] = b;
        size++;
    }

    /**
     * Append an array of bytes to the existing buffer.  The values will be inserted after the
     * last item previously inserted into the buffer.
     * @param bytes the array of bytes to append
     */
    public void add(byte[] bytes) {
        ensureByteCapacity(bytes.length);
        System.arraycopy(bytes, 0, byteArray, size, bytes.length);
        size+= bytes.length;
    }

    /**
     * Sets the internal cursor back to the beginning of the buffer
     */
    public void rewind() {
        position = 0;
    }

    /**
     * Returns the next byte based on the internal cursor position, and moves the cursor to the
     * next item
     * @return the next byte
     */
    public byte get() {
        if (position >= size - 1) {
            return 0x0;
        } else {
            position++;
            return byteArray[position];
        }
    }

    /**
     * Return the full size of the buffer
     * @return the full size of the buffer;
     */
    public int size() {
        return size;
    }

    /**
     * returns the current cursor position in the buffer
     * @return the current cursor position in the buffer
     */
    public int getPosition() {
        return position + 1;
    }

    /**
     * Returns the count of the remaining items to be accessed relative the current cursor position
     * @return the count of the remaining items
     */
    public int getRemaining() {
        return size - (position + 1);
    }

    /**
     * Returns whether there are any remaining items following the current cursor position
     * @return <code>true</code> if there additional items following; <code>false</code> if the cursor is at the
     * end.
     */
    public boolean hasRemaining() {
        return getRemaining() > 0;
    }

    /**
     * Retrieves the next byte following the current cursor's position, without updating the cursor position
     * @return the next byte
     */
    public byte peek() {
        return peek(1);
    }

    /**
     * Retrieves the last byte in the buffer, without updating the cursor position
     * @return the last byte in the buffer or a null byte (<code>0x0</code>) if the buffer is empty
     */
    public byte last() {
        if (size == 0) {
            return 0x0;
        }

        return byteArray[size - 1];
    }


    /**
     * Retrieves the first byte in the buffer, without updating the cursor position
     * @return the first byte in the buffer or a null byte (<code>0x0</code>) if the buffer is empty
     */
    public byte first() {
        if (size == 0) {
            return 0x0;
        }

        return byteArray[0];
    }

    /**
     * Retrieves the byte at the specified location ahead of the cursor, without moving the cursor position
     * @param lookAhead the number of bytes ahead of the current cursor position to move to
     * @return the byte value at the look ahead position
     * @throws IndexOutOfBoundsException if the lookAhead value exceeds the size of the buffer
     */
    public byte peek(int lookAhead) {
        if (size - (position + lookAhead)  < 0) {
            throw new IndexOutOfBoundsException();
        }

        return byteArray[position + lookAhead];
    }

    /**
     * Retrieves the byte at the position behind the current cursor
     * @return the byte value at position behind the cursor
     * @throws IndexOutOfBoundsException if the buffer is empty, or the cursor is at the start position (0)
     */
    public byte previous() {
        return previous(1);
    }

    /**
     * Retrieves the byte at the specified number of bytes behind the current cursor, without moving the cursor
     * @param lookBehind the number of bytes behind the cursor position
     * @return the byte value at the specified location behind the cursor
     * @throws IndexOutOfBoundsException if the buffer is empty, or the cursor is at the start position (0)
     */
    public byte previous(int lookBehind) {
        if (lookBehind - 1 > position) {
            throw new IndexOutOfBoundsException("Cannot look behind beyond the starting position of the buffer");
        }

        return byteArray[position - lookBehind];
    }

    /**
     * Converts the buffer to a {@link ByteBuffer}
     * @return a new ByteBuffer instance
     */
    public ByteBuffer toByteBuffer() {
        final ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOfRange(byteArray, 0, size));
        return buffer;
    }

    /**
     * Clears the buffer
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            byteArray[i] = 0x0;
        }

        size = 0;
    }

    /**
     * Retrieves the array for this buffer
     * @return an array of bytes
     */
    public byte[] array() {
        return toByteBuffer().array();
    }

    @Override
    public String toString() {
        return new String(trim());
    }

    /**
     * Retrieves a subset of the buffer as an array.  Note: this is not the underlying array, but a copy.
     * @param offset The starting offset to begin the slice
     * @param length THe number of bytes starting from the offset to include in the array
     * @return a copy of the underlying array
     * @throws IndexOutOfBoundsException if the offset and/or length exceed the length of the buffer
     */
    private byte[] slice(int offset, int length) {
        if (offset + length > size) {
            throw new IndexOutOfBoundsException("Combination of offset and length exceed size of buffer");
        }
        final byte[] target = new byte[length];
        System.arraycopy(byteArray, offset, target, 0, length);
        return target;
    }

    /**
     * Trims the underlying array to remove any unallocated items at the end of the array.
     * @return
     */
    private byte[] trim() {
        if (size == 0) {
            return new byte[] {};
        }

        return slice(0, size);
    }

    /**
     * Make sure the array has enough space for one more item.
     */
    private void ensureByteCapacity() {
        ensureByteCapacity(size + 1);
    }

    /**
     * Make sure the array has enough space for the minimum number of items to be added.  Typically this will continue to add
     * at least 50% more space than what was asked for to avoid repeated resizing.
     * @param minimumCapacity
     */
    private void ensureByteCapacity(int minimumCapacity) {
        if (byteArray.length - (size + minimumCapacity) <= 0) {
            int newCapacity = byteArray.length + minimumCapacity >> 1;

        if (newCapacity - byteArray.length < DEFAULT_CAPACITY) {
            newCapacity = byteArray.length + DEFAULT_CAPACITY;
        }

        grow(newCapacity);
        }
    }


    /**
     * Creates a new copy of the array with additional capacity
     * @param capacity the amount of new capacity to provide
     */
    private void grow(int capacity) {
        byteArray = Arrays.copyOf(byteArray, capacity);
    }

}
