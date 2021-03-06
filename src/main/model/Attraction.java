package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// An attraction with its completed status, priority, and comments

public class Attraction implements Writable {
    public static final int MAX_PRIORITY = 5;

    private String name; //the name of the attraction
    private boolean completed; //completion status of the attraction
    private int priority; //priority level of the attraction [0,5]
    private List<String> comments; //list of comments made about this attraction


    // REQUIRES: the name must be non-empty
    // EFFECT: Instantiates a new not completed attraction
    // with the given name, 0 level priority, and an empty comment list
    public Attraction(String attractionName) {
        this.name = attractionName;
        this.completed = false;
        this.priority = 0;
        this.comments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getPriority() {
        return priority;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setName(String name) {
        EventLog.getInstance().logEvent(new Event("Attraction: " + this.name + " changed name to " + name));
        this.name = name;
    }

    // MODIFIES: this
    // EFFECT: Change the completed status to true
    public void markCompleted() {
        this.completed = true;
        EventLog.getInstance().logEvent(new Event("Attraction: " + this.name + " marked as complete"));
    }

    // MODIFIES: this
    // EFFECT: Changes the completed status to false
    public void markNotCompleted() {
        this.completed = false;
        EventLog.getInstance().logEvent(new Event("Attraction: " + this.name + " marked as not complete"));
    }

    // MODIFIES: this
    // EFFECT: Changes the priority level of the attraction and returns true.
    // If the inputted priority is not in the valid range [0,5], return false and does not change the priority.
    public boolean changePriority(int priority) {
        if (priority >= 0 && priority <= 5) {
            this.priority = priority;
            EventLog.getInstance().logEvent(new Event("Attraction: " + this.name + " changed priority to "
                    + priority));
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECT: Adds a comment to the attraction
    public void addComment(String comment) {
        this.comments.add(comment);
        EventLog.getInstance().logEvent(new Event("Attraction: " + this.name + " added comment"));
    }

    // MODIFIES: this
    // EFFECT: Removes a comment at the given position and returns true.
    // If the given index does not exist then do nothing
    // NOTE: Unused for this iteration, will be using for future functionalities
    public void removeComment(int position) {
        if (position >= 0 && position <= this.comments.size()) {
            this.comments.remove(position - 1);
            EventLog.getInstance().logEvent(new Event("Attraction: " + this.name + " removed comment"));
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("attractionName", name);
        jsonObject.put("completed", completed);
        jsonObject.put("priority", priority);
        jsonObject.put("comments", commentsToJson());

        return jsonObject;
    }

    // EFFECTS: returns the comments in this attraction as a JSON array
    private JSONArray commentsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (String comment : comments) {
            jsonArray.put(comment);
        }

        return jsonArray;
    }

    @Override
    public String toString() {
        return getName();
    }
}
