import java.util.List;

public class DynamicProgrammingSolver {

    public static int solveOptimized(List<Item> items, int capacity) {
        // 2 boyutlu [10001][100001] yerine sadece tek boyutlu [100001] dizi!
        int[] dp = new int[capacity + 1];

        for (Item item : items) {
            // Döngüyü kapasiteden geriye doğru çalıştırıyoruz ki aynı eşyayı tekrar seçmeyelim
            for (int w = capacity; w >= item.weight; w--) {
                dp[w] = Math.max(dp[w], item.value + dp[w - item.weight]);
            }
        }
        return dp[capacity];
    }
}