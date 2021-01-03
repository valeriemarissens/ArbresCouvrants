package graph;

import java.util.ArrayList;
import java.io.*;
import java.awt.*;
import java.awt.image.*;

/**
 * Représente un graphe.
 */
public class Graph{
    private final ArrayList<Edge>[] adj;
    private final int[] coordX;
    private final int[] coordY;
    private final int V;

    /**
     *
     * @param N nombre de sommets.
     */
    @SuppressWarnings("unchecked")
    public Graph(int N){
        this.V = N;
        adj = (ArrayList<Edge>[]) new ArrayList[N];
        for (int v= 0; v < N; v++)
            adj[v] = new ArrayList<>();
        coordX = new int[N];
        coordY = new int[N];
        for (int v= 0; v < N; v++)
            coordX[v] = 0;
        for (int v= 0; v < N; v++)
            coordY[v] = 0;
    }

    public int vertices()
    {
        return V;
    }

    public void setCoordinate(int i, int x, int y){
        coordX[i] = x;
        coordY[i] = y;
    }

    /**
     * Ajoute une arête au graphe.
     *
     * @param e arête.
     */
    public void addEdge(Edge e){
        int v = e.getFrom();
        int w = e.getTo();
        adj[v].add(e);
        adj[w].add(e);
    }

    /**
     * @param v sommet.
     * @return liste des arêtes reliées à un sommet.
     */
    public ArrayList<Edge> adj(int v)
    {
        return new ArrayList<>(adj[v]);
    }

    /**
     * @return toutes les arêtes du graphe.
     */
    public ArrayList<Edge> edges(){
        ArrayList<Edge> list = new ArrayList<>();
        for (int v = 0; v < V; v++)
            for (Edge e : adj(v)) {
                if (e.getFrom() == v)
                    list.add(e);
            }
        return list;
    }

    /**
     * @return un petit graphe de graph.Test (G1).
     */
    static Graph example(){
        Graph g = new Graph(4);
        g.setCoordinate(0, 100,100);
        g.setCoordinate(1, 300,300);
        g.setCoordinate(2, 300,100);
        g.setCoordinate(3, 100,300);
        g.addEdge(new Edge(0,1));
        g.addEdge(new Edge(0,2));
        g.addEdge(new Edge(0,3));
        g.addEdge(new Edge(1,2));
        g.addEdge(new Edge(1,3));
        return g;
    }

    /**
     * @param n taille de la grille
     * @return une grille n x n
     */
    static Graph Grid(int n){
        Graph g = new Graph(n * n);
        int i, j;
        for (i = 0 ; i < n; i ++)
            for (j = 0 ; j < n; j ++)
                g.setCoordinate(n*i+j, 50+(300*i)/n,50+(300*j)/n);

        for (i = 0 ; i < n; i ++)
            for (j = 0 ; j < n; j ++){
                if (i < n-1)
                    g.addEdge(new Edge(n*i+j,n*(i+1)+j));
                if (j < n-1)
                    g.addEdge(new Edge(n*i+j,n*i+j+1));
            }
        return g;
    }

    public BufferedImage toImage(){
        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.fillRect(0, 0, 400, 400);
        g2d.setColor(Color.BLACK);
        BasicStroke bs = new BasicStroke(2);
        g2d.setStroke(bs);
        // dessine les arêtes
        for (Edge e: edges())
        {
            int i = e.getFrom();
            int j = e.getTo();
            if (e.isUsed())
                g2d.setColor(Color.RED);
            else
                g2d.setColor(Color.GRAY);

            g2d.drawLine(coordX[i], coordY[i], coordX[j], coordY[j]);
        }
        // dessine les sommets
        for (int i = 0; i < V; i++)
        {
            g2d.setColor(Color.WHITE);
            g2d.fillOval(coordX[i]-15, coordY[i]-15,30,30);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(coordX[i]-15, coordY[i]-15,30,30);
            g2d.drawString(Integer.toString(i), coordX[i], coordY[i]);
        }

        return image;
    }

    /**
     * Écrit un fichier dot contenant le résultat.
     *
     * @param s nom du fichier.
     */
    public void writeFile(String s){
        try
        {
            PrintWriter writer = new PrintWriter(s, "UTF-8");
            writer.println("digraph G{");
            for (Edge e: edges())
                writer.println(e.getFrom() + "->" + e.getTo() + ";");
            writer.println("}");
            writer.close();
        }
        catch (IOException e){
            System.out.println("pb write file");
        }
    }

    public void clean() {
        for (Edge e : edges()){
            e.mark(false);
        }
    }
}
