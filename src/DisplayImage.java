/*


File : ImageView
 */



import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DisplayImage extends JPanel implements MouseListener, MouseMotionListener {
    public transient Image photo = null;
    public String roadWay = null;
    private int widthSet, heightSet;
    private int widthMid, heightMid;
    private int movingRow, movingCol;
    private int offsetRow, offsetColumn;
    public Grid grid;


    public DisplayImage(Image photoI, String pathway, Grid mid) {
        this(photoI, pathway);
        grid = mid;
    }

    public DisplayImage(Image img, String absolutePath) {
        super();
        roadWay = absolutePath;
        photo = img;

        while (img.getWidth(this) == -1) ;
        widthSet = img.getWidth(this);

        while (img.getHeight(this) == -1) ;
        heightSet = img.getHeight(this);

        grid = new Grid(MainMorph.gridRow, MainMorph.gridColumn, widthSet, heightSet);
        widthMid = MainMorph.gridRow;
        heightMid = MainMorph.gridColumn;

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void drawGrid(Graphics g, int offsetx, int offsety) {
        this.offsetRow = offsetx;
        this.offsetColumn = offsety;

        for (int i = 0; i < widthMid; i++) {
            for (int j = 0; j < heightMid; j++) {
                Point p = grid.points[i][j];

                /* Draw connecting lines */
                g.setColor(Color.white);
                if (j != heightMid - 1) {
                    Point next = grid.points[i][j + 1];
                    g.drawLine(p.x + offsetx, p.y + offsety, next.x + offsetx, next.y + offsety);
                }
                if (i != widthMid - 1) {
                    Point next = grid.points[i + 1][j];
                    g.drawLine(p.x + offsetx, p.y + offsety, next.x + offsetx, next.y + offsety);
                }
                if (j != heightMid - 1 && i != widthMid - 1) {
                    Point next = grid.points[i + 1][j + 1];
                    g.drawLine(p.x + offsetx, p.y + offsety, next.x + offsetx, next.y + offsety);
                }

                /* Draw circles */
                int radius = 6;
                g.setColor(Color.black);
                g.fillOval(offsetx - radius + p.x, offsety - radius + p.y, 2 * radius, 2 * radius);

                radius = 3;
                g.setColor(Color.green);
                g.fillOval(offsetx - radius + p.x, offsety - radius + p.y, 2 * radius, 2 * radius);


            }
        }
    }

    public void update(Graphics g) {
        int offsetx = 20, offsety = 20;

        g.drawImage(photo, offsetx, offsety, this);
        drawGrid(g, offsetx, offsety);
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void paintComponent(Graphics g) {
        paint(g);
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX() - offsetRow;
        int y = e.getY() - offsetColumn;

        for (int i = 0; i < widthMid; i++) {
            for (int j = 0; j < heightMid; j++) {
                Point p = grid.points[i][j];
                int radius = 6;
                if (p.distance(x, y) <= radius) {
                    if (i != 0 && i != widthMid - 1 && j != 0 && j != heightMid - 1) {
                        movingRow = i;
                        movingCol = j;
                        return;
                    }
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        movingRow = movingCol = -1;
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() - offsetRow;
        int y = e.getY() - offsetColumn;

        if (movingRow != -1 && movingCol != -1) {
            grid.points[movingRow][movingCol].x = x;
            grid.points[movingRow][movingCol].y = y;
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}