package ru.bmstu.bioinf.ngram;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.bmstu.bioinf.bigram.BiGram;
import ru.bmstu.bioinf.bigram.BiGramSelector;
import ru.bmstu.bioinf.filtering.Node;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.List;
import java.util.Set;

public class BiGramSelectorTest {
    /**
     * Тесты для метода КМП
     **/
    @Test
    public void test1() {
        Sequence searchedSeq = new Sequence("searched", "AGACGTAAGA");
        Sequence dataSeq = new Sequence("data", "AGGGGGGGGGGGGGGGGGGG");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<BiGram> result = biGramSelector.getNewNGrams();
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(0, result.get(0).getSearchedSeqCoordinate());
        Assert.assertEquals(0, result.get(0).getDataSetSeqCoordinate());
        Assert.assertEquals(7, result.get(1).getSearchedSeqCoordinate());
        Assert.assertEquals(0, result.get(0).getDataSetSeqCoordinate());
    }

    @Test
    public void test2() {
        Sequence searchedSeq = new Sequence("searched", "");
        Sequence dataSeq = new Sequence("data", "");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<BiGram> result = biGramSelector.getNewNGrams();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test3() {
        Sequence searchedSeq = new Sequence("searched", "AGAG");
        Sequence dataSeq = new Sequence("data", "AGAG");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<BiGram> result = biGramSelector.getNewNGrams();
        Assert.assertEquals(5, result.size());
    }

    @Test
    public void test4() {
        Sequence searchedSeq = new Sequence("searched", "AGAG");
        Sequence dataSeq = new Sequence("data", "AGAG");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<BiGram> result = biGramSelector.getNewNGrams();
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("AGA", result.get(0).getString());
        Assert.assertEquals("GAG", result.get(1).getString());
    }

    @Test
    public void test5() {
        Sequence searchedSeq = new Sequence("searched", "AGAG");
        Sequence dataSeq = new Sequence("data", "AGAGTTTTTTAGA");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<BiGram> result = biGramSelector.getNewNGrams();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("AGA", result.get(0).getString());
        Assert.assertEquals(0, result.get(0).getDataSetSeqCoordinate());
        Assert.assertEquals("AGA", result.get(1).getString());
        Assert.assertEquals(10, result.get(1).getDataSetSeqCoordinate());
        Assert.assertEquals("GAG", result.get(2).getString());
        Assert.assertEquals(1, result.get(2).getDataSetSeqCoordinate());
    }

    /**
     * Те же самые тесты, но для метода итератором
     **/
    @Test
    public void test6() {
        Sequence searchedSeq = new Sequence("searched", "AGACGTAAGA");
        Sequence dataSeq = new Sequence("data", "AGGGGGGGGGGGGGGGGGGG");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<BiGram> result = biGramSelector.getNewNGramsByIterator();
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(0, result.get(0).getSearchedSeqCoordinate());
        Assert.assertEquals(0, result.get(0).getDataSetSeqCoordinate());
        Assert.assertEquals(7, result.get(1).getSearchedSeqCoordinate());
        Assert.assertEquals(0, result.get(0).getDataSetSeqCoordinate());
    }

    @Test
    public void test7() {
        Sequence searchedSeq = new Sequence("searched", "");
        Sequence dataSeq = new Sequence("data", "");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<BiGram> result = biGramSelector.getNewNGramsByIterator();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test8() {
        Sequence searchedSeq = new Sequence("searched", "AGAG");
        Sequence dataSeq = new Sequence("data", "AGAG");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<BiGram> result = biGramSelector.getNewNGramsByIterator();
        Assert.assertEquals(5, result.size());
    }

    @Test
    public void test11() {
        Sequence searchedSeq = new Sequence("searched", "AGACGTAAGA");
        Sequence dataSeq = new Sequence("data", "AGGGGGGGGGGGGGGGGGGG");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<Set<Node>> result = biGramSelector.getNewNGramsByHash();
        System.out.println(result);
    }

    @Test
    public void test12() {
        Sequence searchedSeq = new Sequence("searched", "AABA");
        Sequence dataSeq = new Sequence("data", "ABAA");
        BiGramSelector biGramSelector = new BiGramSelector(searchedSeq, dataSeq);
        List<Set<Node>> result = biGramSelector.getNewNGramsByHash();
        System.out.println(result);
    }
}
