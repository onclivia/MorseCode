package com.tppa.morsecode;

import android.util.ArrayMap;

public class Alphabet {

    public final ArrayMap<String, String> alphabet = new ArrayMap<>();
    public final ArrayMap<String, String> morse_code_alphabet = new ArrayMap<>();

    public Alphabet(){
        createAlphabet();
        createMorseCodeAlphabet();
    }

    public void createAlphabet() {

        alphabet.put("a", "•-");
        alphabet.put("b", "-•••");
        alphabet.put("c", "-•-•");
        alphabet.put("d", "-••");
        alphabet.put("e", "•");
        alphabet.put("f", "••-•");
        alphabet.put("g", "--•");
        alphabet.put("h", "••••");
        alphabet.put("i", "••");
        alphabet.put("j", "•---");
        alphabet.put("k", "-•-");
        alphabet.put("l", "•-••");
        alphabet.put("m", "--");
        alphabet.put("n", "-•");
        alphabet.put("o", "---");
        alphabet.put("p", "•--•");
        alphabet.put("q", "--•-");
        alphabet.put("r", "•-•");
        alphabet.put("s", "•••");
        alphabet.put("t", "-");
        alphabet.put("u", "••-");
        alphabet.put("v", "•••-");
        alphabet.put("w", "•--");
        alphabet.put("x", "-••-");
        alphabet.put("y", "-•--");
        alphabet.put("z", "--••");

        alphabet.put("0", "-----");
        alphabet.put("1", "•----");
        alphabet.put("2", "••---");
        alphabet.put("3", "•••--");
        alphabet.put("4", "••••-");
        alphabet.put("5", "•••••");
        alphabet.put("6", "-••••");
        alphabet.put("7", "--•••");
        alphabet.put("8", "---••");
        alphabet.put("9", "----•");

        alphabet.put(".", "•-•-•-");
        alphabet.put(",", "--••--");
        alphabet.put("?", "••--••");
        alphabet.put(":", "---•••");
        alphabet.put("/", "-••-•");
        alphabet.put("-", "-••••-");
        alphabet.put("=", "-•••-");
        alphabet.put("!", "-•-•--");
        alphabet.put(";", "-•-•-•");
    }


    public void createMorseCodeAlphabet() {

        morse_code_alphabet.put("•-", "a");
        morse_code_alphabet.put("-•••", "b");
        morse_code_alphabet.put("-•-•", "c");
        morse_code_alphabet.put("-••", "d");
        morse_code_alphabet.put("•", "e");
        morse_code_alphabet.put("••-•", "f");
        morse_code_alphabet.put("--•", "g");
        morse_code_alphabet.put("••••", "h");
        morse_code_alphabet.put("••", "i");
        morse_code_alphabet.put("•---", "j");
        morse_code_alphabet.put("-•-", "k");
        morse_code_alphabet.put("•-••", "l");
        morse_code_alphabet.put("--", "m");
        morse_code_alphabet.put("-•", "n");
        morse_code_alphabet.put("---", "o");
        morse_code_alphabet.put("•--•", "p");
        morse_code_alphabet.put("--•-", "q");
        morse_code_alphabet.put("•-•", "r");
        morse_code_alphabet.put("•••", "s");
        morse_code_alphabet.put("-", "t");
        morse_code_alphabet.put( "••-", "u");
        morse_code_alphabet.put("•••-", "v");
        morse_code_alphabet.put("•--", "w");
        morse_code_alphabet.put("-••-", "x");
        morse_code_alphabet.put("-•--", "y");
        morse_code_alphabet.put("--••", "z");

        morse_code_alphabet.put("-----", "0");
        morse_code_alphabet.put("•----", "1");
        morse_code_alphabet.put("••---", "2");
        morse_code_alphabet.put("•••--", "3");
        morse_code_alphabet.put("••••-", "4");
        morse_code_alphabet.put("•••••", "5");
        morse_code_alphabet.put("-••••", "6");
        morse_code_alphabet.put("--•••", "7");
        morse_code_alphabet.put("---••", "8");
        morse_code_alphabet.put("----•", "9");

        morse_code_alphabet.put("•-•-•-", ".");
        morse_code_alphabet.put("--••--", ",");
        morse_code_alphabet.put("••--••", "?");
        morse_code_alphabet.put("---•••", ":");
        morse_code_alphabet.put("-••-•", "/");
        morse_code_alphabet.put("-••••-", "-");
        morse_code_alphabet.put("-•••-", "=");
        morse_code_alphabet.put("-•-•--", "!");
        morse_code_alphabet.put("-•-•-•", ";");
    }
}
