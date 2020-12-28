/*
 * Test.java
 * Project
 *
 * Created by ValerieMarissens on 27/12/2020.
 * Copyright © 2020 ValerieMarissens. All rights reserved.
 */

package graph;

import algorithms.Algorithm;
import algorithms.AlgorithmAldousBroder;
import algorithms.AlgorithmWilson;
import labyrinth.Labyrinth;
import utils.Histogram;
import java.io.*;
import java.util.*;


public class Test{

	/**
	 * Suppose que G est une grille de taille size x size et
	 * crée un .tex qui contient le labyrinthe correspondant.
	 *
	 * @param G graphe
	 * @param size taille
	 * @param file fichier où le labyrinthe est écrit.
	 */
    public static void printLaby(Graph G, int size, String file){
    {
		try
			{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println("\\documentclass{article}\\usepackage{tikz}\\begin{document}");
			writer.println("\\begin{tikzpicture}");

			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++)
				{
				writer.println(String.format(Locale.US, "\\begin{scope}[xshift=%dcm, yshift=%dcm]", i , j));
				writer.println("\\draw (0.1,0.1) -- (0.4,0.1);");
				writer.println("\\draw (0.6,0.1) -- (0.9,0.1);");
				writer.println("\\draw (0.1,0.9) -- (0.4,0.9);");
				writer.println("\\draw (0.6,0.9) -- (0.9,0.9);");
				writer.println("\\draw (0.1,0.1) -- (0.1, 0.4);");
				writer.println("\\draw (0.1,0.6) -- (0.1, 0.9);");
				writer.println("\\draw (0.9,0.1) -- (0.9,0.4);");
				writer.println("\\draw (0.9,0.6) -- (0.9,0.9);");
				writer.println("\\end{scope}");
				}

			/* bord */
			for (int i = 0; i < size; i++)
				{
				writer.println(String.format(Locale.US, "\\begin{scope}[xshift=%dcm, yshift=%dcm]", i , 0));
				writer.println("\\draw(0.4,0.1) -- (0.6, 0.1);");
				writer.println("\\end{scope}");
				writer.println(String.format(Locale.US, "\\begin{scope}[xshift=%dcm, yshift=%dcm]", i , size-1));
				writer.println("\\draw(0.4,0.9) -- (0.6, 0.9);");
				writer.println("\\end{scope}");
				if (i > 0)
					{
					writer.println(String.format(Locale.US, "\\begin{scope}[xshift=%dcm, yshift=%dcm]", 0 , i));
					writer.println("\\draw(0.1,0.4) -- (0.1, 0.6);");
					writer.println("\\end{scope}");

					}
				if (i < size - 1)
					{
					writer.println(String.format(Locale.US, "\\begin{scope}[xshift=%dcm, yshift=%dcm]", size -1 , i));
					writer.println("\\draw(0.9,0.4) -- (0.9, 0.6);");
					writer.println("\\end{scope}");

					}
				writer.println("\\draw (0,0.4) -- (0.1, 0.4);");
				writer.println("\\draw (0,0.6) -- (0.1, 0.6);");
				writer.println(String.format(Locale.US, "\\draw (%d, %d) ++ (0, 0.4)  -- ++ (-0.1, 0); ", size , size -1));
				writer.println(String.format(Locale.US, "\\draw (%d, %d) ++ (0, 0.6)  -- ++ (-0.1, 0); ", size , size -1));

				}


			for (Edge e: G.edges())
				{
				int i = e.getFrom() % size;
				int j = e.getFrom() / size;
				writer.println(String.format(Locale.US, "\\begin{scope}[xshift=%dcm, yshift=%dcm]", i , j));
				if (e.getTo() == e.getFrom() + size){
					/* arête verticale */
					if (!e.used)
					{
						writer.println("\\draw (0.4,0.9) -- (0.6,0.9);");
						writer.println("\\draw (0.4,1.1) -- (0.6,1.1);");
					}
					else
					{
						writer.println("\\draw (0.4,0.9) -- (0.4,1.1);");
						writer.println("\\draw (0.6,0.9) -- (0.6,1.1);");
					}
				}
				else{
					/* arête horizontale */

					if (!e.used)
					{
						writer.println("\\draw (0.9,0.4) -- (0.9,0.6);");
						writer.println("\\draw (1.1,0.4) -- (1.1,0.6);");
					}
					else
					{
						writer.println("\\draw (0.9,0.4) -- (1.1,0.4);");
						writer.println("\\draw (0.9,0.6) -- (1.1,0.6);");
					}
					}
					writer.println("\\end{scope}");
				}
			writer.println("\\end{tikzpicture}");
			writer.println("\\end{document}");
			writer.close();
			}
		catch (IOException e){}
    }    
	

	    
    }	
    
    public static void main(String[] args) {
    	// Création des graphes.
		int size = 4;
		Graph g = Graph.Grid(size);
		Graph g1 = Graph.example();

		// Test une fois
		oneTime(g, Algorithm.type.wilson, size);
		//oneTime(g, Algorithm.type.aldousBroder, size);

		// Tests un million de fois.
		//oneMillionTimes(g1, Algorithm.type.aldousBroder);
		//oneMillionTimes(g1, Algorithm.type.wilson);

		// Labyrinthes
    }

    private static void oneTime(Graph g, Algorithm.type type, int size){
    	// Lance l'algo 1 fois
		Algorithm algo;
		if (type.equals(Algorithm.type.aldousBroder))
			algo = new AlgorithmAldousBroder(g);
		else
			algo = new AlgorithmWilson(g);

		SpanningTree st = algo.algo();
		System.out.println(st.toString());

		// Affiche le graphe.
		Display d = new Display("Graphe");
		d.setImage(g.toImage());

		// Crée le labyrinthe associé
		/*System.out.println("appuyez sur une touche");
		new Scanner(System.in).nextLine();
		d.close();
		printLaby(g,size, "toto.tex");*/

		Labyrinth labyrinth = new Labyrinth(g);
	}

	/**
	 * Lance l'algorithme un million de fois et compte le nombre d'apparitions
	 * de chaque arbre couvrant. Ensuite les transforme en probabilités, avec
	 * des pourcentages.
	 * Finalement, affiche un histogramme avec les statistiques.
	 *
	 * @param g graphe.
	 */
	private static void oneMillionTimes(Graph g, Algorithm.type type){
		Algorithm algo;
		if (type.equals(Algorithm.type.aldousBroder))
			algo = new AlgorithmAldousBroder(g);
		else
			algo = new AlgorithmWilson(g);

		// Compte le nombre d'apparitions de chaque arbre couvrant
		Map<SpanningTree, Double> nbApparition = new HashMap<>();
		for (int i = 1; i <= 1000000; i++){
			SpanningTree st = algo.algo();

			if (!nbApparition.containsKey(st)) {
				double p = 1.0;
				nbApparition.put(st, p);
			}
			else{
				double p = nbApparition.get(st);
				p += 1.0;
				nbApparition.put(st, p);
			}
		}

		// Transforme le nombre d'apparitions en probabilités sous forme de pourcentage
		nbApparition.forEach((s, nbA) -> {
			double p = nbA/1000000;
			p *= 100;
			nbApparition.put(s, p);
		});

		// Affiche l'histogramme.
		Display d = new Display("Histogramme "+algo.getTitle());
		Histogram histogram = new Histogram(nbApparition);
		d.setImage(histogram.toImage());
		System.out.println(histogram.toString());
	}

	private static void thousandLabyrinhts(Algorithm.type type){
		Graph g = Graph.Grid(20);

		// Lance l'algo 1 fois
		Algorithm algo;
		if (type.equals(Algorithm.type.aldousBroder))
			algo = new AlgorithmAldousBroder(g);
		else
			algo = new AlgorithmWilson(g);

		SpanningTree st = algo.algo();
	}
} 
