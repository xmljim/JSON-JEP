package org.ghotibeaun.json;

import org.ghotibeaun.json.util.ByteConstants;
import org.ghotibeaun.json.util.ByteRange;
import org.ghotibeaun.json.util.ByteSequence;

public class TestMain {
    
    public static void main(String[] args) {
        System.out.println(ByteConstants.SPACE & ByteConstants.COMMA & ByteConstants.COLON & ByteConstants.END_ARRAY & ByteConstants.END_MAP);
        
        System.out.println(8 | 4 | 2);
        System.out.println((13 & 4) != 0);

        System.out.println(15 >> 1);


        final char[] hello = new char[5];
        hello[0] = 'h';
        hello[1] = 'e';
        hello[2] = 'l';

        final char [] lo = new char[] {'l','a'};
        
        System.arraycopy(lo, 0, hello, 3, 2);
        
        System.out.println(new String(hello));
        
        System.out.println( (ByteConstants.f - ByteConstants.t));

        final ByteRange range = ByteRange.startWith(ByteConstants.a, ByteConstants.z).andAdd(ByteConstants.AMPERSAND);
        System.out.println(range.contains(ByteConstants.j));
        System.out.println(range.contains(ByteConstants.AMPERSAND));

        System.out.println(range);
        
        final ByteSequence sequence = ByteSequence.startsWith(ByteConstants.h).followedBy(ByteConstants.e).followedBy(ByteConstants.y);
        System.out.println(sequence.matches("hey".getBytes()));
        
        final char space = ' ';
        final byte sp = (byte)space;
        System.out.println(sp);
    }
    
    public TestMain() {
        // TODO Auto-generated constructor stub
    }
    
}
