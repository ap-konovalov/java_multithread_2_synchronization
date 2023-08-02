package ru.netology;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// https://github.com/netology-code/jd-homeworks/blob/video/synchronization/README.md
public class Main {

    private static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final Random random = new Random();

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countOfCharInString = getCountOfCharInString('R', route);
                updateSizeToFreqMap(countOfCharInString);
            }).start();
        }
        Map.Entry<Integer, Integer> maxEntry = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue());
        printMaxEntry(maxEntry);
        printOtherSizes(maxEntry.getKey());
    }

    private static void printMaxEntry(Map.Entry<Integer, Integer> maxEntry) {
        System.out.println("Самое частое количество повторений " + maxEntry.getKey() + " (встретилось " +
                maxEntry.getValue() + " раз)");
    }

    private static void printOtherSizes(Integer maxEntryKey) {
        Map<Integer, Integer> otherSizesMap = new HashMap<>(sizeToFreq);
        otherSizesMap.remove(maxEntryKey);
        System.out.println("Другие размеры:");
        otherSizesMap.forEach((key, value) -> System.out.println("- " + key + " (" + value + " раз)"));
    }

    private static synchronized void updateSizeToFreqMap(int countOfCharInString) {
        if (sizeToFreq.containsKey(countOfCharInString)) {
            Integer newValue = sizeToFreq.get(countOfCharInString) + 1;
            sizeToFreq.replace(countOfCharInString, newValue);
        } else {
            sizeToFreq.put(countOfCharInString, 1);
        }
    }

    public static String generateRoute(String letters, int length) {
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    private static int getCountOfCharInString(Character character, String string) {
        Pattern pattern = Pattern.compile("[^" + character + "]*" + character);
        Matcher matcher = pattern.matcher(string);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}