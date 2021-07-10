// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier;

/**
 * Questa classe implementa Message
 * Definisce un messaggio, viene usata solo
 * per ritornare una stringa
 */
public class LiteralMessage implements Message {
    private final String string; //La stringa che contiene il messaggio

    /**
     * Il costruttore che inizializza la stringa del messaggio
     * @param string valore da inizializzare a string
     */
    public LiteralMessage(final String string) {
        this.string = string;
    }

    /**
     * @return La stringa
     */
    @Override
    public String getString() {
        return string;
    }

    /**
     * @return Ritorna la stringa
     */
    @Override
    public String toString() {
        return string;
    }
}
