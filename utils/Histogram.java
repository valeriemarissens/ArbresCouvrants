/*
 * Histogram.java
 * Project
 *
 * Created by ValerieMarissens on 27/12/2020.
 * Copyright © 2020 ValerieMarissens. All rights reserved.
 */

package utils;

import graph.SpanningTree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Histogram {
    private final Map<SpanningTree, Double> bars;
    /**
     * Décalage en X du graphique par rapport à la gauche de la fenêtre.
     */
    private static final int DEC_X = 40;

    /**
     * Décalage en Y du graphique par rapport au bas de la fenêtre.
     */
    private static final int DEC_Y = 40;

    /**
     * Décalage en X du texte au-dessus des barres de l'histogramme.
     */
    private static final int DEC_TX = DEC_X + 5;

    /** Décalage en Y du texte au-dessus des barres de l'histogramme. */
    private static final int DEC_TY = DEC_Y + 2;

    /** Décalage en hauteur de la ligne permettant de créer la flèche. */
    private static final int DEC_FH = 4;

    /** Décalage en longueur de la ligne permettant de créer la flèche. */
    private static final int DEC_FL = 8;

    /** Largeur d'une barre de l'histogramme. */
    private static final int LG_B = 40;

    /** Incrément pour calculer la hauteur des barres de l'histogramme en fonction de la valeur. */
    private static final int INCR = 15;

    public Histogram(@NotNull Map<SpanningTree, Double> probabilities){
        this.bars = probabilities;
    }

    public BufferedImage toImage(){
        int imageSize = 400;
        BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.fillRect(0, 0, imageSize, imageSize);
        BasicStroke bs = new BasicStroke(2);
        g2d.setStroke(bs);

        // Trouve la plus longe barre
        double max = Integer.MIN_VALUE;
        for (Double p : bars.values()){
            max = Math.max(max, p);
        }

        // Dessine les barres
        int x, y, x1, y1, x2, y2, width, height, i = 0;
        for (SpanningTree s : bars.keySet()){
            // Barre
            double pba = bars.get(s);
            x = DEC_X + i * (LG_B + 1);
            y = (int) (imageSize - DEC_Y - pba * INCR);
            width = LG_B;
            height = (int) (pba * INCR);
            if (i%2 == 0)
                g2d.setColor(new Color(71, 103, 219));
            else
                g2d.setColor(new Color(253, 194, 167));
            g2d.fillRect(x, y, width, height);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, width, height);

            // Valeur
            x = DEC_TX + i * (LG_B + 1);
            y = (int) (imageSize - DEC_TY - pba * INCR);
            g2d.setColor(Color.BLACK);
            double pbaRounded = Math.round(pba*100)/100d;
            g2d.drawString(String.valueOf(pbaRounded), x, y);

            i++;
        }

        // Affichage axe des abscisses.
        g2d.setColor(Color.BLACK);
        x1 = DEC_X * 2/3;
        y1 = imageSize - DEC_Y * 2/3 ;
        x2 = x1 + bars.size() * LG_B + LG_B;
        y2 = y1;
        g2d.drawLine(x1, y1, x2, y2);
        g2d.drawLine(x2, y2, x2 - DEC_FL, y2 - DEC_FH);
        g2d.drawLine(x2, y2, x2 - DEC_FL, y2 + DEC_FH);
        g2d.drawString("Arbres couvrants", x2 * 2/3, y2 + 15);

        // Affichage axe des ordonnées.
        x1 = DEC_X * 2/3;
        y1 = imageSize - DEC_Y * 2/3;
        x2 = x1;
        y2 = (int) (y1 - max * INCR * 1.5);
        g2d.drawLine(x1, y1, x2, y2);
        g2d.drawLine(x2, y2, x2 - DEC_FH, y2 + DEC_FL);
        g2d.drawLine(x2, y2, x2 + DEC_FH, y2 + DEC_FL);
        g2d.drawString("Probabilité d'apparition (%)", x2 - 5, y2 - 15);

        return image;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Valeurs détaillées de l'histogramme :\n");

        for (SpanningTree s : bars.keySet()){
            double p = bars.get(s);
            double pbaRounded = Math.round(p*100)/100d;
            stringBuilder.append(s.toString());
            stringBuilder.append(" : ");
            stringBuilder.append(pbaRounded);
            stringBuilder.append(" % \n");
        }

        return stringBuilder.toString();
    }
}
