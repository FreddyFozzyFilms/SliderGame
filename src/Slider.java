
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
    
    private final Color PEICES_COLOR;
    private final Color EMPTY_COLOR;
    
    private final int[][] TARGET;

    private JButton[][] btns;
    private JPanel gameboard, menu, menuBottom;

    private JLabel movesUI;
    private int moves = 0;
    
    private JButton revealSolution;
    private JButton nextStep;
    private JPanel solution;

    private JLabel scoreUI;

    private JButton[][] intialState;

    private JButton playAgain, quit, settings, saveScore;
    private boolean scoreSaved = false;

    private int emptyI;
    private int emptyJ;

    private Font f = new Font("SansSeriff", Font.BOLD, 30);
    private Font f2 = new Font("SansSeriff", Font.BOLD, 10);
    
    private boolean solutionRevealed = false;
    private Node currentNode;

    // constructor
    public Slider(int size, int scramble, Color peicesColor, Color emptyColor) {
        super("Slider!!!!");

        SIZE = size;
        SCRAMBLE = scramble;

        btns = new JButton[SIZE][SIZE];
        TARGET = new int[SIZE][SIZE];
        
        emptyI = SIZE - 1;
        emptyJ = SIZE - 1;
        
        PEICES_COLOR = peicesColor;
        EMPTY_COLOR = emptyColor;

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }

        gameboard = new JPanel(new GridLayout(SIZE, SIZE));
        gameboard.setBackground(EMPTY_COLOR);

        menu = new JPanel();
        menu.setBackground(Color.WHITE);
        menu.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 10));

        movesUI = new JLabel("Moves: 0");
        movesUI.setFont(f);

        scoreUI = new JLabel("Score: 0");
        scoreUI.setFont(f);
        
        revealSolution = new JButton("Reveal Solution");
        revealSolution.addActionListener(this);
        revealSolution.setFont(f);
        
        nextStep = new JButton("Next Step");
        nextStep.addActionListener(this);
        
        solution = new JPanel();
        solution.add(nextStep);
        solution.setVisible(false);

        menu.add(movesUI);
        menu.add(scoreUI);
        menu.add(revealSolution);

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
        this.add(solution, BorderLayout.EAST);

        int value = 1;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                //btns[i][j] = new JButton(i + ", " + j);
                TARGET[i][j] = value;
                
                btns[i][j] = new JButton(Integer.toString(value));
                value++;
                btns[i][j].setBackground(PEICES_COLOR);

                btns[i][j].setFont(f);
                btns[i][j].addActionListener(this);
                gameboard.add(btns[i][j]);
            }
        }
        // set empty button
        btns[emptyI][emptyJ].setText("");
        btns[emptyI][emptyJ].setBackground(EMPTY_COLOR);
        intialState = btns;

        setSize(800 + RIGHT_MARGIN, 800);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(EMPTY_COLOR);

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
        if (!isWin() && !solutionRevealed) {
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
            
            if (e.getSource() == revealSolution){
                cheat();
                solution.setVisible(true);
                menu.setVisible(false);
                solutionRevealed = true;
                scoreSaved = true; // cant save a score since you cheated
                moves += 10;
                updateStats();
            }
        }
        
        if (e.getSource() == nextStep){
            try{
                currentNode = currentNode.getParent();
                setBoard(currentNode.getBOARD());
            }catch(Exception ex){}
        }
        
        if (isWin()) {
            menuBottom.setVisible(true);
            solution.setVisible(false);
        }

        if (e.getSource() == playAgain) {
            menuBottom.setVisible(false);
            solution.setVisible(false);
            menu.setVisible(true);
            solutionRevealed = false;
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

        btns[i][j].setText("");
        btns[i][j].setBackground(EMPTY_COLOR);

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
        return SIZE * SIZE + SCRAMBLE - moves;
    }
    
    private void cheat(){
        int board[][] = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                try{
                    board[i][j] = Integer.parseInt(btns[i][j].getText());
                }catch (Exception e){
                    board[i][j] = SIZE * SIZE;
                }
                
            }
        }
        Node root = new Node(board);
        Node target = new Node(TARGET);
        currentNode = Search.bfs(target, root);
    }
    
    private void setBoard(int[][] b){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (b[i][j] == SIZE * SIZE){
                    btns[i][j].setText("");
                    btns[i][j].setBackground(EMPTY_COLOR);
                    emptyI = i;
                    emptyJ = j;
                }
                else{
                    btns[i][j].setText("" + b[i][j]);
                    btns[i][j].setBackground(PEICES_COLOR);
                }
            }
        }
    }

}
