
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Frederick Pu
 */
public class Slider extends JFrame implements ActionListener {

    // instance variables
    private final int SIZE;
    private final int SCRAMBLE;
    private final int RIGHT_MARGIN = 10;
    private final String EMPTY_TEXT = "";

    private JButton[][] btns;
    private JPanel gameboard, menu, menuBottom;

    private JLabel movesUI;
    private int moves = 0;

    private JLabel scoreUI;

    private JButton[][] intialState;

    private JButton playAgain, quit, settings, saveScore;
    private boolean scoreSaved = false;

    private int emptyI;
    private int emptyJ;

    private Font f = new Font("SansSeriff", Font.BOLD, 36);
    private Font f2 = new Font("SansSeriff", Font.BOLD, 10);

    // constructor
    public Slider(int size, int scramble) {
        super("Slider!!!!");

        SIZE = size;
        SCRAMBLE = scramble;

        btns = new JButton[SIZE][SIZE];
        emptyI = SIZE - 1;
        emptyJ = SIZE - 1;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }

        gameboard = new JPanel(new GridLayout(SIZE, SIZE));
        gameboard.setBackground(Color.BLACK);

        menu = new JPanel();
        menu.setBackground(Color.WHITE);
        menu.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 10));

        movesUI = new JLabel("Moves: 0");
        movesUI.setFont(f);

        scoreUI = new JLabel("Score: 0");
        scoreUI.setFont(f);

        menu.add(movesUI);
        menu.add(scoreUI);

        menuBottom = new JPanel();
        menuBottom.setBackground(Color.WHITE);
        menuBottom.setMaximumSize(new Dimension(5, 5));
        menuBottom.setVisible(false);

        playAgain = new JButton("Play Again");
        playAgain.addActionListener(this);

        quit = new JButton("Quit");
        quit.addActionListener(this);

        settings = new JButton("Settings");
        settings.addActionListener(this);
        
        saveScore = new JButton("Save Score");
        saveScore.addActionListener(this);

        menuBottom.add(playAgain);
        menuBottom.add(quit);
        menuBottom.add(settings);
        menuBottom.add(saveScore);

        setLayout(new BorderLayout());

        this.add(gameboard, BorderLayout.CENTER);
        this.add(menu, BorderLayout.NORTH);
        this.add(menuBottom, BorderLayout.SOUTH);

        int value = 1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                //btns[i][j] = new JButton(i + ", " + j);
                btns[i][j] = new JButton(Integer.toString(value));
                value++;
                btns[i][j].setBackground(Color.yellow);

                btns[i][j].setFont(f);
                btns[i][j].addActionListener(this);
                gameboard.add(btns[i][j]);
            }
        }
        // set empty button
        btns[emptyI][emptyJ].setText(EMPTY_TEXT);
        btns[emptyI][emptyJ].setBackground(Color.BLACK);
        intialState = btns;

        setSize(800 + RIGHT_MARGIN, 800);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(Color.black);

        newGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // lame version
        /*if (emptyI + 1 < SIZE && e.getSource() == btns[emptyI + 1][emptyJ])
            updateEmpty(emptyI + 1, emptyJ);
        else if (0 <= emptyI - 1 && e.getSource() == btns[emptyI - 1][emptyJ])
            updateEmpty(emptyI - 1, emptyJ);
        else if (emptyJ + 1 < SIZE && e.getSource() == btns[emptyI][emptyJ + 1])
            updateEmpty(emptyI, emptyJ + 1);
        else if (0 <= emptyJ - 1 && e.getSource() == btns[emptyI][emptyJ - 1])
            updateEmpty(emptyI, emptyJ - 1);*/

        // Note: score is only updated if the board state is change
        // Note: The player cannot modifiy the board once they have one
        if (!isWin()) {
            // all buttons which share the same i coordinate as Empty
            for (int j = 0; j < SIZE; j++) {
                if (e.getSource() == btns[emptyI][j] && e.getSource() != btns[emptyI][emptyJ]) {
                    moveRow(j);
                    updateStats();
                }
            }
            // all buttons which share the same j coordinate as Empty
            for (int i = 0; i < SIZE; i++) {
                if (e.getSource() == btns[i][emptyJ] && e.getSource() != btns[emptyI][emptyJ]) {
                    moveColumn(i);
                    updateStats();
                }
            }
        }

        if (isWin()) {
            menuBottom.setVisible(true);
        }

        if (e.getSource() == playAgain) {
            menuBottom.setVisible(false);
            newGame();
        }

        if (e.getSource() == settings) {
            this.dispose();
            new Settings();
        }

        if (e.getSource() == quit) {
            System.exit(0);
        }
        
        if (e.getSource() == saveScore && !scoreSaved) {
            scoreSaved = true;
            new ScoreBoard(computeScore());
        }
    }

    // takes in the coordinates of the new empty button
    private void updateEmpty(int i, int j) {
        // swap buttons
        btns[emptyI][emptyJ].setText(btns[i][j].getText());
        btns[emptyI][emptyJ].setBackground(btns[i][j].getBackground());

        btns[i][j].setText(EMPTY_TEXT);
        btns[i][j].setBackground(Color.BLACK);

        // update empty coordinates
        emptyI = i;
        emptyJ = j;
    }

    // scrambles the board by n iterations
    private void newGame() {
        moves = 0;
        scoreSaved = false;
        
        movesUI.setText("Moves: " + Integer.toString(moves));
        scoreUI.setText("Score: " + computeScore());
        for (int i = 0; i < SCRAMBLE; i++) {
            scramble();
        }
    }

    // scrambles the board by 1 iteration
    private void scramble() {
        // array of possible coordinates for the new empty button
        ArrayList<int[]> coors = new ArrayList<>();

        if (emptyI + 1 < SIZE) {
            coors.add(new int[]{emptyI + 1, emptyJ});
        }
        if (0 <= emptyI - 1) {
            coors.add(new int[]{emptyI - 1, emptyJ});
        }
        if (emptyJ + 1 < SIZE) {
            coors.add(new int[]{emptyI, emptyJ + 1});
        }
        if (0 <= emptyJ - 1) {
            coors.add(new int[]{emptyI, emptyJ - 1});
        }

        // chooses coordinate of new empty button
        Random rand = new Random();
        int[] newCoor = coors.get(rand.nextInt(coors.size()));

        // updates the empty button
        updateEmpty(newCoor[0], newCoor[1]);
    }

    // Determines if the player has won the game
    private boolean isWin() {
        for (int value = 0; value < SIZE * SIZE - 1; value++) {
            if (!btns[value / SIZE][value % SIZE].getText().equals(Integer.toString(value + 1))) {
                return false;
            }
        }

        return true;
    }

    // slides the row as if physics existed
    private void moveRow(int j) {
        int change = 0;
        if (j - emptyJ > 0) {
            change = 1;
        } else {
            change = -1;
        }

        while (j - emptyJ != 0) {
            updateEmpty(emptyI, emptyJ + change);
        }
    }

    // slides the column as if physics existed
    private void moveColumn(int i) {
        int change = 0;
        if (i - emptyI > 0) {
            change = 1;
        } else {
            change = -1;
        }

        while (i - emptyI != 0) {
            updateEmpty(emptyI + change, emptyJ);
        }

    }

    private void updateStats() {
        moves++;
        movesUI.setText("Moves: " + Integer.toString(moves));
        scoreUI.setText("Score: " + computeScore());
    }

    private int computeScore() {
        // TODO: (SIZE^2)! - moves + SCRAMBLE
        return SIZE * SIZE + SCRAMBLE - moves;
    }

}
