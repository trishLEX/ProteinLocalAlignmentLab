package ru.bmstu.bioinf;

import ru.bmstu.bioinf.cli.ArgumentParser;
import ru.bmstu.bioinf.cli.Arguments;
import ru.bmstu.bioinf.filtering.DiagSelection;
import ru.bmstu.bioinf.filtering.Node;
import ru.bmstu.bioinf.sequence.Sequence;
import ru.bmstu.bioinf.sw.SWAlignment;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Arguments arguments = ArgumentParser.parse(args);

        FineTable.getInstance(arguments.getGap());

        TopSequences tops = new TopSequences(arguments.getTopSize(), arguments.isPrintAlignment());

        while (arguments.getDataSequences().hasNext()) {
            Sequence dataSetSequence = arguments.getDataSequences().next();

            DiagSelection selection = new DiagSelection(
                    arguments.getSearchedSequence(),
                    dataSetSequence,
                    arguments.getGap(),
                    arguments.getNgramLength(),
                    arguments.getDiagScore(),
                    arguments.getNgramCount(),
                    arguments.getRadius()
            );

            Map<Node, Node> diagonals = selection.getDiagonals();
            if (diagonals == null) {
                continue;
            }

            for (Map.Entry<Node, Node> entry : diagonals.entrySet()) {
                SWAlignment alignment = new SWAlignment(
                        arguments.getSearchedSequence(),
                        dataSetSequence,
                        entry.getKey(),
                        entry.getValue(),
                        FineTable.getInstance()
                );
                tops.add(alignment);
            }
        }

        System.out.println(tops);
    }
}
