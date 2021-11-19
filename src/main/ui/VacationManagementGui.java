package ui;

import model.VacationCollection;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

// Vacation Management System Graphical User Interface
public class VacationManagementGui extends JFrame implements ActionListener {
    public static final Font MENU_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 24);

    private JMenuBar menuBar;
    private VacationPanel vacationPanel;

    private VacationCollection vacationCollection;
    private static final String JSON_STORE = "./data/vacationCollection.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    JMenuItem loadItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    // Constructs the main VacationManagementSystem window
    // MODIFIES: this
    public VacationManagementGui() throws FileNotFoundException {
        super("Vacation Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));


        initializeDataFields();
        initializeUiFields();

        this.pack();

        setVisible(true);

    }

    // MODIFIES: this
    // EFFECTS: initialized the fields and functionality of the ui components
    private void initializeUiFields() {
        addLogo();

        setUpMenu();
        setJMenuBar(menuBar);

        vacationPanel = new VacationPanel(vacationCollection);

        add(vacationPanel, BorderLayout.WEST);

    }

    // MODIFIES: this
    // EFFECTS: initialized the fields and functionality of the data components
    private void initializeDataFields() {
        vacationCollection = new VacationCollection();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
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
        fileMenu.setFont(MENU_FONT);

        addFileMenuItems(fileMenu);

        menuBar.add(fileMenu);
    }


    // MODIFIES: this, fileMenu
    // EFFECTS: add menu items to the file menu and handles action depending on the selection in this menu
    private void addFileMenuItems(JMenu fileMenu) {
        loadItem = new JMenuItem("Load");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        loadItem.setFont(MENU_FONT);
        saveItem.setFont(MENU_FONT);
        exitItem.setFont(MENU_FONT);

        loadItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
    }

    // MODIFIES: this
    // EFFECTS: Load the vacationCollection data from JSON file at JSON_STORE
    private void loadVacationCollection() {
        try {
            vacationCollection = jsonReader.read();
            vacationPanel.updateVacationCollection(vacationCollection);

        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error while loading file from: " + JSON_STORE,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the current vacationCollection data to a JSON file to JSON_STORE
    private void saveVacationCollection() {
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

    // EFFECTS: handles the action of each menu items
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveItem) {
            saveVacationCollection();
        }
        if (e.getSource() == loadItem) {
            loadVacationCollection();
        }
        if (e.getSource() == exitItem) {
            System.exit(0);
        }
    }
}
