package ru.bmstu.bioinf.ngram;

import ru.bmstu.bioinf.filtering.Node;

import java.util.*;

public class NGramWord {
    private List<NGram> nGrams;
    private Set<Node> nodes;

    public NGramWord() {
        this.nGrams = new ArrayList<>();
        this.nodes = new LinkedHashSet<>();
    }

    public void addNGram(NGram nGram) {
        nGrams.add(nGram);
        nGram.setNGramWord(this);

        nodes.addAll(nGram.getNodeList());
    }

    public NGram get(int i) {
        return nGrams.get(i);
    }

    public int size() {
        return nGrams.size();
    }

    public Set<Node> getNodes() {
        return nodes;
    }
}
