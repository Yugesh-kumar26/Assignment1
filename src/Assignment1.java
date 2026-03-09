import java.util.*;

public class Assignment1 {

    class VideoData {
        String videoId;
        String content;

        VideoData(String videoId, String content) {
            this.videoId = videoId;
            this.content = content;
        }
    }

    private int L1Capacity = 10000;
    private int L2Capacity = 100000;

    private LinkedHashMap<String, VideoData> L1Cache;
    private HashMap<String, String> L2Cache;
    private HashMap<String, VideoData> L3Database;

    private HashMap<String, Integer> accessCount;

    private int L1Hits = 0, L1Misses = 0;
    private int L2Hits = 0, L2Misses = 0;
    private int L3Hits = 0, L3Misses = 0;

    public Assignment1() {
        L1Cache = new LinkedHashMap<String, VideoData>(L1Capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1Capacity;
            }
        };

        L2Cache = new HashMap<>();
        L3Database = new HashMap<>();
        accessCount = new HashMap<>();
    }

    public void addVideoToDatabase(String videoId, String content) {
        L3Database.put(videoId, new VideoData(videoId, content));
    }

    public String getVideo(String videoId) {

        if (L1Cache.containsKey(videoId)) {
            L1Hits++;
            return "L1 Cache HIT";
        } else {
            L1Misses++;

            if (L2Cache.containsKey(videoId)) {
                L2Hits++;
                accessCount.put(videoId, accessCount.getOrDefault(videoId, 0) + 1);

                if (accessCount.get(videoId) > 3 && L3Database.containsKey(videoId)) {
                    L1Cache.put(videoId, L3Database.get(videoId));
                }

                return "L1 MISS → L2 HIT → Promoted to L1";
            } else {
                L2Misses++;

                if (L3Database.containsKey(videoId)) {
                    L3Hits++;
                    accessCount.put(videoId, 1);

                    if (L2Cache.size() >= L2Capacity) {
                        Iterator<String> it = L2Cache.keySet().iterator();
                        if (it.hasNext()) {
                            String oldest = it.next();
                            it.remove();
                        }
                    }

                    L2Cache.put(videoId, videoId);

                    return "L1 MISS → L2 MISS → L3 HIT → Added to L2";
                } else {
                    L3Misses++;
                    return "Video not found in database";
                }
            }
        }
    }

    public String getStatistics() {
        int totalL1 = L1Hits + L1Misses;
        int totalL2 = L2Hits + L2Misses;
        int totalL3 = L3Hits + L3Misses;

        double L1Rate = totalL1 == 0 ? 0 : (L1Hits * 100.0) / totalL1;
        double L2Rate = totalL2 == 0 ? 0 : (L2Hits * 100.0) / totalL2;
        double L3Rate = totalL3 == 0 ? 0 : (L3Hits * 100.0) / totalL3;

        double overallHits = L1Hits + L2Hits + L3Hits;
        double overallTotal = overallHits + L1Misses + L2Misses + L3Misses;
        double overallRate = overallTotal == 0 ? 0 : (overallHits * 100.0) / overallTotal;

        return "L1: Hit Rate " + String.format("%.1f", L1Rate) + "%\n" +
                "L2: Hit Rate " + String.format("%.1f", L2Rate) + "%\n" +
                "L3: Hit Rate " + String.format("%.1f", L3Rate) + "%\n" +
                "Overall: Hit Rate " + String.format("%.1f", overallRate) + "%";
    }
}