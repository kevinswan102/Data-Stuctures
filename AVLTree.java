
public class AVLTree<Key extends Comparable<Key>> {
	static Node root;
	static AVLTree sourceTree;

	int height(Node N) {
		if (N == null)
			return 0;

		return N.getHeight();
	}

	static AVLTree insert(AVLTree sourceTree, Node newNode) throws Exception {
		AVLTree newTree = new AVLTree();

		// make sure new Node has no default values other than data
		if (newNode == null) {
			throw new Exception("Node must be a valid object with data");
		}

		if (sourceTree.root == null) {
			newTree.root = newNode;
			return newTree;
		}

		// start comparison
		newTree.root = sourceTree.insertNode(sourceTree.root, newNode);
		return newTree;
	}

	@SuppressWarnings("rawtypes")
	static AVLTree delete(AVLTree sourceTree, Comparable value) throws Exception {
		AVLTree newTree = new AVLTree();

		if (sourceTree.root == null) {
			throw new Exception("Tree is empty");
		}
		newTree.root = sourceTree.deleteNode(sourceTree.root, value);
		return newTree;
	}

	@SuppressWarnings("unchecked")
	Node insertNode(Node node, Node newNode) {

		if (node == null)
			return (new Node(newNode.getData()));

		// if current is greater than input then send to left
		if (node.getData().compareTo(newNode.getData()) > 0) {

			node.setLeftChild(insertNode(node.getLeftChild(), newNode));
		} else {

			node.setRightChild(insertNode(node.getRightChild(), newNode));

		}

		node.setHeight(1 + maxHeight(node));

		// get the balance factor, then handle if it is unbalanced
		int balance = getBalance(node);
		if (balance > 1) {
			if (newNode.getData().compareTo(node.getLeftChild().getData()) < 0)
				return rightRotate(node);

			if (newNode.getData().compareTo(node.getLeftChild().getData()) > 0) {
				node.setLeftChild(leftRotate(node.getLeftChild()));
				return rightRotate(node);
			}

		} else if (balance < -1) {
			if (newNode.getData().compareTo(node.getRightChild().getData()) > 0)
				return leftRotate(node);

			if (newNode.getData().compareTo(node.getRightChild().getData()) < 0) {
				node.setRightChild(rightRotate(node.getRightChild()));
				return leftRotate(node);
			}
		}

		return node;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	Node deleteNode(Node node, Comparable valueToDelete) {

		if (node == null)
			return node;

		// If the value to be deleted is smaller than
		// the node value, then it lies in left subtree
		if (node.getData().compareTo(valueToDelete) > 0)
			node.setLeftChild(deleteNode(node.getLeftChild(), valueToDelete));

		// If the value to be deleted is greater than the
		// node value, then it lies in right subtree
		else if (node.getData().compareTo(valueToDelete) < 0)
			node.setRightChild(deleteNode(node.getRightChild(), valueToDelete));

		// to be deleted
		else {
			// node with only one child or no child
			if ((node.getLeftChild() == null) || (node.getRightChild() == null)) {
				Node temp = null;
				if (temp == node.getLeftChild())
					temp = node.getRightChild();
				else
					temp = node.getLeftChild();

				if (temp == null) {
					temp = node;
					node = null;
				} else
					node = temp;
			} else {

				// node with two children: Get the successor (smallest in the
				// right subtree)
				Node temp = smallestNode(node.getRightChild());

				// Copy the inorder successor's data to this node
				node.setData(temp.getData());

				// Delete the successor
				node.setRightChild(deleteNode(node.getRightChild(), temp.getData()));
			}

		}

		// If the tree had only one node then return
		if (node == null)
			return node;

		// UPDATE HEIGHT OF THE CURRENT NODE
		node.setHeight(1 + maxHeight(node));

		// GET THE BALANCE FACTOR OF THIS NODE (to check whether
		// this node became unbalanced)
		int balance = getBalance(node);

		// If this node becomes unbalanced, then there are 4 cases
		// Left Left Case
		if (balance > 1 && getBalance(node.getLeftChild()) >= 0)
			return rightRotate(node);

		// Left Right Case
		if (balance > 1 && getBalance(node.getLeftChild()) < 0) {
			node.setLeftChild(leftRotate(node.getLeftChild()));
			return rightRotate(node);
		}

		// Right Right Case
		if (balance < -1 && getBalance(node.getRightChild()) <= 0)
			return leftRotate(node);

		// Right Left Case
		if (balance < -1 && getBalance(node.getRightChild()) > 0) {
			node.setRightChild(rightRotate(node.getRightChild()));
			return leftRotate(node);
		}

		return node;
	}

	// left rotate subtree rooted with y
	Node rightRotate(Node node) {
		Node x = node.getLeftChild();
		Node x1 = x.getRightChild();

		// Perform rotation
		x.setRightChild(node);
		node.setLeftChild(x1);

		// Update heights
		node.setHeight(1 + maxHeight(node));
		x.setHeight(1 + maxHeight(x));

		// Return new root
		return x;
	}

	// left rotate subtree rooted with x
	Node leftRotate(Node node) {
		Node y = node.getRightChild();
		Node y1 = y.getLeftChild();

		// Perform rotation
		y.setLeftChild(node);
		node.setRightChild(y1);

		// Update heights
		y.setHeight(1 + maxHeight(y));
		node.setHeight(1 + maxHeight(node));

		// Return new root
		return y;
	}

	int maxHeight(Node x) {

		if (x.getLeftChild() != null && x.getRightChild() != null) {
			return (x.getLeftChild().getHeight() > x.getRightChild().getHeight() ? x.getLeftChild().getHeight()
					: x.getRightChild().getHeight());
		} else if (x.getLeftChild() != null && x.getRightChild() == null) {
			return x.getLeftChild().getHeight();
		} else if (x.getLeftChild() == null && x.getRightChild() != null) {
			return x.getRightChild().getHeight();
		}

		return 0;
	}

	int getBalance(Node x) {

		if (x.getLeftChild() != null && x.getRightChild() != null) {
			return (x.getLeftChild().getHeight() - x.getRightChild().getHeight());
		} else if (x.getLeftChild() != null && x.getRightChild() == null) {
			return x.getLeftChild().getHeight();
		} else if (x.getLeftChild() == null && x.getRightChild() != null) {
			return 0 - x.getRightChild().getHeight();
		}

		return 0;
	}

	Node smallestNode(Node node) {
		Node current = node;

		/* loop down to find the leftmost leaf */
		while (current.getLeftChild() != null)
			current = current.getLeftChild();

		return current;
	}

	public boolean recurContains(Node node, Comparable data) {

		// if tree is empty
		if (node == null) {
			return false;
		}
		// data < node data, go left
		else if (data.compareTo(node.getData()) < 0) {

			return recurContains(node.getLeftChild(), data);
		}
		// data > node data, go right
		else if (data.compareTo(node.getData()) > 0) {

			return recurContains(node.getRightChild(), data);
		} else {
			// node is found
			return true;
		}
	}

	public boolean contains(Comparable data) {
		return recurContains(root, data);
	}

	static void inOrder(AVLTree tree) throws Exception {
		if (tree.root == null)
			throw new Exception("AVL Tree is empty");

		tree.inOrderTraverse(tree.root);
		System.out.print("\n");
	}

	void inOrderTraverse(Node node) {
		if (node != null) {
			inOrderTraverse(node.getLeftChild());
			System.out.print(node.getData() + " ");
			inOrderTraverse(node.getRightChild());
		}
	}

	static void preOrder(AVLTree tree) throws Exception {
		if (tree.root == null)
			throw new Exception("AVL Tree is empty");

		tree.preOrderTraverse(tree.root);
		System.out.print("\n");
	}

	void preOrderTraverse(Node node) {
		if (node != null) {
			System.out.print(node.getData() + " ");
			preOrderTraverse(node.getLeftChild());
			preOrderTraverse(node.getRightChild());
		}
	}

	static void postOrder(AVLTree tree) throws Exception {
		if (tree.root == null)
			throw new Exception("AVL Tree is empty");

		tree.postOrderTraverse(tree.root);
		System.out.print("\n");
	}

	void postOrderTraverse(Node node) {
		if (node != null) {
			postOrderTraverse(node.getLeftChild());
			postOrderTraverse(node.getRightChild());
			System.out.print(node.getData() + " ");
		}
	}

	public String toString() {

		return root.toString();

	}

	public static void main(String[] args) throws Exception {

		System.out.println("-------------------------------------");
		System.out.println("AVL TREE");
		System.out.println("-------------------------------------");

		System.out.println("");
		AVLTree tree = new AVLTree();

		tree = AVLTree.insert(tree, new Node(10));
		tree = AVLTree.insert(tree, new Node(20));
		tree = AVLTree.insert(tree, new Node(60));
		tree = AVLTree.insert(tree, new Node(40));
		tree = AVLTree.insert(tree, new Node(50));
		tree = AVLTree.insert(tree, new Node(30));
		tree = AVLTree.insert(tree, new Node(25));
		tree = AVLTree.insert(tree, new Node(55));

		System.out.println("Root - " + tree.root.getData());
		System.out.println("-------------------------------------");

		System.out.println("Printing in order");
		AVLTree.inOrder(tree);
		System.out.println("-------------------------------------");

		System.out.println("Printing preorder");
		AVLTree.preOrder(tree);
		System.out.println("-------------------------------------");

		System.out.println("Printing postorder");
		AVLTree.postOrder(tree);
		System.out.println("-------------------------------------");

		System.out.println("Delete item - 40 & print new list of items");
		AVLTree.delete(tree, 40);
		System.out.println("New Root - " + tree.root.getData());
		AVLTree.inOrder(tree);
		System.out.println("-------------------------------------");

		System.out.println("Delete item - 15 & print new list of items");
		AVLTree.delete(tree, 15);
		System.out.println("New Root - " + tree.root.getData());
		AVLTree.inOrder(tree);
		System.out.println("-------------------------------------");

		System.out.println("Delete item - 40 & print new list of items");
		AVLTree.delete(tree, 40);
		System.out.println("New Root - " + tree.root.getData());
		AVLTree.inOrder(tree);
		System.out.println("-------------------------------------");

		System.out.println("Does tree contain 21?");
		System.out.println(tree.contains(21));

		System.out.println("Does tree contain 20?");
		System.out.println(tree.contains(20));
		System.out.println("ToString method:");
		System.out.println(tree.toString());

	}
}