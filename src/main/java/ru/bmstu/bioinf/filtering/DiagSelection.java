package ru.bmstu.bioinf.filtering;

import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.bigram.BiGramSelector;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для получения максимальных путей по дот-мапе
 */
public class DiagSelection {
    private BiGramSelector biGramSelector;
    private Sequence searchedSequence;
    private Sequence dataSetSequence;
    private FineTable fineTable;
    private List<Node> endNodes;
    private float gap;
    private float diagScore;
    private int minBiGrams;
    private int radius;

    public DiagSelection(Sequence searchedSequence, Sequence dataSetSequence, float gap, float diagScore, int minBiGrams, int radius) {
        this.biGramSelector = new BiGramSelector(searchedSequence, dataSetSequence);

        this.searchedSequence = searchedSequence;
        this.dataSetSequence = dataSetSequence;
        this.gap = gap;
        this.diagScore = diagScore;
        this.minBiGrams = minBiGrams;
        this.radius = radius;

        this.fineTable = FineTable.getInstance();
        this.endNodes = new ArrayList<>();
    }

    /**
     * @return отображение начала пути в его конец
     */
    public Map<Node, Node> getDiagonals() {
        List<Set<Node>> diagNGrams = getDiagNGrams();
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

    private List<List<Node>> sortAndLinkDiags(List<Set<Node>> diags) {
        List<List<Node>> result = new ArrayList<>(diags.size());

        for (Set<Node> diag : diags) {
            ArrayList<Node> sortedDiag = new ArrayList<>(diag);
            Collections.sort(sortedDiag);

            for (int i = 0; i < sortedDiag.size() - 1; i++) {
                Node first = sortedDiag.get(i);
                Node second = sortedDiag.get(i + 1);

                int diffSearchedSeqCoordinates = second.getSearchedSeqCoordinate() - first.getSearchedSeqCoordinate();
                int diffDataSetSeqCoordinates = second.getDataSetSeqCoordinate() - first.getDataSetSeqCoordinate();
                if (diffSearchedSeqCoordinates + diffDataSetSeqCoordinates <= radius) {
                    first.addChild(second);
                    second.addParent(first);
                }
            }

            result.add(sortedDiag);
        }

        diags = null;
        return result;
    }

    private Map<Node, Node> getMaxRoutes(List<Set<Node>> diags) {
        List<List<Node>> sortedAndLinkedDiags = sortAndLinkDiags(diags);
        linkNodes(sortedAndLinkedDiags);

        List<Node> startNodes = new ArrayList<>();
        for (List<Node> diag : sortedAndLinkedDiags) {
            for (Node node : diag) {
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

    private void linkNodes(List<List<Node>> diags) {
        for (int i = 0; i < diags.size() - 1; i++) {
            for (int j = i + 1; j < diags.size(); j++) {
                List<Node> currentNodes = diags.get(i);
                List<Node> toLinkNodes = diags.get(j);

                for (Node currentNode : currentNodes) {
                    for (Node toLinkNode : toLinkNodes) {

                        Node current = currentNode;
                        Node toLink = toLinkNode;

                        if (current.compareTo(toLink) > 0) {
                            Node temp = current;
                            current = toLink;
                            toLink = temp;
                        }

                        int diffSearchedSeqCoordinates = toLink.getSearchedSeqCoordinate() - current.getSearchedSeqCoordinate();
                        int diffDataSetSeqCoordinates = toLink.getDataSetSeqCoordinate() - current.getDataSetSeqCoordinate();
                        if (
                                diffSearchedSeqCoordinates >= 0 &&
                                diffDataSetSeqCoordinates >= 0 &&
                                diffSearchedSeqCoordinates + diffDataSetSeqCoordinates <= radius
                        ) {
                            current.addChild(toLink);
                            toLink.addParent(current);
                        }
                    }
                }
            }
        }
    }

    private List<Set<Node>> getDiagNGrams() {
        List<Set<Node>> nGrams = biGramSelector.getNewNGramsByHash();

        return nGrams
                .stream()
                .filter(nodes -> nodes.size() > minBiGrams * 2)
                .limit(10)
                .filter(nodes -> getDiagScore(nodes) > diagScore)
                .collect(Collectors.toList());
    }

    private int getDiagScore(Set<Node> nodes) {
        Node node = nodes.iterator().next();
        int searchedStart = node.getSearchedSeqCoordinate();
        int dataSetStart = node.getDataSetSeqCoordinate();

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
