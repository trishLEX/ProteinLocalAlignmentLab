package ru.bmstu.bioinf.filtering;

import org.junit.Before;
import org.junit.Test;
import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.ngram.NGram;
import ru.bmstu.bioinf.ngram.NGramSelector;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiagSelectionTest {
    private static final int LENGTH = 2;
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";

    @Before
    public void before() {
        FineTable.getInstance(-2);
    }

    @Test
    public void getDiagonalsTest1() {
        Sequence searchedSeq = new Sequence(NAME_1, "ABCBCC");
        Sequence dataSetSeq = new Sequence(NAME_2, "CBCCB");

        DiagSelection diagSelection = new DiagSelection(searchedSeq, dataSetSeq, -2, LENGTH, 0, 0, 5);
        Map<Node, Node> words = diagSelection.getDiagonals();
        assertEquals(1, words.size());
    }

    @Test
    public void getDiagonalsTest2() {
        Sequence searchedSeq = new Sequence(NAME_1, "CBCBCB");
        Sequence dataSetSeq = new Sequence(NAME_2, "CBCBB");

        DiagSelection diagSelection = new DiagSelection(searchedSeq, dataSetSeq, -2, LENGTH, 0, 0, 5);
        Map<Node, Node> words = diagSelection.getDiagonals();
        assertEquals(1, words.size());
    }

    @Test
    public void getDiagonalsTest3() {
        Sequence searchedSeq = new Sequence(NAME_1, "CBCBCB");
        Sequence dataSetSeq = new Sequence(NAME_2, "CBCBB");

        DiagSelection diagSelection = new DiagSelection(searchedSeq, dataSetSeq, -2, LENGTH, 0, 0, 2);
        Map<Node, Node> words = diagSelection.getDiagonals();
        assertEquals(1, words.size());
    }

    @Test
    public void getDiagonalsTest4() {
        Sequence searchedSeq = new Sequence(NAME_1, "CBCABCB");
        Sequence dataSetSeq = new Sequence(NAME_2, "CBCBB");

        DiagSelection diagSelection = new DiagSelection(searchedSeq, dataSetSeq, -2, LENGTH, 0, 0, 2);
        Map<Node, Node> words = diagSelection.getDiagonals();
        assertEquals(2, words.size());
    }
}
