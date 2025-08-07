package me.eva.buidmgui.model;

import me.eva.buidmgui.gui.LoginPage;
import me.eva.buidmgui.gui.SingleValueEditor;

import javax.swing.*;

public class TreeOrgNode implements IHasConfigMenu {

    private EntityOrganisation organisation;

    public TreeOrgNode(EntityOrganisation organisation) {
        this.organisation = organisation;
    }

    @Override
    public JPanel getPanel() {
        return new SingleValueEditor(organisation.getEntityName(), organisation.getOrgName());
    }

    @Override
    public String toString() {
        return organisation.getOrgName();
    }
}