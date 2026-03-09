import java.util.*;

public class Assignment1 {

    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    private HashMap<String, List<String>> documentNgrams = new HashMap<>();
    private int n = 5;

    public void addDocument(String docId, String text) {
        List<String> words = Arrays.asList(text.split("\\s+"));
        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.size() - n; i++) {
            String gram = String.join(" ", words.subList(i, i + n));
            ngrams.add(gram);

            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }

        documentNgrams.put(docId, ngrams);
    }

    public String analyzeDocument(String docId) {
        List<String> grams = documentNgrams.get(docId);
        if (grams == null) {
            return "Document not found";
        }

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {
            Set<String> docs = ngramIndex.get(gram);
            if (docs != null) {
                for (String otherDoc : docs) {
                    if (!otherDoc.equals(docId)) {
                        matchCount.put(otherDoc, matchCount.getOrDefault(otherDoc, 0) + 1);
                    }
                }
            }
        }

        String result = "";
        int total = grams.size();

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {
            double similarity = (entry.getValue() * 100.0) / total;
            result += "Match with " + entry.getKey() + " → " + entry.getValue() +
                    " n-grams, Similarity: " + similarity + "%\n";
        }

        return result;
    }
}