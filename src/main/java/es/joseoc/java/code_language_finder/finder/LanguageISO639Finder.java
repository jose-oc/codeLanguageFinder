package es.joseoc.java.code_language_finder.finder;

import es.joseoc.java.code_language_finder.model.LanguageISO639;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class stores the ISO 639 language codes to find the language by name or any of its codes.
 * It reads the codes from a file downloaded from http://www-01.sil.org/iso639-3/iso-639-3.tab
 *
 * That file has this structure:
 * <pre>
 * CREATE TABLE [ISO_639-3] (
 Id      char(3) NOT NULL,  -- The three-letter 639-3 identifier
 Part2B  char(3) NULL,      -- Equivalent 639-2 identifier of the bibliographic applications
 -- code set, if there is one
 Part2T  char(3) NULL,      -- Equivalent 639-2 identifier of the terminology applications code
 -- set, if there is one
 Part1   char(2) NULL,      -- Equivalent 639-1 identifier, if there is one
 Scope   char(1) NOT NULL,  -- I(ndividual), M(acrolanguage), S(pecial)
 Type    char(1) NOT NULL,  -- A(ncient), C(onstructed),
 -- E(xtinct), H(istorical), L(iving), S(pecial)
 Ref_Name   varchar(150) NOT NULL,   -- Reference language name
 Comment    varchar(150) NULL)       -- Comment relating to one or more of the columns
 * </pre>
  *
 * Information about this:
 * - http://www-01.sil.org/iso639-3/default.asp
 * - https://en.wikipedia.org/wiki/ISO_639
 * - https://en.wikipedia.org/wiki/ISO_639-3
 */
@Slf4j
public class LanguageISO639Finder {
    private static final String LANG_CODES_FILE = "iso-639-3_20170202.tab";
    private static final String LANG_CODES_URL = "http://www-01.sil.org/iso639-3/iso-639-3.tab";

    private final List<LanguageISO639> languages;


    private static class LazyHolder {
        private static final LanguageISO639Finder INSTANCE = new LanguageISO639Finder();
    }

    public static LanguageISO639Finder getInstance() {
        return LazyHolder.INSTANCE;
    }

    private LanguageISO639Finder() {
        languages = loadLanguagesFromFile();
    }

    private List<LanguageISO639> loadLanguagesFromFile() {
        try {
            return loadFile();
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file " + LANG_CODES_FILE + " if the file does not exist you can download it from " + LANG_CODES_URL, e);
        }
    }

