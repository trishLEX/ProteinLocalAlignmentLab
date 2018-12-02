package ru.bmstu.bioinf.ngram;

import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.filtering.Node;

import java.util.ArrayList;
import java.util.List;

public class NGram {
    private final int length;
    private final String string;
    private final int searchedSeqCoordinate;
    private final int dataSetSeqCoordinate;
    private NGramWord nGramWord;
    private List<Node> nodeList;

    public NGram(int length, String string, int searchedSeqCoordinate, int dataSetSeqCoordinate) {
        this.length = length;
        this.string = string;
        this.searchedSeqCoordinate = searchedSeqCoordinate;
        this.dataSetSeqCoordinate = dataSetSeqCoordinate;

        toNodeList();
    }

    private void toNodeList() {
        nodeList = new ArrayList<>();

        char charAtSeq = string.charAt(0);

        FineTable fineTable = FineTable.getInstance();

        nodeList.add(new Node(
                        searchedSeqCoordinate,
                        dataSetSeqCoordinate,
                        charAtSeq,
                        charAtSeq,
                        fineTable.get(charAtSeq, charAtSeq)
                )
        );

        for (int i = 1; i < length; i++) {
            charAtSeq = string.charAt(i);

            Node node = new Node(
                    searchedSeqCoordinate + i,
                    dataSetSeqCoordinate + i,
                    string.charAt(i),
                    string.charAt(i),
                    fineTable.get(charAtSeq, charAtSeq)
            );

            nodeList.get(i - 1).addChild(node);
            node.addParent(nodeList.get(i - 1));

            nodeList.add(node);
        }
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNGramWord(NGramWord nGramWord) {
        this.nGramWord = nGramWord;
    }

    public int getLength() {
        return length;
    }

    public String getString() {
        return string;
    }

    public int getSearchedSeqCoordinate() {
        return searchedSeqCoordinate;
    }

    public int getDataSetSeqCoordinate() {
        return dataSetSeqCoordinate;
    }

    public NGramWord getNGramWord() {
        return nGramWord;
    }
}
