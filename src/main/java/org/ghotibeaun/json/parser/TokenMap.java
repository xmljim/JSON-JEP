package org.ghotibeaun.json.parser;

import java.util.LinkedHashMap;

import org.ghotibeaun.json.exception.JSONParserException;

class TokenMap extends LinkedHashMap<String, Object> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TokenMap(Tokenizer x) throws JSONParserException {

        char c;
        String key;

        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }

        for (;;) {
            c = x.nextClean();
            switch (c) {
            case 0:
                throw x.syntaxError("A JSONObject text must end with '}'");
            case '}':
                return;
            default:
                x.back();
                key = x.nextValue().toString();
            }

            // The key is followed by ':'. We will also tolerate '=' or '=>'.

            c = x.nextClean();
            if (c == '=') {
                if (x.next() != '>') {
                    x.back();
                }
            } else if (c != ':') {
                throw x.syntaxError("Expected a ':' after a key");
            }
            this.put(key, x.nextValue());

            // Pairs are separated by ','. We will also tolerate ';'.

            switch (x.nextClean()) {
            case ';':
            case ',':
                if (x.nextClean() == '}') {
                    return;
                }
                x.back();
                break;
            case '}':
                return;
            default:
                throw x.syntaxError("Expected a ',' or '}'");
            }
        }
    }

}
