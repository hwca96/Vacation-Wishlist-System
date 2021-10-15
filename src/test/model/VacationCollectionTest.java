package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VacationCollectionTest {

    VacationCollection testVacationCollection;
    Vacation testVacation;

    @BeforeEach
    void setUp() {
        testVacationCollection = new VacationCollection();
        testVacation = new Vacation("test");
    }

    @Test
    void addVacationNoDuplicateTest() {

        assertEquals(0, testVacationCollection.size());

        assertTrue(testVacationCollection.addVacation(testVacation));

        assertEquals(1, testVacationCollection.size());

        assertFalse(testVacationCollection.addVacation(testVacation));
    }


    @Test
    void isEmptyTest() {
        assertTrue(testVacationCollection.isEmpty());

        testVacationCollection.addVacation(testVacation);

        assertEquals(1, testVacationCollection.size());
        assertFalse(testVacationCollection.isEmpty());
    }

    @Test
    void sizeTest() {
        assertEquals(0, testVacationCollection.size());

        testVacationCollection.addVacation(testVacation);

        assertEquals(1, testVacationCollection.size());
        //Compare with Arraylist size method
        assertEquals(testVacationCollection.getVacationCollection().size(), testVacationCollection.size());
    }

    @Test
    void getVacationByPositionTest() {
        for (int i = 1; i <= 5; i++) {
            Vacation v = new Vacation("test" + i);
            testVacationCollection.addVacation(v);
        }

        for (int i = 1; i <= 5; i++) {
            String result = testVacationCollection.getVacationByPosition(i).getName();
            assertEquals("test" + i, result);
        }
    }
}
