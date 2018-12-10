package ru.bmstu.bioinf.sequence;

import java.io.Serializable;
import java.util.*;

/**
 * Последовательность белка
 */
public class Sequence implements Serializable {
    private String name;
    private String sequence;
    private Map<String, List<Integer>> biGrams;

    public Sequence(String name, String sequence) {
        for (char c : sequence.toCharArray()) {
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("Not letter in sequence: " + c);
            }
        }

        this.name = name;
        this.sequence = sequence;
    }

    public String getSequence() {
        return sequence;
    }

    public String substring(int i, int j) {
        return sequence.substring(i, j);
    }

    public String substring(int i) {
        return sequence.substring(i);
    }

    public int length() {
        return sequence.length();
    }

    public char get(int i) {
        return sequence.charAt(i);
    }

    public String getName() {
        return name;
    }

    public synchronized Map<String, List<Integer>> getBiGrams() {
        if (biGrams == null) {
            biGrams = new LinkedHashMap<>();
            for (int i = 0; i < sequence.length() - 1; i++) {
                String biGram = sequence.substring(i, i + 2);
                if (biGrams.containsKey(biGram)) {
                    biGrams.get(biGram).add(i);
                } else {
                    List<Integer> indices = new ArrayList<>(Collections.singletonList(i));
                    biGrams.put(biGram, indices);
                }
            }
        }

        return biGrams;
    }

    @Override
    public String toString() {
        return "name: " + name + "\n" + "sequence: " + sequence;
    }
}
