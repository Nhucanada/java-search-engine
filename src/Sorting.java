import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may (or may not) need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable.
	 * It returns an ArrayList containing all the keys from the map, ordered
	 * in descending order based on the values they mapped to.
	 *
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number
	 * of pairs in the map.
	 */
	public static <K, V extends Comparable<V>> ArrayList<K> slowSort (HashMap<K, V> results) {
		ArrayList<K> sortedUrls = new ArrayList<K>();
		sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

		int N = sortedUrls.size();
		for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) < 0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);
				}
			}
		}
		return sortedUrls;
	}


	/*
	 * This method takes as input an HashMap with values that are Comparable.
	 * It returns an ArrayList containing all the keys from the map, ordered
	 * in descending order based on the values they mapped to.
	 *
	 * The time complexity for this method is O(n*log(n)), where n is the number
	 * of pairs in the map.
	 */
	public static <K, V extends Comparable<V>> ArrayList<K> fastSort(HashMap<K, V> results) {
		ArrayList<Entry<K, V>> entries = new ArrayList<>(results.entrySet());
		mergeSort(entries);
		ArrayList<K> sortedKeys = new ArrayList<>();
		for (Entry<K, V> entry : entries) {
			sortedKeys.add(entry.getKey());
		}
		return sortedKeys;
	}

	private static <K, V extends Comparable<V>> void mergeSort(ArrayList<Entry<K, V>> entries) {
		if (entries.size() < 2) {
			return;
		}
		int mid = entries.size() / 2;
		ArrayList<Entry<K, V>> left = new ArrayList<>(entries.subList(0, mid));
		ArrayList<Entry<K, V>> right = new ArrayList<>(entries.subList(mid, entries.size()));

		mergeSort(left);
		mergeSort(right);

		merge(entries, left, right);
	}

	private static <K, V extends Comparable<V>> void merge(ArrayList<Entry<K, V>> entries,
														   ArrayList<Entry<K, V>> left,
														   ArrayList<Entry<K, V>> right) {
		int i = 0, j = 0, k = 0;
		while (i < left.size() && j < right.size()) {
			if (left.get(i).getValue().compareTo(right.get(j).getValue()) >= 0) {
				entries.set(k++, left.get(i++));
			} else {
				entries.set(k++, right.get(j++));
			}
		}
		while (i < left.size()) {
			entries.set(k++, left.get(i++));
		}
		while (j < right.size()) {
			entries.set(k++, right.get(j++));
		}
	}
}


