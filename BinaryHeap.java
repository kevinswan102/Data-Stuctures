import java.util.Arrays;

class BinaryHeap {
	private int currSize;
	private int maxSize;
	private int numChild = 2;
	private int[] bHeap;

	// Constructor, heap size starts at 0
	public BinaryHeap(int size) {
		bHeap = new int[size + 1];
		currSize = 0;
		maxSize = bHeap.length;
		for (int i = 0; i < maxSize; i++)
			bHeap[i] = -1;
	}

	// Get parent
	private int parent(int i) {
		return (i - 1) / numChild;
	}

	// Get kth child
	private int kthChild(int i, int k) {
		return numChild * i + k;
	}

	// Get min Child
	private int minChild(int position) {
		int tempChild = kthChild(position, 1);
		int k = 2;
		int p = kthChild(position, k);
		while ((k <= 2) && (p < currSize)) {
			if (bHeap[p] < bHeap[tempChild])
				tempChild = p;
			p = kthChild(position, k++);
		}
		return tempChild;
	}

	// Prints heap
	public void printHeap() {
		System.out.println("Heap: ");
		for (int i = 0; i < currSize; i++)
			System.out.print(bHeap[i] + " ");
		System.out.println();
	}

	// Prints array
	public void printArr(int arr[]) {
		System.out.println("Sorted Heap: ");
		for (int i = 0; i < arr.length; i++)
			System.out.print(arr[i] + " ");
		System.out.println();
	}

	// Sorts heap
	public int[] bHeapSort() {
		int[] tempHeap = Arrays.copyOf(bHeap, maxSize);
		int tempcurrSize = currSize;

		int[] arr = new int[currSize];
		int k = 0;
		for (int i = currSize; i > 0; i--) {
			arr[k] = bHeap[0];
			deleteNode(0);
			k++;
		}
		currSize = tempcurrSize;
		bHeap = Arrays.copyOf(tempHeap, tempHeap.length);
		return arr;
	}

	// Merges heap with underlying bHeap
	public void mergeHeaps(int[] paramHeap) throws Exception {
		for (int i = 0; i < paramHeap.length; i++)
			insert(paramHeap[i]);
	}

	// Insert new node
	public void insert(int x) throws Exception {
		if (currSize == maxSize)
			throw new Exception("Cannot insert, bHeap is full");
		bHeap[currSize++] = x;
		HeapifyToTop(currSize - 1);
	}

	// Delete a node
	public boolean deleteNode(int ind) {
		try {
			int keyItem = bHeap[ind];
			if (currSize == 0)
				throw new Exception("Node does not exist");

			bHeap[ind] = bHeap[currSize - 1];
			currSize--;
			HeapifyUptoDown(ind);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Heapify to top
	private void HeapifyToTop(int cposition) {
		int tempNode = bHeap[cposition];
		while (cposition > 0 && tempNode < bHeap[parent(cposition)]) {
			bHeap[cposition] = bHeap[parent(cposition)];
			cposition = parent(cposition);
		}
		bHeap[cposition] = tempNode;
	}

	// Deletes min node
	public int deleteMin() {
		int min = bHeap[0];
		deleteNode(0);
		return min;
	}

	// Heapify to bottom
	private void HeapifyUptoDown(int position) {
		int child;
		int temp = bHeap[position];
		while (kthChild(position, 1) < currSize) {
			child = minChild(position);
			if (bHeap[child] < temp)
				bHeap[position] = bHeap[child];
			else
				break;
			position = child;
		}
		bHeap[position] = temp;
	}

	public static void main(String[] args) throws Exception {
		BinaryHeap binaryHeap = new BinaryHeap(5);
		int[] paramHeap = { 2, 10, 12 };
		int num = 3;

		binaryHeap.insert(5);
		binaryHeap.insert(2);
		binaryHeap.insert(20);
		binaryHeap.insert(9);
		binaryHeap.insert(9);
		binaryHeap.printHeap();

		try {
			System.out.println("Min Element deleted: " + binaryHeap.deleteMin());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println();
		binaryHeap.printHeap();

		if (binaryHeap.deleteNode(num)) {
			System.out.println(num + " Removed (Either index or #)");
		} else {
			System.out.println("Not found");
		}
		System.out.println();
		binaryHeap.printHeap();

		System.out.println("Merging heap with underlying heap");
		binaryHeap.mergeHeaps(paramHeap);
		System.out.println();
		binaryHeap.printHeap();

		System.out.println();
		int[] sortedArr = binaryHeap.bHeapSort();
		binaryHeap.printArr(sortedArr);

	}
}
