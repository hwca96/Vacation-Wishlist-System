package persistence;

import org.json.JSONObject;

public interface Writable {
    // EFFECTS: returns the object as a Json object
    JSONObject toJson();
}
