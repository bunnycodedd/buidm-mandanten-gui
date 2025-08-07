package me.eva.buidmgui.model;

import me.eva.buidmgui.gui.MainPage;
import me.eva.buidmgui.gui.SingleValueEditor;

import javax.swing.*;

public class TreeParameterNode implements IHasConfigMenu {

    private String paramName;
    private String paramValue;

    public TreeParameterNode(String paramName, String paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    @Override
    public JPanel getPanel() {
        return new SingleValueEditor(paramName, paramValue);
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamName() {
        return paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    @Override
    public String toString() {
        return paramName;
    }
}