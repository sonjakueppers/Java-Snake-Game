package com.zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;
import java.util.*;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 250;
    private final int B_HEIGHT = B_WIDTH;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = B_WIDTH/DOT_SIZE;
    

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private final int DELAY = 175;
    private int dots;
    private int apple_x;
    private int apple_y;
    private ArrayList<Integer> pApples_x = new ArrayList<Integer>();
    private ArrayList<Integer> pApples_y = new ArrayList<Integer>();
    private int score = 0;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image pApple;
    private Image blueSquare;
    private BufferedImage backGround = new BufferedImage (B_WIDTH, B_HEIGHT, BufferedImage.TYPE_INT_RGB);
    private Graphics2D blueBox;
    private Image head;
    private int difficulty = 1;
    
    //button-related
    private JButton restartButton = new JButton("Replay");
    

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
        blueBox = (Graphics2D)backGround.getGraphics();
        getGrid();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();
        
        ImageIcon iip = new ImageIcon("src/resources/pApple.png");
        pApple = iip.getImage();
        ImageIcon iib = new ImageIcon("src/resources/blueSquare.png");
        blueSquare = iib.getImage();
    }

    private void getGrid(){
    	for(int i = 0; i < RAND_POS; i++) {
    		for(int j = 0; j < RAND_POS; j++) {
        		int posI = i * 10;
        		int posJ = j * 10;
        		blueBox.drawImage(blueSquare, posI, posJ, this);
        				
        	}
    	}
    }
    
    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * DOT_SIZE;
            y[z] = 50;
        }
        locatepApple();
        locateApple();
        
        

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

        	g.drawImage(backGround, 0, 0, this);
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < pApples_x.size(); i++) {
            	g.drawImage(pApple, pApples_x.get(i), pApples_y.get(i), this);
            }
           

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        }else {

            gameOver(g);
            //button-related
            add(restartButton);
            restartButton.addActionListener(this);
            revalidate();
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over - Too Bad!!!";
        String msg1 = "Score: " + score;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        Font medium = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 3);
        g.setFont(medium);
        g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            score++;
            timer.setDelay(timer.getDelay() - difficulty);
            locateApple();
            try {
            	Sound.playAppleSound();
            } catch (Exception e) {        	
            }
            if (dots % 3 == 0) {
            	locatepApple();
            }
        }
    }
    private void checkpApple() {
    	
    	for(int i = 0; i < pApples_x.size(); i++) {
	    	if ((x[0] == pApples_x.get(i)) && (y[0] == pApples_y.get(i))) {
	    		inGame = false;
	    		try {
	            	Sound.playpAppleSound();
	            } catch (Exception e) {	
	            }
	    	}
    	}	
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        else if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        else if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        else if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {
        	if ((x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        else if (y[0] < 0) {
            inGame = false;
        }

        else if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        else if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
           	try {
				Sound.playGameOverSound();
			} catch (Exception e) {
			}

        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
        for (int i = 0; i < pApples_x.size(); i++) {
	        if (pApples_x.get(i) == apple_x && pApples_y.get(i) == apple_y) {
	        	if (apple_x + DOT_SIZE > B_WIDTH - DOT_SIZE) {
	        		apple_x -= DOT_SIZE;
	        	}
	        	else { 
	        		apple_x =+ DOT_SIZE;
	        	}
        }
       }
    }
    private void locatepApple() {
    	
    	int r = (int) (Math.random() * RAND_POS);
        pApples_x.add((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        pApples_y.add ((r * DOT_SIZE));
        int size = pApples_x.size() - 1;
        	
        if (pApples_x.get(size) == apple_x && pApples_y.get(size) == apple_y) {
        	if (pApples_x.get(size) + DOT_SIZE > B_WIDTH - DOT_SIZE) {
        		pApples_x.set(size, pApples_x.get(size) - DOT_SIZE);
        	}
        	else { 
        		pApples_x.set(size, pApples_x.get(size) + DOT_SIZE);
        	}
       }
        
    }
    
    private void replayGame() {
    	remove(restartButton);
    	restartButton.removeActionListener(this);
    	leftDirection = false;
    	rightDirection = true;
    	upDirection = false;
    	downDirection = false;
    	inGame = true;
    	score = 0;
    	pApples_x.clear();
    	pApples_y.clear();
    	revalidate();
    	initGame();
    }
    
    
    

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame && e.getSource() != restartButton) {
        	
        	checkpApple();
            checkApple();
            checkCollision();
            move();
        }
        
        if (!inGame) {
        	if (e.getSource() == restartButton) {
        		replayGame();
        	}
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            

            if ((key == KeyEvent.VK_LEFT) && (!((x[0] - DOT_SIZE) == x[1])) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            else if ((key == KeyEvent.VK_RIGHT) && (!((x[0] + DOT_SIZE) == x[1])) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            else if ((key == KeyEvent.VK_UP) && (!((y[0] - DOT_SIZE) == y[1])) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            else if ((key == KeyEvent.VK_DOWN) && (!((y[0] + DOT_SIZE) == y[1])) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
