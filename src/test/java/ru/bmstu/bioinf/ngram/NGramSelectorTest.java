package ru.bmstu.bioinf.ngram;

import org.junit.Assert;
import org.junit.Test;
import ru.bmstu.bioinf.ngram.NGram;
import ru.bmstu.bioinf.ngram.NGramSelector;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.List;

public class NGramSelectorTest {

    /* Тесты для метода КМП */
    @Test
    public void test1() {
        Sequence searchedSeq = new Sequence("searched", "AGACGTAAGA");
        Sequence dataSeq = new Sequence("data", "AGGGGGGGGGGGGGGGGGGG");
        NGramSelector nGramSelector = new NGramSelector(searchedSeq, dataSeq, 2);
        List<NGram> result = nGramSelector.getNewNGrams();
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
        NGramSelector nGramSelector = new NGramSelector(searchedSeq, dataSeq, 2);
        List<NGram> result = nGramSelector.getNewNGrams();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test3() {
        Sequence searchedSeq = new Sequence("searched", "AGAG");
        Sequence dataSeq = new Sequence("data", "AGAG");
        NGramSelector nGramSelector = new NGramSelector(searchedSeq, dataSeq, 2);
        List<NGram> result = nGramSelector.getNewNGrams();
        Assert.assertEquals(5, result.size());
    }

    @Test
    public void test4() {
        Sequence searchedSeq = new Sequence("searched", "AGAG");
        Sequence dataSeq = new Sequence("data", "AGAG");
        NGramSelector nGramSelector = new NGramSelector(searchedSeq, dataSeq, 3);
        List<NGram> result = nGramSelector.getNewNGrams();
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("AGA", result.get(0).getString());
        Assert.assertEquals("GAG", result.get(1).getString());
    }

    @Test
    public void test5() {
        Sequence searchedSeq = new Sequence("searched", "AGAG");
        Sequence dataSeq = new Sequence("data", "AGAGTTTTTTAGA");
        NGramSelector nGramSelector = new NGramSelector(searchedSeq, dataSeq, 3);
        List<NGram> result = nGramSelector.getNewNGrams();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("AGA", result.get(0).getString());
        Assert.assertEquals(0, result.get(0).getDataSetSeqCoordinate());
        Assert.assertEquals("AGA", result.get(1).getString());
        Assert.assertEquals(10, result.get(1).getDataSetSeqCoordinate());
        Assert.assertEquals("GAG", result.get(2).getString());
        Assert.assertEquals(1, result.get(2).getDataSetSeqCoordinate());
    }

    /* Те же самые тесты, но для метода итератором */

    @Test
    public void test6() {
        Sequence searchedSeq = new Sequence("searched", "AGACGTAAGA");
        Sequence dataSeq = new Sequence("data", "AGGGGGGGGGGGGGGGGGGG");
        NGramSelector nGramSelector = new NGramSelector(searchedSeq, dataSeq, 2);
        List<NGram> result = nGramSelector.getNewNGramsByIterator();
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
        NGramSelector nGramSelector = new NGramSelector(searchedSeq, dataSeq, 2);
        List<NGram> result = nGramSelector.getNewNGramsByIterator();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void test8() {
        Sequence searchedSeq = new Sequence("searched", "AGAG");
        Sequence dataSeq = new Sequence("data", "AGAG");
        NGramSelector nGramSelector = new NGramSelector(searchedSeq, dataSeq, 2);
        List<NGram> result = nGramSelector.getNewNGramsByIterator();
        Assert.assertEquals(5, result.size());
    }

    @Test
    public void test9() {
        Sequence searchedSeq = new Sequence("searched", "AGAG");
        Sequence dataSeq = new Sequence("data", "AGAG");
        NGramSelector nGramSelector = new NGramSelector(searchedSeq, dataSeq, 3);
        List<NGram> result = nGramSelector.getNewNGramsByIterator();
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("AGA", result.get(0).getString());
        Assert.assertEquals("GAG", result.get(1).getString());
    }

    @Test
    public void test10() {
        Sequence searchedSeq = new Sequence("searched", "AGAG");
        Sequence dataSeq = new Sequence("data", "AGAGTTTTTTAGA");
        NGramSelector nGramSelector = new NGramSelector(searchedSeq, dataSeq, 3);
        List<NGram> result = nGramSelector.getNewNGramsByIterator();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("AGA", result.get(0).getString());
        Assert.assertEquals(0, result.get(0).getDataSetSeqCoordinate());
        Assert.assertEquals("AGA", result.get(1).getString());
        Assert.assertEquals(10, result.get(1).getDataSetSeqCoordinate());
        Assert.assertEquals("GAG", result.get(2).getString());
        Assert.assertEquals(1, result.get(2).getDataSetSeqCoordinate());
    }

}
