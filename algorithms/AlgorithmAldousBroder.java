/*
 * AlgorithmAldousBroder.java
 * Project
 *
 * Created by ValerieMarissens on 27/12/2020.
 * Copyright © 2020 ValerieMarissens. All rights reserved.
 */

package algorithms;

import com.sun.istack.internal.NotNull;
import graph.Edge;
import graph.Graph;
import graph.SpanningTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlgorithmAldousBroder implements Algorithm {
    private final Graph graph;

    /**
     * Liste qui contient les sommets visités.
     */
    private List<Integer> grey;
    private SpanningTree spanningTree;

    public AlgorithmAldousBroder(@NotNull Graph graph){
        this.graph = graph;
        this.grey = new ArrayList<>();
        this.spanningTree = new SpanningTree();
    }

    @Override
    public SpanningTree algo(){
        grey = new ArrayList<>();
        spanningTree = new SpanningTree();
        graph.clean();

        // Choisit un sommet quelconque.
        Random random = new Random();
        int start = random.nextInt(graph.vertices());

        // Marche aléatoire.
        this.randomWalk(start);

        return spanningTree;
    }

    @Override
    public String getTitle() {
        return "Aldous-Broder";
    }

    private boolean randomWalk(@NotNull int red){
        // Tous les sommets ont été visités.
        if (isFinished()){

            // Récupère l'arbre couvrant construit.
            for (Edge e : graph.edges()){
                if (e.isUsed()){
                    spanningTree.add(e);
                }
            }
            return true;

        }
        else {
            ArrayList<Edge> adj = graph.adj(red);
            Random random = new Random();
            int nextIndex = random.nextInt(adj.size());
            Edge nextEdge = adj.get(nextIndex);
            int nextVertex = nextEdge.other(red);

            // Si le sommet n'était pas visité, on marque l'arête.
            if (!grey.contains(nextVertex)) {
                nextEdge.mark(true);
            }

            grey.add(red);

            // Appel récursif.
            return this.randomWalk(nextVertex);
        }
    }

    /**
     * @return true si tous les sommets ont été visités.
     */
    private boolean isFinished(){
        boolean f = true;

        // Tous les sommets visités.
        for (int i = 0; i < graph.vertices(); i++){
            if (!grey.contains(i)){
                f = false;
            }
        }

        return f;
    }

    @Override
    public String toString() {
        return spanningTree.toString();
    }
}
