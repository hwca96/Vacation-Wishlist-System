package ui;

import model.Attraction;
import model.Vacation;
import model.VacationCollection;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

// Panel containing information about VacationCollection, Vacation, and Attraction
public class VacationPanel extends JPanel implements ListSelectionListener, ActionListener {

    public static final Font STANDARD_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 32);
    public static final int WIDTH = 1980;
    public static final int HEIGHT = 1080;
    private static final int BUTTONS_HEIGHT = 100;
    private static final int ROWS = 5;

    private VacationCollection vacationCollection;

    private static final String JSON_STORE = "./data/vacationCollection.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JList<Object> list;
    private DefaultListModel<Vacation> listModel;

    private JButton newButton;
    private JButton deleteButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton addAttractionButton;

    private JSplitPane splitPane;
    private AttractionPanel attractionPanel;


    // CONSTRUCTOR
    // MODIFIES: this
    public VacationPanel(VacationCollection vc) {
        this.vacationCollection = vc;
        this.setLayout(new BorderLayout());

        initializeUi();
    }

    private void initializeUi() {
        attractionPanel = new AttractionPanel();

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        setUpLists();

        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT - BUTTONS_HEIGHT));

        setUpButtons();

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(addAttractionButton);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, attractionPanel);
        splitPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(WIDTH / 4);

        this.add(splitPane);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: Sets up the new, delete, load, save, and add attraction buttons
    private void setUpButtons() {
        newButton = new JButton("New Vacation");
        newButton.setFont(STANDARD_FONT);
        newButton.addActionListener(this);

        deleteButton = new JButton("Delete Vacation");
        deleteButton.setFont(STANDARD_FONT);
        deleteButton.addActionListener(this);
        deleteButton.setEnabled(false);

        loadButton = new JButton("Load");
        loadButton.setFont(STANDARD_FONT);
        loadButton.addActionListener(this);

        saveButton = new JButton("Save");
        saveButton.setFont(STANDARD_FONT);
        saveButton.addActionListener(this);

        addAttractionButton = new JButton("Add Attraction");
        addAttractionButton.setFont(STANDARD_FONT);
        addAttractionButton.addActionListener(this);
        addAttractionButton.setEnabled(false);
    }

    // MODIFIES: this
    // EFFECTS: sets up the DefaultListModel and JList objects for the list of vacations
    private void setUpLists() {
        listModel = new DefaultListModel();
        for (Vacation vacation : vacationCollection.getVacationCollection()) {
            listModel.addElement(vacation);
        }

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(ROWS);
        list.setFont(STANDARD_FONT);
    }


    // EFFECTS: Returns the index of the selected vacation
    public int getSelectedVacationIndex() {
        return list.getSelectedIndex();
    }

    // EFFECTS: Returns the selected Vacation, return null if nothing is selected
    public Vacation getSelectedVacation() {
        if (getSelectedVacationIndex() == -1) {
            return null;
        } else {
            return vacationCollection.getVacationByPosition(getSelectedVacationIndex() + 1);
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates the graphics to display the most current status of the data
    public void updateVacationCollection(VacationCollection vacationCollection) {
        this.vacationCollection = vacationCollection;
        removeAll();
        initializeUi();
        updateUI();
    }

    // MODIFIES: this
    // EFFECTS: handles the change of selection in the Scroll Pane,
    // only enable the delete button and add attraction button
    // if the list is not empty and a vacation has been selected
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            attractionPanel.updateVacation(getSelectedVacation());
            if (listModel.size() == 0 || list.getSelectedIndex() == -1) {
                deleteButton.setEnabled(false);
                addAttractionButton.setEnabled(false);
            } else {
                deleteButton.setEnabled(true);
                addAttractionButton.setEnabled(true);
            }
        }
    }

    // EFFECTS: handles the newButton, deleteButton, loadButton, saveButton, and addAttractionButton Actions
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) (e.getSource());
        if (source == newButton) {
            handleNewButton();
        }
        if (source == deleteButton) {
            handleDeleteButton();
        }

        if (source == loadButton) {
            handleLoadButton();
        }

        if (source == saveButton) {
            handleSaveButton();
        }
        if (source == addAttractionButton) {
            handleAddAttractionButton();
        }
    }

    // MODIFIES: this
    // EFFECT: Prompts user for a new attraction name.
    // If the name is valid, add a new attraction to the selected vacation.
    // If the name is not valid, display an error. Then refreshes the UI
    private void handleAddAttractionButton() {
        String input = JOptionPane.showInputDialog(null, "Enter a New Attraction Name: ", null);
        if (checkNameValid(input)) {
            Attraction attractionToAdd = new Attraction(input);
            Vacation selectedVacation = getSelectedVacation();
            selectedVacation.addAttraction(attractionToAdd);
            attractionPanel.updateVacation(selectedVacation);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Name, please enter another name",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: return true if the name is not empty and not full of white spaces
    public static boolean checkNameValid(String input) {
        return !input.equals("") && input.trim().length() > 0;
    }

    // MODIFIES: this
    // EFFECT: deletes the selected vacation and refreshes the UI
    private void handleDeleteButton() {
        int selectedIndex = getSelectedVacationIndex();
        vacationCollection.remove(selectedIndex);
        updateVacationCollection(vacationCollection);
        JOptionPane.showMessageDialog(null, "Vacation Deleted", "Message", JOptionPane.PLAIN_MESSAGE);
    }

    private void handleNewButton() {
        String newName = null;
        boolean notValid = true;
        while (notValid) {
            String input = JOptionPane.showInputDialog(null, "Enter a Name for the Vacation: ", null);
            notValid = checkValidName(input);
            newName = input;
        }

        Vacation vacationToAdd = new Vacation(newName);
        if (vacationCollection.addVacation(vacationToAdd)) {
            updateVacationCollection(vacationCollection);
        }
    }

    // EFFECTS: returns true if the input name is not empty, full of white spaces,
    // and already exists in another vacation. Display pop up messages to show the corresponding errors.
    private boolean checkValidName(String newName) {
        Boolean notValid = false;
        if (!checkNameValid(newName)) {
            JOptionPane.showMessageDialog(null, "Invalid Name, please enter another name",
                    "Error", JOptionPane.ERROR_MESSAGE);
            notValid = true;
        } else if (vacationCollection.containsName(newName)) {
            JOptionPane.showMessageDialog(null, "This name already exists, please enter another name",
                    "Error", JOptionPane.ERROR_MESSAGE);
            notValid = true;
        }
        return notValid;
    }


    // MODIFIES: this
    // EFFECTS: Load the vacationCollection data from JSON file at JSON_STORE
    private void handleLoadButton() {
        try {
            vacationCollection = jsonReader.read();
            updateVacationCollection(vacationCollection);

        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error while loading file from: " + JSON_STORE,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the current vacationCollection data to a JSON file to JSON_STORE
    private void handleSaveButton() {
        try {
            jsonWriter.open();
            jsonWriter.write(vacationCollection);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Saved data to " + JSON_STORE,
                    "Message", JOptionPane.PLAIN_MESSAGE);

        } catch (FileNotFoundException fileNotFoundException) {
            JOptionPane.showMessageDialog(null, "Error while loading file from: " + JSON_STORE,
                    "Error", JOptionPane.ERROR_MESSAGE);

        }
    }
}
