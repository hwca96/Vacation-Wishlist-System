package ui;

import model.Vacation;
import model.VacationCollection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VacationPanel extends JPanel implements ListSelectionListener {

    private VacationCollection vacationCollection;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 1000;
    private static final int BUTTONS_HEIGHT = 200;
    private static final int ROWS = 5;

    private JList list;
    private DefaultListModel listModel;

    JButton newButton;
    JButton deleteButton;

    public VacationPanel(VacationCollection vc) {
        this.vacationCollection = vc;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());

        listModel = new DefaultListModel();
        for (Vacation vacation : vacationCollection.getVacationCollection()) {
            listModel.addElement(vacation);
        }

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(ROWS);
        list.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 32));


        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT - BUTTONS_HEIGHT));

        newButton = new JButton("New Vacation"); //TODO
        newButton.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 32));
        deleteButton = new JButton("Delete Vacation"); //TODO
        deleteButton.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 32));
        deleteButton.addActionListener(new DeleteListener());
        deleteButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);

        add(listScrollPane, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // listener for the add button
    class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO
        }
    }

    // listener for the delete button
    class DeleteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = list.getSelectedIndex();
            Vacation selectedVacation = vacationCollection.getVacationByPosition(selectedIndex + 1);
            vacationCollection.remove(selectedVacation);
            //TODO very confused
        }
    }

    public int getSelectedVacationIndex() {
        return list.getSelectedIndex();
    }

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
}
