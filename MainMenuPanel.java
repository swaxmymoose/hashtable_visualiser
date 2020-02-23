/*  07/02/2020
    MainMenuPanel.java
    COSC326
*/

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * MainMenuPanel class
 * This Panel contains components displayed on the main menu.
 */
public class MainMenuPanel extends JPanel {

    private static final long serialVersionUID = 1L; // to get rid of warning
    private JLabel title, helpTitleLbl, staticHelpTitleLbl, hashLbl, collisionLbl, tableLbl;
    private JComboBox<String> hashList, collResList;
    private JButton helpLinearBtn, helpQuadBtn, helpIntBtn, helpStringBtn, startVisualiserBtn;
    private String hoveredButton;
    private boolean signedTable = false;
    private ImageIcon signedIcon, unsignedIcon;

    public MainMenuPanel() {
        setPreferredSize(new Dimension(600, 700));
        setBackground(Color.decode("#A3C3D9"));
        ButtonListener buttonListener = new ButtonListener();
        MainMenuMouseListener mainMouseListener = new MainMenuMouseListener();
        title = new JLabel("Visualiser Menu");
        title.setFont(title.getFont().deriveFont(40.0f));
        title.setFont(title.getFont().deriveFont(20.0f));
        JPanel containerPanel = new JPanel(new GridLayout(2,0));
        containerPanel.setPreferredSize(new Dimension(600, 150));
        JPanel helpStuffPanel = new JPanel(new GridLayout(2,0));
        JPanel setUpPanel = new JPanel(new GridLayout(0,2));
        staticHelpTitleLbl = new JLabel("Select a hash function and a collision resolution technique to begin.");
        
        // setUpPanel set up
            // start visualiser button set up
        startVisualiserBtn = new JButton("Start Hash Table Visualiser");
        startVisualiserBtn.addActionListener(buttonListener);
        startVisualiserBtn.addMouseListener(mainMouseListener);

            // selectersPanel set up
        JPanel selectersPanel = new JPanel(new GridLayout(2, 0));
        selectersPanel.setBackground(Color.decode("#A3C3D9"));
        String[] hashAlgorithms = { "simple integer mod hash", "string hash" };
        hashList = new JComboBox<>(hashAlgorithms);
        hashList.setSelectedIndex(0);
        hashList.addActionListener(buttonListener);
        String[] collResStrats = { "linear probing", "quadratic probing" };
        collResList = new JComboBox<>(collResStrats);
        collResList.setSelectedIndex(0);
        collResList.addActionListener(buttonListener);
        selectersPanel.add(hashList);
        selectersPanel.add(collResList);
        setUpPanel.add(selectersPanel);
        setUpPanel.add(startVisualiserBtn);
        setUpPanel.setPreferredSize(new Dimension(600, 80));
        setUpPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        setUpPanel.setBackground(Color.decode("#A3C3D9"));
        
        // helpStuffPanel set up
            // helpTitlePanel
        JPanel helpTitlePanel = new JPanel(new GridLayout(0,2));
            // helpTitleLabel
        helpTitleLbl = new JLabel("click a button below for more information");
        helpTitleLbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            // collision label
        collisionLbl = new JLabel("collision resolution techniques");
        collisionLbl.setOpaque(true);
        collisionLbl.setBackground(Color.decode("#edf8ee"));
        collisionLbl.setHorizontalAlignment(JLabel.CENTER);
        helpTitlePanel.add(collisionLbl);
        // hash function label
        hashLbl = new JLabel("hashing functions");
        hashLbl.setBackground(Color.decode("#edf8ee"));
        hashLbl.setOpaque(true);
        hashLbl.setHorizontalAlignment(JLabel.CENTER);
        helpTitlePanel.add(hashLbl);
            //helpButtonPanel
        helpLinearBtn = new JButton("linear probing");
        helpLinearBtn.addMouseListener(mainMouseListener);
        helpLinearBtn.addActionListener(buttonListener);
        helpQuadBtn = new JButton("quadratic probing");
        helpQuadBtn.addActionListener(buttonListener);
        helpQuadBtn.addMouseListener(mainMouseListener);
        helpStringBtn = new JButton("string hash");
        helpStringBtn.addActionListener(buttonListener);
        helpStringBtn.addMouseListener(mainMouseListener);
        helpIntBtn = new JButton("integer hash");
        helpIntBtn.addActionListener(buttonListener);
        helpIntBtn.addMouseListener(mainMouseListener);
        JPanel helpButtonPanel = new JPanel(new GridLayout(0,4));
            // finally set up the help panel itself and add its components
        helpStuffPanel.setPreferredSize(new Dimension(600, 80));
        helpStuffPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        helpStuffPanel.setBackground(Color.decode("#A3C3D9"));
        helpButtonPanel.add(helpLinearBtn);
        helpButtonPanel.add(helpQuadBtn);
        helpButtonPanel.add(helpStringBtn);
        helpButtonPanel.add(helpIntBtn);
        helpStuffPanel.add(helpTitlePanel);
        helpStuffPanel.add(helpButtonPanel);

        // set up hash table image label
        unsignedIcon = getImageIcon("./images/green.png");
        signedIcon = getImageIcon("./images/greensigned.png");
        tableLbl = new JLabel();
        tableLbl.setIcon(unsignedIcon);
        tableLbl.addMouseListener(mainMouseListener);
        tableLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // add all components to this (MainMenuPanel)
        add(title);
        add(tableLbl);
        add(staticHelpTitleLbl);
        add(setUpPanel);
        add(helpTitleLbl);
        add(helpStuffPanel);
    }

