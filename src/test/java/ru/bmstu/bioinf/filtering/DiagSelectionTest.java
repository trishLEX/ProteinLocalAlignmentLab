package ru.bmstu.bioinf.filtering;

import org.junit.Before;
import org.junit.Test;
import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.bigram.BiGramSelector;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class DiagSelectionTest {
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";

    @Before
    public void before() {
        FineTable.getInstance(-2.0f);
    }

    @Test
    public void getDiagonalsTest1() {
        Sequence searchedSeq = new Sequence(NAME_1, "ABCBCC");
        Sequence dataSetSeq = new Sequence(NAME_2, "CBCCB");

        DiagSelection diagSelection = new DiagSelection(-2,0, 0, 5);
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSetSeq);
        List<Set<Node>> nGrams = biGramSelector.getNewNGramsByHash();
        Map<Node, Node> words = diagSelection.getDiagonals(nGrams);
        assertEquals(1, words.size());
    }

    @Test
    public void getDiagonalsTest2() {
        Sequence searchedSeq = new Sequence(NAME_1, "CBCBCB");
        Sequence dataSetSeq = new Sequence(NAME_2, "CBCBB");

        DiagSelection diagSelection = new DiagSelection(-2, 0, 0, 5);
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSetSeq);
        List<Set<Node>> nGrams = biGramSelector.getNewNGramsByHash();
        Map<Node, Node> words = diagSelection.getDiagonals(nGrams);
        assertEquals(1, words.size());
    }

    @Test
    public void getDiagonalsTest3() {
        Sequence searchedSeq = new Sequence(NAME_1, "CBCBCB");
        Sequence dataSetSeq = new Sequence(NAME_2, "CBCBB");

        DiagSelection diagSelection = new DiagSelection(-2, 0, 0, 2);
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSetSeq);
        List<Set<Node>> nGrams = biGramSelector.getNewNGramsByHash();
        Map<Node, Node> words = diagSelection.getDiagonals(nGrams);
        assertEquals(1, words.size());
    }

    @Test
    public void getDiagonalsTest4() {
        Sequence searchedSeq = new Sequence(NAME_1, "CBCABCB");
        Sequence dataSetSeq = new Sequence(NAME_2, "CBCBB");

        DiagSelection diagSelection = new DiagSelection(-2, 0, 0, 2);
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSetSeq);
        List<Set<Node>> nGrams = biGramSelector.getNewNGramsByHash();
        Map<Node, Node> words = diagSelection.getDiagonals(nGrams);
        assertEquals(2, words.size());
    }
}
