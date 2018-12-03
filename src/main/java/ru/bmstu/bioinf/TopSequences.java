package ru.bmstu.bioinf;

import ru.bmstu.bioinf.sw.SWAlignment;

import java.util.ArrayList;
import java.util.List;


public class TopSequences {

    private int size;
    //Хранит топ size последовательностей, самая похожая под индексом 0
    private List<SWAlignment> alignments;
    private boolean printAlignments;

    public TopSequences(int size, boolean printAlignments) {
        this.size = size;
        this.alignments = new ArrayList<>();
        this.printAlignments = printAlignments;
    }

    public void add(SWAlignment alignment) {
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
                        .append(alignment.toString())
                        .append("\n\n");
            } else {
                result
                        .append("Score: ")
                        .append(alignment.getScore())
                        .append(" ")
                        .append(alignment.getFromBase().getName())
                        .append("\n\n");
            }


        }

        return result.toString();
    }
}
