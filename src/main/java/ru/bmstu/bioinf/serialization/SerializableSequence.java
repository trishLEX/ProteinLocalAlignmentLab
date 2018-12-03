package ru.bmstu.bioinf.serialization;

import ru.bmstu.bioinf.sequence.Sequence;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SerializableSequence implements Serializable {
    private Sequence sequence;
    private Map<String, List<Integer>> indices;

    public SerializableSequence(Sequence sequence, Map<String, List<Integer>> indices) {
        this.sequence = sequence;
        this.indices = indices;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public Map<String, List<Integer>> getIndices() {
        return indices;
    }
}
