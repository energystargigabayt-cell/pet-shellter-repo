package net.paulweider.repos;

import net.paulweider.entity.Pet;

import java.util.*;

public class PetRepository
{
    private final Map<Integer, Pet> petDB = new HashMap<>();
    private static final PetRepository single = new PetRepository();
    private static int increment = 0;

    public PetRepository getInstance()
    {
        return single;
    }

    public boolean addPet(Pet pet)
    {
        if(Optional.ofNullable(pet).isEmpty())
            return false;

        increment++;
        pet.setId(increment);

        updatePet(pet);
        return petDB.containsKey(pet.getId());
    }

    public List<Pet> getAllPets()
    {
        return new ArrayList<>(petDB.values());
    }

    public Optional<Pet> getPetById(int petId)
    {
        if(petDB.containsKey(petId))
            return Optional.of(petDB.get(petId));
        else
            return Optional.empty();
    }

    public boolean removePetById(int petId)
    {
        if(petDB.containsKey(petId))
            return petDB.remove(petId, petDB.get(petId));
        else
            return false;
    }

    public void updatePet(Pet pet)
    {
        petDB.put(pet.getId(), pet);
    }
}
