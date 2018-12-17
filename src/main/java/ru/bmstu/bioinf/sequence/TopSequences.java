package ru.bmstu.bioinf.sequence;

import ru.bmstu.bioinf.sw.SWAlignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Хранит топ size последовательностей, самая похожая под индексом 0 в {@link TopSequences#alignments}
 */
public class TopSequences {
    private List<SWAlignment> alignments;
    private boolean printAlignments;

    public TopSequences(boolean printAlignments) {
        this.alignments = new ArrayList<>();
        this.printAlignments = printAlignments;
    }

    public synchronized void add(SWAlignment alignment) {
        alignments.add(alignment);
    }

    @Override
    public String toString() {
        Collections.sort(alignments);
        StringBuilder result = new StringBuilder();
        int index = 0;
        for (SWAlignment alignment : alignments) {
            ++index;
            if (printAlignments) {
                result
                        .append(String.format("%d %s %.0f\n",
                                index,
                                alignment.getDataSetSequence().getId(),
                                alignment.getScore())
                        )
                        .append(alignment.toString())
                        .append("\n\n");
            } else {
                result
                        .append(String.format("%d %s %.0f\n",
                                index,
                                alignment.getDataSetSequence().getId(),
                                alignment.getScore())
                        );
            }
        }

        return result.toString();
    }
}
