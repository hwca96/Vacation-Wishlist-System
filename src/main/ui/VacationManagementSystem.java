package ui;

import model.Attraction;
import model.Vacation;
import model.VacationCollection;

import java.util.Scanner;

public class VacationManagementSystem {
    private VacationCollection vacationCollection;
    private Scanner input = new Scanner(System.in);

    // EFFECT: Start the application
    public VacationManagementSystem() {
        runVacationManagementSystem();
    }

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

    private void init() {
        this.vacationCollection = new VacationCollection();
    }

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
        System.out.println("\tquit");
    }

    private void processVacationCollectionCommand(String command) {
        if (command.equals("n")) {
            addNewVacation();
        } else if (isNumeric(command)) {
            int select = Integer.parseInt(command);
            boolean inVacationMenu = true;
            Vacation selectedVacation = vacationCollection.getVacationByPosition(select);

            while (inVacationMenu) {
                displayVacationMenu(selectedVacation);
                String commandVacation = input.next();

                if (commandVacation.equals("back")) {
                    inVacationMenu = false;
                } else {
                    processVacationCommand(commandVacation, selectedVacation);
                }
            }
        } else {
            System.out.println("Invalid Input...");
        }
    }

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

    private boolean isNumeric(String command) {
        try {
            Integer.parseInt(command);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

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
        System.out.println("\tback");
    }

    private void processVacationCommand(String vacationCommand, Vacation selectedVacation) {
        if (vacationCommand.equals("n")) {
            addNewAttraction(selectedVacation);
        } else if (isNumeric(vacationCommand)) {
            int selected = Integer.parseInt(vacationCommand);
            boolean inAttractionMenu = true;
            Attraction selectedAttraction = selectedVacation.getAttractionByPosition(selected);

            while (inAttractionMenu) {
                displayAttractionMenu(selectedAttraction);
                String commandAttraction = input.next();

                if (commandAttraction.equals("back")) {
                    inAttractionMenu = false;
                } else {
                    processAttractionCommand(commandAttraction, selectedAttraction);
                }
            }
        } else {
            System.out.println("\nThat is not a valid input...");
        }
    }

    private void addNewAttraction(Vacation vacation) {
        System.out.println("Enter the attraction name: ");
        input.nextLine(); // This is needed to clear the keyboard buffers so the line can be recorded
        String name = input.nextLine();
        Attraction attractionToAdd = new Attraction(name);
        if (vacation.addAttraction(attractionToAdd)) {
            System.out.println("Attraction Added");
        } else {
            System.out.println("There is another attraction with the same name. Please try another name.");
        }
    }

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
        System.out.println("\tback");
    }

    private void processAttractionCommand(String command, Attraction attraction) {
        if (command.equals("a")) {
            attraction.markCompleted();
        } else if (command.equals("b")) {
            attraction.markNotCompleted();
        } else if (command.equals("c")) {
            boolean notValid = true;
            while (notValid) {
                System.out.println("\nSelect an integer between 0 and 5: ");
                String priority = input.next();
                if (isNumeric(priority) && Integer.parseInt(priority) >= 0 && Integer.parseInt(priority) <= 5) {
                    notValid = false;
                    attraction.changePriority(Integer.parseInt(priority));
                }
            }
        } else if (command.equals("d")) {
            System.out.println("\nWhat is your comment: ");
            input.nextLine(); // This is needed to clear the keyboard buffers so the line can be recorded
            String comment = input.nextLine();
            attraction.addComment(comment);
        } else {
            System.out.println("\nThat is not a valid input...");
        }
    }
}
