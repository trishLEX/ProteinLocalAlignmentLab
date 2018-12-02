package ru.bmstu.bioinf;

import org.apache.commons.cli.*;
import ru.bmstu.bioinf.filtering.DiagSelection;
import ru.bmstu.bioinf.sequence.Sequence;
import ru.bmstu.bioinf.sequence.SequenceReader;

public class Main {
    private static final int GAP_DEFAULT = -2;
    private static final int N_GRAM_LENGTH_DEFAULT = 2;
    private static final int MIN_DIAG_SCORE_DEFAULT = 60;
    private static final int MIN_N_GRAMS_COUNT_DEFAULT = 10;
    private static final int RADIUS_DEFAULT = 5;

    public static void main(String[] args) {
        Options options = new Options();

        Option gapOption = new Option("g", "gap", true, "gap error, default is -2");
        gapOption.setRequired(false);
        options.addOption(gapOption);

        Option nGramLenOption = new Option("l", "len", true, "nGram length, default is 2");
        nGramLenOption.setRequired(false);
        options.addOption(nGramLenOption);

        Option diagScoreOption = new Option("d", "diag", true, "diagonal min score, default is 60");
        diagScoreOption.setRequired(false);
        options.addOption(diagScoreOption);

        Option minNGramsCountOption = new Option("n", "count", true, "min nGrams count, default is 10");
        minNGramsCountOption.setRequired(false);
        options.addOption(minNGramsCountOption);

        Option radiusOption = new Option("r", "radius", true, "radius to connect nodes, default is 5");
        radiusOption.setRequired(false);
        options.addOption(radiusOption);

        Option searchedSeqFileOption = new Option("s", "searched", true, "path to file with searched sequence");
        searchedSeqFileOption.setRequired(true);
        options.addOption(searchedSeqFileOption);

        Option dataSetFileOption = new Option("d", "dataset", true, "path to file with sequence data set");
        dataSetFileOption.setRequired(true);
        options.addOption(dataSetFileOption);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            float gap = cmd.hasOption("g") ? Float.parseFloat(cmd.getOptionValue("g")) : GAP_DEFAULT;
            int nGramLen = cmd.hasOption("l") ? Integer.parseInt(cmd.getOptionValue("l")) : N_GRAM_LENGTH_DEFAULT;
            float diagScore = cmd.hasOption("d") ? Float.parseFloat(cmd.getOptionValue("d")) : MIN_DIAG_SCORE_DEFAULT;
            int nGramsCount = cmd.hasOption("n") ? Integer.parseInt(cmd.getOptionValue("n")) : MIN_N_GRAMS_COUNT_DEFAULT;
            int radius = cmd.hasOption("r") ? Integer.parseInt(cmd.getOptionValue("r")) : RADIUS_DEFAULT;
            String searchedFile = cmd.getOptionValue("s");
            String dataSetFile = cmd.getOptionValue("d");

            SequenceReader searchedReader = new SequenceReader(searchedFile);
            Sequence searchedSequence = searchedReader.next();
            SequenceReader dataSetReader = new SequenceReader(dataSetFile);

            while (dataSetReader.hasNext()) {
                Sequence dataSetSequence = dataSetReader.next();
                DiagSelection selection = new DiagSelection(searchedSequence, dataSetSequence, gap, nGramLen, diagScore, nGramsCount, radius);
                //TODO
            }

        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("arguments", options);
            throw new IllegalStateException("Can't parse arguments", e);
        }
    }
}
