package ru.bmstu.bioinf.ngram;

import ru.bmstu.bioinf.sequence.Sequence;

import java.util.*;

/**
 * Выделяет n-граммы
 */
public class NGramSelector {
    private Sequence searchedSequence;
    private Sequence dataSetSequence;
    private NgramIterator searchedIter;
    private NgramIterator dataSetIter;
    private Integer ngramLen = 2; // Количество элементов в ngram

    public NGramSelector(Sequence searchedSequence, Sequence dataSetSequence) {
        this.searchedSequence = searchedSequence;
        this.dataSetSequence = dataSetSequence;
        this.searchedIter = new NgramIterator(ngramLen, this.searchedSequence);
        this.dataSetIter = new NgramIterator(ngramLen, this.dataSetSequence);
    }

    /**
     * Выделяет набор ngram, используя итераторы
     * @return набор ngram для заданной searchedSequence
     */
    public List<NGram> getNewNGramsByIterator() {
         List<NGram> ngrams = new ArrayList<>();

         while (searchedIter.hasNext()) {
             NgramStruct current = searchedIter.next();
             NGram result = getNewNGram(current);
             if (result != null)
                 ngrams.add(result);
         }

         return ngrams;
    }

    /**
     * Сопоставляет n-грамму из searched и n-грамму из data
     * В случае успеха, возвращает новый объект класса NGram
     * Если searched или data последовательность закончена, возвращает null
     * @return новую ngram или null
     */
    private NGram getNewNGram(NgramStruct searchedNgramSeq) {
        dataSetIter.setPos(0); // Для новой ngramm начинаем поиск с нуля

        while (dataSetIter.hasNext()) {

            NgramStruct dataNgramSeq = dataSetIter.next();

            if (searchedNgramSeq.getNgram().equals(dataNgramSeq.getNgram()))
                return new NGram(ngramLen, searchedNgramSeq.getNgram(), searchedNgramSeq.getPos(), dataNgramSeq.getPos());

        }

        return null;
    }

    /**
     * Выделяет набор ngram внутри dataSetSequence с помощью алгоритма КМП
     * @return набор ngram для заданной searchedSequence
     */
    public List<NGram> getNewNGrams() {
        List<NGram> ngrams = new ArrayList<>();

        while (searchedIter.hasNext()) {
            NgramStruct current = searchedIter.next();
            List<Integer> indexes = KMPSubstr(current.getNgram());
            if (indexes == null)
                continue;

            for (int i = 0; i < indexes.size(); i++) {
                ngrams.add(new NGram(ngramLen, current.getNgram(), current.getPos(), indexes.get(i)));
            }
        }

        return ngrams;
    }

    /**
     * Вспомогательный алгоритм для КМП
     * Выделяет префикс
     */
    private int[] prefix(String s) {
        int n = s.length();
        int[] pi = new int[n];
        Arrays.fill(pi, 1);

        int j;
        for (int i = 1; i < n; i++) {
            j = pi[i - 1];
            while(j > 0 && s.charAt(i) != s.charAt(j)) {
                j = pi[j - 1];
            }
            if (s.charAt(i) == s.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }

        return pi;
    }

    /**
     * Алгоритм Кнута-Морриса-Пратта
     * @param searchedNgram последовательность, вхождение которой нужно найти
     * @return набор координат из dataSet, в которых входная последовательность существует
     */
    List<Integer> KMPSubstr(String searchedNgram) {
        List<Integer> entries = new ArrayList<>();

        int[] pi = prefix(searchedNgram);

        int q = 0;

        for(int k = 0; k < dataSetSequence.size(); k++) {
            while (q > 0 && searchedNgram.charAt(q) != dataSetSequence.get(k)) {
                q = pi[q - 1];
            }
            if (searchedNgram.charAt(q) == dataSetSequence.get(k)) {
                q++;
            }
            if (q == searchedNgram.length()) {
                entries.add(k - searchedNgram.length() + 1);
            }
        }

        return entries;
    }
}
