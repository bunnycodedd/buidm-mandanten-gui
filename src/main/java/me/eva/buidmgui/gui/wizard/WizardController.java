package me.eva.buidmgui.gui.wizard;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import me.eva.buidmgui.gui.MainPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class WizardController {

    public static final String FIRST = "panel_first";
    public static final String SECOND = "panel_second";
    public static final String THIRD = "panel_third";
    public static final String FOURTH = "panel_fourth";
    public static final String FINAL = "panel_final";

    private static final ImmutableBiMap<String, String> PANEL_KEYS =
            ImmutableBiMap.<String, String>builder()
                    .put(FIRST, SECOND)
                    .put(SECOND, THIRD)
                    .put(THIRD, FOURTH)
                    .put(FOURTH, FINAL)
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
                .put(FINAL, new WizardFinal(layout, wizard))
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
            runBeforeCheckupPanel();
        }

        if(currentPanelId.equals(FINAL)) {
            runBeforeLastPanel();
            wizard.getNextButton().setEnabled(true);
            wizard.getNextButton().setText("Ausführen");
            for (ActionListener actionListener : wizard.getNextButton().getActionListeners()) {
                wizard.getNextButton().removeActionListener(actionListener);
            }

            wizard.getNextButton().addActionListener(e -> {
                wizard.dispose();

                for (String sql : ((WizardFinal) panelById.get(FINAL)).generateSql(context)) {
                    MainPage.getInstance().writeLineToConsole(sql);
                    try {
                       MainPage.getInstance().getDatabaseConnection().execute(sql);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(MainPage.getInstance(), ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
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
        wizard.getNextButton().setEnabled((PANEL_KEYS.get(currentPanelId) != null));
        wizard.getBackButton().setEnabled(PANEL_KEYS.inverse().get(currentPanelId) != null);

        if(currentPanelId.equals(FINAL)) {
            wizard.getNextButton().setEnabled(true);
        }
    }

    private void runBeforeCheckupPanel() {
        WizardFourth wizardFourth = (WizardFourth) getCurrentPanel();
        wizardFourth.getNameValueLabel().setText(context.getEntityName());
        wizardFourth.getIdValueLabel().setText(context.getEntityId());

        wizardFourth.getAddressValueLabel().setText(context.getAddress());
        wizardFourth.getInternalPhoneValueLabel().setText(context.getTelInternal());
        wizardFourth.getExternalValueLabel().setText(context.getTelExternal());
        wizardFourth.getFaxValueLabel().setText(context.getFax());
        wizardFourth.getOrgNameValueLabel().setText(context.getEntityName());
    }

    private void runBeforeLastPanel() {
        WizardFinal wizardFinal = (WizardFinal) getCurrentPanel();
        wizardFinal.generateSql(context);
    }
}