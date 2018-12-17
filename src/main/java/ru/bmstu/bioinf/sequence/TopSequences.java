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
    private int size;
    private List<SWAlignment> alignments;
    private boolean printAlignments;

    public TopSequences(int size, boolean printAlignments) {
        this.size = size;
        this.alignments = new ArrayList<>(size);
        this.printAlignments = printAlignments;
    }

    public synchronized void add(SWAlignment alignment) {
//        if (alignments.isEmpty()) {
//            alignments.add(alignment);
//        } else if (alignments.size() < size) {
//            for (int i = 0; i < alignments.size(); i++) {
//                if (alignments.get(i).getScore() <= alignment.getScore()) {
//                    alignments.add(i, alignment);
//                    return;
//                }
//            }
//
//            alignments.add(alignment);
//        } else {
//            for (int i = 0; i < alignments.size(); i++) {
//                if (alignments.get(i).getScore() <= alignment.getScore()) {
//                    alignments.add(i, alignment);
//                    alignments.remove(size);
//                    return;
//                }
//            }
//        }
        alignments.add(alignment);
    }

    @Override
    public String toString() {
//        alignments.sort(SWAlignment::compareTo);
        Collections.sort(alignments);
        StringBuilder result = new StringBuilder();
        int index = 0;
        for (SWAlignment alignment : alignments) {
            ++index;
            if (printAlignments) {
                result
                        .append(String.format("%6d | %s | %10.1f\n",
                                index,
                                alignment.getDataSetSequence().getId(),
                                alignment.getScore())
                        )
                        .append(alignment.toString())
                        .append("\n\n");
            } else {
//                result
//                        .append("Score: ")
//                        .append(alignment.getScore())
//                        .append(" ")
//                        .append(alignment.getDataSetSequence().getName())
//                        .append("\n\n");
                result
                        .append(String.format("%6d | %s | %10.1f\n",
                                index,
                                alignment.getDataSetSequence().getId(),
                                alignment.getScore())
                        );
            }
        }

    //        if (result.length() != 0) {
    //            result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1);
    //        }

        return result.toString();
    }
}
