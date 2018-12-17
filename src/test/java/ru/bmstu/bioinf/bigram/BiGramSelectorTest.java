package ru.bmstu.bioinf.bigram;

import org.junit.Test;
import ru.bmstu.bioinf.filtering.Node;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.List;
import java.util.Set;

public class BiGramSelectorTest {
    @Test
    public void test1() {
        Sequence searchedSeq = new Sequence("searched", "AGACGTAAGA");
        Sequence dataSeq = new Sequence("data", "AGGGGGGGGGGGGGGGGGGG");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<Set<Node>> result = biGramSelector.getNewBiGrams();
        System.out.println(result);
    }

    @Test
    public void test2() {
        Sequence searchedSeq = new Sequence("searched", "AABA");
        Sequence dataSeq = new Sequence("data", "ABAA");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<Set<Node>> result = biGramSelector.getNewBiGrams();
        System.out.println(result);
    }
}