    /**
     * getImageIcon() uses a url to return an image icon with the image.
     * @param url location to image
     * @return icon with image
     */
    public ImageIcon getImageIcon(String url) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(getClass().getResource(url));
        } catch (Exception e) {
            return null;
        }
        
        if (img == null) {
            System.out.println("failed image load");
            return null;
        } else {
            ImageIcon icon = new ImageIcon(img);
            return icon;
        }
    }

   /**
    *  MainMenuMouseListener is used for dynamic aspect of the components on the main menu
    * e.g. text boldness and button colours change with mouse hover
    */
    private class MainMenuMouseListener implements MouseListener {
        @Override
        public void mouseEntered(MouseEvent event) {
            if (event.getSource() == startVisualiserBtn) {
                startVisualiserBtn.setFont(startVisualiserBtn.getFont().deriveFont(Font.BOLD));
            } else if (event.getSource() == helpLinearBtn) {
                hoveredButton = "linear probing";
                helpTitleLbl.setText("what is " + hoveredButton + " ?");
                collisionLbl.setBackground(Color.decode("#c8e9cc"));
                collisionLbl.setBorder(new LineBorder(Color.decode("#a0cfa4"), 2));
                helpLinearBtn.setFont(helpLinearBtn.getFont().deriveFont(Font.BOLD));
            } else if (event.getSource() == helpQuadBtn) {
                hoveredButton = "quadratic probing";
                helpTitleLbl.setText("what is " + hoveredButton + " ?");
                collisionLbl.setBackground(Color.decode("#c8e9cc"));
                collisionLbl.setBorder(new LineBorder(Color.decode("#a0cfa4"), 2));
                helpQuadBtn.setFont(helpQuadBtn.getFont().deriveFont(Font.BOLD));
            } else if (event.getSource() == helpStringBtn) {
                hoveredButton = "string hash";
                helpTitleLbl.setText("what is " + hoveredButton + " ?");
                hashLbl.setBackground(Color.decode("#c8e9cc"));
                hashLbl.setBorder(new LineBorder(Color.decode("#a0cfa4"), 2));
                helpStringBtn.setFont(helpStringBtn.getFont().deriveFont(Font.BOLD));
            } else if (event.getSource() == helpIntBtn) {
                hoveredButton = "integer hash";
                helpTitleLbl.setText("what is " + hoveredButton + " ?");
                hashLbl.setBackground(Color.decode("#c8e9cc"));
                hashLbl.setBorder(new LineBorder(Color.decode("#a0cfa4"), 2));
                helpIntBtn.setFont(helpIntBtn.getFont().deriveFont(Font.BOLD));
            }
            
        }
        @Override
        public void mouseExited(MouseEvent event) {
            if (event.getSource() == startVisualiserBtn) {
                startVisualiserBtn.setFont(startVisualiserBtn.getFont().deriveFont(~Font.BOLD));
            } else if (event.getSource() == helpLinearBtn) {
                hoveredButton = "click a button below for more information.";
                helpTitleLbl.setText(hoveredButton);
                collisionLbl.setBackground(Color.decode("#edf8ee"));
                collisionLbl.setBorder(null);
                helpLinearBtn.setFont(helpLinearBtn.getFont().deriveFont(~Font.BOLD));
            } else if (event.getSource() == helpQuadBtn) {
                hoveredButton = "click a button below for more information.";
                helpTitleLbl.setText(hoveredButton);
                collisionLbl.setBackground(Color.decode("#edf8ee"));
                collisionLbl.setBorder(null);
                helpQuadBtn.setFont(helpQuadBtn.getFont().deriveFont(~Font.BOLD));
            } else if (event.getSource() == helpStringBtn) {
                hoveredButton = "click a button below for more information.";
                helpTitleLbl.setText(hoveredButton);
                hashLbl.setBackground(Color.decode("#edf8ee"));
                hashLbl.setBorder(null);
                helpStringBtn.setFont(helpStringBtn.getFont().deriveFont(~Font.BOLD));
            } else if (event.getSource() == helpIntBtn) {
                hoveredButton = "click a button below for more information.";
                helpTitleLbl.setText(hoveredButton);
                hashLbl.setBackground(Color.decode("#edf8ee"));
                hashLbl.setBorder(null);
                helpIntBtn.setFont(helpIntBtn.getFont().deriveFont(~Font.BOLD));
            }
            
        }
        @Override
        public void mouseClicked(MouseEvent event) {
            if (event.getSource() == tableLbl) {
                if (signedTable) {
                    tableLbl.setIcon(unsignedIcon);
                } else {
                    tableLbl.setIcon(signedIcon);
                }
                signedTable = !signedTable;
                
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    /**
    * ButtonListner has one method (actionPerformed) that deals with button presses
    */
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == startVisualiserBtn) {
                JFrame frame = new JFrame("Hash Table Visualiser");
                frame.getContentPane().add(new MainPanel(String.valueOf(hashList.getSelectedItem()), String.valueOf(collResList.getSelectedItem())));
                frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
                frame.setResizable(false);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } else if (e.getSource() == helpLinearBtn) {
                JDialog j = new JDialog();
                j.setSize(500, 600);
                JLabel imgLbl = new JLabel();
                imgLbl.setIcon(getImageIcon("./images/linearprobe.png"));
                j.add(imgLbl);
                j.setLocationRelativeTo(null);
                j.setVisible(true);
            } else if (e.getSource() == helpQuadBtn) {
                JDialog j = new JDialog();
                j.setSize(500, 614);
                JLabel imgLbl = new JLabel();
                imgLbl.setIcon(getImageIcon("./images/quadraticprobe.png"));
                j.add(imgLbl);
                j.setLocationRelativeTo(null);
                j.setVisible(true);
            } else if (e.getSource() == helpStringBtn) {
                JDialog j = new JDialog();
                j.setSize(500, 400);
                JLabel imgLbl = new JLabel();
                imgLbl.setIcon(getImageIcon("./images/stringhash.png"));
                j.add(imgLbl);
                j.setLocationRelativeTo(null);
                j.setVisible(true);
            } else if (e.getSource() == helpIntBtn) {
                JDialog j = new JDialog();
                j.setSize(500, 300);
                JLabel imgLbl = new JLabel();
                imgLbl.setIcon(getImageIcon("./images/integerhash.png"));
                j.add(imgLbl);
                j.setLocationRelativeTo(null);
                j.setVisible(true);
            }
        }

    }
}

