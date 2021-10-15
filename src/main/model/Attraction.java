package model;

import java.util.ArrayList;
import java.util.List;

public class Attraction {
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
        this.comments = new ArrayList<String>();
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

    // MODIFIES: this
    // EFFECT: Change the completed status to true
    public void markCompleted() {
        this.completed = true;
    }

    // MODIFIES: this
    // EFFECT: Changes the completed status to false
    public void markNotCompleted() {
        this.completed = false;
    }

    // MODIFIES: this
    // EFFECT: Changes the priority level of the attraction and returns true.
    // If the inputted priority is not in the valid range [0,5], return false and does not change the priority.
    public boolean changePriority(int priority) {
        if (priority >= 0 && priority <= 5) {
            this.priority = priority;
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECT: Adds a comment to the attraction
    public void addComment(String comment) {
        this.comments.add(comment);
    }

    // MODIFIES: this
    // EFFECT: Removes a comment at the given position and returns true.
    // If the given index does not exist then do nothing
    // NOTE: Unused for this iteration, will be using for future functionalities
    public void removeComment(int position) {
        if (position >= 0 && position <= this.comments.size()) {
            this.comments.remove(position - 1);
        }
    }
}
