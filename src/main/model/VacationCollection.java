package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// A collection of Vacation that will be viewed in the application

public class VacationCollection implements Writable {
    private List<Vacation> vacationCollection; // The list of vacations

    public VacationCollection() {
        this.vacationCollection = new ArrayList<>();
    }

    public List<Vacation> getVacationCollection() {
        return vacationCollection;
    }

    //MODIFIES: this
    //EFFECTS: add a vacation to the collection and returns true, if there is another vacation with the same name
    // return false and the collection is unchanged
    public boolean addVacation(Vacation vacation) {
        boolean result = true;

        for (Vacation v:this.vacationCollection) {
            if (vacation.getName().equals(v.getName())) {
                result = false;
                break;
            }
        }

        if (result) {
            this.vacationCollection.add(vacation);
            EventLog.getInstance().logEvent(new Event("New Vacation Added: " + vacation.getName()));
        }
        return result;
    }

    // EFFECTS: returns true if the vacation collection is empty
    public boolean isEmpty() {
        return vacationCollection.isEmpty();
    }

    // EFFECTS: returns the size of the vacation collection
    public int size() {
        return vacationCollection.size();
    }

    //REQUIRES: the give position must exist
    //EFFECTS: return the vacation that is at the given position starting at 1
    public Vacation getVacationByPosition(int position) {
        return this.vacationCollection.get(position - 1);
    }


    // EFFECTS: Returns true if the input name already exists in another vacation
    public boolean containsName(String newName) {
        boolean result = false;
        for (Vacation vacation : vacationCollection) {
            if (newName.equals(vacation.getName())) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vacations", vacationCollectionToJson());
        return jsonObject;
    }

    // EFFECTS: returns the vacations in this vacation collection as a JSON Array
    private JSONArray vacationCollectionToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Vacation vacation : vacationCollection) {
            jsonArray.put(vacation.toJson());
        }

        return jsonArray;
    }

    // REQUIRES: the given index must be in range
    // MODIFIES: this
    // EFFECTS: removes the given vacation from vacationCollection
    public void remove(int index) {
        Vacation toRemove = vacationCollection.get(index);
        vacationCollection.remove(index);
        EventLog.getInstance().logEvent(new Event("Removed vacation " + toRemove.getName()));
    }
}
