
import java.awt.*;
import java.awt.event.*;
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
public class Settings extends JFrame implements ActionListener{
    // instance variables
    private JTextField sizeField;
    private JLabel sizeLabel;
    private JPanel sizePanel;
        
    private JTextField scrambleField;
    private JLabel scrambleLabel;
    private JPanel scramblePanel;
    
    private JPanel donePanel;
    private JButton done;
    
    private JLabel errorLabel; 
    private JPanel errorPanel;
    
    // constructor
    public Settings(){
        super("Settings");
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	} catch (Exception e) {
            System.out.println("ERROR: " + e);
	}
        
        sizeField = new JTextField(5);
        sizePanel = new JPanel();
        sizeLabel = new JLabel("Size: ");
        
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeField);
        
        scrambleField = new JTextField(5);
        scramblePanel = new JPanel();
        scrambleLabel = new JLabel("Scramble: ");
        
        scramblePanel.add(scrambleLabel);
        scramblePanel.add(scrambleField);
        
        done = new JButton("Done");
        done.addActionListener(this);
        donePanel = new JPanel();
        
        donePanel.add(done);
        
        errorLabel = new JLabel("Invalid Input");
        errorLabel.setVisible(false);
        
        errorPanel = new JPanel();
        errorPanel.add(errorLabel);
        
        setLayout(new GridLayout(4, 1));
        
        this.add(sizePanel);
        this.add(scramblePanel);
        this.add(donePanel);
        this.add(errorPanel);
        
        setSize(300, 150);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    // instance methodes
    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            int size = Integer.parseInt(sizeField.getText());
            int scramble = Integer.parseInt(scrambleField.getText());
            
            if (1 < size && size < 10 && 0 < scramble && scramble < 100000){
                new Slider(size, scramble);
                this.dispose();
            }
            else
                errorLabel.setVisible(true);
        }catch (Exception ex){
            errorLabel.setVisible(true);
        }
    }
}
