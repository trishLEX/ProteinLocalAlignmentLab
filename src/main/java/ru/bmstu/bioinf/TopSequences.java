package ru.bmstu.bioinf;

import ru.bmstu.bioinf.sw.SWAlignment;

import java.util.ArrayList;


public class TopSequences {

    private int size;
    //Хранит топ size последовательностей, самая похожая под индексом 0
    private ArrayList<SWAlignment> alignments;
    private boolean printAlignments;

    TopSequences(int size, boolean printAlignments) {
        this.size = size;
        this.alignments = new ArrayList<>();
        this.printAlignments = printAlignments;
    }

    public void add(SWAlignment alignment) {
        if (alignments.size() == 0) {
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

        for (int i = 0; i < alignments.size(); i++) {

            if (printAlignments) {
                result
                        .append(alignments.get(i).toString())
                        .append("\n\n");
            } else {
                result
                        .append("Score: ")
                        .append(alignments.get(i).getScore())
                        .append(" ")
                        .append(alignments.get(i).getFromBase().getName())
                        .append("\n\n");
            }


        }

        return result.toString();
    }
}
