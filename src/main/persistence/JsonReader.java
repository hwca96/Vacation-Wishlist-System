package persistence;

import model.Attraction;
import model.Vacation;
import model.VacationCollection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

// Represents a json reader that reads a vacation collection stored in a json file
public class JsonReader {
    private String source;


    // EFFECTS: constructs the json reader with a given source file directory
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads the json file and returns the vacation collection contained in the file
    // throws IOException if an error occurs reading data from file
    public VacationCollection read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseVacationCollection(jsonObject);
    }

    // EFFECTS: reads source json file as string and returns the string
    private String readFile(String source) throws IOException {
        StringBuilder dataBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> dataBuilder.append(s));
        }

        return dataBuilder.toString();
    }

    // EFFECTS: parse the json object and returns the stored vacation collection
    public VacationCollection parseVacationCollection(JSONObject jsonObject) {
        VacationCollection vacationCollection = new VacationCollection();
        addVacations(vacationCollection, jsonObject); //TODO
        return vacationCollection;
    }

    // MODIFIES: vacationCollection
    // EFFECTS: parse vacations stored in the json and adds them to the vacation collection
    private void addVacations(VacationCollection vacationCollection, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("vacations");
        for (Object json : jsonArray) {
            JSONObject vacationToAdd = (JSONObject) json;
            addVacation(vacationCollection, vacationToAdd);
        }
    }

    private void addVacation(VacationCollection vacationCollection, JSONObject jsonObject) {
        String name = jsonObject.getString("vacationName");
        Vacation vacation = new Vacation(name);

        addAttractions(vacation, jsonObject);

        vacationCollection.addVacation(vacation);
    }

    private void addAttractions(Vacation vacation, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("attractions");
        for (Object json : jsonArray) {
            JSONObject attractionToAdd = (JSONObject) json;
            addAttraction(vacation, attractionToAdd);
        }
    }

    private void addAttraction(Vacation vacation, JSONObject jsonObject) {
        String name = jsonObject.getString("attractionName");

        Attraction attraction = new Attraction(name);

        boolean completed = jsonObject.getBoolean("completed");
        if (completed) {
            attraction.markCompleted();
        }

        int priority = jsonObject.getInt("priority");
        attraction.changePriority(priority);

        JSONArray jsonArray = jsonObject.getJSONArray("comments");
        for (Object json : jsonArray) {
            JSONObject commentToAdd = (JSONObject) json;
            String comment = commentToAdd.getString("comment");
            attraction.addComment(comment);
        }
    }

}
