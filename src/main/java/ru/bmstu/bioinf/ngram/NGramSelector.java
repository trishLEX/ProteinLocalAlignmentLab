package ru.bmstu.bioinf.ngram;

import ru.bmstu.bioinf.sequence.Sequence;

import java.util.Arrays;
import java.util.List;

/**
 * Выделяет n-граммы
 */
public class NGramSelector {
    public NGramSelector(Sequence searchedSequence, Sequence dataSetSequence) {
        //TODO
    }

    /**
     * @return координаты n-грамм, int[0] - в первой последовательности, int[1] - в последовательности из базы
     */
    public List<NGram> getNewNGrams() {
        //throw new UnsupportedOperationException();
        return Arrays.asList(
                new NGram(-2, "AB", 0, 0),
                new NGram(-2, "CD", 0, 2),
                new NGram(-2, "EF", 0, 6),
                new NGram(-2, "CD", 2, 4),
                new NGram(-2, "GD", 3, 3),
                new NGram(-2, "LK", 5, 5)
        );

    }
}
