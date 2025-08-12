package me.eva.buidmgui.gui.wizard;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class WizardController {

    public static final String FIRST = "panel_first";
    public static final String SECOND = "panel_second";
    public static final String THIRD = "panel_third";
    public static final String FOURTH = "panel_fourth";

    private static final ImmutableBiMap<String, String> PANEL_KEYS =
            ImmutableBiMap.<String, String>builder()
                    .put(FIRST, SECOND)
                    .put(SECOND, THIRD)
                    .put(THIRD, FOURTH)
                    .put(FOURTH, "null")
                    .build();

    private final EntityCreationContext context;

    private final ImmutableMap<String, JPanel> panelById;
    private final LayoutManager layout;

    private String currentPanelId = "null";
    private final EntityCreationWizard wizard;

    public WizardController(EntityCreationWizard wizard, LayoutManager layout) {
        this.wizard = wizard;
        this.context = new EntityCreationContext();
        currentPanelId = FIRST;
        this.layout = layout;
        this.panelById = ImmutableMap.<String, JPanel>builder()
                .put(FIRST, new WizardFirst(layout, context))
                .put(SECOND, new WizardSecond(layout, context))
                .put(THIRD, new WizardThird(layout, context))
                .put(FOURTH, new WizardFourth(layout, context))
                .build();

        panelById.forEach(layout::addLayoutComponent);

        updateButtons();
    }


    public ImmutableMap<String, JPanel> getPanelById() {
        return panelById;
    }

    public JPanel getCurrentPanel() {
        return panelById.get(currentPanelId);
    }

    public String getCurrentPanelId() {
        return currentPanelId;
    }

    public void next() {
        currentPanelId = PANEL_KEYS.get(currentPanelId);

        if(currentPanelId.equals(FOURTH)) {
            runBeforeLastPanel();
        }

        ((CardLayout) wizard.getMainPanel().getLayout()).show(wizard.getMainPanel(), currentPanelId);
        updateButtons();
    }

    public void previous() {
        currentPanelId = PANEL_KEYS.inverse().get(currentPanelId);
        ((CardLayout) wizard.getMainPanel().getLayout()).show(wizard.getMainPanel(), currentPanelId);
        updateButtons();
    }

    private void updateButtons() {
        wizard.getNextButton().setEnabled(PANEL_KEYS.get(currentPanelId) != null);
        wizard.getBackButton().setEnabled(PANEL_KEYS.inverse().get(currentPanelId) != null);
    }

    private void runBeforeLastPanel() {
        WizardFourth wizardFourth = (WizardFourth) getCurrentPanel();
        wizardFourth.getNameValueLabel().setText(context.getEntityName());
        wizardFourth.getIdValueLabel().setText(context.getEntityId());

        wizardFourth.getAddressValueLabel().setText(context.getAddress());
        wizardFourth.getInternalPhoneValueLabel().setText(context.getTelInternal());
        wizardFourth.getExternalValueLabel().setText(context.getTelExternal());
        wizardFourth.getFaxValueLabel().setText(context.getFax());
        wizardFourth.getOrgNameValueLabel().setText(context.getEntityName());
    }
}