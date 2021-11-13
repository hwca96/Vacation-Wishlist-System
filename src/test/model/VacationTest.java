package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VacationTest {
    private Vacation testVacation;

    @BeforeEach
    void runBefore() {
        testVacation = new Vacation("test");
    }

    @Test
    void addAttractionTrueTest() {
        assertEquals(0, testVacation.getAttractions().size());

        assertTrue(testVacation.addAttraction(new Attraction("test1")));

        assertEquals(1, testVacation.getAttractions().size());
    }

    @Test
    void addAttractionFalseTest() {
        testVacation.addAttraction(new Attraction("test1"));

        assertFalse(testVacation.addAttraction(new Attraction("test1")));
        assertEquals(1, testVacation.getAttractions().size());
    }

    @Test
    void removeAttractionEmptyTest() {
        assertEquals(0, testVacation.getAttractions().size());

        testVacation.removeAttraction("test1");

        assertEquals(0, testVacation.getAttractions().size());
    }

    @Test
    void removeAttractionNonEmptyTest() {
        testVacation.addAttraction(new Attraction("test1"));
        testVacation.addAttraction(new Attraction("test2"));

        testVacation.removeAttraction("test1");

        assertEquals(1, testVacation.getAttractions().size());
        assertEquals("test2", testVacation.getAttractionByPosition(1).getName());
    }

    @Test
    void getAttractionByPositionTest() {
        assertNull(testVacation.getAttractionByPosition(1));

        testVacation.addAttraction(new Attraction("test1"));
        testVacation.addAttraction(new Attraction("test2"));

        assertEquals("test1", testVacation.getAttractionByPosition(1).getName());
        assertEquals("test2", testVacation.getAttractionByPosition(2).getName());

        // Invalid positions
        assertNull(testVacation.getAttractionByPosition(-5));
        assertNull(testVacation.getAttractionByPosition(3));
    }

    @Test
    void getAttractionByNameTest() {
        assertNull(testVacation.getAttractionByName("does not exist"));

        testVacation.addAttraction(new Attraction("test1"));
        testVacation.addAttraction(new Attraction("test2"));

        assertEquals(testVacation.getAttractionByPosition(1), testVacation.getAttractionByName("test1"));
        assertEquals(testVacation.getAttractionByPosition(2), testVacation.getAttractionByName("test2"));

        assertNull(testVacation.getAttractionByName("does not exist"));
    }

    @Test
    void sortAttractionsPriorityTest() {
        Attraction lowPriorityAttraction = new Attraction("low priority");
        Attraction mediumPriorityAttraction = new Attraction("medium priority");
        Attraction highPriorityAttraction = new Attraction("high priority");

        lowPriorityAttraction.changePriority(0);
        mediumPriorityAttraction.changePriority(2);
        highPriorityAttraction.changePriority(5);

        testVacation.addAttraction(lowPriorityAttraction);
        testVacation.addAttraction(mediumPriorityAttraction);
        testVacation.addAttraction(highPriorityAttraction);

        testVacation.sortAttractionsPriority();

        assertEquals(lowPriorityAttraction, testVacation.getAttractionByPosition(3));
        assertEquals(mediumPriorityAttraction, testVacation.getAttractionByPosition(2));
        assertEquals(highPriorityAttraction, testVacation.getAttractionByPosition(1));
    }

    @Test
    void toStringTest() {
        assertEquals(testVacation.getName(), testVacation.toString());
    }

}
