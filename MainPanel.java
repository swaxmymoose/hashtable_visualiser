/*  07/02/2020
    MainPanel.java   
*/

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import static javax.swing.ScrollPaneConstants.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * MainPanel class contains DrawingPanel and InteractionPanel
 */
public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L; // to get rid of warning
    private JButton addValueBtn, createHashTableBtn, deleteValueBtn, searchValueBtn, resetBtn, toggleVerbose, createRandomBtn;
    private JTextField valueInputTxt, addCapacityTxt, deleteValueTxt, searchValueTxt;
    private JTable table;
    private HashTable hashTable;
    private JPanel drawingPanel, interactionPanel, operationPanel;
    private DefaultTableModel model;
    private MyTableCellRender cellRender;
    private JTextArea updateArea;
    private boolean stringHash = false, linearProb = false, quadProb = false, intHash = false, verboseOutput = false;
    private String collisionRes, hashingAlgorithm;
    private JLabel lastInsertLbl, lastDeletedLbl, lastSearchedLbl;
    private VisualiserMouseListener mouseListener = new VisualiserMouseListener();

    /**
     * VisualiserMouseListener class deals with mouse events
     * e.g. changes boldness of button labels on mouse hover
     */
    private class VisualiserMouseListener implements MouseListener {
        @Override
        public void mouseEntered(MouseEvent event) {
            if (event.getSource() == createHashTableBtn && createHashTableBtn.isEnabled() == true) {
                createHashTableBtn.setFont(createHashTableBtn.getFont().deriveFont(Font.BOLD));
            } else if (event.getSource() == addValueBtn && addValueBtn.isEnabled() == true) {
                addValueBtn.setFont(addValueBtn.getFont().deriveFont(Font.BOLD));
            } else if (event.getSource() == deleteValueBtn && deleteValueBtn.isEnabled() == true) {
                deleteValueBtn.setFont(deleteValueBtn.getFont().deriveFont(Font.BOLD));
            } else if (event.getSource() == searchValueBtn && searchValueBtn.isEnabled() == true) {
                searchValueBtn.setFont(searchValueBtn.getFont().deriveFont(Font.BOLD));
            } else if (event.getSource() == toggleVerbose && toggleVerbose.isEnabled() == true) {
                toggleVerbose.setFont(toggleVerbose.getFont().deriveFont(Font.BOLD));
            } else if (event.getSource() == createRandomBtn && createRandomBtn.isEnabled() == true) {
                createRandomBtn.setFont(createRandomBtn.getFont().deriveFont(Font.BOLD));
            } else if (event.getSource() == resetBtn && resetBtn.isEnabled() == true) {
                resetBtn.setFont(resetBtn.getFont().deriveFont(Font.BOLD));
            }
            
        }
        @Override
        public void mouseExited(MouseEvent event) {
            if (event.getSource() == createHashTableBtn && createHashTableBtn.isEnabled() == true) {
                createHashTableBtn.setFont(createHashTableBtn.getFont().deriveFont(~Font.BOLD));
            } else if (event.getSource() == addValueBtn && addValueBtn.isEnabled() == true) {
                addValueBtn.setFont(addValueBtn.getFont().deriveFont(~Font.BOLD));
            } else if (event.getSource() == deleteValueBtn && deleteValueBtn.isEnabled() == true) {
                deleteValueBtn.setFont(deleteValueBtn.getFont().deriveFont(~Font.BOLD));
            } else if (event.getSource() == searchValueBtn && searchValueBtn.isEnabled() == true) {
                searchValueBtn.setFont(searchValueBtn.getFont().deriveFont(~Font.BOLD));
            } else if (event.getSource() == toggleVerbose && toggleVerbose.isEnabled() == true) {
                toggleVerbose.setFont(toggleVerbose.getFont().deriveFont(~Font.BOLD));
            } else if (event.getSource() == createRandomBtn && createRandomBtn.isEnabled() == true) {
                createRandomBtn.setFont(createRandomBtn.getFont().deriveFont(~Font.BOLD));
            } else if (event.getSource() == resetBtn && resetBtn.isEnabled() == true) {
                resetBtn.setFont(resetBtn.getFont().deriveFont(~Font.BOLD));
            }
        }
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    

    /**
     * resetStartingText() will reset updateTextAreas text to its begining state 
     * which is dependent on the hash algorithm and collision resolution strategy enabled.
     */
    public void resetStartingText() {
        String hashAlgorithm = null;
        String collisionRes = null;
        if (this.stringHash) {
            hashAlgorithm = "string hash";
        } else if (this.intHash) {
            hashAlgorithm = "simple integer mod hash";
        }

        if (this.linearProb) {
            collisionRes = "linear probing";
        } else if (this.quadProb) {
            collisionRes = "quadratic probing";
        }

        updateArea.setText("");
        updateArea.append("Collision resolution strategy is: " + collisionRes + ".\n");
        updateArea.append("The hash function used is: " + hashAlgorithm + ".\n\n");
        updateArea.append(
                "To learn about collision resolution and hash functions,\nclick on the information buttons on main menu.\n");
        updateArea.append("-----------------------------------------------------\n\n");
        updateArea.append("Hash table operations are shown below...\n\n");
    }

    /**
     * MainPanel contains everything on Visualiser frame.
     */
    public MainPanel(String hashAlgorithm, String collisionRes) {
        // set hashing algorithm & collision resolution technique
        if (hashAlgorithm == "simple integer mod hash") {
            intHash = true;
        } else {
            stringHash = true;
        }

        if (collisionRes == "linear probing") {
            linearProb = true;
        } else {
            quadProb = true;
        }
        this.hashingAlgorithm = hashAlgorithm;
        this.collisionRes = collisionRes;
        setPreferredSize(new Dimension(1100, 600));
        setBackground(Color.decode("#C8E9CC"));
        drawingPanel = new DrawingPanel();
        interactionPanel = new InteractionPanel();
        add(interactionPanel);
        add(drawingPanel);
        resetStartingText();
    }

    /**
     * DrawingPanel contains the JTable hash table and JLabel last update
     */
    private class DrawingPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        public DrawingPanel() {

            // set up drawing panel
            setLayout(new GridLayout(0,2));
            setBackground(Color.decode("#A3C3D9"));
            setPreferredSize(new Dimension(890, 590));

            // set up table
            String[] columnNames = { "index", "key" };
            model = new DefaultTableModel(columnNames, 0);
            table = new JTable(model);
            table.setEnabled(false);
            cellRender = new MyTableCellRender();
            table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer());
            table.getColumnModel().getColumn(1).setCellRenderer(cellRender);
            table.setBackground(Color.decode("#C8E9CC"));
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.getViewport().setBackground(Color.decode("#C8E9CC"));

            // set up update text area
            updateArea = new JTextArea(37, 40);
            updateArea.setLineWrap(true);
            updateArea.setWrapStyleWord(true);
            updateArea.setEditable(false);
            updateArea.setBackground(Color.decode("#A3C3D9"));
            JScrollPane updateScrollPane = new JScrollPane(updateArea);
            updateScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            
            add(scrollPane);
            add(updateScrollPane);
        }
    }

    /**
     * InteractionPanel contains all user interactive components
     */
    private class InteractionPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        public InteractionPanel() {

            ButtonListener buttonListener = new ButtonListener();

            // set up panel
            setLayout(new GridLayout(3, 0));
            setBackground(Color.decode("#A3C3D9"));
            setPreferredSize(new Dimension(200, 590));

            // set up operation panel (contains buttons & input fields)
            operationPanel = new JPanel(new GridLayout(0, 2));
            operationPanel.setBackground(Color.decode("#A3C3D9"));
            add(operationPanel);

            // set up text fields and buttons on operation panel
            addCapacityTxt = new JTextField(3);
            operationPanel.add(addCapacityTxt);

            createHashTableBtn = new JButton("create");
            createHashTableBtn.addActionListener(buttonListener);
            createHashTableBtn.setToolTipText("insert a key into adjacent box and click me");
            createHashTableBtn.addMouseListener(mouseListener);
            operationPanel.add(createHashTableBtn);

            valueInputTxt = new JTextField(3);
            operationPanel.add(valueInputTxt);

            addValueBtn = new JButton("insert value");
            addValueBtn.addActionListener(buttonListener);
            addValueBtn.setEnabled(false);
            addValueBtn.addMouseListener(mouseListener);
            operationPanel.add(addValueBtn);

            deleteValueTxt = new JTextField(3);
            operationPanel.add(deleteValueTxt);

            deleteValueBtn = new JButton("delete value");
            deleteValueBtn.addActionListener(buttonListener);
            deleteValueBtn.setEnabled(false);
            deleteValueBtn.addMouseListener(mouseListener);
            operationPanel.add(deleteValueBtn);

            searchValueTxt = new JTextField(3);
            operationPanel.add(searchValueTxt);

            searchValueBtn = new JButton("search value");
            searchValueBtn.addActionListener(buttonListener);
            searchValueBtn.setEnabled(false);
            searchValueBtn.addMouseListener(mouseListener);
            operationPanel.add(searchValueBtn);

            JPanel miscButtonPanel = new JPanel(new GridLayout(5, 0));
            miscButtonPanel.setBackground(Color.decode("#A3C3D9"));
            add(miscButtonPanel);


            JButton invisBtn = new JButton();
            invisBtn.addActionListener(buttonListener);
            invisBtn.setVisible(false);
            miscButtonPanel.add(invisBtn);

            // set up reset button
            resetBtn = new JButton("reset hash table");
            resetBtn.addActionListener(buttonListener);
            resetBtn.setToolTipText("reset the hash table and clear text");
            resetBtn.addMouseListener(mouseListener);
            miscButtonPanel.add(resetBtn);

            createRandomBtn = new JButton("set capacity to random");
            createRandomBtn.addActionListener(buttonListener);
            createRandomBtn.addMouseListener(mouseListener);
            miscButtonPanel.add(createRandomBtn);

            toggleVerbose = new JButton("set verbose output");
            toggleVerbose.addActionListener(buttonListener);
            toggleVerbose.addMouseListener(mouseListener);
            miscButtonPanel.add(toggleVerbose);


            JButton invisBtn2 = new JButton();
            invisBtn2.addActionListener(buttonListener);
            invisBtn2.setVisible(false);
            miscButtonPanel.add(invisBtn2);
            miscButtonPanel.add(invisBtn2);

            JPanel colorPanel = new JPanel(new GridLayout(3, 0));
            
            lastInsertLbl = new JLabel("null");
            lastDeletedLbl = new JLabel("null");
            lastSearchedLbl = new JLabel("null");

            JPanel greenPanel = new JPanel(new GridLayout(2,0));
            greenPanel.setBackground(Color.GREEN);
            greenPanel.add(new JLabel("<html><font color='black'><strong>last key inserted</strong></font><html>"));
            greenPanel.setBorder(BorderFactory.createLineBorder(Color.white));
            greenPanel.add(lastInsertLbl);

            JPanel redPanel = new JPanel(new GridLayout(2, 0));
            redPanel.setBackground(Color.RED);
            redPanel.add(new JLabel("<html><font color='black'><strong>last key deleted</strong></font><html>"));
            redPanel.setBorder(BorderFactory.createLineBorder(Color.white));
            redPanel.add(lastDeletedLbl);

            JPanel bluePanel = new JPanel(new GridLayout(2, 0));
            bluePanel.setBackground(Color.cyan);
            bluePanel.add(new JLabel("<html><font color='black'><strong>last key searched</strong></font><html>"));
            bluePanel.setBorder(BorderFactory.createLineBorder(Color.white));
            bluePanel.add(lastSearchedLbl);

            colorPanel.add(greenPanel);
            colorPanel.add(redPanel);
            colorPanel.add(bluePanel);
            add(colorPanel);


        }

        /**
         * ButtonListner has one method (actionPerformed) that deals with button presses
         */
        private class ButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {

                // reset coloured cells
                cellRender.setLastDeleted(-1);
                cellRender.setLastInserted(-1);
                cellRender.setLastSearched(-1);

                if (e.getSource() == toggleVerbose) { // toggle verbose
                    verboseOutput = !verboseOutput;
                    if (verboseOutput) {
                        toggleVerbose.setText("set reduced output");
                    } else {
                        toggleVerbose.setText("set verbose output");
                    }
                    
                }

                /**
                 * set the text field corresponding to capacity to a random number < 500
                 * if quadtratic probing is selected it will be a prime number < 500
                 */
                if (e.getSource() == createRandomBtn) { // create random
                    
                    Random rand = new Random();
                    int r = Math.abs(rand.nextInt(500));
                    BigInteger b;
                    
                    if (collisionRes == "quadratic probing") {
                        b = new BigInteger(Integer.toString(r));
                        while (!b.isProbablePrime(1)) {
                            r = Math.abs(rand.nextInt(500));
                            b = new BigInteger(Integer.toString(r));      
                        } 
                    } else  {
                        b = new BigInteger(Integer.toString(r));
                    }
                    addCapacityTxt.setText(b.toString());
                }

                /**
                 * Creates a hash table by validating user input then creating the jtabel component and populating it with empty rows.
                 */
                if (e.getSource() == createHashTableBtn) { // create hash table
                    int tableSize = -1;
                    try {
                        tableSize = Integer.parseInt(addCapacityTxt.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane j = new JOptionPane();
                        j.showMessageDialog(null,
                                "Capacity must be a number\nless than or equal to 1,000\nand greater than 0.");
                        addCapacityTxt.setText("");
                        return;
                    }

                    if (tableSize > 1000 || tableSize <= 0) {
                        JOptionPane j = new JOptionPane();
                        j.showMessageDialog(null,
                                "Capacity must be a number\nless than or equal to 1,000\nand greater than 0.");
                        addCapacityTxt.setText("");
                        return;

                    }

                    if (collisionRes == "quadratic probing") {
                        BigInteger b = new BigInteger(Integer.toString(tableSize));
                        if (!b.isProbablePrime(1)) {
                            JOptionPane j = new JOptionPane();
                            j.showMessageDialog(null, "Did you read the quadratic probing information?\nCapacity must be a prime number for quadratic probing.");
                            addCapacityTxt.setText("");
                            return;
                        }


                    }
                   
                    if (tableSize != -1 && tableSize <= 1000 && tableSize > 0) {
                        createHashTableBtn.setEnabled(false);
                        addValueBtn.setEnabled(true);
                        deleteValueBtn.setEnabled(true);
                        searchValueBtn.setEnabled(true);
                        resetBtn.setEnabled(true);
                        addCapacityTxt.setText("");
                        hashTable = new HashTable(tableSize);
                        // fill rows up to 37
                        for (int i = 0; i < 37; i++) {
                            model.addRow(new Object[] { "", "" });
                        }
                        // add set rows to nul bellow table size, add new rows after table size
                        for (int i = 0; i < tableSize; i++) {
                           if (i < 37){
                                model.setValueAt("null", i, 1);
                                model.setValueAt(i, i, 0);
                           } else {
                                model.addRow(new Object[] { i, "null" });
                           }
                            
                        }
                        
                        updateArea.append("Created hash table of size " + tableSize + ".\n\n");
                    }
                }

                /**
                 * Inserts an item into the hash table by validating user input then using a list of collisions and insertions displays it on the update area.
                 */
                if (e.getSource() == addValueBtn) { // insert
                    String valueToInsert = valueInputTxt.getText();
                    valueInputTxt.setText("");
                    if (valueToInsert.trim().equals("")) {
                        JOptionPane j = new JOptionPane();
                        j.showMessageDialog(null, "Inserting nothing isn't going\nto help anyone.");
                    } else {
                        ArrayList<Integer> returnedList = new ArrayList<Integer>();
                        try {
                            returnedList = hashTable.insert(valueToInsert, hashingAlgorithm, collisionRes);
                        } catch (Exception ex) {
                            JOptionPane j = new JOptionPane();
                            j.showMessageDialog(null,
                                    "cannot simple integer mod hash a non integer,\nplease try again with a digit or change the hash function to\nstring hash on the main menu.");
                            addCapacityTxt.setText("");
                            return;
                        }
                        int hashValue;
                        int rowNumber = returnedList.get(returnedList.size() - 1);
                        if (stringHash) {
                            hashValue = Math.abs(hashTable.hashString(valueToInsert) % hashTable.getCapacity());
                            updateArea.append(valueToInsert + " -> string hash =  " + hashValue + ". \n");
                        } else {
                            hashValue = hashTable.hashInt(valueToInsert);
                            updateArea.append(valueToInsert + " -> simple integer mod hash =  " + hashValue + ". \n");
                            if (verboseOutput) {
                                updateArea.append(valueToInsert + " % " + "10 " + " = "
                                        + (Integer.parseInt(valueToInsert) % 10) + "\n");
                                updateArea.append((Integer.parseInt(valueToInsert) % 10) + " % capacity ("
                                        + hashTable.getCapacity() + ")" + " = " + hashValue + "\n");
                            } 
                        }

                        if (returnedList.size() > 1) {
                            // collision(s) has occured
                            if (verboseOutput) {
                                for (int i = 0; i < returnedList.size() - 1; i++) {
                                    updateArea.append("Collision: tried to insert " + valueToInsert + " at position "
                                            + returnedList.get(i) + " but " + model.getValueAt(returnedList.get(i), 1)
                                            + " was found. \n");
                                    if (linearProb) {
                                        updateArea.append("Attempting to linear probe at index (" + hashValue + " + "
                                                + (i + 1) + ") % " + hashTable.getCapacity() + "\nwhich evaluates to "
                                                + (hashValue + (i + 1)) % hashTable.getCapacity() + "\n");
                                    } else {
                                        updateArea.append("Attempting to quadratic probe at index (" + hashValue + " + "
                                                + (i + 1) + "^2) % " + hashTable.getCapacity() + "\nwhich evaluates "
                                                + (int)(((hashValue)+ (Math.pow(i + 1, 2))) % hashTable.getCapacity()) + "\n");
                                    }

                                }
                            } else {
                                String collisions = "";
                                if (rowNumber != -1) {
                                    for (int i = 0; i < returnedList.size(); i++) {
                                        collisions = collisions + returnedList.get(i) + ", ";
                                    }
                                } else {
                                    for (int i = 0; i < returnedList.size()-1; i++) {
                                        collisions = collisions + returnedList.get(i) + ", ";
                                    }
                                }
                                
                                updateArea.append("Attemped to " + collisionRes + " at indexes " + collisions + "\n");
                            }
                            
                            // check if -1
                            if (rowNumber != -1) {
                                cellRender.setLastInserted(rowNumber);
                                model.setValueAt(valueToInsert, rowNumber, 1); // add value to row number on table
                                updateArea.append("Due to collision: inserted " + valueToInsert + " at index "
                                        + rowNumber + ". \n");
                                model.fireTableDataChanged(); // update table
                                lastInsertLbl.setText(valueToInsert);
                            } else {
                                if (quadProb) {
                                    if(hashTable.getCapacity() == hashTable.getNumberOfKeys()) {
                                        updateArea.append(
                                                "Could not insert " + valueToInsert + " because table is full. \n");
                                    } else {
                                        JOptionPane j = new JOptionPane();
                                        j.showMessageDialog(null,
                                                "The hash table is at least half full.\nDue to the limitations of quadratic probing this insertion has failed.\n Click on quadratic probing button on main menu for more information.");
                                                updateArea.append(
                                                "Could not insert " + valueToInsert + " because of limitations of quadratic probing. \n");
                                    }
                                    
                                } else {
                                    updateArea
                                            .append("Could not insert " + valueToInsert + " because table is full. \n");
                                }
                                
                            }
                           
                        } else {
                            // no collisions occured
                            if (rowNumber != -1) {
                                cellRender.setLastInserted(rowNumber);
                                model.setValueAt(valueToInsert, rowNumber, 1); // add value to row number on table
                                updateArea.append("Inserted " + valueToInsert + " at index " + rowNumber + ". \n");
                                lastInsertLbl.setText(valueToInsert);
                                model.fireTableDataChanged(); // update table
                            } else {
                                if (quadProb) {
                                    JOptionPane j = new JOptionPane();
                                    j.showMessageDialog(null,
                                            "The hash table is at least half full.\nDue to the limitations of quadratic probing this insertion has failed.\n Click on quadratic probing button on main menu for more information.");
                                }
                                
                                updateArea.append("Could not insert " + valueToInsert + " because table is full. \n");
                            }
                        }
                        updateArea.append("\n");
                    }
                }

                /**
                 * Deletes an item into the hash table by validating user input then using a
                 * list of collisions and insertions displays it on the update area.
                 */
                if (e.getSource() == deleteValueBtn) { // delete
                    String valueToDelete = deleteValueTxt.getText();
                    deleteValueTxt.setText("");
                    if (valueToDelete.trim().equals("")) {
                        JOptionPane j = new JOptionPane();
                        j.showMessageDialog(null, "Deleting nothing isn't going\nto help anyone.");
                    } else {
                        ArrayList<Integer> returnedList = new ArrayList<>();


                        try {
                            returnedList = hashTable.search(valueToDelete, hashingAlgorithm, collisionRes);
                        } catch (Exception ex) {
                            JOptionPane j = new JOptionPane();
                            j.showMessageDialog(null,
                                    "cannot simple integer mod hash a non integer,\nplease try again with a digit or change the hash function to\nstring hash on the main menu.");
                            addCapacityTxt.setText("");
                            return;
                        }

                        int rowNumber = returnedList.get(returnedList.size() - 1);
                        int hashValue;

                        if (stringHash) {
                            hashValue = Math.abs(hashTable.hashString(valueToDelete) % hashTable.getCapacity());
                            updateArea.append(valueToDelete + " -> string hash =  " + hashValue + ". \n");
                        } else {
                            hashValue = hashTable.hashInt(valueToDelete);
                            updateArea.append(valueToDelete + " -> simple integer mod hash =  " + hashValue + ". \n");
                            if (verboseOutput) {
                                updateArea.append(valueToDelete + " % " + "10 " + " = "
                                        + (Integer.parseInt(valueToDelete) % 10) + "\n");
                                updateArea.append((Integer.parseInt(valueToDelete) % 10) + " % capacity ("
                                        + hashTable.getCapacity() + ")" + " = " + hashValue + "\n");
                            }
                        }
                        
                        if (returnedList.size() > 1) {
                            // collision(s) has occured
                            if (verboseOutput) {
                                for (int i = 0; i < returnedList.size() - 1; i++) {
                                    updateArea.append("Collision: tried to delete " + valueToDelete + " at position "
                                            + returnedList.get(i) + " but " + model.getValueAt(returnedList.get(i), 1)
                                            + " was found. \n");
                                    if (linearProb) {
                                        updateArea.append("Attempting to linear probe at index (" + hashValue + " + "
                                                + (i + 1) + ") % " + hashTable.getCapacity() + "\nwhich evaluates to "
                                                + (hashValue + (i + 1)) % hashTable.getCapacity() + "\n");
                                    } else {
                                        updateArea.append("Attempting to quadratic probe at index (" + hashValue
                                                + " + " + (i + 1) + "^2) % " + hashTable.getCapacity()
                                                + "\nwhich evaluates "
                                                + (int) (((hashValue) + (Math.pow(i + 1, 2))) % hashTable.getCapacity())
                                                + "\n");
                                    }

                                }
                            } else {
                                String collisions = "";
                                if (rowNumber != -1) {
                                    for (int i = 0; i < returnedList.size(); i++) {
                                        collisions = collisions + returnedList.get(i) + ", ";
                                    }
                                } else {
                                    for (int i = 0; i < returnedList.size()-1; i++) {
                                        collisions = collisions + returnedList.get(i) + ", ";
                                    }
                                }

                                updateArea.append("Attemped to " + collisionRes + " at indexes " + collisions + "\n");
                            }
                            // check if -1
                            if (rowNumber != -1) {
                                hashTable.delete(valueToDelete, hashingAlgorithm, collisionRes);
                                cellRender.setLastDeleted(rowNumber);
                                model.setValueAt("null", rowNumber, 1); // add value to row number on table
                                updateArea.append("Due to collision: deleted " + valueToDelete + " at index "
                                        + rowNumber + ". \n");
                                lastDeletedLbl.setText(valueToDelete);
                                model.fireTableDataChanged(); // update table
                            } else {
                                updateArea.append(
                                        "Could not delete " + valueToDelete + " it is not in the hash table. \n");
                            }
                        } else {
                            // no collisions occured
                            if (rowNumber != -1) {
                                hashTable.delete(valueToDelete, hashingAlgorithm, collisionRes);
                                cellRender.setLastDeleted(rowNumber);
                                model.setValueAt("null", rowNumber, 1); // add value to row number on table
                                updateArea.append("Deleted " + valueToDelete + " at index " + rowNumber + ". \n");
                                model.fireTableDataChanged(); // update table
                                lastDeletedLbl.setText(valueToDelete);
                            } else {
                                updateArea.append("Could not delete " + valueToDelete
                                        + " because table it is not in the hash table. \n");
                            }
                        }
                    }
                    updateArea.append("\n");
                }
                
                /**
                 * Searches an item into the hash table by validating user input then using a
                 * list of collisions and insertions displays it on the update area.
                 */
                if (e.getSource() == searchValueBtn) { // search
                    String valueToSearch = searchValueTxt.getText();
                    searchValueTxt.setText("");
                    if (valueToSearch.trim().equals("")) {
                        JOptionPane j = new JOptionPane();
                        j.showMessageDialog(null, "Searching nothing isn't going\nto help anyone.");
                    } else {
                        ArrayList<Integer> returnedList = new ArrayList<Integer>();

                        try {
                            returnedList = hashTable.search(valueToSearch, hashingAlgorithm, collisionRes);
                        } catch (Exception ex) {
                            JOptionPane j = new JOptionPane();
                            j.showMessageDialog(null,
                                    "cannot simple integer mod hash a non integer,\nplease try again with a digit or change the hash function to\nstring hash on the main menu.");
                            addCapacityTxt.setText("");
                            return;
                        }

                        int rowNumber = returnedList.get(returnedList.size() - 1);
                        int hashValue;

                        if (stringHash) {
                            hashValue = Math.abs(hashTable.hashString(valueToSearch) % hashTable.getCapacity());
                            updateArea.append(valueToSearch + " -> string hash =  " + hashValue + ". \n");
                        } else {
                            hashValue = hashTable.hashInt(valueToSearch);
                            updateArea.append(valueToSearch + " -> simple integer mod hash =  " + hashValue + ". \n");
                            if (verboseOutput) {
                                updateArea.append(valueToSearch + " % " + "10 " + " = "
                                        + (Integer.parseInt(valueToSearch) % 10) + "\n");
                                updateArea.append((Integer.parseInt(valueToSearch) % 10) + " % capacity ("
                                        + hashTable.getCapacity() + ")" + " = " + hashValue + "\n");
                            }
                        }
                        if (returnedList.size() > 1) {
                            // collision(s) has occured
                            if (verboseOutput) {
                                for (int i = 0; i < returnedList.size() - 1; i++) {
                                    updateArea.append("Collision: tried to search " + valueToSearch + " at position "
                                            + returnedList.get(i) + " but " + model.getValueAt(returnedList.get(i), 1)
                                            + " was found. \n");
                                    if (linearProb) {
                                        updateArea.append("Attempting to linear probe at index (" + hashValue + " + "
                                                + (i + 1) + ") % " + hashTable.getCapacity() + "\nwhich evaluates to "
                                                + (hashValue + (i + 1)) % hashTable.getCapacity() + "\n");
                                    } else {
                                        updateArea.append("Attempting to quadratic probe at index (" + hashValue
                                                + " + " + (i + 1) + "^2) % " + hashTable.getCapacity()
                                                + "\nwhich evaluates "
                                                + (int) (((hashValue) + (Math.pow(i + 1, 2))) % hashTable.getCapacity())
                                                + "\n");
                                    }

                                }
                            } else {
                                String collisions = "";
                                if (rowNumber != -1) {
                                    for (int i = 0; i < returnedList.size(); i++) {
                                        collisions = collisions + returnedList.get(i) + ", ";
                                    }
                                } else {
                                    for (int i = 0; i < returnedList.size()-1; i++) {
                                        collisions = collisions + returnedList.get(i) + ", ";
                                    }
                                }

                                updateArea.append("Attemped to " + collisionRes + " at indexes " + collisions + "\n");
                            }
                            // check if -1
                            if (rowNumber != -1){
                                cellRender.setLastSearched(rowNumber);
                                updateArea.append("Due to collision: found " + valueToSearch + " at index " + rowNumber + ". \n");
                                model.fireTableDataChanged(); // update table
                                lastSearchedLbl.setText(valueToSearch);
                            } else {
                                updateArea.append("Could not find " + valueToSearch + "\n");
                            }
                        } else {
                            // no collisions occured
                            if (rowNumber != -1){
                                cellRender.setLastSearched(rowNumber);
                                updateArea.append("Found " + valueToSearch + " at index " + rowNumber + ". \n");
                                model.fireTableDataChanged(); // update table
                                lastSearchedLbl.setText(valueToSearch);
                            } else {
                                updateArea.append("Could not find " + valueToSearch + "\n");
                            }
                    }
                }
                    updateArea.append("\n");
                }

                if (e.getSource() == resetBtn) { // reset
                    model.setRowCount(0); // removes all rows
                    cellRender.setLastDeleted(-1);
                    cellRender.setLastInserted(-1);
                    cellRender.setLastSearched(-1);
                    createHashTableBtn.setEnabled(true);
                    addValueBtn.setEnabled(false);
                    deleteValueBtn.setEnabled(false);
                    searchValueBtn.setEnabled(false);
                    lastDeletedLbl.setText("null");
                    lastInsertLbl.setText("null");
                    lastSearchedLbl.setText("null");
                    resetStartingText();
                }
                
            }
            
        }

    }

}
