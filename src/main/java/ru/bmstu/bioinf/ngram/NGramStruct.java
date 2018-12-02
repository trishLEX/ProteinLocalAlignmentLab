package ru.bmstu.bioinf.ngram;

/**
 * Класс-структура, хранящая n-грамму и координату её начала
 */
public class NGramStruct {
    private String ngram;
    private int pos;

    public NGramStruct(String ngram, int pos) {
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
