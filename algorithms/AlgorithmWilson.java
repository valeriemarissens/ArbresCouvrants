/*
 * AlgorithmWilson.java
 * Project
 *
 * Created by ValerieMarissens on 27/12/2020.
 * Copyright © 2020 ValerieMarissens. All rights reserved.
 */

package algorithms;

import graph.Edge;
import graph.Graph;
import graph.SpanningTree;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AlgorithmWilson implements Algorithm{
    private final Graph graph;
    private final int nbVertices;
    private SpanningTree spanningTree;
    private boolean finished;

    public AlgorithmWilson(@NotNull Graph g){
        this.graph = g;
        this.nbVertices = graph.vertices();
        this.spanningTree = new SpanningTree();
        this.finished = false;
    }

    @Override
    public SpanningTree algo() {
        graph.clean();
        spanningTree = new SpanningTree();
        finished = false;
        List<Integer> unvisited = new ArrayList<>();
        List<Integer> visited = new ArrayList<>();

        // Tous les sommets n'ont pas été visités au début.
        for (int i = 0; i < nbVertices; i++)
            unvisited.add(i);

        // On visite le premier sommet.
        visited.add(unvisited.get(0));
        unvisited.remove(0);

        return auxiliaryAlgorithm(unvisited, visited);
    }

    @Override
    public String getTitle() {
        return "Wilson";
    }

    private SpanningTree auxiliaryAlgorithm(List<Integer> unvisited, List<Integer> visited){
        // Tous les sommets sont visités.
        if (unvisited.isEmpty()){

            // Récupère l'arbre couvrant construit.
            for (Edge e : graph.edges()){
                if (e.isUsed()){
                    spanningTree.add(e);
                }
            }
            return spanningTree;
        }
        else {
            // On choisit un sommet non visité aléatoirement.
            Random random = new Random();
            int index = random.nextInt(unvisited.size());
            int start = unvisited.get(index);
            List<Integer> walk = new ArrayList<>();
            walk.add(start);
            walk = randomWalk(start, walk, visited);

            // On elève les boucles de la marche aléatoire.
            walk = this.cleanedRandomWalk(walk);

            // On ajoute les sommets qui restent aux sommets visités.
            for (Integer v : walk) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    unvisited.remove(v);
                }
            }

            // On ajoute toutes les arêtes à l'arbre.
            for (int k = 0; k < walk.size(); k++) {
                int vertex = walk.get(k);
                if (k + 1 < walk.size()) { // todo: voir autre façon de récupérer les edges ou faire que avec des edges
                    for (Edge e : graph.adj(vertex)) {
                        if (e.other(vertex) == walk.get(k + 1)) {
                            e.mark(true);
                        }
                    }
                }
            }

            return auxiliaryAlgorithm(unvisited, visited);
        }
    }

    private List<Integer> randomWalk(int start, List<Integer> walk, List<Integer> visited){
        if (visited.contains(start)){
            return walk;
        }
        else{
            ArrayList<Edge> adj = graph.adj(start);
            Random random = new Random();
            int nextIndex = random.nextInt(adj.size());
            Edge nextEdge = adj.get(nextIndex);
            int nextVertex = nextEdge.other(start);

            walk.add(nextVertex);

            return this.randomWalk(nextVertex, walk, visited);
        }
    }

    /**
     * Fonction récursive qui cherches les doublons. S'il y en a, alors on élimine tout
     * entre la 1ère et la dernière occurrence de ce doublon. Ensuite on recherche les
     * doublons qui restent.
     * S'il n'y en a pas, la marche aléatoire est déjà nettoyée.
     *
     * @param randomWalk marche aléatoire à nettoyer.
     * @return marche aléatoire sans boucles.
     */
    private List<Integer> cleanedRandomWalk(List<Integer> randomWalk){
        // On cherche des doublons.
        int start = -1, end = -1, i = 0, j = randomWalk.size() - 1;
        boolean foundRepeated = false;

        while ((i < randomWalk.size())) { // croissant
            int v1 = randomWalk.get(i);
            while ((j >= 0) && !foundRepeated) { // décroissant
                if (i != j) {
                    int v2 = randomWalk.get(j);
                    if (v1 == v2) {
                        start = i;
                        end = j;
                        foundRepeated = true;
                    }
                }
                j--;
            }
            i++;
            j = randomWalk.size() - 1;
        }

        // On élimine les boucles de la marche.
        if (foundRepeated) {
            cleanDoubles(randomWalk, start, end);
            return cleanedRandomWalk(randomWalk);
        }
        else {
            return randomWalk;
        }
    }

    /**
     * Enlève tout entre la première apparition et la dernière occurence du sommet.
     *
     * @param list à nettoyer.
     * @param firstAppearance première apparition du doublon.
     * @param lastAppearance dernière apparition du doublon.
     */
    private void cleanDoubles(List<Integer> list, int firstAppearance, int lastAppearance){
        if ((firstAppearance >= 0) && (lastAppearance <= list.size())) {
            list.subList(firstAppearance + 1, lastAppearance + 1).clear();
        }
    }
}
