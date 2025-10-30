package net.paulweider.services;

import net.paulweider.entity.Food;
import net.paulweider.entity.FoodType;
import net.paulweider.entity.Pet;
import net.paulweider.entity.PetType;
import net.paulweider.exceptions.PetAlreadyAdoptedException;
import net.paulweider.repos.InventRepository;
import net.paulweider.repos.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetServiceIT
{
    private PetRepository petRepo;
    private InventRepository invRepo;
    private InventoryService invService;
    private PetService petService;

    @BeforeEach
    void init()
    {
        petRepo = new PetRepository();
        invRepo = new InventRepository();
        invService = new InventoryService(invRepo);
        petService = new PetService(petRepo, invService);
    }

    @Test
    void addNewPetMemberSuccess()
    {
        Pet pet = getPet("cat:kitty", PetType.MAMMAL);

        var actual = petService.addNewPetMember(pet);

        assertTrue(actual);
    }

    @Test
    void addNewPetMemberFailIfSameNameFound()
    {
        Pet pet = getPet("cat:kitty", PetType.MAMMAL);
        Pet pet2 = getPet("cat:kitty", PetType.MAMMAL);

        petService.addNewPetMember(pet);
        petService.addNewPetMember(pet2);

        assertNotEquals(pet.getId(), pet2.getId());
    }

    @Test
    void getAllAvailablePets()
    {
        Pet pet = getPet("cat:kitty", PetType.MAMMAL);
        Pet pet2 = getPet("dog:Sashe", PetType.MAMMAL);

        petService.addNewPetMember(pet);
        petService.addNewPetMember(pet2);

        var actual = petService.getAllAvailablePets()
                .stream()
                .map(Pet::getId)
                .collect(Collectors.toList());

        assertThat(actual).hasSize(2);
        assertThat(actual).contains(pet.getId(), pet2.getId());
    }

    @Test
    void adoptPet()
    {
        Pet pet = getPet("dog:Sashe", PetType.MAMMAL);

        petService.addNewPetMember(pet);

        var actual = petService.adoptPet(pet.getId());

        assertEquals(actual.getId(), pet.getId());
        assertTrue(actual.isAdopted());
    }

    @Test
    void adoptPetFailIfAlreadyAdopted()
    {
        Pet pet = getPet("dog:Sashe", PetType.MAMMAL);

        petService.addNewPetMember(pet);
        petService.adoptPet(pet.getId());

        assertThrows(PetAlreadyAdoptedException.class, () -> petService.adoptPet(pet.getId()));
    }

    @Test
    void feedPet()
    {
        Pet pet = getPet("dog:Sashe", PetType.MAMMAL);
        Food petMeal = getFoodTest("SuperDog", FoodType.FOR_MAMMALS);
        int initialHungerLvl = pet.getHungerLevel();
        int initialMealCount = petMeal.getCount();

        invService.addFoodToStock(petMeal);
        petService.addNewPetMember(pet);

        petService.feedPet(pet, petMeal.getFoodType(), 4);

        var actual = petRepo.getPetById(pet.getId());
        var actualMeal = invRepo.getFoodById(petMeal.getId());

        assertTrue(actual.isPresent() && actualMeal.isPresent());
        assertThat(actual.get().getHungerLevel()).isEqualTo(initialHungerLvl - 4);
        assertThat(actualMeal.get().getCount()).isEqualTo(initialMealCount - 4);
    }

    private static Pet getPet(String name, PetType type)
    {
        return Pet.builder()
                .name(name)
                .hungerLevel(10)
                .happinessLevel(10)
                .petType(type)
                .build();
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
