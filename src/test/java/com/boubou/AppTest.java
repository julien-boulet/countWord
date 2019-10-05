package com.boubou;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    // Test splitText

    @Test
    public void splitTextTest() {

        // test casual sentence
        Stream<String> test = Stream.of("Il y a un an à peu près, qu'en");
        Map<String, Long> result = Map.of("il", 1L, "y", 1L, "a", 1L, "un", 1L,
                "an", 1L, "à", 1L, "peu", 1L, "près", 1L, "qu", 1L, "en", 1L);
        assertEquals(result, App.splitText(test));

        // test all regex split characters
        test = Stream.of("a'a.a,a/a#a!a$a%a^a&a*a;a:a{a}a=a-a_a`a~a(a)a[a]a«a»a?a a");
        result = Map.of("a", 29L);
        assertEquals(result, App.splitText(test));

        // test empty sentence
        test = Stream.empty();
        result = Map.of();
        assertEquals(result, App.splitText(test));

        // test lowercase
        test = Stream.of("Paul DURANT PAUL paul pAuL");
        result = Map.of("paul", 4L, "durant", 1l);
        assertEquals(result, App.splitText(test));

    }

    @ParameterizedTest
    @ValueSource(strings = {"'", ".", ",", "/", "#", "!", "$", "%", "^", "&", "*", ";", ":", "{", "}", "=", "-", "_", "`", "~", "(", ")",
            "[", "]", "«", "»", "?", " "})
    public void splitTextTest(String character) {

        // test only one split character in Stream
        Stream<String> test = Stream.of(character);
        Map<String, Long> result = Map.of();
        assertEquals(result, App.splitText(test));
    }

    // test sorted   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

    @Test
    public void orderTest() {

        // test same count map
        Map<String, Long> test = Map.of("il", 1L, "y", 1L, "a", 1L, "un", 1L,
                "an", 1L, "à", 1L, "peu", 1L, "près", 1L, "qu", 1L, "en", 1L);
        Map<String, Long> result = App.order(test).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        assertEquals(result, test);

        // test disorder map
        Map<String, Long> expected = Map.of("a", 7L, "b", 6L, "c", 5L, "d", 4L, "e", 3L, "f", 2L, "g", 1L);
        test = Map.of("e", 3L, "b", 6L, "g", 1L, "c", 5L, "a", 7L, "d", 4L, "f", 2L);
        result = App.order(test).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        assertEquals(result, expected);

        // test empty map
        result = App.order(Map.of()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        assertEquals(result, Map.of());
    }
}
