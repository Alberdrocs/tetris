/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static tetris.Board.COLORS;

/**
 *
 * @author alu20925322z
 */
public class NextPiecePanel extends javax.swing.JPanel {

    
    public static final int NUM_COLS = 9;
    public static final int NUM_ROWS = 5;
    
    private Shape shape;
    
    /**
     * Creates new form NextPiecePanel
     */
    public NextPiecePanel() {
        initComponents();
        shape = new Shape();
    }
    
    public Shape getNextPiece(){
        return shape;
    }
    
    public void setNextPiece(){
        shape = new Shape();
        repaint();
    }
    
    private int squareWidth(){
        return getWidth() / NUM_COLS;
    }
    
    private int squareHeight() {
        return getHeight() / NUM_ROWS;
    }
    
    
    private void drawSquare(Graphics g, int row, int col, Tetrominoes shape) {
        int x = col * squareWidth();
        int y = row * squareHeight();
        Color color = COLORS[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2,
        squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
        x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1,
        y + squareHeight() - 1,
        x + squareWidth() - 1, y + 1);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawCurrentShape(g2d);
    }

    private void drawCurrentShape(Graphics2D g2d) {
        for (int i = 0; i <= 3; i++) {
            drawSquare(g2d, (NUM_ROWS/2) + shape.getY(i), (NUM_COLS/2) + shape.getX(i), shape.getShape());
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
