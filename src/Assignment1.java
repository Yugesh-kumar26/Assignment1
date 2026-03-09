import java.util.*;

public class Assignment1 {

    private HashMap<String, Integer> stockMap = new HashMap<>();
    private HashMap<String, LinkedList<Integer>> waitingList = new HashMap<>();

    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    public synchronized String purchaseItem(String productId, int userId) {

        if (!stockMap.containsKey(productId)) {
            return "Product not found";
        }

        int stock = stockMap.get(productId);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        }
        else {
            LinkedList<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }

    public String checkStock(String productId) {

        if (!stockMap.containsKey(productId)) {
            return "Product not found";
        }

        return stockMap.get(productId) + " units available";
    }

    public int getWaitingListSize(String productId) {
        if (!waitingList.containsKey(productId)) {
            return 0;
        }
        return waitingList.get(productId).size();
    }
}