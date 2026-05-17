import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        // --- TEST PARAMETRELERİ (Tabloya göre buradan değiştirebilirsin) ---
        int nValue = 10000;
        int capacity = 100000;

        // Adil kıyaslama için her iki algoritmaya da gidecek ortak rastgele veri seti
        List<Item> items = DataGenerator.generateItems(nValue);

        System.out.println("--- PERFORMANS KARŞILAŞTIRMASI ---");
        System.out.println("Esya Sayisi: " + nValue);
        System.out.println("Canta Kapasitesi: " + capacity);
        System.out.println("---------------------------------");

        // 1. ADIM: GENETİK ALGORİTMA ÇALIŞIYOR (Sezgisel)
        System.out.println("GA Algoritması başlatılıyor...");
        long startTimeGA = System.nanoTime();
        int maxValGA = GeneticAlgorithmSolver.solve(items, capacity);
        long endTimeGA = System.nanoTime();
        long durationGA = endTimeGA - startTimeGA;

        System.out.println("GA Sonucu: " + maxValGA);
        System.out.println("GA Süresi: " + (durationGA / 1_000_000_000.0) + " saniye");
        System.out.println("---------------------------------");

        // 2. ADIM: ALAN OPTİMİZASYONLU DP ÇALIŞIYOR (Kesin Çözüm - ÇÖKMEZ!)
        System.out.println("Alan Optimizasyonlu DP Algoritması başlatılıyor...");
        long startTimeDP = System.nanoTime();

        // DİKKAT: Burası yeni yazdığımız 1D dizili optimize fonksiyonu çağırıyor!
        int maxValDP = DynamicProgrammingSolver.solveOptimized(items, capacity);

        long endTimeDP = System.nanoTime();
        long durationDP = endTimeDP - startTimeDP;

        System.out.println("DP Sonucu (Optimal): " + maxValDP);
        System.out.println("DP Süresi: " + (durationDP / 1_000_000_000.0) + " saniye");
        System.out.println("---------------------------------");

        // 3. ADIM: ACCURACY GAP (HATA PAYI) HESAPLAMA
        // Formül: ((Optimal - Sezgisel) / Optimal) * 100 [cite: 1095]
        double accuracyGap = ((double)(maxValDP - maxValGA) / maxValDP) * 100;
        System.out.println("Accuracy Gap (Hata Payı): %" + String.format("%.2f", accuracyGap));
        System.out.println("---------------------------------");

        // 4. ADIM: SONUÇLARINI .TXT DOSYALARINA OTOMATİK KAYDET
        saveResultsToTxt(nValue, capacity, durationGA, maxValGA, durationDP, maxValDP, accuracyGap);
    }

    // Sonuçları Excel'e kolay çekebilmek için CSV (noktalı virgül) formatında txt'ye yazar [cite: 805-806, 1012]
    private static void saveResultsToTxt(int n, int cap, long gaTime, int gaVal, long dpTime, int dpVal, double gap) {
        try {
            // results/klasörünün önceden oluşturulmuş olduğundan emin olun [cite: 1054]
            FileWriter fw = new FileWriter("results/accuracy_results.txt", true);
            PrintWriter pw = new PrintWriter(fw);

            // Excel başlık mantığı: N;Kapasite;GA_Sure;GA_Kar;DP_Sure;DP_Kar;Hata_Payi
            pw.println(n + ";" + cap + ";" + gaTime + ";" + gaVal + ";" + dpTime + ";" + dpVal + ";%" + String.format("%.2f", gap));
            pw.close();
            System.out.println("Veriler 'results/accuracy_results.txt' dosyasına başarıyla eklendi!");
        } catch (IOException e) {
            System.out.println("Dosyaya kaydederken bir hata oluştu: " + e.getMessage());
        }
    }
}