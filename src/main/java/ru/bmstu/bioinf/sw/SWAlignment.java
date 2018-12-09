package ru.bmstu.bioinf.sw;

import ru.bmstu.bioinf.FineTable;
import ru.bmstu.bioinf.filtering.Node;
import ru.bmstu.bioinf.sequence.Sequence;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SWAlignment {
    private Sequence searchedSequence;
    private Sequence dataSetSequence;
    private FineTable table;
    private SWNode maxNode;

    private static final int BORDER_ADD = 32;

    public SWAlignment(Sequence searchedSequence, Sequence dataSetSequence,
                       Node routeStart, Node routeFinish, FineTable table) {
        this.searchedSequence = searchedSequence;
        this.dataSetSequence = dataSetSequence;
        this.table = table;

        Pair<Integer, Integer> borders = getBorder(routeStart, routeFinish);
        int top = borders.getKey();
        int bottom = borders.getValue();
        int left = routeStart.getDataSetSeqCoordinate() + routeStart.getSearchedSeqCoordinate();
        int right = routeFinish.getDataSetSeqCoordinate() + routeFinish.getSearchedSeqCoordinate();

        computeMatrix(top, bottom, left, right);
    }

    private void computeMatrix(int top, int bottom, int left, int right) {
        Map<Pair<Integer, Integer>, SWNode> matrix = new LinkedHashMap<>();
        maxNode = SWNode.ZERO;

        int searchStartCoord = Math.max((left - top) / 2, 0);
        int searchUntilCoord = Math.min((right - bottom) / 2 + 1, searchedSequence.length());

        for(int i = searchStartCoord; i < searchUntilCoord; ++i) {
            int start = Math.max(
                    Math.max(left - i, bottom + i),
                    0);

            int until = Math.min(
                    Math.min(right - i + 1, top + i + 1),
                    dataSetSequence.length());

            for(int j = start; j < until; ++j) {
                SWNode nodeIJ = SWNode.max(
                        SWNode.ZERO,
                        new SWNode(i, j, table.getE(), Action.GAP_BASE,
                                matrix.getOrDefault(
                                        new Pair<>(i - 1, j),
                                        SWNode.ZERO
                                )
                        ),
                        new SWNode(i, j, table.getE(), Action.GAP_SEARCHED,
                                matrix.getOrDefault(
                                        new Pair<>(i, j - 1),
                                        SWNode.ZERO
                                )
                        ),
                        new SWNode(i, j, table.get(searchedSequence.get(i), dataSetSequence.get(j)), Action.MATCH,
                                matrix.getOrDefault(
                                        new Pair<>(i - 1, j - 1),
                                        SWNode.ZERO
                                )
                        )
                );

                maxNode = SWNode.max(maxNode, nodeIJ);

                matrix.put(new Pair<>(i, j), nodeIJ);
            }
        }
    }

    /**
     * @return пару (верхняя диагональ, нижняя диагональ) + дополнительный отступ (где возможно)
     */
    private Pair<Integer, Integer> getBorder(Node routeStart, Node routeFinish) {
        Node curNode = routeFinish;

        int topDiag = routeFinish.getDataSetSeqCoordinate() - routeFinish.getSearchedSeqCoordinate();
        int bottomDiag = topDiag;

        while(!curNode.equals(routeStart)) {
            curNode = curNode.getMaxParent();

            int candidate = curNode.getDataSetSeqCoordinate() - curNode.getSearchedSeqCoordinate();

            topDiag = Math.max(topDiag, candidate);
            bottomDiag = Math.min(bottomDiag, candidate);
        }

        return new Pair<>(
                Math.min(topDiag + BORDER_ADD, dataSetSequence.length() - 1),
                Math.max(bottomDiag - BORDER_ADD, 1 - searchedSequence.length())
        );
    }

    public Float getScore() {
        return maxNode.getScore();
    }

    public Sequence getDataSetSequence() {
        return dataSetSequence;
    }

    @Override
    public String toString() {
        return String.format("%sScore: %f", getAlignment(), getScore());
    }

    private String getAlignment() {
        StringBuilder sbSearched = new StringBuilder();
        StringBuilder sbBase = new StringBuilder();

        SWNode curNode = maxNode;

        int lenBase = 0;
        int lenSearched = 0;
        while(Float.compare(curNode.getScore(), 0.0f) != 0) {
            switch(curNode.getAction()) {
                case MATCH:
                    sbBase.insert(0, dataSetSequence.get(curNode.getDataSetSeqCoordinate()));
                    sbSearched.insert(0, searchedSequence.get(curNode.getSearchedSeqCoordinate()));
                    ++lenBase;
                    ++lenSearched;
                    break;
                case GAP_BASE:
                    sbBase.insert(0, "-");
                    sbSearched.insert(0, searchedSequence.get(curNode.getSearchedSeqCoordinate()));
                    ++lenSearched;
                    break;
                case GAP_SEARCHED:
                    sbBase.insert(0, dataSetSequence.get(curNode.getDataSetSeqCoordinate()));
                    ++lenBase;
                    sbSearched.insert(0, "-");
                    break;
            }

            curNode = curNode.getParent();
        }

        if(lenBase + lenSearched != 0) {
            sbSearched.insert(0,
                    String.join("",
                            Collections.nCopies(maxNode.getDataSetSeqCoordinate() - lenBase + 1, "-")));

            sbSearched.append(
                    String.join("",
                            Collections.nCopies(dataSetSequence.length() - maxNode.getDataSetSeqCoordinate() - 1, "-")));

            sbSearched.insert(0,
                    searchedSequence.substring(0, maxNode.getSearchedSeqCoordinate() - lenSearched + 1));
            sbSearched.append(searchedSequence.substring(maxNode.getSearchedSeqCoordinate() + 1));
            sbBase.insert(0, dataSetSequence.substring(0, maxNode.getDataSetSeqCoordinate() - lenBase + 1));
            sbBase.append(dataSetSequence.substring(maxNode.getDataSetSeqCoordinate() + 1));

            sbBase.insert(0,
                    String.join("",
                            Collections.nCopies(maxNode.getSearchedSeqCoordinate() - lenSearched + 1, "-")));

            sbBase.append(
                    String.join("",
                            Collections.nCopies(searchedSequence.length() - maxNode.getSearchedSeqCoordinate() - 1, "-")));
        } else {
            sbBase.insert(0,
                    String.join("",
                            Collections.nCopies(searchedSequence.length(), "-")
                    )
            );
            sbBase.append(dataSetSequence.getSequence());
            sbSearched.append(searchedSequence.getSequence());
            sbSearched.append(String.join("",
                    Collections.nCopies(dataSetSequence.length(), "-"))
            );
        }

        String newSearched = sbSearched.toString();
        String newBase = sbBase.toString();

        StringBuilder resulting = new StringBuilder();
        int nextI;
        for(int i = 0; i < newBase.length(); i = nextI) {
            nextI = Math.min(i + 70, newBase.length());
            resulting.append("searched: ");
            resulting.append(newSearched, i, nextI);
            resulting.append("\n");
            resulting.append("fromBase: ");
            resulting.append(newBase, i, nextI);
            resulting.append("\n");
        }

        return resulting.toString();
    }
}
