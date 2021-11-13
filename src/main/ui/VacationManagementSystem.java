package ui;

import model.Attraction;
import model.Vacation;
import model.VacationCollection;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Vacation Management System Console Interface
public class VacationManagementSystem {
    private VacationCollection vacationCollection;
    private Scanner input;
    private static final String JSON_STORE = "./data/vacationCollection.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECT: Start the application
    public VacationManagementSystem() throws FileNotFoundException {
        input = new Scanner(System.in);
        vacationCollection = new VacationCollection();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runVacationManagementSystem();
    }

    // MODIFIES: this
    // EFFECTS: sets up the application, processes user inputs, and display the ui information
    private void runVacationManagementSystem() {
        boolean running = true;
        String command;

        init();

        while (running) {
            displayVacationCollectionMenu();

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("quit")) {
                running = false;
            } else {
                processVacationCollectionCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the VacationCollection
    private void init() {
        this.vacationCollection = new VacationCollection();
    }

    // EFFECTS: Displays the vacation collection menu showing vacations and the available input options
    private void displayVacationCollectionMenu() {
        if (vacationCollection.isEmpty()) {
            System.out.println("There is no vacation stored in the system");
        } else {
            int i = 1;
            for (Vacation v : vacationCollection.getVacationCollection()) {
                System.out.println("\n" + i + ": " + v.getName());
                i++;
            }
        }

        System.out.println("\nSelect one of the following options:");

        int n = 1;
        for (Vacation v: vacationCollection.getVacationCollection()) {
            System.out.println("\t" + n + " - Go to " + v.getName());
            n++;
        }
        System.out.println("\tn - new vacation");
        System.out.println("\ts - save to file");
        System.out.println("\tl - load from file");
        System.out.println("\tquit");
    }

    // MODIFIES: this
    // EFFECTS: Processes the input from the vacation collection menu
    private void processVacationCollectionCommand(String command) {
        if (command.equals("n")) {
            addNewVacation();
        } else if (command.equals("s")) {
            saveWorkRoom();
        } else if (command.equals("l")) {
            loadWorkRoom();
        } else if (isNumeric(command)) {
            int select = Integer.parseInt(command);
            try {
                vacationMenu(select);
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                System.out.println("That position does not exist.");
            }
        } else {
            System.out.println("Invalid Input...");
        }
    }


    private void vacationMenu(int select) throws IndexOutOfBoundsException {
        Vacation selectedVacation = vacationCollection.getVacationByPosition(select);
        boolean inVacationMenu = true;

        while (inVacationMenu) {
            displayVacationMenu(selectedVacation);
            String commandVacation = input.next();

            if (commandVacation.equals("back")) {
                inVacationMenu = false;
            } else {
                processVacationCommand(commandVacation, selectedVacation);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds a new vacation to the vacation collection with a name inputted by the user,
    // but prints an error message if another vacation with the same name already exists.
    private void addNewVacation() {
        System.out.println("Enter the vacation name: ");
        input.nextLine(); // This is needed to clear the keyboard buffers so the line can be recorded
        String name = input.nextLine();
        Vacation vacationToAdd = new Vacation(name);
        if (vacationCollection.addVacation(vacationToAdd)) {
            System.out.println("Vacation Added");
        } else {
            System.out.println("There is another vacation with the same name. Please try a different name.");
        }
    }

    // EFFECTS: Produce true if the given string can be converted into an integer
    private boolean isNumeric(String command) {
        try {
            Integer.parseInt(command);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    // EFFECTS: Displays the vacation menu showing the attractions in the given vacation
    // and the available inputs options
    private void displayVacationMenu(Vacation vacation) {
        if (vacation.getAttractions().isEmpty()) {
            System.out.println("There is no attractions stored in this vacation");
        } else {
            int i = 1;

            for (Attraction attraction : vacation.getAttractions()) {
                System.out.println("\n" + i + ": " + attraction.getName());
                System.out.println("\tCompleted: " + attraction.isCompleted());
                System.out.println("\tPriority: " + attraction.getPriority());
                i++;
            }
        }
        int n = 1;
        System.out.println("\nSelect One of the Following Options:");
        for (Attraction attraction:vacation.getAttractions()) {
            System.out.println("\t" + n + " - " + "Go to " + attraction.getName());
            n++;
        }
        System.out.println("\tn - add new attraction");
        System.out.println("\ts - save to file");
        System.out.println("\tback");
    }

    // MODIFIES: this
    // EFFECTS: processes the input from the vacation menu
    private void processVacationCommand(String vacationCommand, Vacation selectedVacation) {
        if (vacationCommand.equals("n")) {
            addNewAttraction(selectedVacation);
        } else if (vacationCommand.equals("s")) {
            saveWorkRoom();
        } else if (isNumeric(vacationCommand)) {
            int selected = Integer.parseInt(vacationCommand);
            try {
                attractionMenu(selectedVacation, selected);
            } catch (NullPointerException nullPointerException) {
                System.out.println("That position does not exist.");
            }
        } else {
            System.out.println("\nThat is not a valid input...");
        }
    }

    private void attractionMenu(Vacation selectedVacation, int selected) {
        Attraction selectedAttraction = selectedVacation.getAttractionByPosition(selected);
        boolean inAttractionMenu = true;

        while (inAttractionMenu) {
            displayAttractionMenu(selectedAttraction);
            String commandAttraction = input.next();

            if (commandAttraction.equals("back")) {
                inAttractionMenu = false;
            } else {
                processAttractionCommand(commandAttraction, selectedAttraction);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds a new attraction to the given vacation with a name inputted by the user,
    // but prints an error message if there is already another attraction with the same name
    private void addNewAttraction(Vacation vacation) {
        System.out.println("Enter the attraction name: ");
        input.nextLine(); // This is needed to clear the keyboard buffers so the line can be recorded
        String name = input.nextLine();
        Attraction attractionToAdd = new Attraction(name);
        if (vacation.addAttraction(attractionToAdd)) {
            System.out.println("Attraction Added");
        } else {
            System.out.println("There is another attraction with the same name. Please try a different name.");
        }
    }

    // EFFECTS: displays the attraction menu showing details of the attraction with available input options
    private void displayAttractionMenu(Attraction attraction) {
        System.out.println("\n" + attraction.getName());
        System.out.println("\tCompleted: " + attraction.isCompleted());
        System.out.println("\tPriority: " + attraction.getPriority());
        System.out.println("\tComments:");

        int i = 1;
        for (String comment:attraction.getComments()) {
            System.out.println("\n" + i + ": " + comment);
            i++;
        }

        System.out.println("\nSelect One of the Following Options:");
        System.out.println("\ta - mark completed");
        System.out.println("\tb - mark not completed");
        System.out.println("\tc - set priority");
        System.out.println("\td - add comment");
        System.out.println("\ts - save to file");
        System.out.println("\tback");
    }

    // MODIFIES: this
    // EFFECTS: processes the input from the attraction menu
    private void processAttractionCommand(String command, Attraction attraction) {
        if (command.equals("a")) {
            attraction.markCompleted();
        } else if (command.equals("b")) {
            attraction.markNotCompleted();
        } else if (command.equals("c")) {
            changePriority(attraction);
        } else if (command.equals("d")) {
            System.out.println("\nWhat is your comment: ");
            input.nextLine(); // This is needed to clear the keyboard buffers so the line can be recorded
            String comment = input.nextLine();
            attraction.addComment(comment);
        } else if (command.equals("s")) {
            saveWorkRoom();
        } else {
            System.out.println("\nThat is not a valid input...");
        }
    }

    private void changePriority(Attraction attraction) {
        boolean notValid = true;
        while (notValid) {
            System.out.println("\nSelect an integer between 0 and 5: ");
            String priority = input.next();
            if (isNumeric(priority) && Integer.parseInt(priority) >= 0 && Integer.parseInt(priority) <= 5) {
                notValid = false;
                attraction.changePriority(Integer.parseInt(priority));
            }
        }
    }

    private void loadWorkRoom() {
        try {
            vacationCollection = jsonReader.read();
            System.out.println("Loaded data from: " + JSON_STORE);
        } catch (IOException ioException) {
            System.out.println("Error while loading file from: " + JSON_STORE);
        }
    }

    private void saveWorkRoom() {
        try {
            jsonWriter.open();
            jsonWriter.write(vacationCollection);
            jsonWriter.close();
            System.out.println("Saved data to " + JSON_STORE);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Error while saving file to: " + JSON_STORE);
        }
    }
}
