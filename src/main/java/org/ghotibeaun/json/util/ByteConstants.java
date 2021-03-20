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

public class ByteConstants {

    public static final byte NULL = 0x0;
    public static final byte SOH = 0x1;
    public static final byte STX = 0x2;
    public static final byte ETX = 0x3;
    public static final byte EOT = 0x4;
    public static final byte ENQ = 0x5;
    public static final byte ACK = 0x6;
    public static final byte BEL = 0x7;
    public static final byte BS = 0x8;
    public static final byte TAB = 0x9;
    public static final byte LF = 0xa;
    public static final byte VT = 0xb;
    public static final byte FF = 0xc;
    public static final byte CR = 0xd;
    public static final byte SO = 0xe;
    public static final byte SI = 0xf;
    public static final byte DLE = 0x10;
    public static final byte DC1 = 0x11;
    public static final byte DC2 = 0x12;
    public static final byte DC3 = 0x13;
    public static final byte DC4 = 0x14;
    public static final byte NAK = 0x15;
    public static final byte SYN = 0x16;
    public static final byte ETB = 0x17;
    public static final byte CAN = 0x18;
    public static final byte EM = 0x19;
    public static final byte SUB = 0x1a;
    public static final byte ESC = 0x1b;
    public static final byte FS = 0x1c;
    public static final byte GS = 0x1d;
    public static final byte RS = 0x1e;
    public static final byte US = 0x1f;
    public static final byte SPACE = 0x20;
    public static final byte EXCLAMATION = 0x21;
    public static final byte QUOTE = 0x22;
    public static final byte HASH = 0x23;
    public static final byte DOLLAR = 0x24;
    public static final byte PERCENT = 0x25;
    public static final byte AMPERSAND = 0x26;
    public static final byte SINGLEQUOTE = 0x27;
    public static final byte LEFTPAREN = 0x28;
    public static final byte RIGHTPAREN = 0x29;
    public static final byte ASTERISK = 0x2a;
    public static final byte PLUS = 0x2b;
    public static final byte COMMA = 0x2c;
    public static final byte MINUS = 0x2d;
    public static final byte DECIMAL = 0x2e;
    public static final byte SOLIDUS = 0x2f;
    public static final byte ZERO = 0x30;
    public static final byte ONE = 0x31;
    public static final byte TWO = 0x32;
    public static final byte THREE = 0x33;
    public static final byte FOUR = 0x34;
    public static final byte FIVE = 0x35;
    public static final byte SIX = 0x36;
    public static final byte SEVEN = 0x37;
    public static final byte EIGHT = 0x38;
    public static final byte NINE = 0x39;
    public static final byte COLON = 0x3a;
    public static final byte SEMICOLON = 0x3b;
    public static final byte LESSTHAN = 0x3c;
    public static final byte EQUALS = 0x3d;
    public static final byte GREATERTHAN = 0x3e;
    public static final byte QUESTION = 0x3f;
    public static final byte AT = 0x40;
    public static final byte A = 0x41;
    public static final byte B = 0x42;
    public static final byte C = 0x43;
    public static final byte D = 0x44;
    public static final byte E = 0x45;
    public static final byte F = 0x46;
    public static final byte G = 0x47;
    public static final byte H = 0x48;
    public static final byte I = 0x49;
    public static final byte J = 0x4a;
    public static final byte K = 0x4b;
    public static final byte L = 0x4c;
    public static final byte M = 0x4d;
    public static final byte N = 0x4e;
    public static final byte O = 0x4f;
    public static final byte P = 0x50;
    public static final byte Q = 0x51;
    public static final byte R = 0x52;
    public static final byte S = 0x53;
    public static final byte T = 0x54;
    public static final byte U = 0x55;
    public static final byte V = 0x56;
    public static final byte W = 0x57;
    public static final byte X = 0x58;
    public static final byte Y = 0x59;
    public static final byte Z = 0x5a;
    public static final byte LEFT_SQUARE_BRACKET = 0x5b;
    public static final byte BACKSLASH = 0x5c;
    public static final byte RIGHT_SQUARE_BRACKET = 0x5d;
    public static final byte a = 0x61;
    public static final byte b = 0x62;
    public static final byte c = 0x63;
    public static final byte d = 0x64;
    public static final byte e = 0x65;
    public static final byte f = 0x66;
    public static final byte g = 0x67;
    public static final byte h = 0x68;
    public static final byte i = 0x69;
    public static final byte j = 0x6a;
    public static final byte k = 0x6b;
    public static final byte l = 0x6c;
    public static final byte m = 0x6d;
    public static final byte n = 0x6e;
    public static final byte o = 0x6f;
    public static final byte p = 0x70;
    public static final byte q = 0x71;
    public static final byte r = 0x72;
    public static final byte s = 0x73;
    public static final byte t = 0x74;
    public static final byte u = 0x75;
    public static final byte v = 0x76;
    public static final byte w = 0x77;
    public static final byte x = 0x78;
    public static final byte y = 0x79;
    public static final byte z = 0x7a;


    public static final byte START_MAP = 0x7b;
    public static final byte END_MAP = 0x7d;



    public static final byte START_ARRAY = LEFT_SQUARE_BRACKET;
    public static final byte END_ARRAY = RIGHT_SQUARE_BRACKET;
    private ByteConstants() {
        // TODO Auto-generated constructor stub
    }

}
