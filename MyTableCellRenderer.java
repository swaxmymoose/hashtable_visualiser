/*  07/02/2020
    MyTableCellRenderer.java
    COSC326
*/

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * MyTableCellRenderer renders jtable cells
 */
class MyTableCellRender extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    private int lastDeleted = -1;
    private int lastInserted = -1;
    private int lastSearched = -1;

    public MyTableCellRender() {
        super();
        setOpaque(true);
    }

    public void setLastDeleted(int i) {
        this.lastDeleted = i;
    }

    public void setLastInserted(int i) {
        this.lastInserted = i;
    }

    public void setLastSearched(int i) {
        this.lastSearched = i;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
             
        // set colour of cells
        if (rowIndex == lastDeleted) {
            setForeground(Color.white);
            setBackground(Color.red);
        } else if (rowIndex == lastInserted) {
            setForeground(Color.white);
            setBackground(Color.green);
        } else if (rowIndex == lastSearched) {
            setForeground(Color.white);
            setBackground(Color.cyan);
        } else {
            setForeground(Color.black);
            setBackground(Color.decode("#EDF8EE"));
            
        }

        if (table.getValueAt(rowIndex, 1).equals("null")) {
            setForeground(Color.decode("#A3C3D9"));
            setFont(getFont().deriveFont(~Font.BOLD));
        } else{
            setForeground(Color.black);
            setFont(getFont().deriveFont(Font.BOLD));
        }
        
        setText(value != null ? value.toString() : "");
        return this;
    }
}

