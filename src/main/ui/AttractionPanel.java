package ui;

import model.Attraction;
import model.Vacation;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// The Panel containing information on the attractions in a selected vacation
public class AttractionPanel extends JPanel {
    public static final String CHECK_PNG = "./src/main/ui/Images/check.png";
    public static final String FILLED_STAR_PNG = "./src/main/ui/Images/filled_star.png";
    public static final String EMPTY_STAR_PNG = "./src/main/ui/Images/empty_star.png";
    private Vacation vacation = null;
    private JLabel nullVacationLabel;
    private JLabel emptyLabel;
    private JPanel attractionPanel;
    private JScrollPane scrollPane;

    private static final int WIDTH = VacationPanel.WIDTH / 2;
    private static final int HEIGHT = VacationPanel.HEIGHT;
    private static final int GAP = 50;

    // MODIFIES: this
    // EFFECTS: constructs the AttractionPanel
    public AttractionPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        setEmptyLabel();
        setNullVacationLabel();

        initializeUi();
    }

    // MODIFIES: this
    // EFFECTS: set up the label to show if no vacation is selected
    private void setNullVacationLabel() {
        nullVacationLabel = new JLabel();
        nullVacationLabel.setText("No Vacation is Selected");
        nullVacationLabel.setFont(VacationPanel.STANDARD_FONT);
        nullVacationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // MODIFIES: this
    // EFFECTS: set up the label to show if the vacation has no attractions
    private void setEmptyLabel() {
        emptyLabel = new JLabel();
        emptyLabel.setText("There is no attraction in this vacation");
        emptyLabel.setFont(VacationPanel.STANDARD_FONT);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // MODIFIES: this
    // EFFECTS: initiates the graphical components of this panel
    private void initializeUi() {
        if (vacation == null) {
            add(nullVacationLabel);
        } else if (vacation.getAttractions().size() == 0) {
            add(emptyLabel);
        } else {

            attractionPanel = new JPanel();
            attractionPanel.setLayout(new BoxLayout(attractionPanel, BoxLayout.Y_AXIS));

            setVacationNameLabel();
            setAttractionGraphics();

            scrollPane = new JScrollPane(attractionPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            add(scrollPane);
        }
    }

    // MODIFIES: this
    // EFFECTS: initiates the graphics representing information of the attractions
    private void setAttractionGraphics() {
        for (Attraction attraction : vacation.getAttractions()) {

            JPanel attractionContainer = new JPanel();
            attractionContainer.setLayout(new BoxLayout(attractionContainer, BoxLayout.Y_AXIS));
            attractionContainer.setMaximumSize(new Dimension(WIDTH, HEIGHT / 5));

            JPanel buttonContainer = new JPanel();
            JButton editNameButton = addEditNameButton(attraction);
            JButton toggleCompletedButton = addToggleCompletedButton(attraction);
            JButton changePriorityButton = addChangePriorityButton(attraction);
            JButton addCommentButton = addAddCommentButton(attraction);
            JButton deleteCommentButton = addDeleteButton();
            JButton deleteAttractionButton = addDeleteAttractionButton(attraction);

            JPanel labelStarContainer = addLabelStarContainer(attraction);

            JList list = createCommentList(attraction, deleteCommentButton);
            JScrollPane comments = new JScrollPane(list);

            JPanel attractionInfoContainer = setUpAttractionInfoContainer(labelStarContainer, comments);

            deleteButtonAddActionListener(attraction, deleteCommentButton, list);

            addAttractionButtons(buttonContainer, editNameButton, toggleCompletedButton,
                    changePriorityButton, addCommentButton, deleteCommentButton, deleteAttractionButton);

            attractionContainer.add(attractionInfoContainer);


            attractionPanel.add(attractionContainer);
            attractionPanel.add(buttonContainer);
            attractionPanel.add(Box.createRigidArea(new Dimension(0, GAP)));
        }
    }

    // EFFECTS: returns a JPanel containing the labelStarContainer and comments in a vertical box layout
    private JPanel setUpAttractionInfoContainer(JPanel labelStarContainer, JScrollPane comments) {
        JPanel attractionInfoContainer = new JPanel();
        attractionInfoContainer.setLayout(new BoxLayout(attractionInfoContainer, BoxLayout.Y_AXIS));
        attractionInfoContainer.add(labelStarContainer);
        attractionInfoContainer.add(comments);
        attractionInfoContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        return attractionInfoContainer;
    }

    // MODIFIES: buttonContainer
    // EFFECTS: Add the buttons related to editing the attraction to the buttonContainer
    private void addAttractionButtons(JPanel buttonContainer, JButton editNameButton,
                                      JButton toggleCompletedButton, JButton changePriorityButton,
                                      JButton addCommentButton, JButton deleteCommentButton,
                                      JButton deleteAttractionButton) {
        buttonContainer.add(editNameButton);
        buttonContainer.add(toggleCompletedButton);
        buttonContainer.add(changePriorityButton);
        buttonContainer.add(addCommentButton);
        buttonContainer.add(deleteCommentButton);
        buttonContainer.add(deleteAttractionButton);
    }

    // MODIFIES: this, deleteCommentButton
    // EFFECTS: add an ActionListener to deleteCommentButton with context about selected comments from list.
    // The panel is update to then updated.
    private void deleteButtonAddActionListener(Attraction attraction, JButton deleteCommentButton, JList list) {
        deleteCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedPosition = list.getSelectedIndex() + 1;
                attraction.removeComment(selectedPosition);
                updateVacation(vacation);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Returns a JList consisting of comments of the attraction.
    // The ListSelectionListener will enable deleteCommentButton if a comment is selected.
    private JList createCommentList(Attraction attraction, JButton deleteCommentButton) {
        DefaultListModel listModel = new DefaultListModel();
        for (String comment : attraction.getComments()) {
            listModel.addElement(comment);
        }

        JList list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    if (listModel.size() == 0 || list.getSelectedIndex() == -1) {
                        deleteCommentButton.setEnabled(false);
                    } else {
                        deleteCommentButton.setEnabled(true);
                    }
                }
            }
        });
        list.setFont(VacationPanel.STANDARD_FONT);
        return list;
    }

    // EFFECTS: returns a JPanel containing the name and a star graphic representing the priority of the attraction
    private JPanel addLabelStarContainer(Attraction attraction) {
        JPanel labelStarContainer = new JPanel();
        labelStarContainer.setLayout(new BorderLayout());

        JLabel attractionLabel = makeAttractionNameLabel(attraction, 32);

        JPanel stars = makeStarsGraphics(attraction);
        labelStarContainer.add(attractionLabel, BorderLayout.WEST);
        labelStarContainer.add(stars, BorderLayout.EAST);
        return labelStarContainer;
    }

    // MODIFIES: this
    // EFFECTS: sets up and returns the deleteAttractionButton which will delete the attraction when clicked.
    private JButton addDeleteAttractionButton(Attraction attraction) {
        ImageIcon deleteIcon = new ImageIcon("./src/main/ui/Images/delete_icon.png");
        JButton deleteAttractionButton = new JButton(deleteIcon);
        deleteAttractionButton.setText("Delete Attraction");

        deleteAttractionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vacation.removeAttraction(attraction.getName());

                updateVacation(vacation);
            }
        });

        return deleteAttractionButton;
    }

    // EFFECTS: sets up and returns the deleteCommentButton with no ActionListener
    private JButton addDeleteButton() {
        JButton deleteCommentButton = new JButton("Delete Comment");
        deleteCommentButton.setEnabled(false);

        return deleteCommentButton;
    }

    // MODIFIES: this, attraction
    // EFFECTS: sets up and returns the addCommentButton which will prompt to add a new comment when clicked
    private JButton addAddCommentButton(Attraction attraction) {
        JButton addCommentButton = new JButton("Add Comment");
        addCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(null, "New Comment: ", null);
                attraction.addComment(input);

                updateVacation(vacation);
            }
        });
        return addCommentButton;

    }

    // MODIFIES: this, attraction
    // EFFECTS: sets up and returns changePriorityButton which prompts to change the priority level of the attraction
    private JButton addChangePriorityButton(Attraction attraction) {
        JButton changePriorityButton = new JButton("Change Priority");
        changePriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer[] priorityLevels = {0, 1, 2, 3, 4, 5};
                int choice = JOptionPane.showOptionDialog(null, "Selected one of the following priorities",
                        "Change Priority", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                        priorityLevels, 0);

                attraction.changePriority(choice);
                updateVacation(vacation);
            }
        });

        return changePriorityButton;
    }

    // MODIFIES: this, attraction
    // EFFECTS: sets up and returns toggleCompleteButton
    // which will mark the attraction the opposite of its completed status
    private JButton addToggleCompletedButton(Attraction attraction) {
        JButton toggleCompleteButton = new JButton("Toggle Completed");
        toggleCompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (attraction.isCompleted()) {
                    attraction.markNotCompleted();
                } else {
                    attraction.markCompleted();
                }
                updateVacation(vacation);
            }
        });

        return toggleCompleteButton;
    }

    // MODIFIES: this, attraction
    // EFFECTS: sets up and returns editNameButton which prompts for a new attraction name when clicked
    private JButton addEditNameButton(Attraction attraction) {
        JButton editNameButton = new JButton("Edit Name");
        editNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(null, "Enter a New Attraction Name: ", null);
                if (Attraction.checkNameValid(input)) {
                    attraction.changeName(input);
                    updateVacation(vacation);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Name, please enter another name",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return editNameButton;
    }

    // EFFECTS: Returns a JLabel of the attraction name with font size i
    private JLabel makeAttractionNameLabel(Attraction attraction, int i) {
        String name = attraction.getName();
        JLabel attractionLabel = new JLabel();
        attractionLabel.setText(name);
        attractionLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, i));
        if (attraction.isCompleted()) {
            ImageIcon checkMark = new ImageIcon(CHECK_PNG);
            attractionLabel.setIcon(checkMark);
        }
        return attractionLabel;
    }

    // EFFECTS: returns a JPanel containing filled stars and empty stars
    // representing the priority level of the attraction
    private JPanel makeStarsGraphics(Attraction attraction) {
        JPanel stars = new JPanel();
        stars.setLayout(new BoxLayout(stars, BoxLayout.X_AXIS));
        int priority = attraction.getPriority();
        ImageIcon filledStarImage = new ImageIcon(FILLED_STAR_PNG);
        ImageIcon emptyStarImage = new ImageIcon(EMPTY_STAR_PNG);
        for (int i = 1; i <= priority; i++) {
            JLabel filledStar = new JLabel(filledStarImage);
            filledStar.setAlignmentX(Component.CENTER_ALIGNMENT);
            stars.add(filledStar);
        }
        for (int i = 1; i <= attraction.getMaxPriority() - priority; i++) {
            JLabel emptyStar = new JLabel(emptyStarImage);
            emptyStar.setAlignmentX(Component.CENTER_ALIGNMENT);
            stars.add(emptyStar);
        }
        return stars;
    }

    // MODIFIES: this
    // EFFECTS: Sets up a label of the vacation name and adds it to attractionPanel
    private void setVacationNameLabel() {
        JLabel vacationNameLabel = new JLabel();
        vacationNameLabel.setText(vacation.getName());
        vacationNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        vacationNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 64));
        attractionPanel.add(vacationNameLabel);
        attractionPanel.add(Box.createRigidArea(new Dimension(0, GAP)));
    }

    // MODIFIES: this
    // EFFECTS: updates the graphics to show the most up-to-date data
    public void updateVacation(Vacation vacation) {
        this.vacation = vacation;
        removeAll();
        initializeUi();
        updateUI();
    }
}
