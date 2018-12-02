package ru.bmstu.bioinf.ngram;

import ru.bmstu.bioinf.sequence.Sequence;

import java.util.*;

/**
 * Выделяет n-граммы
 */
public class NGramSelector {
    private Sequence dataSetSequence;
    private NGramIterator searchedIter;
    private NGramIterator dataSetIter;
    private int nGramLen; // Количество элементов в ngram

    public NGramSelector(Sequence searchedSequence, Sequence dataSetSequence, int nGramLen) {
        this.dataSetSequence = dataSetSequence;
        this.nGramLen = nGramLen;
        this.searchedIter = new NGramIterator(nGramLen, searchedSequence);
        this.dataSetIter = new NGramIterator(nGramLen, this.dataSetSequence);
    }

    /**
     * Выделяет набор ngram, используя итераторы
     * @return набор ngram для заданной searchedSequence
     */
    public List<NGram> getNewNGramsByIterator() {
         List<NGram> nGrams = new ArrayList<>();

         while (searchedIter.hasNext()) {
             NGramStruct current = searchedIter.next();
             List<NGram> result = getNewNGram(current);
             nGrams.addAll(result);
         }

         return nGrams;
    }

    /**
     * Сопоставляет n-грамму из searched и n-граммы из data
     * В случае успеха, возвращает список объектов класса NGram
     * Если searched или data последовательность закончена, возвращает пустой список
     * @return список ngram или пустой список
     */
    private List<NGram> getNewNGram(NGramStruct searchedNgramSeq) {
        dataSetIter.setPos(0); // Для новой ngramm начинаем поиск с нуля
        List<NGram> result = new ArrayList<>();

        while (dataSetIter.hasNext()) {
            NGramStruct dataNgramSeq = dataSetIter.next();

            if (searchedNgramSeq.getnGram().equals(dataNgramSeq.getnGram())) {
                result.add(new NGram(nGramLen, searchedNgramSeq.getnGram(), searchedNgramSeq.getPos(), dataNgramSeq.getPos()));
            }
        }

        return result;
    }

    /**
     * Выделяет набор {@link NGram} внутри dataSetSequence с помощью алгоритма КМП
     * @return набор {@link NGram} для заданной searchedSequence
     */
    public List<NGram> getNewNGrams() {
        List<NGram> ngrams = new ArrayList<>();

        while (searchedIter.hasNext()) {
            NGramStruct current = searchedIter.next();
            List<Integer> indices = KMPSubstr(current.getnGram());
            if (indices == null) {
                continue;
            }

            for (Integer index : indices) {
                ngrams.add(new NGram(nGramLen, current.getnGram(), current.getPos(), index));
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
        Arrays.fill(pi, 0);

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
     * @param searchedNGram последовательность, вхождение которой нужно найти
     * @return набор координат из dataSet, в которых входная последовательность существует
     */
    private List<Integer> KMPSubstr(String searchedNGram) {
        List<Integer> entries = new ArrayList<>();

        int[] pi = prefix(searchedNGram);

        int q = 0;

        for(int k = 0; k < dataSetSequence.size(); k++) {
            while (q > 0 && searchedNGram.charAt(q) != dataSetSequence.get(k)) {
                q = pi[q - 1];
            }
            if (searchedNGram.charAt(q) == dataSetSequence.get(k)) {
                q++;
            }
            if (q == searchedNGram.length()) {
                entries.add(k - searchedNGram.length() + 1);
                q = 0;
            }
        }

        return entries;
    }
}
