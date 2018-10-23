/*
 * @author Kevin Swan
 */

public class BST<Key extends Comparable<Key>> {

	private static BSTNode root;

	// private BST Node class
	static private class BSTNode<Key extends Comparable<Key>> {

		private BSTNode<Key> left;
		private BSTNode<Key> right;
		private Key k;

		// constructor
		public BSTNode(Key k) {
			this.k = k;
		}

		public boolean insertion(Key k) {
			if (contains(k) == true)
				return false;

			// compares key to node
			int comp = k.compareTo(this.k);

			// key val < node val, goes on the left
			if (comp < 0) {
				if (left == null)
					left = new BSTNode<Key>(k);
				// recur down tree
				else
					left.insertion(k);
				// key val > node val, goes on the right
			} else if (comp > 0) {
				// empty, do nothing
				if (right == null)
					right = new BSTNode<Key>(k);
				// recur down tree
				else
					right.insertion(k);
			}
			return true;
		}

		private Key findMin(BSTNode<Key> n) {
			if (n == null)
				return null;
			else if (n.left == null)
				return n.k;
			return findMin(n.left);
		}

		public BSTNode<Key> deletion(Key k) {
			return deletion(k, this);
		}

		private BSTNode<Key> deletion(Key k, BSTNode<Key> n) {
			// base case
			if (n == null)
				return n;

			int comp = k.compareTo(n.k);

			// recursion
			if (comp < 0)
				n.left = deletion(k, n.left);
			else if (comp > 0)
				n.right = deletion(k, n.right);
			else if (n.left != null && n.right != null) {
				n.k = findMin(n.right);
				n.right = deletion(n.k, n.right);
			}

			else if (n.right != null)
				n = n.right;
			else
				n = n.left;
			return n;
		}

		public boolean contains(Key k) {
			return contains(k, this);
		}

		private boolean contains(Key k, BSTNode<Key> n) {
			// base case
			if (n == null)
				return false;

			// compares key to node
			int comp = k.compareTo(n.k);
			// key val < node val, return nothing or the node value if it is
			// there
			if (comp < 0)
				return contains(k, n.left);
			// key val > node val, return nothing or the node value
			else if (comp > 0)
				return contains(k, n.right);
			else
				return true;
		}

		public int size() {
			return getSize(this, 0);
		}

		private int getSize(BSTNode<Key> n, int count) {
			// base case
			if (n == null) {
				return 0;
			}
			// recursion
			count = 1 + getSize(n.left, count) + getSize(n.right, count);
			return count;
		}

		public BSTNode<Key> getLeftChild() {
			return left;
		}

		public BSTNode<Key> getRightChild() {
			return right;
		}

		public Comparable getKey() {
			return k;
		}
	}

	// constructor
	public BST(BSTNode node) {
		root = null;
	}
	
	public void insert(Key k) {
		// if tree's empty, add new node
		if (root == null) {
			root = new BSTNode<Key>(k);
			// else insert with recursion
		} else {
			root.insertion(k);
		}
	}

	public boolean delete(Key k) {
		if (root != null) {
			root = root.deletion(k);
			return true;
		} else
			return false;
	}

	public boolean contains(Key k) {
		if (root == null)
			return false;
		return root.contains(k);
	}

	public int size() {
		if (root == null)
			return 0;
		else
			return root.size();
	}

	void inorder(BSTNode<Key> node) {
		// LVR
		if (node != null) {
			inorder(node.getLeftChild());
			System.out.print(node.getKey() + " ");
			inorder(node.getRightChild());
		}
	}

	void preorder(BSTNode<Key> node) {
		// VLR
		if (node != null) {
			System.out.print(node.getKey() + " ");
			preorder(node.getLeftChild());
			preorder(node.getRightChild());
		}
	}

	void postorder(BSTNode<Key> node) {
		// LRV
		if (node != null) {
			postorder(node.getLeftChild());
			postorder(node.getRightChild());
			System.out.print(node.getKey() + " ");
		}
	}

	public static void main(String[] args) {
		BST tree = new BST(root);

		tree.insert(50);
		tree.insert(25);
		tree.insert(15);
		tree.insert(30);
		tree.insert(75);
		tree.insert(85);

		System.out.println("Printing inorder");
		tree.inorder(tree.root);
		System.out.println();
		System.out.println();

		System.out.println("Printing preorder");
		tree.preorder(tree.root);
		System.out.println();
		System.out.println();

		System.out.println("Printing postorder");
		tree.postorder(tree.root);
		System.out.println();
		System.out.println();

		if (tree.contains(85))
			System.out.println("Contains 85");

		tree.delete(85);

		if (tree.contains(85) == false)
			System.out.println("Deleted, no longer contains 85");

		tree.postorder(tree.root);
	}
}