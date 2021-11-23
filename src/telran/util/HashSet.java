package telran.util;

import java.util.Iterator;
import java.util.LinkedList;

public class HashSet<T> extends AbstractSet<T> {
	private static final int DEFAULT_ARRAY_LENGTH = 16;
	private static final float FACTOR = 0.75f;
	LinkedList<T> hashTable[];

	@SuppressWarnings("unchecked")
	public HashSet(int arrayLength) {
		hashTable = new LinkedList[arrayLength];
	}

	public HashSet() {
		this(DEFAULT_ARRAY_LENGTH);
	}

	@Override
	public boolean add(T obj) {
		boolean res = false;
		if (!contains(obj)) {
			res = true;
			size++;
			if (size > FACTOR * hashTable.length) {
				recreateHashTable();
			}
			int index = getHahTableIndex(obj);
			if (hashTable[index] == null) {
				hashTable[index] = new LinkedList<>();
			}
			hashTable[index].add(obj);
		}

		return res;
	}

	private int getHahTableIndex(T obj) {
		int hashCode = obj.hashCode();
		int res = Math.abs(hashCode) % hashTable.length;
		return res;
	}

	private void recreateHashTable() {
		HashSet<T> tmpSet = new HashSet<>(hashTable.length * 2);
		for (LinkedList<T> backet: hashTable) {
			if (backet != null) {
				for (T obj: backet) {
					tmpSet.add(obj);
				}
			}
		}
		hashTable = tmpSet.hashTable;
		tmpSet = null;

	}

	@Override
	public T remove(T pattern) {
		int index = getHahTableIndex(pattern);
		T res = null;
		if (hashTable[index] != null) {
			int indObj = hashTable[index].indexOf(pattern);
			if (indObj >= 0) {
				res = hashTable[index].remove(indObj);
				size--;
			}
		}
		
		return res;
	}

	@Override
	public boolean contains(T pattern) {
		boolean res = false;
		int htIndex = getHahTableIndex(pattern);
		if (hashTable[htIndex] != null) {
			res = hashTable[htIndex].contains(pattern);
		}
		return res;
	}

	@Override
	public Iterator<T> iterator() {
		//TODO 
		return new HashSetIterator<T>();
	}
	private class HashSetIterator<T> implements Iterator<T> {
//TODO iterator required fields
		// Each of backeIterators saves the backet iterator
		Iterator<T> backeIterators[];
		int curBacketIndex = -1 ;
		int prevBacketIndex;
		// Done
		
		// Constructor
		public HashSetIterator() {
			backeIterators = new Iterator[hashTable.length];
			for(int i=0; i < hashTable.length; i++) {
				if(hashTable[i]!= null) {
					// Filling element backeIterators by backet iterator
					backeIterators[i] = (Iterator<T>) hashTable[i].iterator(); 
					if(curBacketIndex<0) { 
						// Saving the index of first non-null hashTable element
						curBacketIndex = i;
					}
				}	
			}
		}
		
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return curBacketIndex < backeIterators.length && backeIterators[curBacketIndex].hasNext();
			// Done
		}

		@Override
		public T next() {
			// TODO Auto-generated method stub
			T res = backeIterators[curBacketIndex].next();
			prevBacketIndex = curBacketIndex;
			curBacketIndex = getNextCurrentIndex(curBacketIndex);
			return res;
			// Done
		}
		@Override
		public void remove() {
			//TODO
			backeIterators[prevBacketIndex].remove();
			size--;
			// Done
		}
		
		private int getNextCurrentIndex(int curIndex) {
			// Checking the current backet.
			if(	!backeIterators[curIndex].hasNext()) {
				// There aren't available elements in the current backet
				while(++curIndex < backeIterators.length && 
						(isLinkListNotCreated(curIndex) || isLinkedListPassed(curIndex))) {
				}
				/* 1. non-null backetIterator isn't found, curIndex is increased
				 * 2. backetIterator is found and its index is returned
				 */
			} else {
				// For comments only. Current backet isn't empty yet.
				// curIndex isn't changed
			}
			// Index of the first non-null backet iterator is found
			return curIndex;
		}
		private boolean isLinkListNotCreated(int index) {
			// backet Iterator isn't created yet
			return backeIterators[index]==null;
		}
		private boolean isLinkedListPassed(int index) {
			// There isn't non-passed elements in the backet
			return !backeIterators[index].hasNext();
		}
		
	}

}
