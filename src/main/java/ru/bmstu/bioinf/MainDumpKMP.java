package ru.bmstu.bioinf;

import ru.bmstu.bioinf.sequence.Sequence;
import ru.bmstu.bioinf.sequence.SequenceReader;
import ru.bmstu.bioinf.serialization.SerializableSequence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainDumpKMP {

    public static final String DUMP_BIGRAMM = "bigramm.txt";
    public static final int SIZE = 558803;

    public static void main(String[] args) {
        SequenceReader sequenceReader = new SequenceReader("/Users/Thomas/dev/bmstu/bio/ProteinLocalAlignmentLab/src/uniprot_sprot.fasta");
        List<SerializableSequence> data = new ArrayList<>(SIZE);
        double k = 0;
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
            SerializableSequence serializableSequence = new SerializableSequence(sequence, bigramsMap);
            data.add(serializableSequence);
            k++;
            if (Double.compare(k % 100, 0) == 0) {
                System.err.println(k / SIZE + "%");
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(DUMP_BIGRAMM);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            throw new IllegalArgumentException("err: ", ioe);
        }

    }


    private static List<String> ngrams(int n, String str) {
        List<String> ngrams = new ArrayList<String>();
        for (int i = 0; i < str.length() - n + 1; i++)
            ngrams.add(str.substring(i, i + n));
        return ngrams;
    }
}
