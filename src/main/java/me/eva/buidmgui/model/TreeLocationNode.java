package me.eva.buidmgui.model;

import me.eva.buidmgui.gui.SingleValueEditor;

import javax.swing.*;

public class TreeLocationNode implements IHasConfigMenu {

    private EntityLocation location;

    public TreeLocationNode(EntityLocation location) {
        this.location = location;
    }

    @Override
    public JPanel getPanel() {
        return new SingleValueEditor(location.getEntityName(), location.getLocationStreet());
    }

    public EntityLocation getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return location.getLocationStreet();
    }
}