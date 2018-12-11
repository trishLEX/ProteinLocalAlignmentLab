package ru.bmstu.bioinf.sequence;

import ru.bmstu.bioinf.sw.SWAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Хранит топ size последовательностей, самая похожая под индексом 0 в {@link TopSequences#alignments}
 */
public class TopSequences {
    private int size;
    private List<SWAlignment> alignments;
    private boolean printAlignments;

    public TopSequences(int size, boolean printAlignments) {
        this.size = size;
        this.alignments = new ArrayList<>(size);
        this.printAlignments = printAlignments;
    }

    public synchronized void add(SWAlignment alignment) {
        if (alignments.isEmpty()) {
            alignments.add(alignment);
        } else if (alignments.size() < size) {
            for (int i = 0; i < alignments.size(); i++) {
                if (alignments.get(i).getScore() <= alignment.getScore()) {
                    alignments.add(i, alignment);
                    return;
                }
            }

            alignments.add(alignment);
        } else {
            for (int i = 0; i < alignments.size(); i++) {
                if (alignments.get(i).getScore() <= alignment.getScore()) {
                    alignments.add(i, alignment);
                    alignments.remove(size);
                    return;
                }
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
