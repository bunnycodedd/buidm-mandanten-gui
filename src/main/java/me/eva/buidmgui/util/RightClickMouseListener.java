package me.eva.buidmgui.util;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

public class RightClickMouseListener implements MouseListener {

    private final Consumer<MouseEvent> lambda;

    public RightClickMouseListener(Consumer<MouseEvent> lambda) {
        this.lambda = lambda;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            lambda.accept(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}