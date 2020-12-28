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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Labyrinth {
    private final Graph graph;
    private int entry;
    private int exit;
    private final List<Edge>[] adjOnlyCorridors;
    private final List<Integer> deadEnds;


    public Labyrinth(Graph g){
        this.graph = g;
        this.adjOnlyCorridors = (ArrayList<Edge>[]) new ArrayList[graph.vertices()];
        for (int v= 0; v < graph.vertices(); v++)
            adjOnlyCorridors[v] = new ArrayList<Edge>();
        this.deadEnds = new ArrayList<>();

        this.toLabyrinth();
        this.crossLabyrinth(entry, -1);
    }

    private void toLabyrinth(){
        // Récupère les couloirs.
        for (int v= 0; v < graph.vertices(); v++) {
            for (Edge e : graph.adj(v)){
                if (e.isUsed())
                    adjOnlyCorridors[v].add(e);
            }
        }

        // Cherche entrée et sortie.
        int size = (int) Math.sqrt(graph.vertices()); // Grid fait un new Graph(n * n)
        // n * i + j
        this.entry = size - 1;
        this.exit = size * (size - 1);

    }

    public boolean crossLabyrinth(int position, int comingFrom){ //todo: mejor Dijkstra ?
        System.out.println("pos: "+position);
        if (position == exit){
            System.out.println("ARRIVED");
            return true;
        }
        else {
            int nextpos = -1;
            List<Edge> corridors = adjOnlyCorridors[position];
            System.out.println("corridors: "+corridors.toString());

            while (nextpos == -1){
                // Prend un couloir au hasard.
                Random random = new Random();
                int index = random.nextInt(corridors.size());
                Edge e = corridors.get(index);

                System.out.println(e.toString());
                nextpos = e.other(position);

                // On ne fait pas une boucle
                if (nextpos != comingFrom){
                    comingFrom = position;
                    return crossLabyrinth(nextpos, comingFrom);
                }
                else {
                    // Le seul moyen est de faire une boucle
                    if (corridors.size() == 1) {
                        System.out.println("DEAD END : " + position + "("+deadEnds.size()+")");
                        if (!deadEnds.contains(position))
                            deadEnds.add(position);
                    }
                    else{
                        //continue mais peut créer des boucles...
                        return crossLabyrinth(comingFrom, position);
                    }
                }
            }
            return false;
        }
    }

    /*private int nbsDeadEnds(int position, int nbDeadEnds){

        for (Edge e : graph.adj(position)) {
            System.out.println(e.toString());
            if (e.isCorridor()) {
                nextpos = e.getTo();
            }
        }
        return 0;
    }*/

    /**
     * @return nombre moyen de culs de sac.
     */
    private boolean isDeadEnd(){
        boolean deadEnd = false;

        if (!crossLabyrinth(entry, -1)){
            deadEnd = true;
        }

        return deadEnd;
    }

    /**
     * @return distance entre l'entrée et la sortie.
     */
    private double distance(){
        return 0.0;
    }
}
