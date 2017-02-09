package es.joseoc.java.code_language_finder.model;

import lombok.Value;

@Value
public final class LanguageISO639 {

    public static final LanguageISO639 UNDEFINED_LANGUAGE = new LanguageISO639("und", "und", "und", "und", "S", "S", "Undetermined");

    String id;
    String part1;
    String part2B;
    String part2T;
    String scope;
    String type;
    String name;
}
