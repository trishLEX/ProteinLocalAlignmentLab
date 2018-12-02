package ru.bmstu.bioinf.ngram;

/**
 * Класс-структура, хранящая n-грамму и координату её начала
 */
public class NGramStruct {
    private String nGram;
    private int pos;

    public NGramStruct(String nGram, int pos) {
        this.nGram = nGram;
        this.pos = pos;
    }

    public String getnGram() {
        return nGram;
    }

    public int getPos() {
        return pos;
    }
}
