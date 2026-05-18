# Büyük Ölçekli Sırt Çantası Problemlerinde Algoritmik Geliştirme ve Optimizasyon Süreci

Bu proje, kombinatoryal optimizasyon ve algoritma analizi alanının en temel NP-Hard problemlerinden biri olan **0/1 Sırt Çantası Probleminin (Knapsack Problem)** büyük ölçekli veri setleri altındaki davranışlarını incelemektedir. Projede, kesin çözüm sunan **Dinamik Programlama (DP)** algoritması ile stokastik-arama tabanlı bir meta-sezgisel (*stochastic-search based metaheuristic*) yaklaşım olan **Genetik Algoritma (GA)** mimarisi; zaman maliyeti, bellek tüketimi ve çözüm kalitesi (doğruluk payı) eksenlerinde karşılaştırmalı deneysel analize tabi tutulmuştur.

Bu çalışma, sıradan bir ödev şablonunun ötesinde; büyük ölçekli problemlerde karşılaşılan donanımsal ve yazılımsal darboğazların (JVM bellek taşmaları, sezgisel algoritmaların kör arama uzaylarında kaybolması) gerçek mühendislik yaklaşımlarıyla nasıl çözüldüğünü gösteren **hibrit bir deneysel analiz çalışmasıdır**.

---

## 🎯 Problemin Tanımı ve Teorik Altyapı

0/1 Sırt Çantası Problemi, matematiksel olarak şu şekilde modellenir:
* Maksimum çanta kapasitesi $W$ ve toplam nesne sayısı $N$ olmak üzere; her $i$ nesnesinin bir $v_i$ değeri (value) ve $w_i$ ağırlığı (weight) bulunmaktadır.
* Amacı, toplam ağırlık kısıtını ihlal etmeden, çantaya alınan nesnelerin toplam kârını maksimize edecek $x_i \in \{0, 1\}$ seçim vektörünü bulmaktır:

$$\max \sum_{i=1}^{N} v_i x_i \quad \text{kısıt:} \quad \sum_{i=1}^{N} w_i x_i \le W$$

Eleman sayısı ($N$) arttıkça olası çözümlerin uzayı $2^N$ şeklinde eksponansiyel olarak büyümekte ve brute-force (kaba kuvvet) yöntemlerini imkansız kılmaktadır. Bu durum, büyük veri ölçeklerinde optimize edilmiş kesin algoritmaların veya akıllı sezgisel mekanizmaların kullanımını zorunlu kılmaktadır.

---

## ⚠️ Karşılaşılan Darboğazlar ve Başarısız İlk Versiyonlar

Projenin geliştirme sürecinde, literatürdeki ham (naive) algoritmalar doğrudan büyük ölçekli verilere ($N=10.000$, $W=100.000$) uygulandığında sistem iki büyük krizle durma noktasına gelmiştir:

### 1. Dinamik Programlama ve `OutOfMemoryError` Krizi
Klasik Dinamik Programlama yaklaşımı, kararları saklamak için ardışık bellek üzerinde $O(N \times W)$ boyutunda iki boyutlu bir matris (`new int[10001][100001]`) oluşturmaktadır. Bu matris yapısı, her bir `int` hücresinin 4 bayt yer kaplaması nedeniyle, varsayılan JVM heap ayarları altında yaklaşık **4 GB ardışık RAM** talep etmiş ve terminale şu hatayı fırlatarak çökmüştür:
```text
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
    at DynamicProgrammingSolver.solve(DynamicProgrammingSolver.java:14)
