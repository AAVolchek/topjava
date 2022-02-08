package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private final static int caloriesPerDay = 2000;
    private List<Meal> meals;
    transient private static AtomicInteger idCounter= new AtomicInteger(0);

    @Override
    public void init() throws ServletException {
        meals = new CopyOnWriteArrayList<>();
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(idCounter.addAndGet(1), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        req.setAttribute("meals",MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, caloriesPerDay));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
