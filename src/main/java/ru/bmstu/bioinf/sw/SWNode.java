package ru.bmstu.bioinf.sw;

public class SWNode {
    public int getSearchedCoord() {
        return searchedCoord;
    }

    public int getBaseCoord() {
        return baseCoord;
    }

    private int searchedCoord;
    private int baseCoord;
    private SWNode parent;
    private float score;
    public static final SWNode zero = new SWNode(-1, -1, 0.0f, null);

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

}
