package net.paulweider.repos;

import net.paulweider.entity.Food;
import net.paulweider.entity.FoodType;

import java.util.*;

public class InventRepository
{
    private Map<Integer, Food> foodDB = new HashMap<>();
//    private static final InventRepository single = new InventRepository();
    private static int increment = 0;

//    private InventRepository()
//    {
//
//    }

    public List<Food> getAllExistingFood()
    {
        return new ArrayList<>(foodDB.values());
    }

    public Optional<Food> getFoodById(int id)
    {
        if(foodDB.containsKey(id))
            return Optional.of(foodDB.get(id));
        else
            return Optional.empty();
    }

//    public static InventRepository getInstance()
//    {
//        return single;
//    }

    public boolean addFood(Food food)
    {
        if(Optional.ofNullable(food).isEmpty())
            return false;

        var matchFood = getAllExistingFood()
                .stream()
                .filter(e -> e.getFoodName().equals(food.getFoodName()))
                .findFirst();

        if(matchFood.isPresent())
        {
            food.setId(matchFood.get().getId());
            food.setCount(food.getCount() + matchFood.get().getCount());
        }
        else
        {
            increment++;
            food.setId(increment);
        }

        update(food);
        return foodDB.containsKey(food.getId());
    }

    public Optional<Food> findFoodByName(String name)
    {
        var food = foodDB.entrySet()
                .stream()
                .filter(e -> e.getValue().getFoodName().equals(name))
                .findFirst();

        return food.map(Map.Entry::getValue);
    }

    public Optional<Food> findFirstFoodOfType(FoodType foodType)
    {
        var food = foodDB.entrySet()
                .stream()
                .filter(e -> e.getValue().getFoodType().equals(foodType))
                .findFirst();

        return food.map(Map.Entry::getValue);
    }

    public void update(Food food)
    {
        foodDB.put(food.getId(), food);
    }

    public boolean removeFoodById(int id)
    {
        if(foodDB.containsKey(id))
        {
            foodDB.remove(id);
            return true;
        }
        else
            return false;
    }

}
