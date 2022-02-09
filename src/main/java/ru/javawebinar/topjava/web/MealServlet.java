package ru.javawebinar.topjava.web;
import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private static String LIST_MEALS = "/meals.jsp";
    private MealDaoImpl dao;

    @Override
    public void init() throws ServletException {
        dao = new MealDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = req.getParameter("action");

        if(action == null) {
            req.setAttribute("meals",MealsUtil.filteredByStreams(dao.getAllUsers(), LocalTime.MIN, LocalTime.MAX, dao.CALORIES_PER_DAY));
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }
        else {
            if (action.equalsIgnoreCase("delete")){
                log.debug("to delete Meals");
                int mealId = Integer.parseInt(req.getParameter("id"));
                dao.deleteMeal(mealId);
                req.setAttribute("meals", MealsUtil.filteredByStreams(dao.getAllUsers(), LocalTime.MIN, LocalTime.MAX, dao.CALORIES_PER_DAY));
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
              } else if (action.equalsIgnoreCase("edit")) {
                log.debug("to edit Meals");
                req.getRequestDispatcher("/editMeal.jsp").forward(req, resp);
             }
             else if (action.equalsIgnoreCase("update")){
               log.debug("to update Meals");
                Integer id = Integer.parseInt(req.getParameter("id"));
               req.setAttribute("meals", dao.readMeal(id));
               req.getRequestDispatcher("/editMeal.jsp").forward(req, resp);
            } else {
                log.debug("to update Meals");
                req.setAttribute("meals",MealsUtil.filteredByStreams(dao.getAllUsers(), LocalTime.MIN, LocalTime.MAX, dao.CALORIES_PER_DAY));
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
             }
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        log.debug("doPost to meals");

        req.setCharacterEncoding("UTF-8");
        String mealId = req.getParameter("id");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("date"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        if(mealId == null || mealId.isEmpty()) {
            log.debug("create Meal");
            dao.createMeal(dateTime, description, calories);
        }
        else {
            log.debug("update Meal");
            dao.updateMeal(Integer.parseInt(mealId), dateTime, description, calories);
        }

        RequestDispatcher view = req.getRequestDispatcher(LIST_MEALS);
        req.setAttribute("meals", MealsUtil.filteredByStreams(dao.getAllUsers(), LocalTime.MIN, LocalTime.MAX, dao.CALORIES_PER_DAY));
        view.forward(req, res);

    }
}
