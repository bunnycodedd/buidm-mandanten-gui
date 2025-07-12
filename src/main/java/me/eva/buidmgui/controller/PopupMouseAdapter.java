package me.eva.buidmgui.controller;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopupMouseAdapter extends MouseAdapter {

    private JPopupMenu popup;

    public PopupMouseAdapter(JPopupMenu popup) {
        this.popup = popup;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
