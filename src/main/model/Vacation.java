package model;

import java.util.ArrayList;
import java.util.List;

public class Vacation {
    private String name; // the name of the vacation
    private List<Attraction> listOfAttractions; // the list of attractions for this vacation

    //REQUIRE: vacationName is not empty
    //EFFECT: instantiates a new vacation a name and an empty attraction list
    public Vacation(String vacationName) {
        name = vacationName;
        listOfAttractions = new ArrayList<Attraction>();
    }

    //MODIFIES: this
    //EFFECT: adds an attraction to the list of attractions and return true.
    // If there is another attraction with the same name, the list is unchanged and returns false.
    public boolean addAttraction(Attraction attraction) {
        return false; // stub
    }

    //MODIFIES: this
    //EFFECT: Searches the listOfAttraction and removes the attraction with the given name
    public void removeAttraction(String name) {
        // stub
    }

    public List<Attraction> getAttractions() {
        return listOfAttractions; //stub
    }

    public List<Attraction> getAttractionsPriority() {
        return listOfAttractions; //stub
    }

}
