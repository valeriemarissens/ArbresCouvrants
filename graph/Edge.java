package graph;

/**
 * Représente une arête non orientée.
 */
public class Edge
{
    /**
     * Sommet d'où l'on vient.
     */
    private final int from;

    /**
     * Sommet où l'on va.
     */
    private final int to;

    /**
    * Vrai si cette arête est utilisée dans l'arbre couvrant.
    */
    boolean used;

    public Edge(int x, int y){
        this.from = x;
        this.to = y;
        this.used = false;
    }

    /**
     * @param v sommet.
     * @return le sommet à l'autre extrémité de l'arête
     */
    public final int other(int v)
    {
	    if (this.from == v) return this.to; else return this.from;
    }

    public void mark(boolean b){
        this.used = b;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public boolean isUsed() {
        return used;
    }

    @Override
    public String toString() {
        return "[" + from + "-" + to + "]";
    }
}
