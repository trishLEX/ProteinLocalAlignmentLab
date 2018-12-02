package ru.bmstu.bioinf.filtering;

import org.junit.Test;
import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.ngram.NGram;
import ru.bmstu.bioinf.ngram.NGramSelector;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiagSelectionTest {
    private static final int LENGTH = 2;
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";

    @Test
    public void getDiagNGramsTest() {
        FineTable.getInstance(-2);

        Sequence searchedSeq = new Sequence(NAME_1, "ABCBCC");
        Sequence dataSetSeq = new Sequence(NAME_2, "CBCCB");

        DiagSelection diagSelection = new DiagSelection(searchedSeq, dataSetSeq, -2, LENGTH, 0, 0, 5);
        Map<Node, Node> words = diagSelection.getDiagonals();
        System.out.println(words);
    }
}
