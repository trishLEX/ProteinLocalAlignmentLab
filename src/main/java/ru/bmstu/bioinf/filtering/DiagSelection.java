package ru.bmstu.bioinf.filtering;

import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.ngram.NGram;
import ru.bmstu.bioinf.ngram.NGramSelector;
import ru.bmstu.bioinf.ngram.NGramWord;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для получения максимальных путей по дот-мапе
 */
public class DiagSelection {
    private NGramSelector nGramSelector;
    private Sequence searchedSequence;
    private Sequence dataSetSequence;
    private FineTable fineTable;
    private List<Node> endNodes;
    private float gap;
    private float diagScore;
    private int minNGrams;
    private int radius;
    private int nGramLen;

    public DiagSelection(Sequence searchedSequence, Sequence dataSetSequence, float gap, int nGramLen, float diagScore, int minNGrams, int radius) {
        this.nGramSelector = new NGramSelector(searchedSequence, dataSetSequence, nGramLen);

        this.searchedSequence = searchedSequence;
        this.dataSetSequence = dataSetSequence;
        this.gap = gap;
        this.diagScore = diagScore;
        this.minNGrams = minNGrams;
        this.radius = radius;
        this.nGramLen = nGramLen;

        this.fineTable = FineTable.getInstance();
        this.endNodes = new ArrayList<>();
    }

    /**
     * @return отображение начала пути в его конец
     */
    public Map<Node, Node> getDiagonals() {
        List<NGramWord> diagNGrams = getDiagNGrams();
        if (diagNGrams.isEmpty()) {
            return null;
        }

        diagNGrams = diagNGrams.stream().filter(word -> getDiagScore(word) > diagScore).collect(Collectors.toList());
        if (diagNGrams.isEmpty()) {
            return null;
        }

        return getMaxRoutes(diagNGrams);
    }

    private void buildRoutes(Node node) {
        if (!node.hasChildren()) {
            endNodes.add(node);
        } else {
            for (Node child : node.getChildren()) {
                int diffSearchedSeqCoordinates = node.getSearchedSeqCoordinate() - child.getSearchedSeqCoordinate();
                int diffDataSetSeqCoordinates = node.getDataSetSeqCoordinate() - child.getDataSetSeqCoordinate();

                float score;
                //если условие выполняется, то n-граммы лежат "впритык" друг к другу и образают 2n-грамму
                if (diffSearchedSeqCoordinates == diffDataSetSeqCoordinates && diffSearchedSeqCoordinates == 1) {
                    score = node.getScore() + child.getScore();
                } else {
                    score = node.getScore() + gap * (diffSearchedSeqCoordinates + diffDataSetSeqCoordinates) + child.getScore();
                }

                if (child.getMaxParent() == null) {
                    child.setMaxParent(node);
                    child.setScore(score);
                    buildRoutes(child);
                } else {
                    if (child.getScore() < score) {
                        child.setMaxParent(node);
                        child.updateScore(score - child.getScore());
                    }
                }
            }
        }
    }

    private Map<Node, Node> getMaxRoutes(List<NGramWord> diagNGrams) {
        linkNodes(diagNGrams);

        List<Node> startNodes = new ArrayList<>();
        for (NGramWord word : diagNGrams) {
            for (Node node : word.getNodes()) {
                if (!node.hasParent()) {
                    startNodes.add(node);
                }
            }
        }

        for (Node node : startNodes) {
            buildRoutes(node);
        }

        Map<Node, Node> startToEndMap = new HashMap<>();

        for (Node endNode : endNodes) {
            Node start = endNode.getStart();
            if (startToEndMap.containsKey(start)) {
                Node max = startToEndMap.get(start).getScore() < endNode.getScore() ? endNode : startToEndMap.get(start);
                startToEndMap.put(start, max);
            } else {
                startToEndMap.put(start, endNode);
            }
        }

        return startToEndMap;
    }

