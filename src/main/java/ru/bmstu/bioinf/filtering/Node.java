package ru.bmstu.bioinf.filtering;

import ru.bmstu.bioinf.FineTable;

import java.util.*;

public class Node implements Comparable<Node> {
    private Set<Node> parents;
    private Set<Node> children;

    private Node maxParent;

    private int searchedSeqCoordinate;
    private int dataSetSeqCoordinate;

    private char searchedSeqChar;
    private char dataSetSeqChar;

    private float score;

    public Node(int searchedSeqCoordinate, int dataSetSeqCoordinate, char searchedSeqChar, char dataSetSeqChar) {
        this.searchedSeqCoordinate = searchedSeqCoordinate;
        this.dataSetSeqCoordinate = dataSetSeqCoordinate;

        this.searchedSeqChar = searchedSeqChar;
        this.dataSetSeqChar = dataSetSeqChar;

        this.score = FineTable.getInstance(-2f).get(searchedSeqChar, dataSetSeqChar);

        this.children = new LinkedHashSet<>();
        this.parents = new LinkedHashSet<>();
    }

    public int getSearchedSeqCoordinate() {
        return searchedSeqCoordinate;
    }

    public int getDataSetSeqCoordinate() {
        return dataSetSeqCoordinate;
    }

    public void addParent(Node parent) {
        parents.add(parent);
    }

    public void addChild(Node child) {
        children.remove(child);
        children.add(child);
    }

    public boolean hasParent() {
        return !parents.isEmpty();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public Set<Node> getChildren() {
        return children;
    }

    public void setMaxParent(Node maxParent) {
        this.maxParent = maxParent;
    }

    public Node getMaxParent() {
        return maxParent;
    }

    public char getSearchedSeqChar() {
        return searchedSeqChar;
    }

    public char getDataSetSeqChar() {
        return dataSetSeqChar;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void updateScore(float delta) {
        this.score += delta;
        for (Node child : children) {
            child.setScore(child.getScore() + delta);
            child.updateScore(delta);
        }
    }

    public Node getStart() {
        if (maxParent == null) {
            return this;
        } else {
            return maxParent.getStart();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Node other = (Node) obj;
        return this.searchedSeqCoordinate == other.searchedSeqCoordinate && this.dataSetSeqCoordinate == other.dataSetSeqCoordinate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchedSeqCoordinate, dataSetSeqCoordinate);
    }

    @Override
    public int compareTo(Node o) {
        if (this.equals(o)) {
            return 0;
        }

        if (this.searchedSeqCoordinate >= o.searchedSeqCoordinate && this.dataSetSeqCoordinate >= o.dataSetSeqCoordinate) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "(" + searchedSeqChar + ":" + searchedSeqCoordinate + "," + dataSetSeqChar + ":" + dataSetSeqCoordinate + ")";
    }
}
