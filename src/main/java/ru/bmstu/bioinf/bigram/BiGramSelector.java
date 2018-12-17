package ru.bmstu.bioinf.bigram;

import ru.bmstu.bioinf.filtering.Node;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.*;

/**
 * Выделяет биграммы
 */
public class BiGramSelector {
    private Sequence searchedSequence;
    private Sequence dataSetSequence;

    public BiGramSelector(Sequence searchedSequence, Sequence dataSetSequence) {
        this.searchedSequence = searchedSequence;
        this.dataSetSequence = dataSetSequence;
    }

    public List<Set<Node>> getNewBiGrams() {
        Map<String, List<Integer>> searchedBiGrams = searchedSequence.getBiGrams();
        Map<String, List<Integer>> dataSetBiGrams = dataSetSequence.getBiGrams();

        int size = searchedSequence.length() + dataSetSequence.length() - 1;
        List<Set<Node>> diags = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            diags.add(Collections.emptySet());
        }

        int maxDiagSize = Math.min(searchedSequence.length(), dataSetSequence.length());

        for (Map.Entry<String, List<Integer>> entry : searchedBiGrams.entrySet()) {
            if (dataSetBiGrams.containsKey(entry.getKey())) {
                List<Integer> searchedIndices = entry.getValue();
                List<Integer> dataSetIndices = dataSetBiGrams.get(entry.getKey());
                for (Integer searchedIndex : searchedIndices) {
                    for (Integer dataSetIndex : dataSetIndices) {
                        int diagIndex = searchedIndex - dataSetIndex + dataSetSequence.length() - 1;
                        Set<Node> diag = diags.get(diagIndex);

                        if (diag.isEmpty()) {
                            diag = new HashSet<>(maxDiagSize);
                            diags.set(diagIndex, diag);
                        }

                        Node first = new Node(searchedIndex, dataSetIndex, entry.getKey().charAt(0), entry.getKey().charAt(0));
                        Node last = new Node(searchedIndex + 1, dataSetIndex + 1, entry.getKey().charAt(1), entry.getKey().charAt(1));

                        diag.add(first);
                        diag.add(last);
                    }
                }
            }
        }

        return diags;
    }
}