    private void linkNodes(List<NGramWord> diagNGrams) {
        if (diagNGrams.size() == 1) {
            linkNodes(diagNGrams.get(0));
        }

        for (int i = 0; i < diagNGrams.size() - 1; i++) {
            for (int j = i + 1; j < diagNGrams.size(); j++) {
                linkNodes(diagNGrams.get(i));
                linkNodes(diagNGrams.get(j));

                Set<Node> currentNodes = diagNGrams.get(i).getNodes();
                Set<Node> toLinkNodes = diagNGrams.get(j).getNodes();

                for (Node currentNode : currentNodes) {
                    for (Node toLinkNode : toLinkNodes) {
                        int diffSearchedSeqCoordinates = toLinkNode.getSearchedSeqCoordinate() - currentNode.getSearchedSeqCoordinate();
                        int diffDataSetSeqCoordinates = toLinkNode.getDataSetSeqCoordinate() - currentNode.getDataSetSeqCoordinate();
                        if (
                                diffSearchedSeqCoordinates >= 0 &&
                                diffDataSetSeqCoordinates >= 0 &&
                                Math.abs(diffSearchedSeqCoordinates + diffDataSetSeqCoordinates) <= radius
                        ) {
                            currentNode.addChild(toLinkNode);
                            toLinkNode.addParent(currentNode);
                        }
                    }
                }
            }
        }
    }

    private void linkNodes(NGramWord word) {
        Iterator<Node> nodeIterator = word.getNodes().iterator();
        Node cur = nodeIterator.next();

        while (nodeIterator.hasNext()) {
            Node next = nodeIterator.next();
            int searchedDiff = cur.getSearchedSeqCoordinate() - next.getSearchedSeqCoordinate();
            int resultSetDiff = cur.getDataSetSeqCoordinate() - next.getDataSetSeqCoordinate();
            if (Math.abs(searchedDiff + resultSetDiff) <= Math.max(radius, nGramLen)) {
                cur.addChild(next);
                next.addParent(cur);
            }

            cur = next;
        }
    }

    private List<NGramWord> getDiagNGrams() {
        List<NGramWord> words = new ArrayList<>();
        List<NGram> nGrams = nGramSelector.getNewNGrams();

        for (int i = 0; i < nGrams.size() - 1; i++) {
            for (int j = i + 1; j < nGrams.size(); j++) {
                int searchedDiff = nGrams.get(i).getSearchedSeqCoordinate() - nGrams.get(j).getSearchedSeqCoordinate();
                int resultSetDiff = nGrams.get(i).getDataSetSeqCoordinate() - nGrams.get(j).getDataSetSeqCoordinate();

                if (searchedDiff == resultSetDiff) {
                    if (nGrams.get(i).getNGramWord() == null && nGrams.get(j).getNGramWord() == null) {
                        NGramWord word = new NGramWord();
                        word.addNGram(nGrams.get(i));
                        word.addNGram(nGrams.get(j));
                        words.add(word);
                    } else {
                        if (nGrams.get(i).getNGramWord() != null && nGrams.get(j).getNGramWord() == null) {
                            nGrams.get(i).getNGramWord().addNGram(nGrams.get(j));
                        }

                        if (nGrams.get(j).getNGramWord() != null && nGrams.get(i).getNGramWord() == null) {
                            nGrams.get(j).getNGramWord().addNGram(nGrams.get(i));
                        }
                    }
                }
            }
        }

        return words
                .stream()
                .filter(word -> word.size() > minNGrams)
                .limit(10)
                .collect(Collectors.toList());
    }

    private int getDiagScore(NGramWord nGramWord) {
        int searchedStart = nGramWord.get(0).getSearchedSeqCoordinate();
        int dataSetStart = nGramWord.get(0).getDataSetSeqCoordinate();

        int i = searchedStart;
        int j = dataSetStart;

        int diagScore = 0;
        while (i > 0 && j > 0) {
            diagScore += fineTable.get(searchedSequence.get(i), dataSetSequence.get(j));
            i--;
            j--;
        }

        while (searchedStart < searchedSequence.length() && dataSetStart < dataSetSequence.length()) {
            diagScore += fineTable.get(searchedSequence.get(searchedStart), dataSetSequence.get(dataSetStart));
            searchedStart++;
            dataSetStart++;
        }

        return diagScore;
    }
}
