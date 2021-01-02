package algorithms;

import graph.Edge;
import graph.Graph;
import graph.SpanningTree;

import java.util.*;

public class AlgorithmKruskal implements Algorithm {
    /**
     * Graphe où on applique l'algorithme.
     */
    private final Graph graph;

    /**
     * Arbre couvrant mis à jour dans la méthode algo().
     */
    private SpanningTree spanningTree;

    public AlgorithmKruskal(Graph graph){
        this.graph = graph;
        this.spanningTree = new SpanningTree();
    }

    /**
     * Applique l'algorithme de Kruskal pour trouver un arbre couvrant dans
     * le graphe.
     *
     * @return un arbre couvrant du graphe.
     */
    @Override
    public SpanningTree algo() {
        this.graph.clean();
        this.spanningTree = new SpanningTree();
        ArrayList<Edge> edges = graph.edges();

        // On mélange toutes les arêtes.
        Random random = new Random();
        Collections.shuffle(edges, random);

        // Initialisation du tableau des parents.
        int[] parent = new int[graph.vertices()];
        for (int i = 0; i < graph.vertices(); i++)
            parent[i] = -1;

        // Ajout des arêtes une par une sans créer de cycle.
        for (Edge e : edges){
            int from = e.getFrom();
            int to = e.getTo();
            if (find(from, parent) != find(to, parent)){
                e.mark(true);
                union(from, to, parent);
            }
        }

        // Récupère l'arbre couvrant.
        for (Edge e : graph.edges()){
            if (e.isUsed())
                spanningTree.add(e);
        }

        return spanningTree;
    }

    /**
     * Union des deux arbres en attachant la racine de l'un à la racine de l'autre.
     *
     * @param from sommet d'où l'on vient.
     * @param to sommet où on va.
     * @param parent tableau des parents.
     */
    private void union(int from, int to, int[] parent){
        int fromRoot = find(from, parent);
        int toRoot = find(to, parent);

        if (fromRoot != toRoot)
            parent[fromRoot] = toRoot;
    }

    /**
     * Suit les liens de parenté vers les noeuds pères jusqu'à atteindre la racine.
     *
     * @param vertex sommet.
     * @param parent tableau des parents.
     * @return la racine de l'arbre où se trouve vertex.
     */
    private int find(int vertex, int[] parent){
        // C'est la racine.
        if (parent[vertex] == -1)
            return vertex;

        // Appel récursif.
        return find(parent[vertex], parent);
    }

    @Override
    public String getTitle() {
        return "Kruskal";
    }
}
