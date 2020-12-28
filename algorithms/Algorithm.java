/*
 * Algorithm.java
 * Project
 *
 * Created by ValerieMarissens on 27/12/2020.
 * Copyright © 2020 ValerieMarissens. All rights reserved.
 */

package algorithms;

import graph.SpanningTree;

public interface Algorithm {
    enum type{
        aldousBroder,
        wilson
    }

    SpanningTree algo();

    String getTitle();
}
