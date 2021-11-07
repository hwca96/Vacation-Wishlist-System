package ui;

import model.Attraction;
import model.Vacation;
import model.VacationCollection;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

public class VacationManagement extends JFrame {

    private JMenuBar menuBar;
    private VacationPanel vacationPanel;
    private AttractionPanel attractionPanel;

    private VacationCollection vacationCollection;
    private Vacation currentVacation = null;
    private Attraction currentAttraction = null;
    private static final String JSON_STORE = "./data/vacationCollection.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;

    // Constructs the main VacationManagementSystem window
    // MODIFIES: this
    public VacationManagement() throws FileNotFoundException {
        super("Vacation Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));


        initializeDataFields();
        initializeUiFields();

        setVisible(true);

    }

    // MODIFIES: this
    // EFFECTS: initialized the fields and functionality of the ui components
    private void initializeUiFields() {
        addLogo();

        setUpMenu();
        setJMenuBar(menuBar);

        vacationPanel = new VacationPanel(vacationCollection);

        add(vacationPanel);

    }

    // MODIFIES: this
    // EFFECTS: initialized the fields and functionality of the data components
    private void initializeDataFields() {
        vacationCollection = new VacationCollection();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        loadVacationCollection();
    }

    // MODIFIES: this
    // EFFECTS: Changes the program's image icon
    private void addLogo() {
        ImageIcon image = new ImageIcon("./src/main/ui/Images/logo.png");
        setIconImage(image.getImage());
    }

    // MODIFIES: this
    // EFFECTS: Set up the menu bar and add menu items to each menu selection
    private void setUpMenu() {
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenu helpMenu = new JMenu("Help");

        addFileMenuItems(fileMenu);
//        addViewMenuItems(viewMenu); //TODO
//        addHelpMenuItems(helpMenu); //TODO

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
    }

    // MODIFIES: this, fileMenu
    // EFFECTS: add menu items to the file menu and handles action depending on the selection in this menu
    private void addFileMenuItems(JMenu fileMenu) {
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        ActionListener actionListener = e -> {
            if (e.getSource().equals(loadItem)) {
                loadVacationCollection();
                System.out.println("loaded");
            }
            if (e.getSource().equals(saveItem)) {
                saveVacationCollection();
                System.out.println("saved");
            }
            if (e.getSource().equals(exitItem)) {
                System.exit(0);
            }
        };

        loadItem.addActionListener(actionListener);
        fileMenu.addActionListener(actionListener);
        exitItem.addActionListener(actionListener);

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
    }

    // MODIFIES: this
    // EFFECTS: Load the vacationCollection data from JSON file at JSON_STORE
    private void loadVacationCollection() {
        try {
            vacationCollection = jsonReader.read();
            System.out.println("Loaded data from: " + JSON_STORE); //TODO
        } catch (IOException ioException) {
            System.out.println("Error while loading file from: " + JSON_STORE); //TODO
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the current vacationCollection data to a JSON file to JSON_STORE
    private void saveVacationCollection() {
        try {
            jsonWriter.open();
            jsonWriter.write(vacationCollection);
            jsonWriter.close();
            System.out.println("Saved data to " + JSON_STORE); //TODO
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Error while saving file to: " + JSON_STORE); //TODO
        }
    }

}
