import java.util.*;

public class Assignment1 {

    private HashMap<String, Integer> pageViews = new HashMap<>();
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(String url, String userId, String source) {

        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    public String getDashboard() {

        List<Map.Entry<String, Integer>> pages = new ArrayList<>(pageViews.entrySet());
        pages.sort((a, b) -> b.getValue() - a.getValue());

        String result = "Top Pages:\n";
        int count = 0;

        for (Map.Entry<String, Integer> entry : pages) {
            if (count == 10) break;

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            result += (count + 1) + ". " + url + " - " + views + " views (" + unique + " unique)\n";
            count++;
        }

        int totalSourceVisits = 0;
        for (int v : trafficSources.values()) {
            totalSourceVisits += v;
        }

        result += "\nTraffic Sources:\n";

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {
            double percent = (entry.getValue() * 100.0) / totalSourceVisits;
            result += entry.getKey() + ": " + percent + "%\n";
        }

        return result;
    }
}