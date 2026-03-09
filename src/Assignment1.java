import java.util.*;

public class Assignment1 {

    class TrieNode {
        HashMap<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
    }

    private TrieNode root = new TrieNode();
    private HashMap<String, Integer> frequency = new HashMap<>();

    public void addQuery(String query) {

        frequency.put(query, frequency.getOrDefault(query, 0) + 1);

        TrieNode node = root;

        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEnd = true;
    }

    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();
        collect(node, prefix, results);

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequency.get(a) - frequency.get(b)
        );

        for (String q : results) {
            pq.offer(q);
            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<String> top = new ArrayList<>();

        while (!pq.isEmpty()) {
            top.add(pq.poll());
        }

        Collections.reverse(top);
        return top;
    }

    private void collect(TrieNode node, String prefix, List<String> results) {

        if (node.isEnd) {
            results.add(prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            collect(entry.getValue(), prefix + entry.getKey(), results);
        }
    }

    public void updateFrequency(String query) {
        frequency.put(query, frequency.getOrDefault(query, 0) + 1);
    }
}