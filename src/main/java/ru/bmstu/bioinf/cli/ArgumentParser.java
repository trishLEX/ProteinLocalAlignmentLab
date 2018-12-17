package ru.bmstu.bioinf.cli;

import org.apache.commons.cli.*;

public class ArgumentParser {

    private static final int GAP_DEFAULT = -2;
    private static final float MIN_DIAG_SCORE_DEFAULT = 60.0f;
    private static final int MIN_N_GRAMS_COUNT_DEFAULT = 10;
    private static final int RADIUS_DEFAULT = 5;
    private static final int TOP_SIZE = 5;

    private ArgumentParser() {
        //utility class
    }

    public static Arguments parse(String[] args) {
        Options options = setupCommandLineOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            float gap = cmd.hasOption("g") ? Float.parseFloat(cmd.getOptionValue("g")) : GAP_DEFAULT;
            float diagScore = cmd.hasOption("D") ? Float.parseFloat(cmd.getOptionValue("D")) : MIN_DIAG_SCORE_DEFAULT;
            int nGramsCount = cmd.hasOption("n") ? Integer.parseInt(cmd.getOptionValue("n")) : MIN_N_GRAMS_COUNT_DEFAULT;
            int radius = cmd.hasOption("r") ? Integer.parseInt(cmd.getOptionValue("r")) : RADIUS_DEFAULT;
            String searchedFile = cmd.getOptionValue("s");
            String dataSetFile = cmd.getOptionValue("d");
            boolean printAlignment = cmd.hasOption("p");


            return new Arguments(gap, diagScore, nGramsCount, radius, searchedFile, dataSetFile, printAlignment);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("cli", options);
            throw new IllegalStateException("Can't parse arguments", e);
        }
    }

    private static Options setupCommandLineOptions() {
        Options options = new Options();

        Option gapOption = new Option("g", "gap", true, "gap error, default is -2");
        gapOption.setRequired(false);
        options.addOption(gapOption);

        Option diagScoreOption = new Option("D", "diag", true, "diagonal min score, default is 60");
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

        Option printAlignmentOption = new Option("p", "print-alignment", false, "print alignment");
        printAlignmentOption.setRequired(false);
        options.addOption(printAlignmentOption);

        return options;
    }
}
