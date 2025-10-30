package net.paulweider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Food
{
    int id;
    String foodName;
    FoodType foodType;
    int count;

}
