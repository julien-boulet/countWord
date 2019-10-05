package com.boubou;

import com.google.common.io.Resources;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws Exception {

        final long startTime = System.nanoTime();

        try (Scanner scanner = new Scanner(Resources.getResource(("les3moustquetaires.txt")).getFile())) {

            // read text and count word
            Stream<String> stream1 = Files.lines(Paths.get(scanner.nextLine()));
            Map<String, Long> wordAndCount = splitText(stream1);

            // sorted and keep top 100
            Stream<Map.Entry<String, Long>> result = order(wordAndCount);
            display(result);

        }
        final long endTime = System.nanoTime();

        System.out.printf("Execution time in millisecond : %d%n", TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
    }

    /**
     * Method to count words form a stream of String
     *
     * @param stream The String stream to study
     * @return A map with in key the word and in value the count of it in this stream
     */
    static Map<String, Long> splitText(Stream<String> stream) {
        return stream
                .parallel()
                .map(String::toLowerCase)
                .map(App::splitLine)
                .flatMap(Arrays::stream)
                .filter(Predicate.not(String::isBlank)) // java 11
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * Method to reverse order the map by value
     *
     * @param wordAndCount The map to order by value
     * @return The stream of the map ordered by value
     */
    static Stream<Map.Entry<String, Long>> order(Map<String, Long> wordAndCount) {
        return wordAndCount
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
    }

    static void display(Stream<Map.Entry<String, Long>> stream) {
        stream.limit(100).forEach(System.out::println);
    }

    /**
     * Method to split a String and only keep word. Remove "a lot of" punctuations
     *
     * @param line The String to parse
     * @return The String array with only word in it
     */
    static String[] splitLine(String line) {
        return line.split("['.,\\/#!$%\\^&\\*;:{}=\\-_`~()\\[\\]«»?\\s\"]+");
    }

}
