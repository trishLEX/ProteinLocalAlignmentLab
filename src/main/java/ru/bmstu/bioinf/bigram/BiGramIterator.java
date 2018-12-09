package ru.bmstu.bioinf.bigram;

import ru.bmstu.bioinf.sequence.Sequence;

import java.util.Iterator;

/**
 * Итератор, позволяющий получить структуру, содержащую n-грамму
 * и её координату в общей строке
 */
public class BiGramIterator implements Iterator<BiGramIterator.NGramStruct> {
    private final Sequence str;
    private int pos = 0;

    public BiGramIterator(Sequence str) {
        this.str = str;
    }

    public boolean hasNext() {
        return pos < str.length() - 1;
    }

    public NGramStruct next() {
        int posTo = pos + 2;
        String nGramString = str.substring(pos, posTo);
        NGramStruct result = new NGramStruct(nGramString, pos);
        pos++;
        return result;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * Класс-структура, хранящая n-грамму и координату её начала
     */
    class NGramStruct {
        private String nGram;
        private int pos;

        NGramStruct(String nGram, int pos) {
            this.nGram = nGram;
            this.pos = pos;
        }

        String getnGram() {
            return nGram;
        }

        int getPos() {
            return pos;
        }
    }
}

