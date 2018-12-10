package ru.bmstu.bioinf;

import ru.bmstu.bioinf.cli.ArgumentParser;
import ru.bmstu.bioinf.cli.Arguments;
import ru.bmstu.bioinf.sequence.Sequence;
import ru.bmstu.bioinf.sequence.SequenceReader;
import ru.bmstu.bioinf.sequence.TopSequences;
import ru.bmstu.bioinf.threading.LocalAligner;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Arguments arguments = ArgumentParser.parse(args);

        FineTable.getInstance(arguments.getGap());

        TopSequences tops = new TopSequences(arguments.getTopSize(), arguments.isPrintAlignment());

        SequenceReader dataSetSequenceReader = arguments.getDataSetSequenceReader();

        long startTime = System.currentTimeMillis();

        List<Thread> threads = new ArrayList<>();

        while (dataSetSequenceReader.hasNext()) {
            Sequence dataSetSequence = dataSetSequenceReader.next();

            LocalAligner aligner = new LocalAligner(
                    tops,
                    arguments.getSearchedSequence(),
                    dataSetSequence,
                    arguments.getGap(),
                    arguments.getDiagScore(),
                    arguments.getBiGramCount(),
                    arguments.getRadius()
            );

            Thread thread = new Thread(aligner);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long endTime = System.currentTimeMillis();

        System.out.println(String.format("%.2fs", (endTime - startTime) / 1000.0f));
        System.out.println(tops);
    }
}
