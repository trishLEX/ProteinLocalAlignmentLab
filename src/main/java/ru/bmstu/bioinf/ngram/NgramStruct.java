package ru.bmstu.bioinf.ngram;

/**
 * Класс-структура, хранящая n-грамму и координату её начала
 */
public class NgramStruct {
    private String ngram;
    private int pos;

    public NgramStruct(String ngram, int pos) {
        this.ngram = ngram;
        this.pos = pos;
    }

    public String getNgram() {
        return ngram;
    }

    public int getPos() {
        return pos;
    }
}
