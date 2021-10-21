package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// A vacation with its list of attractions

public class Vacation {
    private String name; // the name of the vacation
    private List<Attraction> listOfAttractions; // the list of attractions for this vacation

    //REQUIRE: vacationName is not empty
    //EFFECT: instantiates a new vacation a name and an empty attraction list
    public Vacation(String vacationName) {
        name = vacationName;
        listOfAttractions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Attraction> getAttractions() {
        return listOfAttractions; //stub
    }

    //MODIFIES: this
    //EFFECT: adds an attraction to the list of attractions and return true.
    // If there is another attraction with the same name, the list is unchanged and returns false.
    public boolean addAttraction(Attraction attraction) {
        boolean result = true;
        for (Attraction a:listOfAttractions) {
            if (a.getName().equals(attraction.getName())) {
                result = false;
                break;
            }
        }
        if (result) {
            listOfAttractions.add(attraction);
        }
        return result;
    }

    // MODIFIES: this
    // EFFECT: Searches the listOfAttraction and removes the attraction with the given name
    // NOTE: Unused for this iteration, will be using for future functionalities
    public void removeAttraction(String name) {
        listOfAttractions.removeIf(a -> a.getName().equals(name));
    }

    // EFFECT: Returns the Attraction at the given position starting at 1.
    // Return null if given position does not exist.
    public Attraction getAttractionByPosition(int pos) {
        Attraction result = null;

        if (pos >= 1 && pos <= listOfAttractions.size()) {
            result = listOfAttractions.get(pos - 1);
        }

        return result;
    }

    // EFFECT: Returns the Attraction with the give name. Returns null if the no such name exists.
    // NOTE: Unused for this iteration, will be using for future functionalities
    public Attraction getAttractionByName(String name) {
        Attraction result = null;

        for (Attraction a:listOfAttractions) {
            if (a.getName().equals(name)) {
                result = a;
            }
        }
        return result;
    }

    //MODIFIES: this
    //EFFECT: Sorts the listOfAttractions by priority in descending order\
    //NOTE: this method is unused in P1, will be used for later functionalities.
    public void sortAttractionsPriority() {
        listOfAttractions.sort(Comparator.comparing(Attraction::getPriority).reversed());
    }
}
