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
    public static void main(String[] args) throws InterruptedException {
        Arguments arguments = ArgumentParser.parse(args);

        FineTable.getInstance(arguments.getGap());

        TopSequences tops = new TopSequences(arguments.getTopSize(), arguments.isPrintAlignment());

        SequenceReader dataSetSequenceReader = arguments.getDataSetSequenceReader();

        long startTime = System.currentTimeMillis();

        int counter = 0;
        ExecutorService executorService =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletionService<Void> completionService = new ExecutorCompletionService<>(executorService);
        int maxThreads = Runtime.getRuntime().availableProcessors();
        while (dataSetSequenceReader.hasNext()) {
            if(counter == maxThreads) {
                completionService.take();
                --counter;
            } else {
                Sequence[] dataSetSequences = dataSetSequenceReader.next(50);

                LocalAligner aligner = new LocalAligner(
                        tops,
                        arguments.getSearchedSequence(),
                        dataSetSequences,
                        arguments.getGap(),
                        arguments.getDiagScore(),
                        arguments.getBiGramCount(),
                        arguments.getRadius()
                );

//                aligner.run();

                completionService.submit(aligner, null);
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
