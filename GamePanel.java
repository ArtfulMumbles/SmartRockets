import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements ActionListener{
    private Timer timer;
    private final int DELAY = 5;
    private GeneticAlgorithm genetic;
    private boolean running;
    private int count;

    public GamePanel() {
        this.genetic = new GeneticAlgorithm(0, 0);
        initPanel();
    }

    public void initPanel() {
        count = 0;
        running = true;
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(1000, 800));
        System.out.println("-----INIT-----");

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        genetic.drawRockets(g2d);
        drawTargetRed(g2d, 50);
        genetic.drawNoGo(g2d);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running && count > 10) {
            count = 0;
            if(genetic.updateRockets()) {
                running = false;
            }
        
 
        }   
        count++;
        genetic.moveRockets();
        repaint();
    }

    public void drawTargetRed(Graphics2D g) {
        int width = 0;
        g.setColor(Color.red);  
        g.fillOval((int) genetic.target.x, (int) genetic.target.y, 50, 50);
        if(width < 50) {
            drawTargetWhite(g, width + 10);
        }
        
    }

    public void drawTargetRed(Graphics2D g, int width) {
        g.setColor(Color.red);  
        g.fillOval((int) genetic.target.x, (int) genetic.target.y, 50, 50);
        if(width < 50) {
            drawTargetWhite(g, width + 10);
        }
        
    }

    public void drawTargetWhite(Graphics2D g, int width) {
        g.setColor(Color.white);  
        g.fillOval((int) genetic.target.x, (int) genetic.target.y, 50, 50);
        if(width < 50) {
            drawTargetRed(g, width + 10);
        }
        
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

        }
    }
    
}