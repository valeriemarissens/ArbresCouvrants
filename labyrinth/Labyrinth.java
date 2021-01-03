/*
 * Labyrinth.java
 * Project
 *
 * Created by ValerieMarissens on 28/12/2020.
 * Copyright © 2020 ValerieMarissens. All rights reserved.
 */

package labyrinth;

import graph.Edge;
import graph.Graph;

import java.util.*;

import static java.util.Collections.*;

public class Labyrinth {
    private final Graph graph;
    private int entry;
    private int exit;
    private List<Edge>[] adjOnlyCorridors;
    private List<Integer> deadEnds;

    public Labyrinth(Graph g){
        this.graph = g;

        this.toLabyrinth();
    }

    /* -------------------------------------------------
     *            TRANSFORMATION EN LABYRINTHE
     * ------------------------------------------------- */

    /**
     * Transforme le graphe en labyrinthe.
     */
    public void toLabyrinth(){
        this.deadEnds = new ArrayList<>();
        this.adjOnlyCorridors = (ArrayList<Edge>[]) new ArrayList[graph.vertices()];
        for (int v= 0; v < graph.vertices(); v++)
            adjOnlyCorridors[v] = new ArrayList<>();

        // Récupère les couloirs.
        for (int v= 0; v < graph.vertices(); v++) {
            for (Edge e : graph.adj(v)){
                if (e.isUsed())
                    adjOnlyCorridors[v].add(e);
            }
        }

        // Cherche entrée et sortie.
        int size = (int) Math.sqrt(graph.vertices()); // car Grid fait un new Graph(n * n)
        // La position des sommets est définie par : n * i + j
        this.entry = size - 1;
        this.exit = size * (size - 1);

    }

    /* -------------------------------------------------
     *               DISTANCE ENTRÉE-SORTIE
     * ------------------------------------------------- */

    /**
     * @return distance entre l'entrée et la sortie.
     */
    public int distanceEntryExit(){
        List<Integer> path = crossLabyrinth();
        return (path.size() - 1);
    }

    /**
     * Applique l'algorithme de Dijkstra puis construit le chemin à partir
     * de la map des prédécesseurs.
     *
     * @return un chemin entre l'entrée et la sortie.
     */
    private List<Integer> crossLabyrinth(){
        Map<Integer, Integer> predecessors = dijkstra();
        List<Integer> path = new ArrayList<>();

        // Retrouve le chemin.
        int v = exit;
        while (v != entry){
            int pred = predecessors.get(v);
            path.add(v);
            v = pred;
        }
        path.add(entry);
        Collections.reverse(path);

        return path;
    }

    /**
     * Fonction itérative qui cherche le plus court chemin entre l'entrée et la sortie
     * du labyrinthe avec l'algorithme de Dijkstra.
     *
     * @return tableau des prédécesseurs.
     */
    private Map<Integer, Integer> dijkstra(){
        List<Integer> visited = new ArrayList<>();
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> predecessors = new HashMap<>();
        boolean arrived = false;
        initialize(distances, predecessors);

        while (!arrived){
            int actualVertex = minVertex(visited, distances);

            if (actualVertex == exit)
                arrived = true;
            else{
                for (Edge e : adjOnlyCorridors[actualVertex]){
                    int neighbor = e.other(actualVertex);
                    int newDistance = distances.get(actualVertex) + 1;
                    if (newDistance < distances.get(neighbor)){
                        distances.replace(neighbor, newDistance);
                        predecessors.replace(neighbor, actualVertex);
                    }
                }
                visited.add(actualVertex);
            }
        }

        return predecessors;
    }

    /**
     * Initialise toutes les distances des cases du labyrinthe à linfini, sauf
     * l'entrée (où on est). Initialise aussi les prédecesseurs à -1.
     *
     * @param distances map des distances.
     * @param predecessors map des prédécesseurs.
     */
    private void initialize(Map<Integer, Integer> distances, Map<Integer, Integer> predecessors){
        for (int v = 0; v < graph.vertices(); v++){
            if (v == entry)
                distances.put(v, 0);
            else
                distances.put(v, Integer.MAX_VALUE);
            predecessors.put(v, -1);
        }
    }

    /**
     * @param visited liste des sommets déjà visités.
     * @param distances liste des distances.
     * @return sommet ayant la plus petite distance actuelle qui n'a pas encore été
     * visitée.
     */
    private int minVertex(List<Integer> visited, Map<Integer, Integer> distances){
        int min = Integer.MAX_VALUE;
        int vertex = -1;

        for (int v = 0; v < graph.vertices(); v++){
            int distance = distances.get(v);
            if ((!visited.contains(v)) && (distance <= min)){
                min = distance;
                vertex = v;
            }
        }

        return vertex;
    }

    /* -------------------------------------------------
     *                      CULS DE SAC
     * ------------------------------------------------- */

    /**
     * @return nombre de culs-de-sac trouvés dans le labyrinthe.
     */
    public int nbDeadEnds(){
        deadEnds = new ArrayList<>();
        flood_fill(entry, new ArrayList<>());

        return deadEnds.size();
    }

    /**
     * Grâce à l'algorithme de remplissage par diffusion, récupère tous
     * les culs-de-sac du graphe de façon récursive.
     *
     * @param v sommet qu'on étudie.
     * @param visited liste des sommets déjà visités (pour éviter les
     *                boucles infinies).
     */
    private void flood_fill(int v, List<Integer> visited){
        List<Edge> corridors = adjOnlyCorridors[v];
        int cpt = 0;

        if (v != exit) {
            visited.add(v);
            if (!corridors.isEmpty()) {
                for (Edge e : corridors) {
                    int neighbor = e.other(v);

                    if (!adjOnlyCorridors[neighbor].isEmpty()) {
                        cpt++;
                        if (!visited.contains(neighbor))
                            flood_fill(neighbor, visited);
                    }
                }

                if (cpt == 1)
                    deadEnds.add(v);
            }
        }
    }
}
