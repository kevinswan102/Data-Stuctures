import java.lang.Comparable;

public class BinomialHeap<Key extends Comparable<Key>> {

	private Node<Key> head;

	public BinomialHeap() {
		head = null;
	}

	public BinomialHeap(Node<Key> head) {
		this.head = head;
	}

	public static class Node<Key extends Comparable<Key>> implements Comparable<Node<Key>> {
		public Key key;
		public int degree;
		public Node<Key> parent;
		public Node<Key> child;
		public Node<Key> sibling;

		// Constructors
		public Node() {
			key = null;
		}

		public Node(Key key) {
			this.key = key;
		}

		public int compareTo(Node<Key> node) {
			return this.key.compareTo(node.key);
		}

		// Underlying function to print the heap
		public void print(int level) {
			Node<Key> curr = this;
			while (curr != null) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < level; i++) {
					sb.append(" ");
				}
				sb.append(curr.key.toString());
				System.out.println(sb.toString());
				if (curr.child != null) {
					curr.child.print(level + 1);
				}
				curr = curr.sibling;
			}
		}
	}

	// If heap is empty, return true else false
	public boolean isEmpty() {
		return head == null;
	}

	// Insert a key
	public void insert(Key key) {
		Node<Key> n = new Node<Key>(key);
		BinomialHeap<Key> tempHeap = new BinomialHeap<Key>(n);
		head = union(tempHeap);
	}

	// Return the min key
	public Key getMin() {
		if (head == null) {
			return null;
		} else {
			Node<Key> min = head;
			Node<Key> next = min.sibling;

			while (next != null) {
				if (next.compareTo(min) < 0) {
					min = next;
				}
				next = next.sibling;
			}

			return min.key;
		}
	}

	public void decreaseKey(Node<Key> n, Key newKey) {
		n.key = newKey;
		bubbleUp(n, false);
	}

	// Delete a node
	public void delete(Node<Key> n) {
		n = bubbleUp(n, true);
		if (head == n) {
			removeTreeRoot(n, null);
		} else {
			Node<Key> prev = head;
			while (prev.sibling.compareTo(n) != 0) {
				prev = prev.sibling;
			}
			removeTreeRoot(n, prev);
		}
	}

	// Bubble up
	private Node<Key> bubbleUp(Node<Key> n, boolean toRoot) {
		Node<Key> parent = n.parent;
		while (parent != null && (toRoot || n.compareTo(parent) < 0)) {
			Key temp = n.key;
			n.key = parent.key;
			parent.key = temp;
			n = parent;
			parent = parent.parent;
		}
		return n;
	}

	// Deletes the min
	public Key deleteMin() {
		if (head == null) {
			return null;
		}

		Node<Key> min = head;
		Node<Key> minPrev = null;
		Node<Key> next = min.sibling;
		Node<Key> nextPrev = min;

		while (next != null) {
			if (next.compareTo(min) < 0) {
				min = next;
				minPrev = nextPrev;
			}
			nextPrev = next;
			next = next.sibling;
		}

		removeTreeRoot(min, minPrev);
		return min.key;
	}

	// Removes the root of the tree
	private void removeTreeRoot(Node<Key> root, Node<Key> prev) {
		if (root == head) {
			head = root.sibling;
		} else {
			prev.sibling = root.sibling;
		}

		// Reverse the order of root's children and make a new heap
		Node<Key> newHead = null;
		Node<Key> child = root.child;
		while (child != null) {
			Node<Key> next = child.sibling;
			child.sibling = newHead;
			child.parent = null;
			newHead = child;
			child = next;
		}
		BinomialHeap<Key> newHeap = new BinomialHeap<Key>(newHead);

		head = union(newHeap);
	}

	// Merges two binomial trees that have the same order
	private void linkTree(Node<Key> minNodeTree, Node<Key> other) {
		other.parent = minNodeTree;
		other.sibling = minNodeTree.child;
		minNodeTree.child = other;
		minNodeTree.degree++;
	}

	// Combines two binomial heaps into one
	public Node<Key> union(BinomialHeap<Key> heap) {
		Node<Key> newHead = merge(this, heap);

		head = null;
		heap.head = null;

		if (newHead == null) {
			return null;
		}

		Node<Key> prev = null;
		Node<Key> curr = newHead;
		Node<Key> next = newHead.sibling;

		while (next != null) {
			if (curr.degree != next.degree || (next.sibling != null && next.sibling.degree == curr.degree)) {
				prev = curr;
				curr = next;
			} else {
				if (curr.compareTo(next) < 0) {
					curr.sibling = next.sibling;
					linkTree(curr, next);
				} else {
					if (prev == null) {
						newHead = next;
					} else {
						prev.sibling = next;
					}

					linkTree(next, curr);
					curr = next;
				}
			}
			next = curr.sibling;
		}
		return newHead;
	}

	// Merge function
	private static <Key extends Comparable<Key>> Node<Key> merge(BinomialHeap<Key> heap1, BinomialHeap<Key> heap2) {
		if (heap1.head == null) {
			return heap2.head;
		} else if (heap2.head == null) {
			return heap1.head;
		} else {
			Node<Key> head;
			Node<Key> heap1Next = heap1.head;
			Node<Key> heap2Next = heap2.head;

			if (heap1.head.degree <= heap2.head.degree) {
				head = heap1.head;
				heap1Next = heap1Next.sibling;
			} else {
				head = heap2.head;
				heap2Next = heap2Next.sibling;
			}

			Node<Key> tail = head;

			while (heap1Next != null && heap2Next != null) {
				if (heap1Next.degree <= heap2Next.degree) {
					tail.sibling = heap1Next;
					heap1Next = heap1Next.sibling;
				} else {
					tail.sibling = heap2Next;
					heap2Next = heap2Next.sibling;
				}

				tail = tail.sibling;
			}

			if (heap1Next != null) {
				tail.sibling = heap1Next;
			} else {
				tail.sibling = heap2Next;
			}

			return head;
		}
	}

	// Prints the heap
	public void print() {
		System.out.println("Binomial heap:");
		if (head != null) {
			head.print(0);
		}
	}

	public static void main(String[] args) {
		BinomialHeap<Integer> h1 = new BinomialHeap<Integer>();
		BinomialHeap<Integer> h2 = new BinomialHeap<Integer>();
		BinomialHeap.Node<Integer> node = null;
		h1.insert(5);
		h1.insert(1);
		h1.insert(3);
		h1.insert(8);
		h1.print();
		System.out.println("Min deleted");
		h1.deleteMin();
		h1.print();

		h2.insert(4);
		h2.insert(6);
		h2.insert(2);
		h2.print();
		System.out.println("Merging heaps");
		h1.merge(h1, h2);
		h1.print();
		System.out.println("New minimum is: ");
		System.out.println(h1.getMin());
	}
}