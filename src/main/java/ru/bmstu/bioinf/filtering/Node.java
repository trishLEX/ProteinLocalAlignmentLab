package ru.bmstu.bioinf.filtering;

import java.util.*;

public class Node {
    private Set<Node> parents;
    private Set<Node> children;

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

    public void setMaxChild(Node maxChild) {
        this.maxChild = maxChild;
    }

    public Node getMaxParent() {
        return maxParent;
    }

    public Node getMaxChild() {
        return maxChild;
    }

    public char getSearchedSeqChar() {
        return searchedSeqChar;
    }

    public char getDataSetSeqChar() {
        return dataSetSeqChar;
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
    public String toString() {
        return "(" + searchedSeqChar + ":" + searchedSeqCoordinate + "," + dataSetSeqChar + ":" + dataSetSeqCoordinate + ")";
    }
}
