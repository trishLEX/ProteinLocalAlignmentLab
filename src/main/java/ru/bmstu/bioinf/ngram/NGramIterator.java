package ru.bmstu.bioinf.ngram;

import ru.bmstu.bioinf.sequence.Sequence;

import java.util.Iterator;

/**
 * Итератор, позволяющий получить структуру, содержащую n-грамму
 * и её координату в общей строке
 */
class NGramIterator implements Iterator<NGramStruct> {
    private final Sequence str;
    private final int n;
    private int pos = 0;

    public NGramIterator(int n, Sequence str) {
        this.n = n;
        this.str = str;
    }
    public boolean hasNext() {
        return pos < str.size() - n + 1;
    }

    public NGramStruct next() {
        int posTo = pos + n;
        String nGramString = str.subSequence(pos, posTo);
        NGramStruct result = new NGramStruct(nGramString, pos);
        pos++;
        return result;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}

