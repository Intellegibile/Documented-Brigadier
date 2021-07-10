// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class StringReader implements ImmutableStringReader {
    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';

    private final String string;
    private int cursor;

    public StringReader(final StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    public StringReader(final String string) {
        this.string = string;
    }

    /**
     * @return Ritorna la stringa venente letta
     */
    @Override
    public String getString() {
        return string;
    }

    /**
     * @param cursor Posiziona il cursore nella posizione passata in parametro
     */
    public void setCursor(final int cursor) {
        this.cursor = cursor;
    }

    /**
     * @return La lunghezza rimanente della stringa
     */
    @Override
    public int getRemainingLength() {
        return string.length() - cursor;
    }

    /**
     * @return La lunghezza totale della stringa
     */
    @Override
    public int getTotalLength() {
        return string.length();
    }

    /**
     * @return Cursore
     */
    @Override
    public int getCursor() {
        return cursor;
    }

    /**
     * @return La stringa già letta, partendo dall'index 0 fino all'index posizione del cursore
     */
    @Override
    public String getRead() {
        return string.substring(0, cursor);
    }

    /**
     * @return La stringa rimanente non ancora letta, partendo quindi dall'index della posizione del cursore
     */
    @Override
    public String getRemaining() {
        return string.substring(cursor);
    }

    /**
     * Torna vero se il cursore sommato alla lunghezza in parametro è minore
     * della lunghezza della stringa.
     * Utile per sapere se ci sono ancora caratteri della stringa da leggere
     * @param length Lunghezza da sommare al cursore per sapere se entro quella lunghezza, la stringa contiene ancora caratteri quindi è ancora leggibile
     * @return Booleano
     */
    @Override
    public boolean canRead(final int length) {
        return cursor + length <= string.length();
    }

    /**
     * Torna vero se il cursore ha ancora almeno un carattere da leggere dalla stringa.
     * Questo metodo richiama il metodo omonimo di parametro int length
     * @return Booleano
     */
    @Override
    public boolean canRead() {
        return canRead(1);
    }

    /**
     * @return Carattere nella stringa venente letta corrispondente alla posizione del cursore
     */
    @Override
    public char peek() {
        return string.charAt(cursor);
    }

    /**
     * @param offset Spostamento da sommare al cursore
     * @return Carattere nella stringa venente letta corrispondente alla posizione ottenuta dalla somma del cursore e dello spostamento
     */
    @Override
    public char peek(final int offset) {
        return string.charAt(cursor + offset);
    }

    /**
     * Questo metodo avanza nella lettura della stringa leggendo il carattere seguente alla posizione
     * del cursore
     * @return Carattere nella stringa venente letta corrispondente alla posizione del cursore spostata in avanti di 1
     */
    public char read() {
        return string.charAt(cursor++);
    }

    /**
     * Questo metodo fa avanzare di 1 carattere il cursore
     */
    public void skip() {
        cursor++;
    }

    /**
     * Torna vero se il carattere in parametro è compreso tra 0 e 9 o uguale a '.' o '-'
     * @param c Carattere in parametro
     * @return Booleano
     */
    public static boolean isAllowedNumber(final char c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-';
    }

    /**
     * Torna vero se è iniziata una stringa entro virgolette, ovvero se il carattere
     * in parametro è una doppia virgoletta o una singola virgoletta indicando
     * che i caratteri seguenti fanno parte di una stringa tra virgolette
     * @param c Carattere in parametro
     * @return Booleano
     */
    public static boolean isQuotedStringStart(char c) {
        return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
    }

    /**
     * Questo metodo viene usato per saltare gli spazi bianchi tra le parole
     * Un ciclo while si aziona finchè ci sono caratteri da leggere e il carattere
     * preso con il metodo 'peek()' è spazio bianco allora
     * viene invocato il metodo 'skip()'
     */
    public void skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip();
        }
    }

    public int readInt() throws CommandSyntaxException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
        }
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number);
        }
    }

    public long readLong() throws CommandSyntaxException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedLong().createWithContext(this);
        }
        try {
            return Long.parseLong(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidLong().createWithContext(this, number);
        }
    }

    public double readDouble() throws CommandSyntaxException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedDouble().createWithContext(this);
        }
        try {
            return Double.parseDouble(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(this, number);
        }
    }

    public float readFloat() throws CommandSyntaxException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedFloat().createWithContext(this);
        }
        try {
            return Float.parseFloat(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().createWithContext(this, number);
        }
    }

    public static boolean isAllowedInUnquotedString(final char c) {
        return c >= '0' && c <= '9'
            || c >= 'A' && c <= 'Z'
            || c >= 'a' && c <= 'z'
            || c == '_' || c == '-'
            || c == '.' || c == '+';
    }

    public String readUnquotedString() {
        final int start = cursor;
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip();
        }
        return string.substring(start, cursor);
    }

    public String readQuotedString() throws CommandSyntaxException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (!isQuotedStringStart(next)) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this);
        }
        skip();
        return readStringUntil(next);
    }

    public String readStringUntil(char terminator) throws CommandSyntaxException {
        final StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (canRead()) {
            final char c = read();
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c);
                    escaped = false;
                } else {
                    setCursor(getCursor() - 1);
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(this, String.valueOf(c));
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return result.toString();
            } else {
                result.append(c);
            }
        }

        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this);
    }

    public String readString() throws CommandSyntaxException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (isQuotedStringStart(next)) {
            skip();
            return readStringUntil(next);
        }
        return readUnquotedString();
    }

    public boolean readBoolean() throws CommandSyntaxException {
        final int start = cursor;
        final String value = readString();
        if (value.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedBool().createWithContext(this);
        }

        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidBool().createWithContext(this, value);
        }
    }

    public void expect(final char c) throws CommandSyntaxException {
        if (!canRead() || peek() != c) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedSymbol().createWithContext(this, String.valueOf(c));
        }
        skip();
    }
}
