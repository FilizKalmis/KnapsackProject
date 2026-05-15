import java.util.List;

public class DynamicProgrammingSolver {

    // Bu metod dışarıdan eşya listesini ve çanta kapasitesini alır
    public static int solve(List<Item> items, int capacity) {
        int n = items.size();
        // DP tablosunu oluşturuyoruz: Satır=Eşyalar, Sütun=Kapasite
        int[][] dp = new int[n + 1][capacity + 1];

        // Tabloyu doldurmaya başlıyoruz
        for (int i = 1; i <= n; i++) {
            Item currentItem = items.get(i - 1); // Listeler 0'dan başladığı için i-1

            for (int w = 1; w <= capacity; w++) {
                // Eğer mevcut eşyanın ağırlığı, o anki kapasiteden (w) küçükse
                if (currentItem.weight <= w) {
                    // Karar: (Eşyayı alma durumu) vs (Eşyayı alıp kalan kapasiteye bakma durumu)
                    dp[i][w] = Math.max(dp[i - 1][w], //DP[i][w], "ilk i eşyayı kullanarak w kapasitesine sığabilecek maksimum değer" demektir.
                            currentItem.value + dp[i - 1][w - currentItem.weight]);
                } else {
                    // Eşya çok ağır, mecburen bir önceki adımdaki değeri koruyoruz
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        // Sağ alt köşedeki değer, tüm eşyalar ve tam kapasite için en iyi sonuçtur 
        return dp[n][capacity];
    }
}