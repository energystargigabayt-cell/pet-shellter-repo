package net.paulweider.services;

import net.paulweider.entity.Food;
import net.paulweider.entity.FoodType;
import net.paulweider.exceptions.InvalidCountArgumentException;
import net.paulweider.exceptions.NoSuchFoodFoundException;
import net.paulweider.repos.InventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@ExtendWith(
        {
                MockitoExtension.class
        }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryServiceTest
{
    @Mock
    private InventRepository repository;
    @InjectMocks
    private InventoryService service;

    @Test
    void addFoodToStockSuccess()
    {
        doReturn(true).when(repository).addFood(Mockito.any(Food.class));

        assertTrue(service.addFoodToStock(getFoodTest()));
    }

    @Test
    void addFoodToStockNullFailed()
    {
        doReturn(false).when(repository).addFood(null);

        assertFalse(service.addFoodToStock(null));
    }

    @Test
    void getAllFoodSuccess()
    {
        Food food = getFoodTest();
        List<Food> testList = List.of(food);

        doReturn(testList).when(repository).getAllExistingFood();

        var actual = service.getAllAvailableFoodInStock();

        assertThat(actual).isNotEmpty();
        assertThat(actual).contains(food);
    }

    @Test
    void getAllFoodFailure()
    {
        List<Food> testList = List.of();

        doReturn(testList).when(repository).getAllExistingFood();

        var actual = service.getAllAvailableFoodInStock();

        assertThat(actual).isEmpty();
    }

    @Test
    void retainFoodByTypeSuccess()
    {
        var food = getFoodTest();
        int expected = 3;

        doReturn(Optional.of(food)).when(repository).findFirstFoodOfType(food.getFoodType());

        assertThat(service.retainFoodByType(food.getFoodType(), expected)).isEqualTo(expected);
    }

    @Test
    void retainFoodFailIfCountMalformed()
    {
        var food = getFoodTest();
        int expected = -1;

        assertThrows(InvalidCountArgumentException.class, () -> service.retainFoodByType(food.getFoodType(), expected));

        Mockito.verifyNoInteractions(repository);
    }

    @Test
    void retainFoodFailIfNoFoodFoundInDB()
    {
        var food = getFoodTest();
        int expected = 3;

        doReturn(Optional.empty()).when(repository).findFirstFoodOfType(food.getFoodType());

        assertThrows(NoSuchFoodFoundException.class, () -> service.retainFoodByType(food.getFoodType(), expected));
        Mockito.verify(repository, times(0)).update(Mockito.any());
    }

    @Test
    void retainFoodFailIfNotEnoughFood()
    {
        var food = getFoodTest();
        int expected = 7;

        doReturn(Optional.of(food)).when(repository).findFirstFoodOfType(food.getFoodType());

        assertThrows(NoSuchFoodFoundException.class, () -> service.retainFoodByType(food.getFoodType(), expected));
    }

    @Test
    void retainFoodByNameSuccess()
    {
        var food = getFoodTest();
        int expected = 3;

        doReturn(Optional.of(food)).when(repository).findFoodByName(food.getFoodName());

        int actual = service.retainFoodByName(food.getFoodName(), expected);

        assertEquals(expected, actual);
    }

    @Test
    void retainFoodByNameFailIfCountMalformed()
    {
        var food = getFoodTest();
        int expected = -1;

        assertThrows(InvalidCountArgumentException.class, () -> service.retainFoodByName(food.getFoodName(), expected));

        Mockito.verifyNoInteractions(repository);
    }

    @Test
    void retainFoodByNameFailIfNoFoodFoundInDB()
    {
        var food = getFoodTest();
        int expected = 3;

        doReturn(Optional.empty()).when(repository).findFoodByName(food.getFoodName());

        assertThrows(NoSuchFoodFoundException.class, () -> service.retainFoodByName(food.getFoodName(), expected));
        Mockito.verify(repository, times(0)).update(Mockito.any());
    }

    @Test
    void retainFoodByNameFailIfNotEnoughFood()
    {
        var food = getFoodTest();
        int expected = 7;

        doReturn(Optional.of(food)).when(repository).findFoodByName(food.getFoodName());

        assertThrows(NoSuchFoodFoundException.class, () -> service.retainFoodByName(food.getFoodName(), expected));
    }
    
    private static Food getFoodTest()
    {
        return Food.builder()
                .foodType(FoodType.FOR_FISHES)
                .foodName("SuperFish")
                .count(6)
                .build();
    }
}