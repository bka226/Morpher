

import javafx.scene.control.TextFormatter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.RescaleOp;
import java.io.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.*;

public class MainMorph extends JFrame {
    private ArrayList<DisplayImage> imgs = null;
    private boolean ChangeBrightnessQ = false;
    static int currentFirstImage = 1;
    static int gridRow = 11;
    static int gridColumn = 11;
    private int delayTime = 50;
    BufferedImage imageBuff;
    static JPanel lPanel;
    static JPanel rPanel;
    private float value;
    private DisplayImage view;
    ArrayList<Image> images;


    public MainMorph() {
        JPanel top = new JPanel();

        imgs = new ArrayList<DisplayImage>();

        Container c = this.getContentPane();
        JLabel instruction = new JLabel("             Select grid size, and frames. Then select brightness of first image -> add image, brightness of second image -> add image");

        JPanel leftView = new JPanel();
        leftView.setBorder(new LineBorder(Color.black));
        leftView.setLayout(new BorderLayout());
        lPanel = new JPanel();
        lPanel.setLayout(new BorderLayout());
        JSlider FirstBrightness = new JSlider(0, 100, 0);
        FirstBrightness.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                value = (float) (FirstBrightness.getValue() * 2)/100f;
                ChangeBrightnessQ = true;


            }
        });
        FirstBrightness.setBorder(BorderFactory.createTitledBorder("                                                                                                          Brightness"));
        FirstBrightness.setMajorTickSpacing(20);
        FirstBrightness.setMinorTickSpacing(5);
        FirstBrightness.setPaintTicks(true);
        FirstBrightness.setPaintLabels(true);

        JSlider SecondBrightness = new JSlider(0, 100, 0);
        SecondBrightness.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                value = (float) (SecondBrightness.getValue() * 2)/100f;
                ChangeBrightnessQ = true;


            }
        });
        SecondBrightness.setBorder(BorderFactory.createTitledBorder("                                                                                                         Brightness"));
        SecondBrightness.setMajorTickSpacing(20);
        SecondBrightness.setMinorTickSpacing(5);
        SecondBrightness.setPaintTicks(true);
        SecondBrightness.setPaintLabels(true);

        leftView.add(lPanel, BorderLayout.CENTER);
        leftView.add(FirstBrightness, BorderLayout.SOUTH);
        JPanel rightView = new JPanel();
        rightView.setBorder(new LineBorder(Color.black));
        rightView.setLayout(new BorderLayout());
        rPanel = new JPanel();
        rPanel.setLayout(new BorderLayout());
        rightView.add(rPanel, BorderLayout.CENTER);
        rightView.add(SecondBrightness, BorderLayout.SOUTH);
        top.setLayout(new GridLayout(1, 2));
        top.add(leftView);
        top.add(rightView);

        JPanel buttonPanel = new JPanel();


        JButton loadProject = new JButton("Load Project");
        loadProject.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                loadProject();
            }
        });

        JLabel Gridlbl = new JLabel("Grid Size:");

        JButton Fivebyfive = new JButton(("5x5"));
        Fivebyfive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gridRow = 6;
                gridColumn = 6;
            }
        });

        JButton Tenbyten = new JButton(("10x10"));
        Tenbyten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gridRow = 11;
                gridColumn = 11;
            }
        });

        JButton Twentybytwenty = new JButton(("20x20"));
        Twentybytwenty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gridRow = 21;
                gridColumn = 21;
            }
        });
        JButton save = new JButton("Save Project");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                saveProject();

            }
        });

        JLabel pts = new JLabel("Frames: ");
        final JTextField fieldFrames = new JTextField("20", 3);

        JButton morph = new JButton("Morph Pair");
        morph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int frames = Integer.parseInt(fieldFrames.getText());


                ArrayList<DisplayImage> views = imgs;
                ArrayList<Image> images = new ArrayList<Image>(frames);

                int i = currentFirstImage;
                Image from = views.get(0).photo;
                Grid fromMesh = views.get(0).grid;
                Image to = views.get(1).photo;
                Grid toMesh = views.get(1).grid;
                Image[] results = Morph.morph(from, fromMesh, to, toMesh, frames, "");

                for (Image im : results)
                    images.add(im);

                JOptionPane.showMessageDialog(MainMorph.this, "Morph Complete...Showing Animation");

                DisplayAnimation(images);

                SaveFiles(images);

            }
        });

        JButton Preview = new JButton("Preview Morph");

        Preview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DisplayAnimation(images);
            }
        });



        JButton newImage = new JButton("Add Image");
        newImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                addImg();
                UpdatePanel(view);

            }
        });


        buttonPanel.add(loadProject);
        buttonPanel.add(save);
        buttonPanel.add(morph);
       // buttonPanel.add(Preview);
        buttonPanel.add(newImage);
        buttonPanel.add(Gridlbl);
        buttonPanel.add(Fivebyfive);
        buttonPanel.add(Tenbyten);
        buttonPanel.add(Twentybytwenty);
        buttonPanel.add(pts);
        buttonPanel.add(fieldFrames);
        buttonPanel.add(instruction);

        c.add(buttonPanel, BorderLayout.SOUTH);
        c.add(top);

        top.setPreferredSize(new Dimension(100, 550));
        buttonPanel.setPreferredSize(new Dimension(100, 60));

    }


    private void addImg() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setCurrentDirectory(new java.io.File("."));

        int returnVal = chooser.showOpenDialog(MainMorph.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                imageBuff = ImageIO.read(file);
            } catch (IOException e1) { };

            String fname = file.getAbsolutePath();

            if(ChangeBrightnessQ == true)

                ChangeBrightness(imageBuff);

            DisplayImage view = new DisplayImage(imageBuff, fname);

            imgs.add(view);

        }
    }

    public BufferedImage ChangeBrightness(BufferedImage BrightImage){

        RescaleOp rescaleOp = new RescaleOp(value, 15, null);
        rescaleOp.filter(BrightImage, BrightImage);
        return BrightImage;


    }

    public void UpdatePanel(DisplayImage view) {
        if (imgs.indexOf(view) != imgs.size() - 1) {

            lPanel.removeAll();
            rPanel.removeAll();

            lPanel.add(imgs.get(imgs.indexOf(view) + 1), BorderLayout.CENTER);
            rPanel.add(imgs.get(imgs.indexOf(view) + 2), BorderLayout.CENTER);

            lPanel.doLayout();
            rPanel.doLayout();

            lPanel.validate();
            rPanel.validate();

            repaint();
            currentFirstImage = imgs.indexOf(view);

        }
    }

    private void loadProject(){
        try {
            JFileChooser chooser = new JFileChooser();
            if(chooser.showOpenDialog(MainMorph.this) != JFileChooser.APPROVE_OPTION)
                return;
            File inputFile = chooser.getSelectedFile();

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));

            int numViews = ois.readInt();
            gridRow = ois.readInt();
            gridColumn = ois.readInt();
            ArrayList<DisplayImage> views = new ArrayList<DisplayImage>(numViews);
            for(int i = 0; i < numViews; i++){
                String fname = (String) ois.readObject();
                Grid mesh = (Grid) ois.readObject();
                views.add(new DisplayImage(Toolkit.getDefaultToolkit().createImage(fname), fname, mesh));
            }

            MainMorph test  = new MainMorph();
            test.UpdatePanel(view);

            ois.close();

            MainMorph.this.dispose();


        } catch (Exception e) { e.printStackTrace(); }
    }


    private void saveProject(){
        try {
            JFileChooser chooser = new JFileChooser();
            if(chooser.showSaveDialog(MainMorph.this) != JFileChooser.APPROVE_OPTION)
                return;
            File outputFile = chooser.getSelectedFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));

            ArrayList<DisplayImage> images = imgs;
            oos.writeInt(images.size());
            oos.writeInt(gridRow);
            oos.writeInt(gridColumn);
            for(DisplayImage view : images){
                oos.writeObject(view.roadWay);
                oos.writeObject(view.grid);
            }

            oos.flush();
            oos.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void SaveFiles(final ArrayList<Image> frames){



    }


    public void DisplayAnimation(final ArrayList<Image> frames) {

          /* Image size */
        int width, height;
        Image first = frames.get(0);

        while (first.getWidth(null) == -1) ;
        width = first.getWidth(null);

        while (first.getHeight(null) == -1) ;
        height = first.getHeight(null);

        JFrame display = new JFrame("Preview Animation");
        JPanel labels = new JPanel();

        labels.add(Box.createHorizontalGlue());
        labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));
        labels.add(Box.createHorizontalGlue());

        JPanel center = new FrameDisplay(frames);
        display.getContentPane().add(center, BorderLayout.CENTER);
        display.getContentPane().add(labels, BorderLayout.SOUTH);


        display.setSize(width + 50, height + 50);
        display.setVisible(true);
    }

    public static void main(String[] argv) {

        JFrame frame = new MainMorph();
        frame.setSize(1500, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static class FrameDisplay extends JPanel {
        private ArrayList<Image> images;
        private int frame = 0;
        private Timer Animation;
        private int delay = 100;

        public FrameDisplay(ArrayList<Image> images) {
            super(true);
            this.images = images;

            Animation = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    repaint();
                }
            });
            Animation.start();
        }

        public void paint(Graphics g) {
            if (frame >= images.size() - 1)
                frame = 0;

            frame++;
            Image currentFrame = images.get(frame);
            g.drawImage(currentFrame, 30, 15, this);
        }
    }
}



