package btrees;

import java.util.Scanner;

class node {
    private final int MAX = 4;
	private final int MIN = 2;
	int ele[] = new int[MAX];				
	node child[] = new node[MAX];		
	node parent;


}

public class btree  {
    public static node root;
    public static final int MAX = 4;
    public static final int MIN	= 2;

    public btree() {
        root = create_newnode();
    }

    public static node create_newnode() {
        int i;
        node temp = new node();
        if (temp == null) {
            System.out.println("Fatal error");
            System.exit(0);

        }

        for (i = 0; i < MAX; i++) {
            temp.ele[i] = -1;
            temp.child[i] = null;

        }
        temp.parent = null;
        return temp;
    }

    public static node find_leaf(node x, int key) {
        int i;
        if (x.child[0] == null) {
            for (i = 0; i < MAX; i++) {
                if (key == x.ele[i])
                    return null;
            }
            return x;
        }
        for (i = 0; i < MAX; i++) {
            if (x.ele[i] >= key)
                return find_leaf(x.child[i], key);
            if (x.ele[i] == -1)
                return find_leaf(x.child[i - 1], key); // last is empty second last one
        }
        return find_leaf(x.child[10 - 1], key); // last leaf

    }

    public static boolean isFull(node x) {
        return (x.ele[MAX - 1] == -1) ? false : true;
    }

    public static void sort_node(node x) {
        int i, j;
        int temp_ele;
        node temp_child;
        for (i = 0; i < MAX && x.ele[i] != -1; i++) {
            for (j = i + 1; j < MAX && x.ele[j] != -1; j++) {
                if (x.ele[i] > x.ele[j]) {
                    temp_ele = x.ele[i];
                    temp_child = x.child[i];

                    x.ele[i] = x.ele[j];
                    x.child[i] = x.child[j];

                    x.ele[j] = temp_ele;
                    x.child[j] = temp_child;
                }
            }
        }
    }

    public static void split(node x) {
        int i;
        node temp;
        if (x.parent == null) {
            temp = create_newnode();
            root = create_newnode();

            root.ele[0] = x.ele[MIN - 1];
            root.child[0] = x;
            root.ele[1] = x.ele[MAX - 1];
            root.child[1] = temp;

            x.parent = root;
            temp.parent = x.parent;
        } else {
            if (isFull(x.parent))
                split(x.parent);

            temp = create_newnode();
            temp.parent = x.parent;
            for (i = 0; i < MAX; i++) {
                if (x.parent.ele[i] == x.ele[MAX - 1])
                    x.parent.child[i] = temp;
                if (x.parent.ele[i] == -1) {
                    x.parent.ele[i] = x.ele[MIN - 1]; // changes -1 to 4
                    x.parent.child[i] = x;
                    break;
                }

            }
        }
        for (i = 0; i < MIN; i++) {
            temp.ele[i] = x.ele[MIN + i];
            x.ele[MIN + i] = -1;
            temp.child[i] = x.child[MIN + i]; // copy child
            if (temp.child[i] != null)
                temp.child[i].parent = temp;
            x.child[MIN + i] = null;
        }
        sort_node(x.parent);
    }

    public static void update_parent(node x, int key, int max) {
        int i, new_max = 0;
        for (i = 0; i < MAX && (x.ele[i] != -1); i++) {
            new_max = x.ele[i];
            if (x.ele[i] == max)
                x.ele[i] = key;
        }
        if (key > new_max && x.parent != null)
            update_parent(x.parent, key, max);
    }

    public static int search(int key) {
        node temp = find_leaf(root, key);
        if (temp == null)
            return 1;
        return 0;
    }

    public  void display() {
        node queue[] = new node[50];
        int f = 0, r = -1, lvl = -1, i;
        queue[++r] = null;
        queue[++r] = root;
        while (f < r) {
            if (queue[f] == null) {
                System.out.println("\n\nLevel --> " + (++lvl));
                if (queue[r] != null)
                    queue[++r] = null;
                f++;
            } else {
                for (i = 0; i < MAX; i++) {
                    if (queue[f].ele[i] != -1)
                        System.out.print(queue[f].ele[i] + " ");
                    if (queue[f].child[i] != null)
                        queue[++r] = queue[f].child[i];
                }
                System.out.print("\t\t");
                f++;
            }
        }

    }
    void insert(int key) {
		
		int i,j, t, max = 0;			
		node temp = find_leaf(root,key);	
		if(temp == null) {
			System.out.println("Error!: Cannot insert: This integer is already present in the tree\n");
			return;		
		}
		if(!isFull(temp))			
		{
			System.out.println("Element inserted successfully\n");
			for(i=0;i<MAX;i++) {	
				if(temp.ele[i]==-1) {	
					temp.ele[i]=key;
					break;
				}
				max=temp.ele[i];
			}
			sort_node(temp);
			if(key>max && temp.parent!=null)		
				update_parent(temp.parent,key,max);
			return;
		}	
		if(isFull(temp))			
		{
			split(temp);
			insert(key);			
		}
	}
    public static void main(String args[]) {
        btree b= new btree();
        Scanner sc=new Scanner(System.in);
        int ch,key,flag;
        while(true) {
            System.out.println("\nMain menu\n1.Insert\n2.Search\n3.Display\n4.Exit\nEnter choice");
            ch=sc.nextInt();
            switch(ch) {
            case 1:System.out.println("Enter the integer");
                key=sc.nextInt();
                b.insert(key);
                break;
            case 2:System.out.println("Enter search");
                key=sc.nextInt();
                flag=search(key);
                if(flag == 1) {
                    System.out.println("Element is present");
                }
                else
                    System.out.println("Not present");
                break;
            case 3:b.display();
                    break;
            default:sc.close();
                    System.exit(0);
            }
        }
    
    }

}
	
