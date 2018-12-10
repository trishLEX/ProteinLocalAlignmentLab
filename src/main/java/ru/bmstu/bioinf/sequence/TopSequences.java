package ru.bmstu.bioinf.sequence;

import ru.bmstu.bioinf.sw.SWAlignment;

import java.util.TreeSet;

/**
 * Хранит топ size последовательностей, самая похожая под индексом 0 в {@link TopSequences#alignments}
 */
public class TopSequences {
    private int size;
    private TreeSet<SWAlignment> alignments;
    private boolean printAlignments;

    public TopSequences(int size, boolean printAlignments) {
        this.size = size;
        this.alignments = new TreeSet<>();
        this.printAlignments = printAlignments;
    }

    public void add(SWAlignment alignment) {
        if (alignments.size() < size) {
            alignments.add(alignment);
        } else {
            if (alignments.first().getScore() < alignment.getScore()) {
                alignments.remove(alignments.first());
                alignments.add(alignment);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (SWAlignment alignment : alignments) {
            if (printAlignments) {
                result
                        .append(alignment.getDataSetSequence().getName())
                        .append("\n")
                        .append(alignment.toString())
                        .append("\n\n");
            } else {
                result
                        .append("Score: ")
                        .append(alignment.getScore())
                        .append(" ")
                        .append(alignment.getDataSetSequence().getName())
                        .append("\n\n");
            }
        }

        if (result.length() != 0) {
            result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }
}
