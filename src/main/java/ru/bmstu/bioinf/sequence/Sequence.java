package ru.bmstu.bioinf.sequence;

import java.io.Serializable;

/**
 * Последовательность белка
 */
public class Sequence implements Serializable {
    private String name;
    private String sequence;

    public Sequence(String name, String sequence) {
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

    @Override
    public String toString() {
        return "name: " + name + "\n" + "sequence: " + sequence;
    }
}
