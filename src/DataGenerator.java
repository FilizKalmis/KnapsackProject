import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    public static List<Item> generateItems(int n) {
        List<Item> items = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            // Ağırlık ve değerleri örneğin 1-100 arası rastgele üretelim
            int weight = random.nextInt(50) + 1;
            int value = random.nextInt(100) + 1;
            items.add(new Item(weight, value));
        }
        return items;
    }
}