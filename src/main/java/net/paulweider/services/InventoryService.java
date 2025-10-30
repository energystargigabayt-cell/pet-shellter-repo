package net.paulweider.services;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.paulweider.entity.Food;
import net.paulweider.entity.FoodType;
import net.paulweider.exceptions.InvalidCountArgumentException;
import net.paulweider.exceptions.NoSuchFoodFoundException;
import net.paulweider.repos.InventRepository;

import java.util.List;

@AllArgsConstructor
public class InventoryService
{
    private InventRepository repository;

    public boolean addFoodToStock(Food food)
    {
        return repository.addFood(food);
    }

    public List<Food> getAllAvailableFoodInStock()
    {
        return repository.getAllExistingFood();
    }

    @SneakyThrows
    public int retainFoodByType(FoodType foodType, int count)
    {
        if(count < 1)
            throw new InvalidCountArgumentException("Malformed count argument for this food type: " + foodType + ", count input: " + count);

        var food = repository.findFirstFoodOfType(foodType).orElseThrow(() -> new NoSuchFoodFoundException("No food with such type: " + foodType.toString() + " has been found in database!"));

        if(food.getCount() < count)
            throw new NoSuchFoodFoundException("Not enough food of type: " + food.getFoodName() + " found in database, instead there's only " + food.getCount());

        food.setCount(food.getCount() - count);
        repository.update(food);

        return count;
    }

    @SneakyThrows
    public int retainFoodByName(String foodName, int count)
    {
        if(count < 1)
            throw new InvalidCountArgumentException("Malformed count argument for this food of name: " + foodName + ", count input: " + count);

        var food = repository.findFoodByName(foodName).orElseThrow(() -> new NoSuchFoodFoundException("No food with such name: " + foodName + " has been found in database!"));

        if(food.getCount() < count)
            throw new NoSuchFoodFoundException("Not enough food of type: " + food.getFoodName() + " found in database, instead there's only " + food.getCount());

        food.setCount(food.getCount() - count);
        repository.update(food);

        return count;
    }
}
