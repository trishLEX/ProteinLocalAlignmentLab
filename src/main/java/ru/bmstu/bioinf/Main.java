package ru.bmstu.bioinf;

import ru.bmstu.bioinf.cli.ArgumentParser;
import ru.bmstu.bioinf.cli.Arguments;
import ru.bmstu.bioinf.sequence.Sequence;
import ru.bmstu.bioinf.sequence.SequenceReader;
import ru.bmstu.bioinf.sequence.TopSequences;
import ru.bmstu.bioinf.threading.LocalAligner;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    private static final int SIZE = 50;
    private static final Void PLACEHOLDER = null;

    public static void main(String[] args) throws InterruptedException {
        Arguments arguments = ArgumentParser.parse(args);

        FineTable.getInstance(arguments.getGap());

        TopSequences tops = new TopSequences(arguments.isPrintAlignment());

        SequenceReader dataSetSequenceReader = arguments.getDataSetSequenceReader();

        long startTime = System.currentTimeMillis();

        int counter = 0;
        int maxThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);

        CompletionService<Void> completionService = new ExecutorCompletionService<>(executorService);

        arguments.getSearchedSequence().getBiGrams();

        while (dataSetSequenceReader.hasNext()) {
            if(counter == maxThreads) {
                completionService.take();
                --counter;
            } else {
                List<Sequence> dataSetSequences = dataSetSequenceReader.next(SIZE);

                LocalAligner aligner = new LocalAligner(
                        tops,
                        arguments.getSearchedSequence(),
                        dataSetSequences,
                        arguments.getGap(),
                        arguments.getDiagScore(),
                        arguments.getBiGramCount(),
                        arguments.getRadius()
                );

                completionService.submit(aligner, PLACEHOLDER);
                ++counter;
            }
        }

        while(counter > 0) {
            completionService.take();
            --counter;
        }

        executorService.shutdown();

        long endTime = System.currentTimeMillis();

        System.out.println(String.format("%.2fs", (endTime - startTime) / 1000.0f));
        System.out.println(tops);
    }
}
