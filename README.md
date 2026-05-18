🎒 Büyük Ölçekli Sırt Çantası Problemi – Optimizasyon ve Algoritma Karşılaştırması

Bu proje, **0/1 Knapsack Problem** için büyük ölçekli veri setlerinde **Dynamic Programming (DP)** ve **Genetic Algorithm (GA)** yaklaşımlarını karşılaştırarak performans analizi yapmaktadır.

📌 Problem Tanımı

- Kapasite: `W`  
- Nesne sayısı: `N`  
- Her nesne için:  
  - Ağırlık → `w_i`  
  - Değer → `v_i`  

Amaç: Kapasiteyi aşmadan toplam değeri maksimize eden nesne kombinasyonunu bulmak.
⚠️ Karşılaşılan Sorunlar

1. **DP Bellek Problemi**  
   - Klasik 2D DP matrisi (`dp[N+1][W+1]`) büyük veri setlerinde **OutOfMemoryError** üretti.  

2. **GA Geçersiz Çözümler**  
   - Rastgele popülasyon nedeniyle kapasiteyi aşan çözümler oluştu.  
   - Çözümler elenince kâr **0** çıktı.  

🚀 Geliştirilen Çözümler

✅ Space Optimized DP
- 2D matris yerine 1D array (`dp[capacity+1]`) kullanıldı.  
- Bellek karmaşıklığı **O(W)** seviyesine indirildi.  
- 4 GB yerine ~400 KB RAM ile çalışıyor.  

✅ Enhanced GA
- **Guided Initialization** → `value/weight` oranına göre daha verimli başlangıç popülasyonu.  
- **Repair Operator** → Kapasiteyi aşan çözümlerden en verimsiz nesneler çıkarılarak geçerli hale getirildi.  


📊 Karmaşıklık Analizi

- **Dynamic Programming**  
  - Zaman: `O(N × W)`  
  - Alan: `O(W)`  

- **Genetic Algorithm**  
  - Zaman: `O(G × P × N)`  
  - Kapasiteden bağımsız çalışır.
 

🧪 Deney Ortamı
- OS: Windows 11  
- CPU: Intel Core i7  
- RAM: 16 GB  
- Dil: Java 23  
- IDE: IntelliJ IDEA  


📈 Sonuçlar
| N     | Kapasite | GA Süre   | GA Kâr | DP Süre  | DP Kâr | Accuracy Gap |
| ----- | -------- | --------- | ------ | -------- | ------ | ------------ |
| 10    | 50       | 0.00468s  | 254    | 0.00223s | 254    | %0.00        |
| 100   | 2000     | 0.04780s  | 4886   | 0.00272s | 5070   | %3.63        |
| 1000  | 20000    | 0.83000s  | 33422  | 0.01698s | 47389  | %29.47       |
| 10000 | 100000   | 22.66000s | 280299 | 0.65000s | 362720 | %22.72       |


🔍 Analiz
- **DP** → Bellek optimizasyonu ile büyük veri setlerinde kararlı çalışıyor, fakat ölçek büyüdükçe pseudo-polynomial yapı nedeniyle yavaşlıyor.  
- **GA** → Repair Operator sonrası geçerli çözümler üretiyor, büyük ölçeklerde daha avantajlı hale geliyor.  
