package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDao {

    List<Meal> getAllUsers();
    void createMeal(LocalDateTime dateTime, String description, int calories);
    Meal readMeal (int mealId);
    void updateMeal (int mealId, LocalDateTime dateTime, String description, int calories);
    void deleteMeal (int mealId);
}
