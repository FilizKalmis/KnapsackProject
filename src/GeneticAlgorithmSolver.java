import java.util.*;

public class GeneticAlgorithmSolver {
    // Basitlik için popülasyon boyutunu ve nesil sayısını sabitliyoruz
    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = 500;
    private static final double MUTATION_RATE = 0.05;

    public static int solve(List<Item> items, int capacity) {
        int n = items.size();
        Random random = new Random();

        // 1. Başlangıç Popülasyonu
        int[][] population = new int[POPULATION_SIZE][n];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            for (int j = 0; j < n; j++) {
                population[i][j] = random.nextInt(2);
            }
        }

        int bestFitness = 0;

        // 2. Evrim Döngüsü
        for (int gen = 0; gen < GENERATIONS; gen++) {
            int[][] nextPopulation = new int[POPULATION_SIZE][n];

            for (int i = 0; i < POPULATION_SIZE; i++) {
                // SELECTION: En iyi adayları seç
                int[] parent1 = tournamentSelection(population, items, capacity, random);
                int[] parent2 = tournamentSelection(population, items, capacity, random);

                // CROSSOVER: İkisinden yeni bir çocuk yap
                int[] child = crossover(parent1, parent2, random);

                // MUTATION: Küçük bir rastgele değişiklik ekle
                mutate(child, random);

                nextPopulation[i] = child;

                // En iyi sonucu takip et
                int currentFitness = calculateFitness(child, items, capacity);
                if (currentFitness > bestFitness) {
                    bestFitness = currentFitness;
                }
            }
            population = nextPopulation;
        }
        return bestFitness;
    }

    // Seçim: Rastgele 5 aday seç, içlerinden en iyisini döndür
    private static int[] tournamentSelection(int[][] population, List<Item> items, int capacity, Random random) {
        int tournamentSize = 5;
        int bestIdx = random.nextInt(POPULATION_SIZE);
        for (int i = 0; i < tournamentSize; i++) {
            int nextIdx = random.nextInt(POPULATION_SIZE);
            if (calculateFitness(population[nextIdx], items, capacity) > calculateFitness(population[bestIdx], items, capacity)) {
                bestIdx = nextIdx;
            }
        }
        return population[bestIdx].clone();
    }

    // Çaprazlama: İki çözümün bir kısmını yer değiştir
    private static int[] crossover(int[] p1, int[] p2, Random random) {
        int n = p1.length;
        int[] child = new int[n];
        int midpoint = random.nextInt(n);
        for (int i = 0; i < n; i++) {
            child[i] = (i < midpoint) ? p1[i] : p2[i];
        }
        return child;
    }

    // Mutasyon: Rastgele bir biti tersine çevir
    private static void mutate(int[] child, Random random) {
        for (int i = 0; i < child.length; i++) {
            if (random.nextDouble() < MUTATION_RATE) {
                child[i] = 1 - child[i];
            }
        }
    }

    private static int calculateFitness(int[] solution, List<Item> items, int capacity) {
        int totalWeight = 0;
        int totalValue = 0;
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                totalWeight += items.get(i).weight;
                totalValue += items.get(i).value;
            }
        }
        // Eğer ağırlık kapasiteyi aşarsa, bu çözüm "kötüdür" (0 değerini döndürür)
        return (totalWeight <= capacity) ? totalValue : 0;
    }
}
