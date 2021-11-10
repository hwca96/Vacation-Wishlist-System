package ui;

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

public class VacationPanel extends JPanel implements ListSelectionListener, ActionListener {

    private VacationCollection vacationCollection;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final int BUTTONS_HEIGHT = 200;
    private static final int ROWS = 5;

    private static final String JSON_STORE = "./data/vacationCollection.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JList list;
    private DefaultListModel listModel;

    private JButton newButton;
    private JButton deleteButton;
    private JButton loadButton;
    private JButton saveButton;

    private JSplitPane splitPane;
    private JPanel vacationPanel;
    private AttractionPanel attractionPanel;


    // CONSTRUCTOR
    // MODIFIES: this
    public VacationPanel(VacationCollection vc) {
        this.vacationCollection = vc;
        vacationPanel = new JPanel();
        splitPane = new JSplitPane();
        attractionPanel = new AttractionPanel();
        splitPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        splitPane.setLayout(new BorderLayout());

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

        vacationPanel.add(listScrollPane, BorderLayout.NORTH);
        vacationPanel.add(buttonPanel, BorderLayout.SOUTH);

        splitPane.add(vacationPanel);

        this.add(splitPane);
    }

    // MODIFIES: this
    // EFFECTS: Sets up the new and delete buttons
    private void setUpButtons() {
        newButton = new JButton("New Vacation");
        newButton.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 32));
        newButton.addActionListener(this);

        deleteButton = new JButton("Delete Vacation");
        deleteButton.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 32));
        deleteButton.addActionListener(this);
        deleteButton.setEnabled(false);

        loadButton = new JButton("Load");
        loadButton.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 32));
        loadButton.addActionListener(this);

        saveButton = new JButton("Save");
        saveButton.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 32));
        saveButton.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: sets up the DefaultListModel and JList objects
    private void setUpLists() {
        listModel = new DefaultListModel();
        for (Vacation vacation : vacationCollection.getVacationCollection()) {
            listModel.addElement(vacation);
        }

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(ROWS);
        list.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 32));
    }


    // EFFECTS: Returns the index of the selected vacation
    public int getSelectedVacationIndex() {
        return list.getSelectedIndex();
    }

    // MODIFIES: this
    // EFFECTS: handles the change of selection in the Scroll Pane,
    // only enable the delete button if the list is not empty and a vacation has been selected
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (listModel.size() == 0 || list.getSelectedIndex() == -1) {
                deleteButton.setEnabled(false);
            } else {
                deleteButton.setEnabled(true);
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: handles the newButton and deleteButton Actions
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

    }

    private void handleDeleteButton() {
        int selectedIndex = getSelectedVacationIndex();
        listModel.remove(selectedIndex);
        vacationCollection.remove(selectedIndex);
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
            listModel.addElement(vacationToAdd);
        }
    }

    // EFFECTS: returns true if the input name is not empty, full of white spaces,
    // and already exists in another vacation. Display pop up messages to show the corresponding errors.
    private boolean checkValidName(String newName) {
        Boolean notValid = false;
        if (newName == "" || newName.trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Invalid Name, please enter another name",
                    "Error", JOptionPane.ERROR_MESSAGE);
            notValid = true;
        } else if (containsName(newName)) {
            JOptionPane.showMessageDialog(null, "This name already exists, please enter another name",
                    "Error", JOptionPane.ERROR_MESSAGE);
            notValid = true;
        }
        return notValid;
    }

    // EFFECTS: Returns true if the input name already exists in another vacation
    private boolean containsName(String newName) {
        Boolean result = false;
        for (Vacation vacation : vacationCollection.getVacationCollection()) {
            if (newName == vacation.getName()) {
                result = true;
            }
        }
        return result;
    }

    // MODIFIES: this
    // EFFECTS: Load the vacationCollection data from JSON file at JSON_STORE
    private void handleLoadButton() {
        try {
            vacationCollection = jsonReader.read();
            for (Vacation vacation : vacationCollection.getVacationCollection()) {
                listModel.addElement(vacation);
            }

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
