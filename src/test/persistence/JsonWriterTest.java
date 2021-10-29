package persistence;

import model.Attraction;
import model.Vacation;
import model.VacationCollection;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Tests based on design from JSONSerializationDemo
public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            VacationCollection vacationCollection = new VacationCollection();
            JsonWriter writer = new JsonWriter("./data/not\0alegalfile:name.json");
            writer.open();
            fail("IOException Excepted");
        } catch (IOException ioException) {
            // pass the test
        }
    }

    @Test
    void testWriterEmptyVacationCollection() {
        try {
            VacationCollection vacationCollection = new VacationCollection();
            JsonWriter writer = new JsonWriter("./data/testWriterEmpty.json");
            writer.open();
            writer.write(vacationCollection);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmpty.json");
            vacationCollection = reader.read();
            assertEquals(0, vacationCollection.getVacationCollection().size());
        } catch (IOException ioException) {
            fail("Exception not expected");
        }
    }

    @Test
    void testWriterGeneralVacationCollection() {
        try {
            VacationCollection vacationCollection = setUpTestVacationCollection();
            JsonWriter writer = new JsonWriter("./data/testWriterGeneral.json");
            writer.open();
            writer.write(vacationCollection);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneral.json");
            // Same tests as reader general tests
            vacationCollection = reader.read();
            List<Vacation> vacations = vacationCollection.getVacationCollection();
            assertEquals("Japan", vacations.get(0).getName());
            assertEquals("Paris", vacations.get(1).getName());
            assertEquals("US", vacations.get(2).getName());

            List<Attraction> japanAttractions = vacations.get(0).getAttractions();
            List<Attraction> parisAttractions = vacations.get(1).getAttractions();
            List<Attraction> usAttractions = vacations.get(2).getAttractions();

            // Attractions in Japan are all default data other than the name
            assertEquals("Osaka", japanAttractions.get(0).getName());
            assertEquals("Tokyo", japanAttractions.get(1).getName());

            // Attraction in paris has non-default data
            assertEquals("Louvre", parisAttractions.get(0).getName());
            assertEquals(5, parisAttractions.get(0).getPriority());
            assertTrue(parisAttractions.get(0).isCompleted());
            assertEquals("Museum", parisAttractions.get(0).getComments().get(0));

            // US attractions not tested to avoid redundancy
        } catch (IOException ioException) {
            fail("Not Expecting Exception");
        }
    }

    // EFFECTS: Returns a vacationCollection that will be passed into testWriterGeneral.json
    private VacationCollection setUpTestVacationCollection() {
        VacationCollection vacationCollection = new VacationCollection();
        Vacation japan = new Vacation("Japan");
        Vacation paris = new Vacation("Paris");
        Vacation us = new Vacation("US");

        japan.addAttraction(new Attraction("Osaka"));
        japan.addAttraction(new Attraction("Tokyo"));

        Attraction louvre = new Attraction("Louvre");
        louvre.markCompleted();
        louvre.changePriority(5);
        louvre.addComment("Museum");
        paris.addAttraction(louvre);

        us.addAttraction(new Attraction("LA"));

        vacationCollection.addVacation(japan);
        vacationCollection.addVacation(paris);
        vacationCollection.addVacation(us);

        return vacationCollection;
    }
}
