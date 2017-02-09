package es.joseoc.java.code_language_finder.finder;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
public final class LanguageBCP47Translator {

    private static final Map<String, String> LANGUAGES_TRANSLATION_BCP47;

    static {
        SortedMap<String, String> languagesTranslationBCP47 = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        languagesTranslationBCP47.put("ar", "ar");
        languagesTranslationBCP47.put("de-AT", "de-AT");
        languagesTranslationBCP47.put("de-CH", "de-CH");
        languagesTranslationBCP47.put("de", "de");
        languagesTranslationBCP47.put("deu", "de");
        languagesTranslationBCP47.put("en", "en");
        languagesTranslationBCP47.put("eng", "en");
        languagesTranslationBCP47.put("es", "es");
        languagesTranslationBCP47.put("esp", "es");
        languagesTranslationBCP47.put("spa", "es");
        languagesTranslationBCP47.put("fr-CH", "fr-CH");
        languagesTranslationBCP47.put("fr", "fr");
        languagesTranslationBCP47.put("fre", "fr");
        languagesTranslationBCP47.put("hu", "hu");
        languagesTranslationBCP47.put("it-CH", "it-CH");
        languagesTranslationBCP47.put("it", "it");
        languagesTranslationBCP47.put("ita", "it");
        languagesTranslationBCP47.put("nl", "nl");
        languagesTranslationBCP47.put("tr", "tr");
        languagesTranslationBCP47.put("vi", "vi");
        LANGUAGES_TRANSLATION_BCP47 = Collections.unmodifiableSortedMap(languagesTranslationBCP47);
    }

    private LanguageBCP47Translator() {

    }

    public static String translateLanguage(String lang) {
        LanguageBCP47Translator translator = new LanguageBCP47Translator();
        return translator.translate(lang);
    }

    private String translate(String lang) {
        String translatedLanguage = null;
        if (lang !=null) {
            translatedLanguage = LANGUAGES_TRANSLATION_BCP47.get(lang);
            if (translatedLanguage == null) {
                log.warn("The language '" + lang + "' does not match with the accepted BCP47 values. Currently these values are accepted: " + LANGUAGES_TRANSLATION_BCP47.keySet());
            }
        }
        return translatedLanguage;
    }
}