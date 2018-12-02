package ru.bmstu.bioinf.ngram;

import ru.bmstu.bioinf.sequence.Sequence;

import java.util.Iterator;

/**
 * Итератор, позволяющий получить структуру, содержащую n-грамму
 * и её координату в общей строке
 */
class NgramIterator implements Iterator<NgramStruct> {
    private final Sequence str;
    private final int n;
    int pos = 0;

    public NgramIterator(int n, Sequence str) {
        this.n = n;
        this.str = str;
    }
    public boolean hasNext() {
        return pos < str.size() - n + 1;
    }
    public NgramStruct next() {
        int posTo = pos + n;
        String ngramString = str.subSequence(pos, posTo);
        NgramStruct result = new NgramStruct(ngramString, pos);
        pos++;
        return result;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}

