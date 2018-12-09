package ru.bmstu.bioinf.sw;

public class SWNode {
    public static final SWNode ZERO = new SWNode(-1, -1, 0.0f, null, null);

    private int searchedSeqCoordinate;
    private int dataSetSeqCoordinate;
    private SWNode parent;
    private float score;

    public Action getAction() {
        return action;
    }

    private Action action;

    public SWNode(int searchedSeqCoordinate, int dataSetSeqCoordinate, float addition, Action action, SWNode parent) {
        this.searchedSeqCoordinate = searchedSeqCoordinate;
        this.dataSetSeqCoordinate = dataSetSeqCoordinate;
        this.parent = parent;
        this.score = (parent == null) ? addition : parent.getScore() + addition;
        this.action = action;
    }

    public Pair<Integer, Integer> getCoords() {
        return new Pair<>(searchedSeqCoordinate, dataSetSeqCoordinate);
    }

    public SWNode getParent() {
        return parent;
    }

    public float getScore() {
        return score;
    }

    public int getSearchedSeqCoordinate() {
        return searchedSeqCoordinate;
    }

    public int getDataSetSeqCoordinate() {
        return dataSetSeqCoordinate;
    }

    public static SWNode max(SWNode first, SWNode... other) {
        for(SWNode cur : other) {
            if(Float.compare(cur.getScore(), first.getScore()) > 0) {
                first = cur;
            }
        }

        return first;
    }
}
