package persistence;

import model.Attraction;
import model.Vacation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkVacation(String vacationName, Vacation vacation) {
        assertEquals(vacationName, vacation.getName());
    }

    protected void checkAttraction(String attractionName, int priority, List<String> comments,
                                   Boolean completed, Attraction attraction) {
        assertEquals(attractionName, attraction.getName());
        assertEquals(priority, attraction.getPriority());
        assertEquals(completed, attraction.isCompleted());
        assertEquals(comments, attraction.getComments());
    }
}
