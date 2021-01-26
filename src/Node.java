
import java.util.ArrayList;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Frederick Pu
 */
public class Node {
    // intances variable
    private Node parent;
    private final int[][] BOARD;
    
    private final int SIZE;
    
    private int emptyI;
    private int emptyJ;

    // contructor
    public Node(int[][] b) {
        BOARD = cloneArray(b);
        SIZE = BOARD.length;
        assignEmptyCoors();
    }

    // instance methodes
    public ArrayList<Node> getChildren() {
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
        
        ArrayList<Node> children = new ArrayList<>();
        for (int[] coor: coors){
            int[][] board = cloneArray(BOARD);
            board[coor[0]][coor[1]] = BOARD[emptyI][emptyJ];
            board[emptyI][emptyJ] = BOARD[coor[0]][coor[1]];
            
            Node child = new Node(board);
            children.add(child);
        }
        
        return children;
    }

    public Node getParent() {
        return parent;
    }
    public int[][] getBOARD() {
        return BOARD;
    }

    public void setParent(Node p) {
        parent = p;
    }

    public boolean equals(Node n) {
        int len = n.getBOARD().length;
        for (int i = 0; i < len; i++){
            for (int j = 0; j < len; j++){
                if (n.getBOARD()[i][j] != BOARD[i][j])
                    return false;
            }
        }
        
        return true;
    }
    
    @Override
    public String toString(){
        String out = "";
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                out += BOARD[i][j] + ",";
            }
            out += "\n";
        }
        
        return out;
    }
    
    private void assignEmptyCoors(){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (BOARD[i][j] == SIZE * SIZE){
                    emptyI = i;
                    emptyJ = j;
                    return;
                }
            }
        }
    }
    
    private static int[][] cloneArray(int[][] original){
        int[][] clone = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++){
            for (int j = 0; j < original[0].length; j++){
                clone[i][j] = original[i][j];
            }
        }
        
        return clone;
    }
}
