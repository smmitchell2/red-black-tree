import java.io.*;
import java.util.Scanner;

public abstract class Trees{
  static class BST extends Trees{
     class Node{ //contains left and right child of current node and key value
      String key = null;
      String color = "red";
      Node left = null;
      Node right = null;
      Node parent = null;
      int freq = 0;

      public Node(String item){
        key = item;
        left = right = parent = null;
      }
    }

     Node root;//root of BST

    BST(){//constructor
      root = null;
    }

    Node insert(String key){
      root = insertRec(root,key);
      parent(key);
      return root;
    }
// make another argument which will be the parent
    Node insertRec(Node root,String key){// a recursive func to insert a new key in BST
      if(root == null){
        root = new Node(key);
        root.freq = 1;
        return root;
      }
      int b = (key).compareTo(root.key);
      if(b < 0){
        root.left = insertRec(root.left,key);
      }
      else if (b > 0){
        root.right = insertRec(root.right,key);
      }
      else if (b==0){
        root.freq = root.freq + 1;
      }
      return root;
    }

    Node treeMax(Node max){
      while(max.right != null){max = max.right;}
      return max;
    }

    Node treeMin(Node min){
      while(min.left != null){min = min.left;}
      return min;
    }

    Node successor(Node child){
      if (child.right != null){return treeMin(child.right);}
      Node y = child.parent;
      while(y != null && child == y.right){
        child = y;
        y = y.parent;
      }
      return y;
    }

    Node predecessor(Node child){
      if (child.left != null) {return treeMax(child.left);}
      Node y = child.parent;
      while(y != null && child == y.left){
        child = y;
        y = y.parent;
      }
      return y;
    }

    void delete(Node root){
      if(root.left == null || root.right == null){
        root = null;
      }
      else if(root.left == null || root.right != null ){
        root = root.right;
      }
      else if(root.right == null || root.left != null){
        root = root.left;
      }
      else if(root.right != null || root.left != null){
        root = root.left;
      }
    }

    void inorder(){
      inorderRec(root);
    }

    void inorderRec(Node root){
      if(root != null){
        inorderRec(root.left);
        System.out.println(root.key);
        inorderRec(root.right);
      }
    }

    void parent(String key) {root = findParent(root,key,root);}

    Node findParent(Node root,String key, Node Parent){
      if (root == null) {return root;}
      int result = key.compareTo(root.key);

      if (result == 0) {root.parent = Parent;}
      if (result > 0) {root.right = findParent(root.right,key,root);}
      if (result < 0) {root.left = findParent(root.left,key,root);}

      return root;
    }
    /* Returns the node that i'm looking for */
    /* Its used with the insertionFixUp to make the red black tree */
    Node find(String key, Node root){
      int b = (key).compareTo(root.key);

      if (root==null || b==0){return root;}
    // val is less than root's key
      if (b<0){return find(key, root.left);}
    // val is greater than root's key
      if(b>0){return find(key, root.right);}
      return root;
    }

