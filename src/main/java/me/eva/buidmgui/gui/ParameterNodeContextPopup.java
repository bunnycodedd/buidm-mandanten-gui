package me.eva.buidmgui.gui;

import javax.swing.*;

public class ParameterNodeContextPopup extends JPopupMenu {

    private JMenuItem contextMenuItem;
    public ParameterNodeContextPopup() {
        contextMenuItem = new JMenuItem("Context");
        add(contextMenuItem);
    }

}
