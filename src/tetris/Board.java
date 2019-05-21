/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author alu20925322z
 */
public class Board extends JPanel{
    
    public static final Color COLORS[] = { new Color(30, 30, 30),
        new Color(204, 102, 102),
        new Color(102, 204, 102), new Color(102, 102, 204),
        new Color(204, 204, 102), new Color(204, 102, 204),
        new Color(102, 204, 204), new Color(218, 170, 0)
        };
    public static int NUM_ROWS = 22;
    public static int NUM_COLS = 10;
            
    private Tetrominoes[][] board;
    private Shape currentShape;
    private int currentRow;
    private int currentCol;
    private static final int INITIAL_ROW = -2;
    
    private ScoreBehaviour scoreBehaviour;
    
    private Timer timer;
    private int deltaTime;
    private MusicPlayer music = new MusicPlayer();
    private boolean isArcadeMode = false;
    private NextPiecePanel nextPiece;
    
    
    public void setScoreBehaviour(ScoreBehaviour scoreBehaviour){
        this.scoreBehaviour = scoreBehaviour;
    }
    
    public void setNextPiecePanel(NextPiecePanel nextPiece){
        this.nextPiece = nextPiece;
    }
    
    public Board(){
        super();
        askSkillLevel();
        askNumColRow();
        board = new Tetrominoes[NUM_ROWS][NUM_COLS];
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                board[row][col] = Tetrominoes.NoShape;
            }
        }
        currentShape = new Shape();
        currentRow = INITIAL_ROW;
        currentCol = NUM_COLS / 2;
        MyKeyAdapter keyAdepter = new MyKeyAdapter();
        addKeyListener(keyAdepter);
        setFocusable(true);
        
        music.playMusic();
        nextPiece = new NextPiecePanel();
        timer = new Timer (deltaTime, new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                mainLoop();
            }
        });
        timer.start();
    }
    
    public void mainLoop(){
        moveDown();
        
    }
    
    public void askSkillLevel(){
        String[] options = {"Easy", "Normal", "Hard", "Hell", "Arcade"};
        int n = JOptionPane.showOptionDialog(null, "Choose a Skill level suited for you", "Skill Level", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        System.out.println(n);
        switch(n){
            case 0: 
                deltaTime = 1000;
                isArcadeMode = false;
                break;
            case 1:
                deltaTime = 500;
                isArcadeMode = false;
                break;
            case 2:
                deltaTime = 250;
                isArcadeMode = false;
                break;
            case 3:
                deltaTime = 90;
                isArcadeMode = false;
                break;
            case 4:
                deltaTime = 900;
                isArcadeMode = true;
                break;
        }
        
    }
    
   public void startNewGame(){
       askSkillLevel();
       for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                board[row][col] = Tetrominoes.NoShape;
            }
        }
        currentShape = new Shape();
        currentRow = INITIAL_ROW;
        currentCol = NUM_COLS / 2;
        scoreBehaviour.reset();
        
        
        timer.setDelay(deltaTime);
        timer.start();
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
        drawBoard(g2d);
        drawCurrentShape(g2d);
    }

    private void drawCurrentShape(Graphics2D g2d) {
        for (int i = 0; i <= 3; i++) {
            drawSquare(g2d, currentRow + currentShape.getY(i), currentCol + currentShape.getX(i), currentShape.getShape());
        }
    }

    private void drawBoard(Graphics2D g2d) {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                drawSquare(g2d, row, col, board[row][col]);
            }
        }
    }

    public void moveLeft(){
        currentCol--;
        System.out.println("Min x: " + currentShape.minX());
        System.out.println("Max x: " + currentShape.maxX());
    }
    
    public void moveRight(){
        currentCol++;
    }
    
    public void moveDown(){
        if(!collisions(currentRow + 1)){
            currentRow++;
        } else {
            getShapeToBoard();
            int[] row = checkLine();
            deleteLine(row);
            
        }
        repaint();
        System.out.println(currentRow + currentShape.maxY());
        System.out.println(NUM_ROWS - 1);
    }
    
     public int[] checkLine(){
        int rowArray[] = new int[NUM_ROWS];
        Arrays.fill(rowArray, 0);
        int arrayCount = 0;
        for (int row = 0; row < NUM_ROWS; row++) {
            int count = 0; 
            for (int col = 0; col < NUM_COLS; col++) {
                if(board[row][col] != Tetrominoes.NoShape){
                    count++;
                }
            }
            if (count == NUM_COLS){
                System.err.println("Linia nº " + row + " completa");
                rowArray[arrayCount] = row;
                arrayCount++;
            }
        }
        return rowArray;
    }
    
    public void deleteLine(int[] rowArray){
        for (int i = 0; i < rowArray.length; i++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if(rowArray[i] != 0){
                    board[rowArray[i]][col] = Tetrominoes.NoShape;
                    scoreBehaviour.increment(1);
                    if(isArcadeMode){
                        arcadeModeChangeDeltaTime();
                    }
                }
            }
            moveLines(rowArray[i]);
        }
    }
    
    public void moveLines(int rowDeleted){
        for (int row = rowDeleted; row > 1; row--) {
            for (int col = 0; col < NUM_COLS; col++) {
                board[row][col] = board[row-1][col];
            }
        }
    }
    
    private boolean canMove(Shape shape, int newCol) {
        if (newCol + shape.minX() < 0) {
            return false;
        }
        if (newCol + shape.maxX() > NUM_COLS -1) {
            return false;            
        }
        for (int i=0; i<=3; i++) {
            int row = currentRow + shape.getY(i);
            int col = newCol + shape.getX(i);
            if (row>=0) {
                if (board[row][col] != Tetrominoes.NoShape) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean collisions(int newRow){
        if(newRow + currentShape.maxY() >= NUM_ROWS){
            return true;
        } else {
            for (int i = 0; i <= 3; i++){
                int row = newRow + currentShape.getY(i);
                int col = currentCol + currentShape.getX(i);
                if(row >= 0){
                    if (board[row][col] != Tetrominoes.NoShape){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void getShapeToBoard() {
        for (int i = 0; i <= 3; i++) {
            try{
            board[currentRow + currentShape.getY(i)][currentCol + currentShape.getX(i)] = 
                    currentShape.getShape(); 
            } catch (ArrayIndexOutOfBoundsException ex){
                gameOver();
                return;
            }
        }
        
        currentShape = nextPiece.getNextPiece();
        currentRow = INITIAL_ROW;
        currentCol = NUM_COLS / 2;
        nextPiece.setNextPiece();
    }
    
    private void rotateCurrentShape(){
        Shape rotatedShape = currentShape.rotateRight();
        if (canMove(rotatedShape, currentCol)){
            currentShape = rotatedShape;
        }
    }

    private void arcadeModeChangeDeltaTime() {
        switch(scoreBehaviour.getScore()){
            case 10:
                deltaTime = 850;
                timer.setDelay(deltaTime);
                break;
            case 20:
                deltaTime = 800;
                timer.setDelay(deltaTime);
                break;
            case 30:
                deltaTime = 750;
                timer.setDelay(deltaTime);
                break;
            case 40:
                deltaTime = 700;
                timer.setDelay(deltaTime);
                break;
            case 50:
                deltaTime = 650;
                timer.setDelay(deltaTime);
                break;
            case 60:
                deltaTime = 600;
                timer.setDelay(deltaTime);
                break;
            case 70:
                deltaTime = 550;
                timer.setDelay(deltaTime);
                break;
            case 80:
                deltaTime = 500;
                timer.setDelay(deltaTime);
                break;
            case 90:
                deltaTime = 450;
                timer.setDelay(deltaTime);
                break;
            case 100:
                deltaTime = 400;
                timer.setDelay(deltaTime);
                break;
            case 110:
                deltaTime = 350;
                timer.setDelay(deltaTime);
                break;
            case 120:
                deltaTime = 300;
                timer.setDelay(deltaTime);
                break;
            case 130:
                deltaTime = 250;
                timer.setDelay(deltaTime);
                break;
            case 140:
                deltaTime = 200;
                timer.setDelay(deltaTime);
                break;
            case 150:
                deltaTime = 150;
                timer.setDelay(deltaTime);
                break;
            case 160:
                deltaTime = 100;
                timer.setDelay(deltaTime);
                break;
            case 170:
                deltaTime = 50;
                timer.setDelay(deltaTime);
                break;
        
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(null,
            "Game Over.\nYour score has been " + scoreBehaviour.getScore());
        startNewGame();
    }

    private void askNumColRow() {
        int resp = JOptionPane.showConfirmDialog(null, "Do you wish to modify the number of rows and columns?", "Modify columns and rows", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null);
        System.out.println(resp);
        if(resp == 0){
            String rowStr = JOptionPane.showInputDialog(null, "Write the number of rows");
            String colStr = JOptionPane.showInputDialog(null, "Write the number of columns");
            NUM_ROWS = Integer.parseInt(rowStr);
            NUM_COLS = Integer.parseInt(colStr);
        } 
    }

    
    
    
    class MyKeyAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (canMove(currentShape, currentCol - 1)){
                    moveLeft();
                }
            break;
            case KeyEvent.VK_RIGHT:
                if (canMove(currentShape, currentCol + 1)){
                    moveRight();
                }
            break;
            case KeyEvent.VK_UP:
                rotateCurrentShape();
            break;
            case KeyEvent.VK_DOWN:
                moveDown();
            break;
            case KeyEvent.VK_P:
                pauseGame();
            break;
            case KeyEvent.VK_SPACE:
                instantDropDown();
            default:
            break;
        }
        repaint();
        System.out.println("Current Col: " + currentCol);
    }

        private void pauseGame() {
            timer.stop();
            JOptionPane.showMessageDialog(null,
            "Paused.\nPress Okay to resume");
            timer.start();
        }

        private void instantDropDown() {
            for (int row = 0; row < NUM_ROWS; row++) {
                if(!collisions(row)){
                    currentRow = row;
                }
            }
        } 
       
    }
     public void stopTimer(){
            timer.stop();
        }
        
        public void resumeTimer(){
            timer.start();
        }
}