    private List<LanguageISO639> loadFile() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilename()), UTF_8)) {
            return reader.lines()
                    .flatMap(line -> Stream.of(line.split(System.lineSeparator())))
                    .map(line -> (String) line)
                    .filter(validLanguages())
                    .distinct()
                    .map(line -> newLanguageISO639(line))
                    .collect(Collectors.toList());
        }
    }

    private LanguageISO639 newLanguageISO639(String line) {
        String[] chunk = line.split("\t");
        return new LanguageISO639(chunk[0], chunk[1], chunk[2], chunk[3], chunk[4], chunk[5], chunk[6]);
    }

    private Predicate<? super String> validLanguages() {
        return line -> {
            String[] chunkLine = line.split("\t");
            return haspart2B(chunkLine)
                    && hasPart2T(chunkLine)
                    && hasPart1(chunkLine)
                    && isLiveLanguage(chunkLine);
        };
    }

    private boolean haspart2B(String[] chunkLine) {
        return isNotBlank(chunkLine[1]);
    }

    private boolean hasPart2T(String[] chunkLine) {
        return isNotBlank(chunkLine[2]);
    }

    private boolean hasPart1(String[] chunkLine) {
        return isNotBlank(chunkLine[3]);
    }

    private boolean isLiveLanguage(String[] chunkLine) {
        return chunkLine[5].equals("L");
    }

    // Paths cannot be used to read project resources: http://stackoverflow.com/a/34812661/1609873
    private URI inputFilename() {
        try {
            return this.getClass().getResource("/" + LANG_CODES_FILE).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error getting URI, filename: " + Objects.toString(LANG_CODES_FILE), e);
        }
    }

    public LanguageISO639 findLanguageFilteringByCodePart1(String codeLangPart1) {
        List<LanguageISO639> languagesMatched = this.languages
                .parallelStream()
                .filter( l -> l.getPart1().equals(codeLangPart1) )
                .collect(
                        Collectors.toList()
                );
        log.debug(String.format("Finding code languages ISO 639-3 for language '%s'. %d codes found: %s", codeLangPart1, languagesMatched.size(),
                languagesMatched.parallelStream().map(l -> l.getId()).collect(Collectors.joining(", "))));

        return languagesMatched.stream().findFirst().orElse(LanguageISO639.UNDEFINED_LANGUAGE);
    }

    public LanguageISO639 findLanguageFilteringByCodePart2B(String codeLangPart2B) {
        List<LanguageISO639> languagesMatched = this.languages
                .parallelStream()
                .filter( l -> l.getPart2B().equals(codeLangPart2B) )
                .collect(
                        Collectors.toList()
                );
        log.debug(String.format("Finding code languages ISO 639-3 for language '%s'. %d codes found: %s", codeLangPart2B, languagesMatched.size(),
                languagesMatched.parallelStream().map(l -> l.getId()).collect(Collectors.joining(", "))));

        return languagesMatched.stream().findFirst().orElse(LanguageISO639.UNDEFINED_LANGUAGE);
    }

    public LanguageISO639 findLanguageFilteringByCodePart2T(String codeLangPart2T) {
        List<LanguageISO639> languagesMatched = this.languages
                .parallelStream()
                .filter( l -> l.getPart2T().equals(codeLangPart2T) )
                .collect(
                        Collectors.toList()
                );
        log.debug(String.format("Finding code languages ISO 639-3 for language '%s'. %d codes found: %s", codeLangPart2T, languagesMatched.size(),
                languagesMatched.parallelStream().map(l -> l.getId()).collect(Collectors.joining(", "))));

        return languagesMatched.stream().findFirst().orElse(LanguageISO639.UNDEFINED_LANGUAGE);
    }

    public LanguageISO639 findLanguageFilteringByName(String langName) {
        List<LanguageISO639> languagesMatched = this.languages
                .parallelStream()
                .filter( l -> l.getName().equalsIgnoreCase(langName) )
                .collect(
                        Collectors.toList()
                );
        log.debug(String.format("Finding code languages ISO 639-3 for language '%s'. %d codes found: %s", langName, languagesMatched.size(),
                languagesMatched.parallelStream().map(l -> l.getId()).collect(Collectors.joining(", "))));

        return languagesMatched.stream().findFirst().orElse(LanguageISO639.UNDEFINED_LANGUAGE);
    }

    public static LanguageISO639 findLanguage(String lang) {
        LanguageISO639Finder finder = LanguageISO639Finder.getInstance();

        LanguageISO639 language = finder.findLanguageFilteringByCodePart1(lang);
        if (language.equals(LanguageISO639.UNDEFINED_LANGUAGE)) {
            language = finder.findLanguageFilteringByCodePart2B(lang);
        }
        if (language.equals(LanguageISO639.UNDEFINED_LANGUAGE)) {
            language = finder.findLanguageFilteringByCodePart2T(lang);
        }
        if (language.equals(LanguageISO639.UNDEFINED_LANGUAGE)) {
            language = finder.findLanguageFilteringByName(lang);
        }
        if (language.equals(LanguageISO639.UNDEFINED_LANGUAGE)) {
            log.warn("Language " + lang + " not found. If you're sure it has a 693-3 code check it on the latest version on the file " + LANG_CODES_URL);
        }

        return language;
    }
}