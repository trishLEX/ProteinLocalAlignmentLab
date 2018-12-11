package ru.bmstu.bioinf.threading;

import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.bigram.BiGramSelector;
import ru.bmstu.bioinf.filtering.DiagSelection;
import ru.bmstu.bioinf.filtering.Node;
import ru.bmstu.bioinf.sequence.Sequence;
import ru.bmstu.bioinf.sequence.TopSequences;
import ru.bmstu.bioinf.sw.SWAlignment;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalAligner implements Runnable {
    private TopSequences top;
    private Sequence searchedSequence;
    private List<Sequence> dataSetSequences;
    private float gap;
    private float diagScore;
    private int minBiGrams;
    private int radius;

    public LocalAligner(TopSequences top, Sequence searchedSequence, List<Sequence> dataSetSequences, float gap, float diagScore, int minBiGrams, int radius) {
        this.top = top;
        this.searchedSequence = searchedSequence;
        this.dataSetSequences = dataSetSequences;
        this.gap = gap;
        this.diagScore = diagScore;
        this.minBiGrams = minBiGrams;
        this.radius = radius;
    }

    @Override
    public void run() {
        for (Sequence dataSetSequence : dataSetSequences) {
            DiagSelection selection = new DiagSelection(
                    searchedSequence,
                    dataSetSequence,
                    gap,
                    diagScore,
                    minBiGrams,
                    radius
            );

            BiGramSelector biGramSelector = new BiGramSelector(searchedSequence, dataSetSequence);
            List<Set<Node>> nGrams = biGramSelector.getNewNGramsByHash();
            Map<Node, Node> diagonals = selection.getDiagonals(nGrams);

            if (diagonals != null) {
                for (Map.Entry<Node, Node> entry : diagonals.entrySet()) {
                    SWAlignment alignment = new SWAlignment(
                            searchedSequence,
                            dataSetSequence,
                            entry.getKey(),
                            entry.getValue(),
                            FineTable.getInstance()
                    );
                    top.add(alignment);
                }
            }
        }
    }
}
