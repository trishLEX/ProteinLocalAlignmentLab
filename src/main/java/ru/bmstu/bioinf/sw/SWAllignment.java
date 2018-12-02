package ru.bmstu.bioinf.sw;

import javafx.util.Pair;
import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.filtering.Node;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SWAllignment {
    private String searched;
    private String fromBase;
    private Map<Pair<Integer, Integer>, SWNode> matrix;
    private FineTable table;
    private SWNode maxNode;

    private static final int borderAdd = 32;


    public SWAllignment(String searched, String fromBase,
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
        maxNode = new SWNode(0,0, 0.0f, SWNode.getZeroNode());

        int searchStartCoord = Integer.max((left - top) / 2, 0);
        int searchUntilCoord = Integer.min((right - bottom) / 2 + 1, searched.length());

        for(int i = searchStartCoord; i < searchUntilCoord; ++i) {
            int start = Integer.max(
                    Integer.max(left - i, bottom + i),
                    0);
            int until = Integer.min(
                    Integer.min(right - i + 1, top + i + 1),
                    fromBase.length());
            for(Integer j = start; j < until; ++j) {
                SWNode nodeij = SWNode.max(
                        SWNode.getZeroNode(),
                        new SWNode(i, j, table.getE(),
                                matrix.getOrDefault(
                                        new Pair<>(i - 1, j),
                                        new SWNode(
                                                i - 1, j, (float) 0, SWNode.getZeroNode()))
                        ),
                        new SWNode(i, j, table.getE(),
                                matrix.getOrDefault(
                                        new Pair<>(i, j - 1),
                                        new SWNode(
                                                i, j - 1, (float) 0, SWNode.getZeroNode()))
                        ),
                        new SWNode(i, j, (float) table.get(searched.charAt(i), fromBase.charAt(j)),
                                matrix.getOrDefault(
                                        new Pair<>(i - 1, j - 1),
                                        new SWNode(
                                                i - 1, j - 1, (float) 0, SWNode.getZeroNode()))
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
        while(curNode != routeStart) {
            curNode = curNode.getMaxParent();
            int candidate =
                    curNode.getDataSetSeqCoordinate() - curNode.getSearchedSeqCoordinate();
            topDiag = Integer.max(topDiag, candidate);
            bottomDiag = Integer.min(bottomDiag, candidate);
        }

        return new Pair<>(
                Integer.min(topDiag + borderAdd, fromBase.length() - 1),
                Integer.max(bottomDiag - borderAdd, 1 - searched.length())
        );
    }

    public Float getScore() {
        return maxNode.getScore();
    }

    @Override
    public String toString() {
        return String.format("%sScore: %f",getAllignment(), getScore());
    }

    private String getAllignment() {
        StringBuilder sbSearched = new StringBuilder();
        StringBuilder sbBase = new StringBuilder();
        SWNode curNode = maxNode;
        SWNode parent;
        while(Float.compare(curNode.getScore(), 0.0f) != 0) {
            parent = curNode.getParent();
            if(curNode.getSearchedCoord() - parent.getBaseCoord() > 0) {
                sbBase.insert(0, fromBase.charAt(curNode.getBaseCoord()));
            } else {
                sbBase.insert(0, "-");
            }
            if(curNode.getBaseCoord() - parent.getBaseCoord() > 0) {
                sbSearched.insert(0, searched.charAt(curNode.getSearchedCoord()));
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
                        Collections.nCopies(searched.length() - maxNode.getBaseCoord() - 1,"-")));

        String newSearched = sbSearched.toString();
        String newBase = sbBase.toString();

        StringBuilder resulting = new StringBuilder();
        int nexti = 0;
        for(int i = 0; i < newBase.length(); i = nexti) {
            nexti = Integer.min(i + 70, newBase.length());
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

class SWNode {
    public int getSearchedCoord() {
        return searchedCoord;
    }

    public int getBaseCoord() {
        return baseCoord;
    }

    private int searchedCoord;
    private int baseCoord;
    private SWNode parent;
    private Float score;
    private static SWNode zero = null;

    public SWNode(int searchedCoord, int baseCoord, float addition, SWNode parent) {
        this.searchedCoord = searchedCoord;
        this.baseCoord = baseCoord;
        this.parent = parent;
        this.score = (parent == null) ? addition : parent.getScore() + addition;
    }

    public Pair<Integer, Integer> getCoords() {
        return new Pair<>(searchedCoord, baseCoord);
    }

    public SWNode getParent() {
        return parent;
    }

    public float getScore() {
        return score;
    }

    public static SWNode max(SWNode first, SWNode... other) {
        for(SWNode cur : other) {
            if(Float.compare(cur.getScore(), first.getScore()) > 0) {
                first = cur;
            }
        }
        return first;
    }

    public static SWNode getZeroNode() {
        if(zero == null) {
            zero = new SWNode(-1,-1, 0.0f, null);
        }
        return zero;
    }
}
