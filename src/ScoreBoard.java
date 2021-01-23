
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Frederick Pu
 */
public class ScoreBoard extends JFrame implements ActionListener {

    // instance variables
    private final int USER_SCORE;
    private JLabel nameLabel;
    private JTextField nameField;
    private JPanel namePanel;

    private JButton saveScore;

    private JLabel scoresLabel;
    private JPanel scoresPanel;

    // constructor
    public ScoreBoard(int score) {
        super("Score Board");

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }

        USER_SCORE = score;

        nameLabel = new JLabel("Your Name: ");
        nameField = new JTextField(7);
        namePanel = new JPanel();

        saveScore = new JButton("Save Score");
        saveScore.addActionListener(this);

        namePanel.add(nameLabel);
        namePanel.add(nameField);
        namePanel.add(saveScore);

        scoresLabel = new JLabel();
        scoresPanel = new JPanel();

        scoresPanel.add(scoresLabel);

        setLayout(new BorderLayout());
        this.add(namePanel, BorderLayout.NORTH);
        this.add(scoresPanel, BorderLayout.CENTER);

        setSize(300, 150);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        displayHighScores();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveScore) {
            updateScoreDB(nameField.getText());
            this.dispose();
        }
    }

    private void displayHighScores() {
        try {
            //Read from a text file
            FileReader fr = new FileReader("Scores.txt");
            BufferedReader br = new BufferedReader(fr);
           
            String scoreLine = br.readLine();
            
            String text = "<html>";

            int i = 0;
            while (scoreLine != null && i < 5) {
                if (i != 0){
                    int comma = scoreLine.lastIndexOf(",");
                    scoreLine = scoreLine.substring(0, comma) + ": " + scoreLine.substring(comma + 1);
                    
                    text += scoreLine + "<br>";
                }
                
                scoreLine = br.readLine();

                i++;
            }

            text += "</html>";

            br.close();

            scoresLabel.setText(text);

        } catch (IOException e) {
            System.out.println("Error in File writing");
        }
    }

    // updates the score database with player's score and displays the top scores
    private void updateScoreDB(String name) {
        try {
            //Read from a text file
            FileReader fr = new FileReader("Scores.txt");
            BufferedReader br = new BufferedReader(fr);
            
            // first line encodes the number of scores stored
            int length = Integer.parseInt(br.readLine());
            String[] scores = new String[length];

            for (int i = 0; i < length; i++) {
                scores[i] = br.readLine();
            }

            br.close();

            //Write to a text file
            FileWriter fw = new FileWriter("Scores.txt");
            PrintWriter pw = new PrintWriter(fw);

            String[] newScores = insert(scores, name + "," + USER_SCORE);

            pw.println(length + 1);
            for (String newScore : newScores) {
                pw.println(newScore);
            }
            pw.close();

        } catch (IOException e) {
            System.out.println("Error in File writing");
        }
    }

    // inserts score into the array arr
    private static String[] insert(String[] arr, String score) {
        int index = insertionIndex(arr, scoreFromStr(score));
        String[] out = new String[arr.length + 1];

        for (int i = 0; i < index; i++) {
            out[i] = arr[i];
        }

        out[index] = score;

        for (int i = index + 1; i < out.length; i++) {
            out[i] = arr[i - 1];
        }

        return out;
    }

    // gets index where the new score should be inserted
    public static int insertionIndex(String[] arr, int n) {

        if (arr.length == 1) {
            if (scoreFromStr(arr[0]) > n) {
                return 1;
            } else {
                return 0;
            }
        }

        if (arr.length == 0) {
            return 0;
        }

        int lowerBound = 0;
        int upperBound = arr.length - 1;

        // index is betweeen lowerBound and upperBound inclusive
        while (true) {
            System.out.println(lowerBound + ", " + upperBound);

            if (upperBound - lowerBound == 1) {
                if (scoreFromStr(arr[upperBound]) >= n) {
                    return upperBound + 1;
                }
                if (scoreFromStr(arr[lowerBound]) >= n && n > scoreFromStr(arr[upperBound])) {
                    return lowerBound + 1;
                }
                if (n > scoreFromStr(arr[lowerBound])) {
                    return lowerBound;
                }
            }

            int middle = (lowerBound + upperBound) / 2;

            if (n == scoreFromStr(arr[middle])) {
                return middle;
            }
            if (n < scoreFromStr(arr[middle])) {
                lowerBound = middle;
            }
            if (n > scoreFromStr(arr[middle])) {
                upperBound = middle;
            }
        }
    }

    // Gets the 9 in "Frederick,9"
    public static int scoreFromStr(String s) {
        // use lastIndexOf instead of indexOf incase the user puts a comma in their name
        return Integer.parseInt(s.substring(s.lastIndexOf(",") + 1));
    }

}
