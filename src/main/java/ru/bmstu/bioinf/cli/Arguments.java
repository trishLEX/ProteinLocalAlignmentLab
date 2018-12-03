package ru.bmstu.bioinf.cli;

import ru.bmstu.bioinf.sequence.Sequence;
import ru.bmstu.bioinf.sequence.SequenceReader;

public class Arguments {
    private float gap;
    private int ngramLength;
    private float diagScore;
    private int ngramCount;
    private int radius;
    private Sequence searchedSequence;
    private SequenceReader dataSequences;
    private boolean printAlignment;
    private int topSize;

    /**
     * Фабричный метод находится в {@link ArgumentParser::parse}
     */
    Arguments(float gap, int ngramLength, float diagScore, int ngramCount,
            int radius, String searchedFile, String dataFile, boolean printAlignment, int topSize)
    {
        this.gap = gap;
        this.ngramLength = ngramLength;
        this.diagScore = diagScore;
        this.ngramCount = ngramCount;
        this.radius = radius;
        this.searchedSequence = new SequenceReader(searchedFile).next();
        this.dataSequences = new SequenceReader(dataFile);
        this.printAlignment = printAlignment;
        this.topSize = topSize;
    }

    public float getGap() {
        return gap;
    }

    public int getNgramLength() {
        return ngramLength;
    }

    public float getDiagScore() {
        return diagScore;
    }

    public int getNgramCount() {
        return ngramCount;
    }

    public int getRadius() {
        return radius;
    }

    public Sequence getSearchedSequence() {
        return searchedSequence;
    }

    public SequenceReader getDataSequences() {
        return dataSequences;
    }

    public boolean isPrintAlignment() {
        return printAlignment;
    }

    public int getTopSize() {
        return topSize;
    }
}
