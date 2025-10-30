package net.paulweider.services;

import net.paulweider.entity.Food;
import net.paulweider.entity.FoodType;
import net.paulweider.repos.InventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InventoryServiceIT
{
    private InventRepository repository;
    private InventoryService service;

    @BeforeEach
    void init()
    {
//        repository = InventRepository.getInstance();
        repository = new InventRepository();
        service = new InventoryService(repository);
    }

    @Test
    void addFoodToStock()
    {
        var food = getFoodTest("SuperFish", FoodType.FOR_FISHES);

        var actual = service.addFoodToStock(food);

        assertTrue(actual);
    }

    @Test
    void addFoodToStockIfSimilarExist()
    {
        var food1 = getFoodTest("SuperFish", FoodType.FOR_FISHES);
        var food2 = getFoodTest("SuperFish", FoodType.FOR_FISHES);

        service.addFoodToStock(food1);
        var actual2 = service.addFoodToStock(food2);

        assertTrue(actual2);
        assertThat(service.getAllAvailableFoodInStock()).hasSize(1);
        assertTrue(repository.getFoodById(food1.getId()).isPresent());
        assertEquals(food1.getId(), food2.getId());
        assertThat(repository.getFoodById(food1.getId()).get().getCount()).isEqualTo(12);
    }

    @Test
    void addFoodToStockIfSimilarAndDiff()
    {
        var food1 = getFoodTest("SuperFish", FoodType.FOR_FISHES);
        var food2 = getFoodTest("SuperFish", FoodType.FOR_FISHES);
        var food3 = getFoodTest("Reptilizer", FoodType.FOR_REPTILES);

        var actual1 = service.addFoodToStock(food1);
        var actual2 = service.addFoodToStock(food2);
        service.addFoodToStock(food3);

        var foodList = service.getAllAvailableFoodInStock()
                .stream()
                .map(Food::getId)
                .collect(Collectors.toList());

        assertTrue(actual1);
        assertTrue(actual2);
        assertThat(foodList).hasSize(2);
        assertThat(foodList).contains(food1.getId(), food3.getId());
        assertEquals(repository.getFoodById(food1.getId()).get().getCount(), 12);
    }

    @Test
    void getAllAvailableFood()
    {
        var food1 = getFoodTest("SuperFish", FoodType.FOR_FISHES);
        var food2 = getFoodTest("KittyCatz", FoodType.FOR_MAMMALS);
        var food3 = getFoodTest("GigaAmphi", FoodType.FOR_AMPHIBIANS);

        service.addFoodToStock(food1);
        service.addFoodToStock(food2);
        service.addFoodToStock(food3);

        var actual = service.getAllAvailableFoodInStock()
                .stream()
                .map(Food::getId)
                .collect(Collectors.toList());

        assertThat(actual).hasSize(3);
        assertThat(actual).contains(food1.getId(), food2.getId(), food3.getId());
    }

    @Test
    void retainFoodByType()
    {
        var food1 = getFoodTest("SuperFish", FoodType.FOR_FISHES);
        var food2 = getFoodTest("KittyCatz", FoodType.FOR_MAMMALS);
        int requestedCount = 4;

        service.addFoodToStock(food1);
        service.addFoodToStock(food2);
        var actual = service.retainFoodByType(FoodType.FOR_FISHES, requestedCount);

        assertThat(actual).isNotZero();
        assertThat(actual).isEqualTo(requestedCount);
    }

    @Test
    void retainFoodByName()
    {
        var food1 = getFoodTest("SuperFish", FoodType.FOR_FISHES);
        var food2 = getFoodTest("KittyCatz", FoodType.FOR_MAMMALS);
        int requestedCount = 4;

        service.addFoodToStock(food1);
        service.addFoodToStock(food2);
        var actual = service.retainFoodByName("KittyCatz", requestedCount);

        assertThat(actual).isNotZero();
        assertThat(actual).isEqualTo(requestedCount);
    }

    private static Food getFoodTest(String foodName, FoodType foodType)
    {
        return Food.builder()
                .foodType(foodType)
                .foodName(foodName)
                .count(6)
                .build();
    }
}
