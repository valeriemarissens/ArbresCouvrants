/*
 * Algorithm.java
 * Project
 *
 * Created by ValerieMarissens on 27/12/2020.
 * Copyright © 2020 ValerieMarissens. All rights reserved.
 */

package algorithms;

import graph.SpanningTree;

/**
 * Interface dont implémentent les 3 types d'algorithmes.
 */
public interface Algorithm {

    /**
     * Types d'algorithmes.
     */
    enum type{
        aldousBroder,
        wilson,
        kruskal
    }

    /**
     * @return un arbre couvrant en utilisant l'algorithme.
     */
    SpanningTree algo();

    String getTitle();
}
