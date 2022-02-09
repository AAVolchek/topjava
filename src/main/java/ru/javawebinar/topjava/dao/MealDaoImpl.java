package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImpl implements MealDao {

    public static final int CALORIES_PER_DAY = 2000;
    private static List<Meal> meals = new CopyOnWriteArrayList<>();
    transient private static AtomicInteger idCounter= new AtomicInteger(0);

    public MealDaoImpl () {
        //meals = new CopyOnWriteArrayList<>();
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public List<Meal> getAllUsers() {
        return meals;
    }

    @Override
    public void createMeal(LocalDateTime dateTime, String description, int calories) {
        meals.add(new Meal(idCounter.addAndGet(1), dateTime ,description ,calories));
    }

    @Override
    public Meal readMeal(int mealId) {
        for (int i = 0; i < meals.size() ; i++) {
            if(mealId == meals.get(i).getMealId())
            {
                return meals.get(i);
            }
        }

      return new Meal();
    }

    @Override
    public void updateMeal(int mealId, LocalDateTime dateTime, String description, int calories) {

        for (int i = 0; i < meals.size() ; i++) {
           if(mealId == meals.get(i).getMealId())
           {
               Meal meal = meals.get(i);
               meal.setDateTime(dateTime);
               meal.setDescription(description);
               meal.setCalories(calories);
               break;
           }
        }
    }

    @Override
    public void deleteMeal(int mealId) {

        for (int i = 0; i < meals.size(); i++) {
            if(mealId == meals.get(i).getMealId())
            {
                meals.remove(i);
                break;
            }
        }
    }

}
