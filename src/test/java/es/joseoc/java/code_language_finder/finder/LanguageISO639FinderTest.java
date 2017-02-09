package es.joseoc.java.code_language_finder.finder;

import es.joseoc.java.code_language_finder.model.LanguageISO639;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class LanguageISO639FinderTest {

    LanguageISO639Finder lang = LanguageISO639Finder.getInstance();

    @Test
    public void givenValidPart1Language_whenSeekLanguage_findOneLanguage() throws Exception {
        LanguageISO639 actual = lang.findLanguageFilteringByCodePart1("fre");

        assertEquals("fra", actual.getId());
    }

    @Test
    public void givenInvalidPart1Language_whenSeekLanguage_findNooneLanguage() throws Exception {
        LanguageISO639 actual = lang.findLanguageFilteringByCodePart1("nonexisting");

        assertEquals("und", actual.getId());
        assertEquals(LanguageISO639.UNDEFINED_LANGUAGE, actual);
    }

    @Test
    public void givenValidPart2BLanguage_whenSeekLanguage_findOneLanguage() throws Exception {
        LanguageISO639 actual = lang.findLanguageFilteringByCodePart2B("fra");

        assertEquals("fra", actual.getId());
    }

    @Test
    public void givenInvalidPart2BLanguage_whenSeekLanguage_findNooneLanguage() throws Exception {
        LanguageISO639 actual = lang.findLanguageFilteringByCodePart2B("nonexisting");

        assertEquals("und", actual.getId());
        assertEquals(LanguageISO639.UNDEFINED_LANGUAGE, actual);
    }

    @Test
    public void givenValidPart2TLanguage_whenSeekLanguage_findOneLanguage() throws Exception {
        LanguageISO639 actual = lang.findLanguageFilteringByCodePart2T("fr");

        assertEquals("fra", actual.getId());
    }

    @Test
    public void givenInvalidPart2TLanguage_whenSeekLanguage_findNooneLanguage() throws Exception {
        LanguageISO639 actual = lang.findLanguageFilteringByCodePart2T("nonexisting");

        assertEquals("und", actual.getId());
        assertEquals(LanguageISO639.UNDEFINED_LANGUAGE, actual);
    }

    @Test
    public void givenValidNameLanguage_whenSeekLanguage_findOneLanguage() throws Exception {
        LanguageISO639 actual = lang.findLanguageFilteringByName("frENcH");

        assertEquals("fra", actual.getId());
    }

    @Test
    public void givenInvalidNameLanguage_whenSeekLanguage_findNooneLanguage() throws Exception {
        LanguageISO639 actual = lang.findLanguageFilteringByName("nonexisting");

        assertEquals("und", actual.getId());
        assertEquals(LanguageISO639.UNDEFINED_LANGUAGE, actual);
    }

}