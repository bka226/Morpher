/*

File: Grid
 */

import java.awt.*;
import java.io.Serializable;
import java.awt.image.RescaleOp;
import java.io.*;
import java.util.ArrayList;

public class Grid implements Serializable {
    Point[][] points = null;
    private int width, height;

    public Grid(int w, int h, int totalw, int totalh){
        super();
        width = w;
        height = h;
        points = new Point[w][h];
        for(int i = 0; i < w; i ++)
            for(int j = 0; j < h; j++)
                points[i][j] = new Point(totalw * i / (w-1), totalh * j / (h-1));
    }

    public Polygon[] polygonTriangles(){
        /* Each rectangle = 2 triangles */
        int countTrinages = (width-1) * (height-1) * 2;
        Polygon[] trinagesInSqare = new Polygon[countTrinages];

        int count = 0;
        for(int i = 0; i < width - 1; i ++){
            for(int j = 0; j < height - 1; j++) {
                Point a = points[i][j];
                Point b = points[i+1][j];
                Point c = points[i+1][j+1];
                Point d = points[i][j+1];

                int[] xpts1 = {a.x, c.x, b.x};
                int[] ypts1 = {a.y, c.y, b.y};
                Polygon first = new Polygon(xpts1, ypts1, 3);

                int[] xpts2 = {a.x, c.x, d.x};
                int[] ypts2 = {a.y, c.y, d.y};
                Polygon second = new Polygon(xpts2, ypts2, 3);

                trinagesInSqare[count] = first;
                count++;
                trinagesInSqare[count] = second;
                count++;
            }
        }

        return trinagesInSqare;
    }
}

