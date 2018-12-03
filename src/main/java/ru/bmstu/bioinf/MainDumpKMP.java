package ru.bmstu.bioinf;

import ru.bmstu.bioinf.ngram.NGram;
import ru.bmstu.bioinf.ngram.NGramSelector;
import ru.bmstu.bioinf.sequence.Sequence;
import ru.bmstu.bioinf.sequence.SequenceReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainDumpKMP {
    public static void main(String[] args) {
        SequenceReader sequenceReader = new SequenceReader("/Users/Thomas/dev/bmstu/bio/ProteinLocalAlignmentLab/src/uniprot_sprot.fasta");
        List<Map<String, List<Integer>>> data = new ArrayList<>();
        while(sequenceReader.hasNext()) {
            Map<String, List<Integer>> bigramsMap = new HashMap<>();
            Sequence sequence = sequenceReader.next();
            List<String> bigrams = ngrams(2, sequence.getSequence());
            for (int i = 0; i < bigrams.size(); i++) {
                List<Integer> list;
                if (bigramsMap.containsKey(bigrams.get(i))) {
                    list = bigramsMap.get(bigrams.get(i));
                    list.add(i);
                } else {
                    list = new ArrayList<>();
                    list.add(i);
                    bigramsMap.put(bigrams.get(i), list);
                }

            }
            data.add(bigramsMap);
        }


    }


    public static List<String> ngrams(int n, String str) {
        List<String> ngrams = new ArrayList<String>();
        for (int i = 0; i < str.length() - n + 1; i++)
            ngrams.add(str.substring(i, i + n));
        return ngrams;
    }
}
