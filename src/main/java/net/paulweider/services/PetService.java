package net.paulweider.services;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.paulweider.entity.FoodType;
import net.paulweider.entity.Pet;
import net.paulweider.exceptions.ImproperFoodChoiceException;
import net.paulweider.exceptions.PetAlreadyAdoptedException;
import net.paulweider.exceptions.PetNotFoundException;
import net.paulweider.repos.PetRepository;

import java.util.List;

@AllArgsConstructor
public class PetService
{
    private PetRepository petRepository;
    private InventoryService inventoryService;

    public List<Pet> getAllAvailablePets()
    {
        return petRepository.getAllPets();
    }

    @SneakyThrows
    public Pet adoptPet(int petId)
    {
        Pet pet = petRepository.getPetById(petId).orElseThrow(() -> new PetNotFoundException("Pet with ID: " + petId + " has not been found in database!"));

        if(pet.isAdopted())
            throw new PetAlreadyAdoptedException("Pet with ID: " + petId + " has been already adopted!");

        pet.setAdopted(true);
        petRepository.updatePet(pet);

        return pet;
    }

    public boolean addNewPetMember(Pet pet)
    {
        return petRepository.addPet(pet);
    }

    @SneakyThrows
    public void feedPet(Pet pet, FoodType type, int count)
    {
        if(!type.toString().contains(pet.getPetType().toString()))
            throw new ImproperFoodChoiceException("Bad food type: " + type + " has been chosen for such pet type: " + pet.getPetType());

        var petQuery = petRepository.getPetById(pet.getId()).orElseThrow(() -> new PetNotFoundException("Pet with ID: " + pet.getId() + " has not been found in database!"));
        var hungerReduced = inventoryService.retainFoodByType(type, count);

        petQuery.setHungerLevel(petQuery.getHungerLevel() - hungerReduced);

        petRepository.updatePet(petQuery);
    }

//    public void feedPet(Pet pet, String foodName, int count)
//    {
//
//    }
}
