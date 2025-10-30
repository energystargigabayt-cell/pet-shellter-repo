package net.paulweider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
public class Pet
{
    int id;
    String name;
    @Setter boolean adopted;
    int hungerLevel;
    int happinessLevel;
    PetType petType;

    enum HealthStatus { HEALTHY, BIT_INJURED, INJURED, SICK }
}
