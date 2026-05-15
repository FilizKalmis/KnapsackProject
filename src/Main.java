import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Gün Testi: 10 tane rastgele eşya üretelim
        List<Item> items = DataGenerator.generateItems(1000);
        int capacity = 40000;

        System.out.println("--- PERFORMANS KARŞILAŞTIRMASI ---");

        // 1. Dinamik Programlama (DP) - Kesin Çözüm
        long startTimeDP = System.nanoTime();
        int maxValDP = DynamicProgrammingSolver.solve(items, capacity);
        long endTimeDP = System.nanoTime();
        long durationDP = endTimeDP - startTimeDP;

        // 2. GA Çalıştır -Sezgisel Çözüm
        long startTimeGA = System.nanoTime();
        int maxValGA = GeneticAlgorithmSolver.solve(items, capacity);
        long endTimeGA = System.nanoTime();
        long durationGA = endTimeGA - startTimeGA;

        // 4. Sonuçları Karşılaştırmalı Yazdır
        System.out.println("Esya Sayisi: " + items.size());
        System.out.println("Canta Kapasitesi: " + capacity);
        System.out.println("---------------------------------");
        System.out.println("DP Sonucu (Optimal): " + maxValDP);
        System.out.println("DP Süresi: " + durationDP + " ns");
        System.out.println("---------------------------------");
        System.out.println("GA Sonucu: " + maxValGA);
        System.out.println("GA Süresi: " + durationGA + " ns");

        // 5. Accuracy Gap (Hata Payı) Hesabı
        double accuracyGap = ((double)(maxValDP - maxValGA) / maxValDP) * 100;
        System.out.println("---------------------------------");
        System.out.println("Accuracy Gap (Hata Payı): %" + String.format("%.2f", accuracyGap));
    }
}
