package ru.bmstu.bioinf;

import ru.bmstu.bioinf.cli.ArgumentParser;
import ru.bmstu.bioinf.cli.Arguments;
import ru.bmstu.bioinf.sequence.Sequence;
import ru.bmstu.bioinf.sequence.SequenceReader;
import ru.bmstu.bioinf.sequence.TopSequences;
import ru.bmstu.bioinf.threading.LocalAligner;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Arguments arguments = ArgumentParser.parse(args);

        FineTable.getInstance(arguments.getGap());

        TopSequences tops = new TopSequences(arguments.getTopSize(), arguments.isPrintAlignment());

        SequenceReader dataSetSequenceReader = arguments.getDataSetSequenceReader();

        long startTime = System.currentTimeMillis();

        Set<Future> threads = new HashSet<>();

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int maxThreads = Runtime.getRuntime().availableProcessors();

        while (dataSetSequenceReader.hasNext()) {
            if(threads.size() == maxThreads) {
                Iterator<Future> it = threads.iterator();
                while(it.hasNext()) {
                    Future t = it.next();
                    if(t.isDone()){
                        t.get();
                        it.remove();
                    }
                }
            } else {
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

                Future future = executorService.submit(aligner);
                threads.add(future);
            }
        }

        for (Future thread : threads) {
            thread.get();
        }

        executorService.shutdown();

        long endTime = System.currentTimeMillis();

        System.out.println(String.format("%.2fs", (endTime - startTime) / 1000.0f));
        System.out.println(tops);
    }
}
