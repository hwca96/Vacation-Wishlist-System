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

public class AttractionPanel extends JPanel {
    private Vacation vacation = null;
    private JLabel emptyLabel;
    private JPanel attractionPanel;
    private JScrollPane scrollPane;

    private static final int WIDTH = 1980 / 2;
    private static final int HEIGHT = 1080;
    private static final int GAP = 50;

    public AttractionPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        setEmptyLabel();

        initializeUi();
    }

    private void setEmptyLabel() {
        emptyLabel = new JLabel();
        emptyLabel.setText("No Vacation is Selected");
        emptyLabel.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 32));
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void initializeUi() {
        if (vacation == null) {
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
            JButton deleteCommentButton = addDeleteButton(attraction);
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


    private JPanel setUpAttractionInfoContainer(JPanel labelStarContainer, JScrollPane comments) {
        JPanel attractionInfoContainer = new JPanel();
        attractionInfoContainer.setLayout(new BoxLayout(attractionInfoContainer, BoxLayout.Y_AXIS));
        attractionInfoContainer.add(labelStarContainer);
        attractionInfoContainer.add(comments);
        attractionInfoContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        return attractionInfoContainer;
    }

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
        list.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 32));
        return list;
    }

    private JPanel addLabelStarContainer(Attraction attraction) {
        JPanel labelStarContainer = new JPanel();
        labelStarContainer.setLayout(new BorderLayout());

        JLabel attractionLabel = makeAttractionNameLabel(attraction, 32);

        JPanel stars = makeStarsGraphics(attraction);
        labelStarContainer.add(attractionLabel, BorderLayout.WEST);
        labelStarContainer.add(stars, BorderLayout.EAST);
        return labelStarContainer;
    }

    private JButton addDeleteAttractionButton(Attraction attraction) {
        ImageIcon deleteIcon = new ImageIcon("./src/main/ui/Images/delete_icon.png");
        JButton deleteAttractionButton = new JButton(deleteIcon);

        deleteAttractionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vacation.removeAttraction(attraction.getName());

                updateVacation(vacation);
            }
        });

        return deleteAttractionButton;
    }

    private JButton addDeleteButton(Attraction attraction) {
        JButton deleteCommentButton = new JButton("Delete Comment");
        deleteCommentButton.setEnabled(false);

        return deleteCommentButton;
    }

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

    private JButton addEditNameButton(Attraction attraction) {
        JButton editNameButton = new JButton("Edit Name");
        editNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(null, "Enter a New Attraction Name: ", null);
                if (attraction.checkNameValid(input)) {
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

    private JLabel makeAttractionNameLabel(Attraction attraction, int i) {
        String name = attraction.getName();
        JLabel attractionLabel = new JLabel();
        attractionLabel.setText(name);
        attractionLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, i));
        if (attraction.isCompleted()) {
            ImageIcon checkMark = new ImageIcon("./src/main/ui/Images/check.png");
            attractionLabel.setIcon(checkMark);
        }
        return attractionLabel;
    }

    private JPanel makeStarsGraphics(Attraction attraction) {
        JPanel stars = new JPanel();
        stars.setLayout(new BoxLayout(stars, BoxLayout.X_AXIS));
        int priority = attraction.getPriority();
        ImageIcon filledStarImage = new ImageIcon("./src/main/ui/Images/filled_star.png");
        ImageIcon emptyStarImage = new ImageIcon("./src/main/ui/Images/empty_star.png");
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

    private void setVacationNameLabel() {
        JLabel vacationNameLabel = new JLabel();
        vacationNameLabel.setText(vacation.getName());
        vacationNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        vacationNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 64));
        attractionPanel.add(vacationNameLabel);
        attractionPanel.add(Box.createRigidArea(new Dimension(0, GAP)));
    }

    public void updateVacation(Vacation vacation) {
        this.vacation = vacation;
        removeAll();
        initializeUi();
        updateUI();
    }
}
