package me.eva.buidmgui.gui.wizard;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class WizardController {

    public static final String FIRST = "panel_first";
    public static final String SECOND = "panel_second";
    public static final String THIRD = "panel_third";

    private static final ImmutableBiMap<String, String> PANEL_KEYS =
            ImmutableBiMap.<String, String>builder()
                    .put(FIRST, SECOND)
                    .put(SECOND, THIRD)
                    .put(THIRD, "null")
                    .build();

    private String currentPanelId;
    private final EntityCreationWizard wizard;

    public WizardController(EntityCreationWizard wizard) {
        this.wizard = wizard;
        currentPanelId = FIRST;

        updateButtons();
    }

    private void setCurrentPanel(String id) {
        currentPanelId = id;
    }

    public String getCurrentPanelId() {
        return currentPanelId;
    }

    public void next() {
        currentPanelId = PANEL_KEYS.get(currentPanelId);
        ((CardLayout)wizard.getMainPanel().getLayout()).show(wizard.getMainPanel(), currentPanelId);
        updateButtons();
    }

    public void previous() {
        currentPanelId = PANEL_KEYS.inverse().get(currentPanelId);
        ((CardLayout)wizard.getMainPanel().getLayout()).show(wizard.getMainPanel(), currentPanelId);
        updateButtons();
    }

    private void updateButtons() {
        wizard.getNextButton().setEnabled(PANEL_KEYS.get(currentPanelId) != null);
        wizard.getBackButton().setEnabled(PANEL_KEYS.inverse().get(currentPanelId) != null);
    }
}