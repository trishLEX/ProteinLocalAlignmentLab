package ru.bmstu.bioinf.sw;

import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.filtering.Node;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SWAllignment {
    private Sequence searched;
    private Sequence fromBase;
    private Map<Pair<Integer, Integer>, SWNode> matrix;
    private FineTable table;
    private SWNode maxNode;

    private static final int borderAdd = 32;


    public SWAllignment(Sequence searched, Sequence fromBase,
                        Node routeStart, Node routeFinish, FineTable table) {
        this.searched = searched;
        this.fromBase = fromBase;
        this.table = table;

        Pair<Integer, Integer> borders = getBorder(routeStart, routeFinish);
        int top = borders.getKey();
        int bottom = borders.getValue();
        int left = routeStart.getDataSetSeqCoordinate() + routeStart.getSearchedSeqCoordinate();
        int right = routeFinish.getDataSetSeqCoordinate() + routeFinish.getSearchedSeqCoordinate();

        computeMatrix(top, bottom, left, right);
    }

    private void computeMatrix(int top, int bottom, int left, int right) {
        matrix = new LinkedHashMap<>();
        maxNode = new SWNode(0,0, 0.0f, SWNode.zero);

        int searchStartCoord = Math.max((left - top) / 2, 0);
        int searchUntilCoord = Math.min((right - bottom) / 2 + 1, searched.length());

        for(int i = searchStartCoord; i < searchUntilCoord; ++i) {
            int start = Math.max(
                    Math.max(left - i, bottom + i),
                    0);
            int until = Math.min(
                    Math.min(right - i + 1, top + i + 1),
                    fromBase.length());
            for(int j = start; j < until; ++j) {
                SWNode nodeij = SWNode.max(
                        SWNode.zero,
                        new SWNode(i, j, table.getE(),
                                matrix.getOrDefault(
                                        new Pair<>(i - 1, j),
                                        new SWNode(
                                                i - 1, j, 0.0f, SWNode.zero))
                        ),
                        new SWNode(i, j, table.getE(),
                                matrix.getOrDefault(
                                        new Pair<>(i, j - 1),
                                        new SWNode(
                                                i, j - 1, 0.0f, SWNode.zero))
                        ),
                        new SWNode(i, j, (float) table.get(searched.get(i), fromBase.get(j)),
                                matrix.getOrDefault(
                                        new Pair<>(i - 1, j - 1),
                                        new SWNode(
                                                i - 1, j - 1, 0.0f, SWNode.zero))
                        ));
                maxNode = SWNode.max(maxNode, nodeij);

                matrix.put(new Pair<>(i, j), nodeij);
            }
        }
    }

    //Возвращает пару (верхняя диагональ, нижняя диагональ) + дополнительный отступ (где возможно)
    private Pair<Integer, Integer> getBorder(Node routeStart, Node routeFinish) {
        Node curNode = routeFinish;
        int topDiag =
                routeFinish.getDataSetSeqCoordinate() - routeFinish.getSearchedSeqCoordinate();
        int bottomDiag = topDiag;
        while(!curNode.equals(routeStart)) {
            curNode = curNode.getMaxParent();
            int candidate =
                    curNode.getDataSetSeqCoordinate() - curNode.getSearchedSeqCoordinate();
            topDiag = Math.max(topDiag, candidate);
            bottomDiag = Math.min(bottomDiag, candidate);
        }

        return new Pair<>(
                Math.min(topDiag + borderAdd, fromBase.length() - 1),
                Math.max(bottomDiag - borderAdd, 1 - searched.length())
        );
    }

    public Float getScore() {
        return maxNode.getScore();
    }

    @Override
    public String toString() {
        return String.format("%sScore: %f", getAllignment(), getScore());
    }

    private String getAllignment() {
        StringBuilder sbSearched = new StringBuilder();
        StringBuilder sbBase = new StringBuilder();
        SWNode curNode = maxNode;
        SWNode parent;
        while(Float.compare(curNode.getScore(), 0.0f) != 0) {
            parent = curNode.getParent();
            if(curNode.getBaseCoord() - parent.getBaseCoord() > 0) {
                sbBase.insert(0, fromBase.get(curNode.getBaseCoord()));
            } else {
                sbBase.insert(0, "-");
            }
            if(curNode.getSearchedCoord() - parent.getSearchedCoord() > 0) {
                sbSearched.insert(0, searched.get(curNode.getSearchedCoord()));
            } else {
                sbSearched.insert(0, "-");
            }
            curNode = parent;
        }
        sbSearched.insert(0,
                String.join("",
                        Collections.nCopies(curNode.getBaseCoord() + 1, "-")));
        sbSearched.append(
                String.join("",
                        Collections.nCopies(fromBase.length() - maxNode.getBaseCoord() - 1, "-")));
        sbSearched.insert(0, searched.substring(0, curNode.getSearchedCoord() + 1));
        sbSearched.append(searched.substring(maxNode.getSearchedCoord() + 1));
        sbBase.insert(0, fromBase.substring(0, curNode.getBaseCoord() + 1));
        sbBase.append(fromBase.substring(maxNode.getBaseCoord() + 1));
        sbBase.insert(0,
                String.join("",
                        Collections.nCopies(curNode.getSearchedCoord() + 1, "-")));
        sbBase.append(
                String.join("",
                        Collections.nCopies(searched.length() - maxNode.getSearchedCoord() - 1,"-")));

        String newSearched = sbSearched.toString();
        String newBase = sbBase.toString();

        StringBuilder resulting = new StringBuilder();
        int nexti;
        for(int i = 0; i < newBase.length(); i = nexti) {
            nexti = Math.min(i + 70, newBase.length());
            resulting.append("searched: ");
            resulting.append(newSearched.substring(i, nexti));
            resulting.append("\n");
            resulting.append("fromBase: ");
            resulting.append(newBase.substring(i, nexti));
            resulting.append("\n");
        }
        return resulting.toString();
    }
}
