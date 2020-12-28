/*
 * SpanningTree.java
 * Project
 *
 * Created by ValerieMarissens on 27/12/2020.
 * Copyright © 2020 ValerieMarissens. All rights reserved.
 */

package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe qui représente un arbre couvrant.
 */
public class SpanningTree {
    private final List<Edge> spanningTree;

    public SpanningTree(){
        this.spanningTree = new ArrayList<>();
    }

    public void add(Edge e){
        this.spanningTree.add(e);
    }

    public List<Edge> getSpanningTree() {
        return spanningTree;
    }

    @Override
    public String toString() {
        return spanningTree.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpanningTree)) return false;
        SpanningTree that = (SpanningTree) o;
        return Objects.equals(spanningTree, that.spanningTree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spanningTree);
    }
}
