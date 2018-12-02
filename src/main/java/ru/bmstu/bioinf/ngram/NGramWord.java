package ru.bmstu.bioinf.ngram;

import ru.bmstu.bioinf.filtering.Node;

import java.util.ArrayList;
import java.util.List;

public class NGramWord {
    private List<NGram> nGrams;
    private List<Node> nodes;

    public NGramWord() {
        this.nGrams = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    public void addNGram(NGram nGram) {
        nGrams.add(nGram);
        nGram.setNGramWord(this);

        nodes.addAll(nGram.toNodeList());
    }

    public NGram get(int i) {
        return nGrams.get(i);
    }

    public int size() {
        return nGrams.size();
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
