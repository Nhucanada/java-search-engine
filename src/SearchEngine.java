import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String>> wordIndex;   // this will contain a set of pairs (String, ArrayList of Strings)
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception {
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}

	/*
	 * This does an exploration of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 *
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		if (internet.getVisited(url)) {
			return;
		}
		internet.setVisited(url, true);

		ArrayList<String> content = parser.getContent(url);
		for (String word : content) {
			word = word.toLowerCase();

			if (!wordIndex.containsKey(word)) {
				wordIndex.put(word, new ArrayList<>());
			}

			if (!wordIndex.get(word).contains(url)) {
				wordIndex.get(word).add(url);
			}
		}

		ArrayList<String> links = parser.getLinks(url);

		internet.addVertex(url);

		for (String link : links) {

			internet.addEdge(url, link);

			crawlAndIndex(link);
		}
	}


	/*
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex().
	 * To implement this method, refer to the algorithm described in the
	 * assignment pdf.
	 *
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		ArrayList<String> urls = internet.getVertices();

		// Init all PR's to 1
		for (String v : urls)
			internet.setPageRank(v, 1.00);

		ArrayList<Double> prs;
		int conv = 0; // For checking convergence
		do {
			prs = computeRanks(urls);
			conv = 0;
			for (int i = 0; i < urls.size(); i++) {
				if (Math.abs(internet.getPageRank(urls.get(i)) - prs.get(i)) < epsilon) {
					conv++; // # of convergences needs to equal total number of URLs
				}
				internet.setPageRank(urls.get(i), prs.get(i));
			}
		} while (conv < urls.size());
	}


	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls.
	 * Note that the double in the output list is matched to the url in the input list using
	 * their position in the list.
	 *
	 * This method will probably fit in about 20 lines.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		ArrayList<Double> rankList = new ArrayList<>();
		double df = 0.5;

		for (String s : vertices) {
			double sum = 0;
			for (String t : internet.getEdgesInto(s)) {
				if (internet.getPageRank(t) == 0) {
					internet.setPageRank(t, 1);
				}
				sum += (internet.getPageRank(t) / internet.getOutDegree(t));
			}
			double newRank = df + (df * sum);
			rankList.add(newRank);
		}

		for (int i = 0; i < vertices.size(); i++) {
			internet.setPageRank(vertices.get(i), rankList.get(i));
		}
		return rankList;
	}


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 *
	 * This method will probably fit in about 10-15 lines.
	 */
	public ArrayList<String> getResults(String query) {
			ArrayList<String> results = new ArrayList<>();
			if (wordIndex.containsKey(query.toLowerCase())) {
				ArrayList<String> urls = wordIndex.get(query.toLowerCase());

				sortUrlsByRank(urls);

				results.addAll(urls);
			}

			return results;
		}

		private void sortUrlsByRank(ArrayList < String > urls) {
			for (int i = 0; i < urls.size() - 1; i++) {
				for (int j = 0; j < urls.size() - i - 1; j++) {
					String url1 = urls.get(j);
					String url2 = urls.get(j + 1);

					if (internet.getPageRank(url1) < internet.getPageRank(url2)) {
						String temp = url1;
						urls.set(j, url2);
						urls.set(j + 1, temp);
					}
				}
			}
		}
	}
