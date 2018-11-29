package ru.bmstu.bioinf;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> parents;
    private List<Node> children;

    private Node maxParent;
    private Node maxChild;

    private int searchedSeqCoordinate;
    private int dataSetSeqCoordinate;

    private char searchedSeqChar;
    private char dataSetSeqChar;

    private int score;

    public Node(int searchedSeqCoordinate, int dataSetSeqCoordinate, char searchedSeqChar, char dataSetSeqChar, int score) {
        this.searchedSeqCoordinate = searchedSeqCoordinate;
        this.dataSetSeqCoordinate = dataSetSeqCoordinate;

        this.searchedSeqChar = searchedSeqChar;
        this.dataSetSeqChar = dataSetSeqChar;

        this.score = score;

        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
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
        children.add(child);
    }

    public boolean hasParent() {
        return !parents.isEmpty();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setMaxParent(Node maxParent) {
        this.maxParent = maxParent;
    }

    public void setMaxChild(Node maxChild) {
        this.maxChild = maxChild;
    }

    public Node getMaxParent() {
        return maxParent;
    }

    public Node getMaxChild() {
        return maxChild;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void updateScore(int delta) {
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
}
