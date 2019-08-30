
import java.util.Scanner;

public class RedBlackTree {

    private final int RED = 0;
    private final int BLACK = 1; 

    private class Node { //classe nó -> equivalente ao struct do c

        int key = -1, color = BLACK;
        Node left = nil, right = nil, parent = nil;

        Node(int key) {//key = valor int do nó
            this.key = key;
        } 
    }

    private final Node nil = new Node(-1); //nó com valor -1 para referenciar a raíz
    private Node root = nil; //raíz = nó nil 

    public void printTree(Node node) {
        if (node == nil) {
            return;
        }
        printTree(node.left);
        System.out.print(((node.color==RED)?"Color: Red ":"Color: Black ")+"Key: "+node.key+" Parent: "+node.parent.key+"\n");
        printTree(node.right);
    }
    
    //printa a arvore in order
    private void inorder(Node r)
    {
        if (r != nil)
        {
            inorder(r.left);
            char c = 'B';
            if (r.color == 0)
                c = 'R';
            System.out.print(r.key +""+c+" ");
            inorder(r.right);
        }
    }
    
  //printa a arvore pre order
    private void preorder(Node r)
    {
        if (r != nil)
        {
            char c = 'B';
            if (r.color == 0)
                c = 'R';
            System.out.print(r.key +""+c+" ");
            preorder(r.left);             
            preorder(r.right);
        }
    }
    
  //printa a arvore pos order
    private void postorder(Node r)
    {
        if (r != nil)
        {
            postorder(r.left);             
            postorder(r.right);
            char c = 'B';
            if (r.color == 0)
                c = 'R';
            System.out.print(r.key +""+c+" ");
        }
    }     

    //funcao de busca
    private Node findNode(Node findNode, Node node) {
        if (root == nil) {
            return null;
        }

        if (findNode.key < node.key) {
            if (node.left != nil) {
                return findNode(findNode, node.left);
            }
        } else if (findNode.key > node.key) {
            if (node.right != nil) {
                return findNode(findNode, node.right);
            }
        } else if (findNode.key == node.key) {
            return node;
        }
        return null;
    }

    //funcao de insercao
    private void insert(Node node) {
        Node temp = root;
        if (root == nil) { //se a arvore esta vazia
            root = node;//nó adicionado é a nova raíz
            node.color = BLACK;
            node.parent = nil;
        } else {
            node.color = RED;
            while (true) {//coloca o novo nó na posição certa
                if (node.key < temp.key) {
                    if (temp.left == nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.key >= temp.key) {
                    if (temp.right == nil) {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixTree(node);//balanceamento
        }
    }

    //funcao de balanceamento depois de adicionar um nó
    //parametro = nó que foi inserido recentemente (ultimo inserido)
    private void fixTree(Node node) {
        while (node.parent.color == RED) {
            Node uncle = nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                } 
                if (node == node.parent.right) {
                    //Double rotation needed
                    node = node.parent;
                    rotateLeft(node);
                } 
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation 
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                 if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    //Double rotation needed
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation
                rotateLeft(node.parent.parent);
            }
        }
        root.color = BLACK;
    }

    //rotacao para esquerda
    void rotateLeft(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {//Need to rotate root
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }

  //rotacao para direita
    void rotateRight(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {//Need to rotate root
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

    //limpa a arvore por completo
    void deleteTree(){
        root = nil;
    }
    
    //This operation doesn't care about the new Node's connections
    //with previous node's left and right. The caller has to take care
    //of that.
    void transplant(Node target, Node with){ 
          if(target.parent == nil){
              root = with;
          }else if(target == target.parent.left){
              target.parent.left = with;
          }else
              target.parent.right = with;
          with.parent = target.parent;
    }
    
    //funcao para deletar um nó
    boolean delete(Node z){
        if((z = findNode(z, root))==null)return false; //nó n existe
        Node x;
        Node y = z; // temporary reference y
        int y_original_color = y.color;
        
        if(z.left == nil){
            x = z.right;  
            transplant(z, z.right);  
        }else if(z.right == nil){
            x = z.left;
            transplant(z, z.left); 
        }else{
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color; 
        }
        if(y_original_color==BLACK)
            deleteFixup(x);  
        return true;
    }
    
    //funcao de balanceamento depois de deletar um nó
    void deleteFixup(Node x){
        while(x!=root && x.color == BLACK){ 
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = RED;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = RED;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK; 
    }
    
    //retorna o menor valor da arvore == nó mais a esquerda
    Node treeMinimum(Node subTreeRoot){
        while(subTreeRoot.left!=nil){
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }
    
    public void consoleUI() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("\n---Menu---\n1.- Add item\n"
                    + "2.- Delete item\n"
                    + "3.- Check item\n"
                    + "4.- Print tree\n"
                    + "5.- Delete tree\n");
            int choice = scan.nextInt();

            int item;
            Node node;
            switch (choice) {
                case 1:
                	System.out.println("Enter integer element to insert");
                    item = scan.nextInt();
                        node = new Node(item);
                        insert(node);
                        scan.nextLine();
                    printTree(root);
                    break;
                case 2:
                	System.out.println("Enter integer element to delete");
                    item = scan.nextInt();
                        node = new Node(item);
                        System.out.print("\nDeleting item " + item);
                        if (delete(node)) {
                            System.out.println(": deleted!");
                        } else {
                            System.out.println(": does not exist!");
                    }
                    scan.nextLine();
                    System.out.println();
                    printTree(root);
                    break;
                case 3:
                	System.out.println("Enter integer element to check");
                    item = scan.nextInt();
                    node = new Node(item);
                    System.out.println((findNode(node, root) != null) ? "found" : "not found");
                    scan.nextLine();
                    break;
                case 4:
                	System.out.println("\nDisplay tree: ");
                    printTree(root);
                    System.out.println("\nPost order : ");
                    postorder(root);
                    System.out.println("\nPre order : ");
                    preorder(root);
                    System.out.println("\nIn order : ");
                    inorder(root); 
                    System.out.print("\n");
                    break;
                case 5:
                    deleteTree();
                    System.out.println("Tree deleted!");
                    break;
                default:
                	System.out.println("Wrong Entry \n ");
                	break;
            }
        }
    }
    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();
        rbt.consoleUI();
    }
}
