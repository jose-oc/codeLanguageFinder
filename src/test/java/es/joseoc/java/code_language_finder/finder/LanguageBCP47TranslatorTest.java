package es.joseoc.java.code_language_finder.finder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LanguageBCP47TranslatorTest {

    @Test
    public void testTranslateNullLanguage() throws Exception {
        String actual = LanguageBCP47Translator.translateLanguage(null);
        assertNull(actual);
    }

    @Test
    public void testTranslateNotValidLanguage() throws Exception {
        String actual = LanguageBCP47Translator.translateLanguage("NO-VALID");
        assertNull(actual);
    }

    @Test
    public void testTranslateLanguage() throws Exception {
        assertEquals("de-AT", LanguageBCP47Translator.translateLanguage("de-AT"));
        assertEquals("de-AT", LanguageBCP47Translator.translateLanguage("dE-aT"));
        assertEquals("en", LanguageBCP47Translator.translateLanguage("EN"));
        assertEquals("en", LanguageBCP47Translator.translateLanguage("eNg"));
    }

}
