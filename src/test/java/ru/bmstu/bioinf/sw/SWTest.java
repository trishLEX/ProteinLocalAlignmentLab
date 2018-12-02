package ru.bmstu.bioinf.sw;

import org.junit.Before;
import org.junit.Test;
import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.filtering.DiagSelection;
import ru.bmstu.bioinf.filtering.Node;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.Map;

public class SWTest {

    private static final int LENGTH = 2;
    private static final String NAME_1 = "AATCG";
    private static final String NAME_2 = "AACG";

    @Before
    public void before() {
        FineTable.getInstance(-2.0f);
    }

    @Test
    public void test1() {
        Sequence searchedSeq = new Sequence(NAME_1, "ABCBCC");
        Sequence dataSetSeq = new Sequence(NAME_2, "CBCCB");

        DiagSelection diagSelection = new DiagSelection(searchedSeq, dataSetSeq, -2, LENGTH, 0, 0, 5);
        Map<Node, Node> words = diagSelection.getDiagonals();

        for(Map.Entry<Node, Node> e : words.entrySet()) {
            SWAlignment a = new SWAlignment(searchedSeq, dataSetSeq,
                    e.getKey(), e.getValue(), FineTable.getInstance(-2.0f));

            System.out.println(a);
        }
    }

    @Test
    public void test2() {
        Sequence searchedSeq = new Sequence(NAME_1, "GGAGTGAGGGGAGCAGTTGGCTGAAGATGGTCCCCGCCGAGGGACCGGTGGGCGACGGCG");
        Sequence dataSetSeq = new Sequence(NAME_2, "CGCATGCGGAGTGAGGGGAGCAGTTGGGAACAGATGGTCCCCGCCGAGGGACCGGTGGGC");

        DiagSelection diagSelection = new DiagSelection(searchedSeq, dataSetSeq, -2, LENGTH, 0, 0, 5);
        Map<Node, Node> words = diagSelection.getDiagonals();

        for(Map.Entry<Node, Node> e : words.entrySet()) {
            SWAlignment a = new SWAlignment(searchedSeq, dataSetSeq,
                    e.getKey(), e.getValue(), FineTable.getInstance(-2.0f));

            System.out.println(a);
        }

    }
}
