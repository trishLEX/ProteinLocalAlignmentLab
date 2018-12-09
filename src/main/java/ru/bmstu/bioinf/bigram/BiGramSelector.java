package ru.bmstu.bioinf.bigram;

import ru.bmstu.bioinf.filtering.Node;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.*;

/**
 * Выделяет биграммы
 */
public class BiGramSelector {
    private Sequence searchedSequence;
    private Sequence dataSetSequence;
    private BiGramIterator searchedIter;
    private BiGramIterator dataSetIter;

    public BiGramSelector(Sequence searchedSequence, Sequence dataSetSequence) {
        this.searchedSequence = searchedSequence;
        this.dataSetSequence = dataSetSequence;
        this.searchedIter = new BiGramIterator(searchedSequence);
        this.dataSetIter = new BiGramIterator(this.dataSetSequence);
    }

    /**
     * Выделяет набор ngram, используя итераторы
     * @return набор ngram для заданной searchedSequence
     */
    public List<BiGram> getNewNGramsByIterator() {
         List<BiGram> biGrams = new ArrayList<>();

         while (searchedIter.hasNext()) {
             BiGramIterator.NGramStruct current = searchedIter.next();
             List<BiGram> result = getNewNGram(current);
             biGrams.addAll(result);
         }

         return biGrams;
    }

    public List<Set<Node>> getNewNGramsByHash() {
        Map<String, List<Integer>> searchedBiGrams = searchedSequence.getBiGrams();
        Map<String, List<Integer>> dataSetBiGrams = dataSetSequence.getBiGrams();

        int size = searchedSequence.length() + dataSetSequence.length() - 1;
        List<Set<Node>> diags = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            diags.add(Collections.emptySet());
        }

        int maxDiagSize = Math.min(searchedSequence.length(), dataSetSequence.length());

        for (Map.Entry<String, List<Integer>> entry : searchedBiGrams.entrySet()) {
            if (dataSetBiGrams.containsKey(entry.getKey())) {
                List<Integer> searchedIndices = entry.getValue();
                List<Integer> dataSetIndices = dataSetBiGrams.get(entry.getKey());
                for (Integer searchedIndex : searchedIndices) {
                    for (Integer dataSetIndex : dataSetIndices) {
                        int diagIndex = searchedIndex - dataSetIndex + dataSetSequence.length() - 1;
                        Set<Node> diag = diags.get(diagIndex);

                        if (diag.isEmpty()) {
                            diag = new HashSet<>(maxDiagSize);
                            diags.set(diagIndex, diag);
                        }

                        Node first = new Node(searchedIndex, dataSetIndex, entry.getKey().charAt(0), entry.getKey().charAt(0));
                        Node last = new Node(searchedIndex + 1, dataSetIndex + 1, entry.getKey().charAt(1), entry.getKey().charAt(1));

                        diag.add(first);
                        diag.add(last);
                    }
                }
            }
        }

        return diags;
    }

    /**
     * Сопоставляет n-грамму из searched и n-граммы из data
     * В случае успеха, возвращает список объектов класса BiGram
     * Если searched или data последовательность закончена, возвращает пустой список
     * @return список ngram или пустой список
     */
    private List<BiGram> getNewNGram(BiGramIterator.NGramStruct searchedNGramSeq) {
        dataSetIter.setPos(0); // Для новой ngramm начинаем поиск с нуля
        List<BiGram> result = new ArrayList<>();

        while (dataSetIter.hasNext()) {
            BiGramIterator.NGramStruct dataNgramSeq = dataSetIter.next();

            if (searchedNGramSeq.getnGram().equals(dataNgramSeq.getnGram())) {
                result.add(new BiGram(searchedNGramSeq.getnGram(), searchedNGramSeq.getPos(), dataNgramSeq.getPos()));
            }
        }

        return result;
    }

    /**
     * Выделяет набор {@link BiGram} внутри dataSetSequence с помощью алгоритма КМП
     * @return набор {@link BiGram} для заданной searchedSequence
     */
    public List<BiGram> getNewNGrams() {
        List<BiGram> ngrams = new ArrayList<>();

        while (searchedIter.hasNext()) {
            BiGramIterator.NGramStruct current = searchedIter.next();
            List<Integer> indices = KMPSubstr(current.getnGram());
            if (indices == null) {
                continue;
            }

            for (Integer index : indices) {
                ngrams.add(new BiGram(current.getnGram(), current.getPos(), index));
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

        for(int k = 0; k < dataSetSequence.length(); k++) {
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