    String checkInsert(String word){
      String fw = "";
      for(int i = 0; i<word.length();++i){
        char c = word.charAt(i);
        if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
          fw += c;
        }
      }
      return fw.toLowerCase();
    }
  }

  static class RBT extends BST{
    RBT(){
      root = null;
    }

    void insertionFixUp(Node newNode){
      Node uncle = uncle(newNode);
      while(("red").equals(newNode.parent.color)){
        if (newNode == root){break;}
        if(("black").equals(newNode.parent.color)){break;}
        if(uncle != null && ("red").equals((uncle.color)) ){
          newNode.parent.color = "black";
          uncle.color = "black";//uncle
          (grandparent(newNode)).color = "red";//grandparent
        }
        else{
          if(isLinear(newNode) == false){
            Node temp = newNode;
            rightOrLeftRotate(newNode);
            newNode = newNode.parent;
            newNode.parent = temp;
          }
          newNode.parent.color = "black";
          newNode.parent.parent.color = "red";
          rightOrLeftRotate(newNode.parent);
        }
      }
      root.color = "black";
    }

    void rotateLeft(Node newNode){
      Node x = newNode.parent;
      Node y = x.right;
      x.right = y.left;

      if(y.left != null){y.left.parent = x;}

      y.parent = x.parent;

      if(x.parent == null){root = y;}
      else if (x == x.parent.left){x.parent.left = y;}
      else{x.parent.right = y;}

      y.left = x;
      x.parent = y;
    }

    void rotateright(Node newNode){
      Node y = newNode.parent;
      Node x = y.left;
      y.left = x.right;

      if(x.right != null){x.right.parent = newNode;}

      x.parent = y.parent;

      if(y.parent == null){root = x;}
      else if (y == y.parent.right){y.parent.right = x;}
      else{y.parent.left = x;}

      x.right = newNode;
      y.parent = x;
    }

    void inorderRBT(Node root){
      if(root != null){
        inorderRBT(root.left);
        System.out.println(root.key + " " + root.parent.key);
        inorderRBT(root.right);
      }
    }

    String color(Node n){
      if(n == null){return "black";}
      else{return n.color;}
    }

    Node rightChild(Node n){
      if(n.right == null){return null;}
      else{return n.right;}
    }

    Node leftChild(Node n){
      if(n.left == null){return null;}
      else{return n.left;}
    }

    boolean isLeftChild(Node n){
      if (n == n.parent.left){return true;}
      else{return false;}
    }

    boolean isRightChild(Node n){
      if (n == n.parent.right){return true;}
      else{return false;}
    }

    boolean isLinear(Node n){
      boolean b = false;
      if(isRightChild(n) == isRightChild(n.parent)){b = true;}
      if(isLeftChild(n) == isLeftChild(n.parent)){b = true;}
      return b;
    }


    Node uncle(Node n){
      if (isLeftChild(n.parent)) {
        Node rc = rightChild(grandparent(n));
        return rc;
      }
      else {
        Node lc = leftChild(grandparent(n));
        return lc;
      }
    }

    Node grandparent(Node n) {return n.parent.parent;}

    void rightOrLeftRotate(Node root){
      if(isRightChild(root)){
        rotateLeft(root);
      }
      else{
        rotateright(root);
      }
    }
  }

  public static void main(String[] args) throws IOException {
    BST tree = new BST();
    RBT tree1 = new RBT();
    String typeOfTree = args[0];
    Scanner wordFile = new Scanner(new FileReader(args[1]));
    Scanner commands= new Scanner(new FileReader(args[2]));

    if(("-1").equals(typeOfTree)){
      while(wordFile.hasNext()){
        String word = wordFile.next();
        String newWord = tree.checkInsert(word);
        tree.insert(newWord);
      }
      wordFile.close();

      while(commands.hasNext()){
        String com = commands.next();
        String word = commands.next();
        String newWord = tree.checkInsert(word);
        if(("i".equals(com))){
          tree.insert(newWord);
        }
        else if("d".equals(com)){
          tree.delete(tree.find(newWord,tree.root));
        }
        else if("f".equals(newWord)){
          System.out.println(word +" frequency:" + (tree.find(newWord,tree.root)).freq);
        }
        else{
          System.out.println("sorry got stuck and couldn't finish anymore");
        }
      }
      commands.close();
    }

    else if ((("-2").equals(typeOfTree))) {
      while(wordFile.hasNext()){
        String word = wordFile.next();
        String newWord = tree1.checkInsert(word);
        tree1.insert(newWord);
        tree1.insertionFixUp(tree1.find(newWord,tree1.root));
      }
      wordFile.close();

      while(commands.hasNext()){
        String com = commands.next();
        String word = commands.next();
        String newWord = tree1.checkInsert(word);
        if(("i".equals(com))){
          tree1.insert(newWord);
          tree1.insertionFixUp(tree1.find(newWord,tree1.root));
        }
        else if("d".equals(com)){
          tree.delete(tree.find(newWord,tree.root));
        }
        else if("f".equals(newWord)){
          System.out.println(newWord +" frequency:" + (tree.find(newWord,tree.root)).freq);
        }
        else{
          System.out.println("sorry got stuck and couldn't finish anymore");
        }
      }
      commands.close();
    }

    else{
      System.out.println("Command not valid");
    }

    /*System.out.println("inorder:");
    tree.inorder();
    System.out.println();
    System.out.println("RBT:inorder");
    tree1.inorderRBT(tree1.root);*/
  }
}
