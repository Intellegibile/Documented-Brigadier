// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.Message;

import java.util.function.Function;

/**
 * Questa classe definisce un eccezione dinamica, si intende
 * un eccezzione della quale si può definire il modo in cui il messaggio
 * debba venire restituito, per ottenere questo si usa una funzione come parametro
 * al costruttore di DynamicCommandExceptionType. La funzione prende in parametro
 * un oggetto e restituisce un messaggio, l'utilità di avere un oggetto in parametro
 * è quella di poter costruire un messaggio attorno a quell'oggetto
 * ESEMPIO
 * Passando un intero come parametro alla funzione si può sommare l'intero alla stringa
 */
public class DynamicCommandExceptionType implements CommandExceptionType {
    private final Function<Object, Message> function; //Funzione Object parametro restituisce Message

    /**
     * Il costruttore della classe inizializza la funzione in parametro
     * @param function Funzione in parametro
     */
    public DynamicCommandExceptionType(final Function<Object, Message> function) {
        this.function = function;
    }

    /**
     * Questo metodo crea l'effettiva eccezione, restituendo un CommandSyntaxException
     * passando come parametro un istanza di questa classe e il Message restituito
     * dalla funzione
     * @param arg Oggetto parametro del metodo passato in parametro alla funzione
     * @return CommandSyntaxException
     */
    public CommandSyntaxException create(final Object arg) {
        return new CommandSyntaxException(this, function.apply(arg));
    }

    public CommandSyntaxException createWithContext(final ImmutableStringReader reader, final Object arg) {
        return new CommandSyntaxException(this, function.apply(arg), reader.getString(), reader.getCursor());
    }
}
