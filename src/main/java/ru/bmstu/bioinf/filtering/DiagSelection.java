package ru.bmstu.bioinf.filtering;

import ru.bmstu.bioinf.FineTable;

import java.util.*;
/**
 * Класс для получения максимальных путей по дот-мапе
 */
public class DiagSelection {
    private FineTable fineTable;
    private List<Node> endNodes;
    private float gap;
    private float diagScore;
    private int minBiGrams;
    private int radius;

    public DiagSelection(float gap, float diagScore, int minBiGrams, int radius) {
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
    public Map<Node, Node> getDiagonals(List<Set<Node>> nGrams) {
        List<Set<Node>> diagNGrams = getDiagBiGrams(nGrams);
        if (diagNGrams.isEmpty()) {
            return null;
        }

        return getMaxRoutes(diagNGrams);
    }

    /**
     * Построить максимальный маршрут от {@param node}
     */
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

    /**
     * Сортировка по нод внутри диагонали и их соединение внутри диагонали
     */
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

    /**
     * Построить все максимальные маршруты для каждой компоненты связности
     */
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

    /**
     * Соединение всех возможных нод
     */
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
                        float score = fineTable.get(current.getDataSetSeqChar(), toLink.getDataSetSeqChar());
                        if (
                                score + (diffSearchedSeqCoordinates + diffDataSetSeqCoordinates) * gap >= score &&
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

    /**
     * Фильтарция диагоналей
     */
    private List<Set<Node>> getDiagBiGrams(List<Set<Node>> biGrams) {
        List<Set<Node>> filteredBySize = new ArrayList<>(biGrams.size());

        for (Set<Node> diag : biGrams) {
            if (diag.size() > minBiGrams * 2) {
                filteredBySize.add(diag);
            }
        }

        filteredBySize.sort(Comparator.comparingInt(Set::size));

        List<Set<Node>> filteredByScore = new ArrayList<>();

        for (int i = 0; i < Math.min(10, filteredBySize.size()); i++) {
            if (getDiagScore(filteredBySize.get(i)) > diagScore) {
                filteredByScore.add(filteredBySize.get(i));
            }
        }

        return filteredByScore;
    }

    private float getDiagScore(Set<Node> nodes) {
        float diagScore = 0f;

        for (Node node : nodes) {
            diagScore += fineTable.get(node.getSearchedSeqChar(), node.getDataSetSeqChar());
        }

        return diagScore;
    }
}
