package com.chokavo.chosportsman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by repitch on 10.05.16.
 */
public class MathUtils {
    public static List<Integer> getRandomNumbers(int from, int to, int howMuch) {
        int size = to - from;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            numbers.add(from + i);
        }
        howMuch = Math.min(howMuch, size);

        Random random = new Random(System.currentTimeMillis());
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < howMuch; i++) {
            int loc = random.nextInt(numbers.size());
            result.add(numbers.get(loc));
            numbers.remove(loc);
        }
        return result;
    }
}
