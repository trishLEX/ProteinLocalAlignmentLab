package ru.bmstu.bioinf.bigram;

public class BiGram {
    private final String string;
    private final int searchedSeqCoordinate;
    private final int dataSetSeqCoordinate;

    public BiGram(String string, int searchedSeqCoordinate, int dataSetSeqCoordinate) {
        this.string = string;
        this.searchedSeqCoordinate = searchedSeqCoordinate;
        this.dataSetSeqCoordinate = dataSetSeqCoordinate;
    }

    public String getString() {
        return string;
    }

    public int getSearchedSeqCoordinate() {
        return searchedSeqCoordinate;
    }

    public int getDataSetSeqCoordinate() {
        return dataSetSeqCoordinate;
    }
}
