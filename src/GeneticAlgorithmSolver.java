import java.util.*;

public class GeneticAlgorithmSolver {
    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = 120;
    private static final int TOURNAMENT_SIZE =8;

    public static int solve(List<Item> items, int capacity) {
        int n = items.size();
        Random random = new Random();
        // Büyük veri setlerinde mutasyon oranı dinamik olmalıdır!
        double dynamicMutationRate = 0.015;

        // 1. ADIM (Rezoug et al.): Eşyaların verimlilik oranlarını (Değer/Ağırlık) hesapla
        double[] efficiencies = new double[n];
        double maxEfficiency = 0;
        for (int j = 0; j < n; j++) {
            efficiencies[j] = (double) items.get(j).value / items.get(j).weight;
            if (efficiencies[j] > maxEfficiency) {
                maxEfficiency = efficiencies[j];
            }
        }

        // 2. ADIM (Rezoug et al.): Popülasyonu Kılavuzlu (Guided) olarak başlat
        int[][] population = new int[POPULATION_SIZE][n];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            for (int j = 0; j < n; j++) {
                // Verimli olan eşyaların seçilme ihtimali daha yüksek olsun
                double probability = (efficiencies[j] / maxEfficiency) * 0.25; // max  şans
                population[i][j] = (random.nextDouble() < probability) ? 1 : 0;
            }
            // Olası taşmaları hemen tamir et
            repair(population[i], items, capacity);
        }

        int bestFitness = 0;

        // 3. ADIM: Evrim Döngüsü
        for (int gen = 0; gen < GENERATIONS; gen++) {
            int[][] nextPopulation = new int[POPULATION_SIZE][n];

            for (int i = 0; i < POPULATION_SIZE; i++) {
                int[] parent1 = tournamentSelection(population, items, capacity, random);
                int[] parent2 = tournamentSelection(population, items, capacity, random);

                int [] child = crossover(parent1, parent2, random);
                mutate(child, random, dynamicMutationRate);

                // CRITICAL (Chu & Beasley): Yeni doğan çocuğu kurala uymuyorsa TAMIR ET
                repair(child, items, capacity);

                nextPopulation[i] = child;

                int currentFitness = calculateFitness(child, items, capacity);
                if (currentFitness > bestFitness) {
                    bestFitness = currentFitness;
                }
            }
            population = nextPopulation;
        }
        return bestFitness;
    }

    // ONARIM OPERATÖRÜ (Chu & Beasley Yaklaşımı)
    private static void repair(int[] solution, List<Item> items, int capacity) {
        int totalWeight = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) totalWeight += items.get(i).weight;
        }

        // Çanta patladığı sürece en verimsiz eşyayı çantadan at!
        while (totalWeight > capacity) {
            int worstIdx = -1;
            double worstRatio = Double.MAX_VALUE;

            for (int i = 0; i < solution.length; i++) {
                if (solution[i] == 1) {
                    double ratio = (double) items.get(i).value / items.get(i).weight;
                    if (ratio < worstRatio) {
                        worstRatio = ratio;
                        worstIdx = i;
                    }
                }
            }

            if (worstIdx != -1) {
                solution[worstIdx] = 0; // Eşyayı çantadan çıkar
                totalWeight -= items.get(worstIdx).weight;
            } else {
                break;
            }
        }
    }

    private static int[] tournamentSelection(int[][] population, List<Item> items, int capacity, Random random) {
        // Burayı yukarıda tanımladığımız TOURNAMENT_SIZE (10) sabitiyle güncelliyoruz
        int tournamentSize = TOURNAMENT_SIZE;
        int bestIdx = random.nextInt(POPULATION_SIZE);

        for (int i = 0; i < tournamentSize; i++) {
            int nextIdx = random.nextInt(POPULATION_SIZE);
            if (calculateFitness(population[nextIdx], items, capacity) > calculateFitness(population[bestIdx], items, capacity)) {
                bestIdx = nextIdx;
            }
        }
        return population[bestIdx].clone();
    }

    private static int[] crossover(int[] p1, int[] p2, Random random) {
        int n = p1.length;
        int[] child = new int[n];
        int midpoint = random.nextInt(n);
        for (int i = 0; i < n; i++) {
            child[i] = (i < midpoint) ? p1[i] : p2[i];
        }
        return child;
    }

    private static void mutate(int[] child, Random random,double mutationRate) {
        for (int i = 0; i < child.length; i++) {
            if (random.nextDouble() < mutationRate) {
                child[i] = 1 - child[i];
            }
        }
    }

    private static int calculateFitness(int[] solution, List<Item> items, int capacity) {
        int totalValue = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                totalValue += items.get(i).value;
            }
        }
        return totalValue; // Repair fonksiyonu sayesinde ağırlık asla kapasiteyi aşamaz!
    }
}