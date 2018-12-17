package ru.bmstu.bioinf.cli;

import ru.bmstu.bioinf.sequence.Sequence;
import ru.bmstu.bioinf.sequence.SequenceReader;

public class Arguments {
    private float gap;
    private float diagScore;
    private int biGramCount;
    private int radius;
    private Sequence searchedSequence;
    private SequenceReader dataSetSequenceReader;
    private boolean printAlignment;

    /**
     * Фабричный метод находится в {@link ArgumentParser#parse}
     */
    Arguments(float gap, float diagScore, int biGramCount,
            int radius, String searchedFile, String dataFile, boolean printAlignment)
    {
        this.gap = gap;
        this.diagScore = diagScore;
        this.biGramCount = biGramCount;
        this.radius = radius;
        this.searchedSequence = new SequenceReader(searchedFile).next();
        this.dataSetSequenceReader = new SequenceReader(dataFile);
        this.printAlignment = printAlignment;
    }

    public float getGap() {
        return gap;
    }

    public float getDiagScore() {
        return diagScore;
    }

    public int getBiGramCount() {
        return biGramCount;
    }

    public int getRadius() {
        return radius;
    }

    public Sequence getSearchedSequence() {
        return searchedSequence;
    }

    public SequenceReader getDataSetSequenceReader() {
        return dataSetSequenceReader;
    }

    public boolean isPrintAlignment() {
        return printAlignment;
    }
}
