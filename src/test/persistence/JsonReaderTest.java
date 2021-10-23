package persistence;

import model.Attraction;
import model.Vacation;
import model.VacationCollection;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
// Tests based on design from JSONSerializationDemo
public class JsonReaderTest extends JsonTest{

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noFile.json");
        try {
            VacationCollection vacationCollection = reader.read();
            fail("Expected IOException");
        } catch (IOException ioException) {
            // pass the test
        }
    }

    @Test
    void testReaderEmptyVacationCollection() {
        JsonReader reader = new JsonReader("./data/testReaderEmpty.json");
        try{
            VacationCollection vacationCollection = reader.read();
            assertEquals(0, vacationCollection.getVacationCollection().size());
        } catch (IOException ioException) {
            fail("Can't read empty file");
        }
    }

    @Test
    void testReaderGeneralVacationCollection() {
        JsonReader reader = new JsonReader("./data/testReaderGeneral.json");
        try {
            VacationCollection vacationCollection = reader.read();
            List<Vacation> vacations = vacationCollection.getVacationCollection();
            assertEquals("Japan", vacations.get(0).getName());
            assertEquals("Paris", vacations.get(1).getName());
            assertEquals("US", vacations.get(2).getName());

            List<Attraction> japanAttractions = vacations.get(0).getAttractions();
            List<Attraction> parisAttractions = vacations.get(1).getAttractions();
            List<Attraction> usAttractions = vacations.get(2).getAttractions();

            // Attractions in japan are all default data other than the name
            assertEquals("Osaka", japanAttractions.get(0).getName());
            assertEquals("Tokyo", japanAttractions.get(1).getName());

            // Attraction in paris has non-default data
            assertEquals("Louvre", parisAttractions.get(0).getName());
            assertEquals(5, parisAttractions.get(0).getPriority());
            assertTrue(parisAttractions.get(0).isCompleted());
            assertEquals("Museum", parisAttractions.get(0).getComments().get(0));

            // US attractions not tested to avoid redundancy
        } catch (IOException ioException) {
            fail("Exception should not have been thrown.");
        }

    }


}
