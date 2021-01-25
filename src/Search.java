
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Frederick Pu
 */
public class Search {
  // returns a tree which is the shortest path from target to root
  public static Node bfs(Node root, Node target){
    ArrayList<Node> queue = new ArrayList<Node>();
    ArrayList<Node> visited = new ArrayList<Node>();

    queue.add(root);
    visited.add(root);

    while(!queue.isEmpty()){

      Node currentNode = queue.remove(0);
      //System.out.println(currentNode);
      
      for (Node child: currentNode.getChildren()){
        // set the Parent of child to currentNode
        child.setParent(currentNode);
        // we've found the shortest path
        if (child.equals(target))
          return child;
        
        // add it to the end of the queue
        if (!inVisited(child, visited)){
          visited.add(child);
          queue.add(child);
        }
        
      }

    }

    return new Node(new int[0][0]);
  }
  
  private static boolean inVisited(Node n, ArrayList<Node> visited){
      for (Node v: visited){
          if (v.equals(n))
              return true;
      }
      
      return false;
  }
  
}
