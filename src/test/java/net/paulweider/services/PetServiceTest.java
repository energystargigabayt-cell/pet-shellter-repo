package net.paulweider.services;

import net.paulweider.entity.FoodType;
import net.paulweider.entity.Pet;
import net.paulweider.entity.PetType;
import net.paulweider.exceptions.ImproperFoodChoiceException;
import net.paulweider.exceptions.PetAlreadyAdoptedException;
import net.paulweider.exceptions.PetNotFoundException;
import net.paulweider.repos.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(
        {
                MockitoExtension.class
        }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PetServiceTest
{
    @Mock
    private PetRepository repository;
    @Mock
    private InventoryService invService;
    @InjectMocks
    private PetService service;
    @Captor
    private ArgumentCaptor<Pet> petCaptor;

    @Test
    void getAllAvailablePetsSuccess()
    {
        Pet pet = getPet();
        var petList = List.of(pet);

        doReturn(petList).when(repository).getAllPets();

        var actual = service.getAllAvailablePets();

        assertThat(actual).isNotEmpty();
        assertThat(actual).contains(pet);
    }

    //Possible case scenario for db bad connections
    @Test
    void getAllAvailablePetsFailure()
    {
        var petList = List.of();

        doReturn(petList).when(repository).getAllPets();

        assertThat(service.getAllAvailablePets()).isEmpty();
    }

    @Test
    void addNewPetMemberSuccess()
    {
        var pet = getPet();

        doReturn(true).when(repository).addPet(any(Pet.class));

        assertTrue(service.addNewPetMember(pet));
    }

    @Test
    void addNewPetMemberIfNullPet()
    {
        Pet pet = null;

        doReturn(false).when(repository).addPet(null);

        assertFalse(service.addNewPetMember(pet));
    }

    @Test
    void adoptPetSuccess()
    {
        var pet = getPet();
        var expectedPet = Pet.builder()
                .id(2)
                .adopted(true)
                .name("cat:kitty")
                .hungerLevel(10)
                .happinessLevel(10)
                .petType(PetType.MAMMAL)
                .build();

        doReturn(Optional.of(pet)).when(repository).getPetById(pet.getId());

        var actual = service.adoptPet(pet.getId());

        Mockito.verify(repository).updatePet(petCaptor.capture());

        assertEquals(actual, expectedPet);
        assertThat(petCaptor.getValue()).isEqualTo(expectedPet);
    }

    @Test
    void adoptPetFailIfNoPetFound()
    {
        var pet = getPet();

        doReturn(Optional.empty()).when(repository).getPetById(pet.getId());

        assertThrows(PetNotFoundException.class, () -> service.adoptPet(pet.getId()));
    }

    @Test
    void adoptPetFailIfAlreadyAdopted()
    {
        var pet = getPet();
        pet.setAdopted(true);

        doReturn(Optional.of(pet)).when(repository).getPetById(pet.getId());

        assertThrows(PetAlreadyAdoptedException.class, () -> service.adoptPet(pet.getId()));
    }

    @Test
    void feedPetSuccess()
    {
        var pet = getPet();
        int expectedReduce = 2;
        int expectedHunger = 8;

        doReturn(Optional.of(pet)).when(repository).getPetById(pet.getId());
        doReturn(expectedReduce).when(invService).retainFoodByType(Mockito.any(FoodType.class), Mockito.any(Integer.class));

        service.feedPet(pet, FoodType.FOR_MAMMALS, expectedReduce);

        Mockito.verify(repository).updatePet(petCaptor.capture());

        assertThat(petCaptor.getValue().getHungerLevel()).isEqualTo(expectedHunger);
    }

    @Test
    void feedPetFailImproperFoodChosen()
    {
        var pet = getPet();

        assertThrows(ImproperFoodChoiceException.class, () -> service.feedPet(pet, FoodType.FOR_FISHES, 2));
        Mockito.verifyNoInteractions(invService, repository);
    }

    @Test
    void feedPetFailIfPetNotFound()
    {
        var pet = getPet();

        doReturn(Optional.empty()).when(repository).getPetById(pet.getId());

        assertThrows(PetNotFoundException.class, () -> service.feedPet(pet, FoodType.FOR_MAMMALS, 2));
        Mockito.verifyNoInteractions(invService);
    }

    private static Pet getPet()
    {
        return Pet.builder()
                .id(2)
                .name("cat:kitty")
                .hungerLevel(10)
                .happinessLevel(10)
                .petType(PetType.MAMMAL)
                .build();
    }
}
