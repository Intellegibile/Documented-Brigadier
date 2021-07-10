// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;

/**
 * Questa classe definisce un eccezione semplice da usare in caso di comandi sbagliati
 * è composta solo da un semplice messaggio definito dall interfaccia Message
 */
public class SimpleCommandExceptionType implements CommandExceptionType {
    private final Message message;

    /**
     * Il costruttore della classe inizializza il messaggio di tipo interfaccia Message
     * @param message Messaggio in parametro
     */
    public SimpleCommandExceptionType(final Message message) {
        this.message = message;
    }

    public CommandSyntaxException create() {
        return new CommandSyntaxException(this, message);
    }

    public CommandSyntaxException createWithContext(final ImmutableStringReader reader) {
        return new CommandSyntaxException(this, message, reader.getString(), reader.getCursor());
    }

    @Override
    public String toString() {
        return message.getString();
    }
}
