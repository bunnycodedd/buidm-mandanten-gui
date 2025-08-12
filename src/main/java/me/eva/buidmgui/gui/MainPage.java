package me.eva.buidmgui.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import me.eva.buidmgui.controller.MenuBarHandlers;
import me.eva.buidmgui.gui.wizard.EntityCreationWizard;
import me.eva.buidmgui.model.*;
import me.eva.buidmgui.net.DatabaseConnection;
import me.eva.buidmgui.util.EntityConfigTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainPage extends JFrame {
    private JPanel root;
    private JTable entityConfigTable;
    private JTree explorer;
    private JTabbedPane tabbedPane1;
    private JSplitPane mainSplitPane;
    private JLabel valueLabel;
    private JTextField treeSearchField;
    private JTable entityLocationTable;
    private JTable entityOrganisationTable;
    private JPanel editorPanel;
    private JSplitPane splitPaneWithEditor;
    private JTextPane consoleTextPane;
    private JSplitPane rootSplitPane;

    private Optional<ConsolePrintStream> consolePrintStream = Optional.empty();

    private boolean dirty = false;

    private EntityConfigTableModel entityConfigTableModel;

    private final DatabaseConnection databaseConnection;

    private static MainPage INSTANCE;

    private DebugWindow debugWindow;

    public MainPage(DatabaseConnection databaseConnection) {
        INSTANCE = this;
        this.databaseConnection = databaseConnection;

        $$$setupUI$$$();

        setTitle("Titel hier einfügen");
        setContentPane(root);

        getConsoleOutputStream().printlnStamped("Willkommen!");

        rootSplitPane.setDividerLocation(500);
        mainSplitPane.setDividerLocation(500);
        splitPaneWithEditor.setDividerLocation(450);

        setJMenuBar(createMenuBar());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(1280, 640);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        setJMenuBar(menuBar);
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createDataMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Datei");
        JMenuItem newItem = new JMenuItem("Neuer Mandant");
        newItem.addActionListener(e -> {
            EntityCreationWizard wizard = new EntityCreationWizard();
            wizard.setModal(true);
            wizard.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            wizard.setLocationRelativeTo(null);
            wizard.setVisible(true);
        });

        JMenu importMenu = new JMenu("Importieren...");
        JMenuItem exportItem = new JMenuItem("Exportieren");

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> dispose());

        // file/import
        JMenuItem importEntityConfigItem = new JMenuItem("ENTITYCONFIGURATION");
        importEntityConfigItem.addActionListener(e -> {
            MenuBarHandlers.handleImportEntityConfig(e, root);
        });

        exportItem.addActionListener(MenuBarHandlers::handleExport);

        JMenuItem importEntityLocationItem = new JMenuItem("ENTITYLOCATION");
        JMenuItem importEntityOrganisationItem = new JMenuItem("ENTITYORGANISATION");

        importMenu.add(importEntityConfigItem);
        importMenu.add(importEntityLocationItem);
        importMenu.add(importEntityOrganisationItem);

        fileMenu.add(newItem);

        fileMenu.addSeparator();


        fileMenu.add(importMenu);
        fileMenu.add(exportItem);

        fileMenu.addSeparator();

        fileMenu.add(exitItem);
        return fileMenu;
    }

    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Bearbeiten");
        JMenuItem convertItem = new JMenuItem("Konvertieren");
        convertItem.addActionListener(e -> {
            ConverterPage converterPage = new ConverterPage(this, true);
        });

        editMenu.add(convertItem);
        return editMenu;
    }

    private JMenu createDataMenu() {
        JMenu dataMenu = new JMenu("Datensatz");
        JMenuItem showItem = new JMenuItem("Datensatz anzeigen");
        JMenuItem exportItem = new JMenuItem("Datensatz exportieren");
        dataMenu.add(showItem);
        dataMenu.add(exportItem);
        return dataMenu;
    }

    private void createUIComponents() {
        consoleTextPane = new JTextPane();
        consoleTextPane.setContentType("text/plain;charset=UTF-8");
        consolePrintStream = Optional.of(new ConsolePrintStream(new ConsoleOutputStream(consoleTextPane)));

        // Create user-immutable JTable
        entityConfigTable = new JTable(new DefaultTableModel(databaseConnection.getEntityConfigsRaw(), new String[]{"ENTITYNAME", "PARAMETER_NAME", "PARAMETER_VALUE", "ACTIVE"})) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        entityLocationTable = new JTable(new DefaultTableModel(databaseConnection.getEntityLocationsRaw(), new String[]{"ENTITY_NAME", "LOCATION_ID", "LOCATION_NAME", "LOCATION_STREET", "LOCATION_POSTCODE", "LOCATION_CITY", "LOCATION_COUNTRY", "ACTIVE"})) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        entityOrganisationTable = new JTable(new DefaultTableModel(databaseConnection.getEntityOrganisationsRaw(), new Object[]{"ENTITY_NAME", "ORGID", "ORGNAME", "ORGNAME", "ORGLEVEL", "ORGTYPE", "PARENTID", "ACTIVE", "OEKEY"})) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // ###################### TREE ######################
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Mandanten");
        explorer = new JTree(rootNode);
        explorer.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        explorer.addTreeSelectionListener(new TreeSelectionListenerImpl(this));

        try {
            ArrayList<EntityConfig> entityConfigs = databaseConnection.getEntityConfigs();
            for (EntityConfig entityConfig : entityConfigs) {
                DefaultMutableTreeNode entityNode = new DefaultMutableTreeNode(String.format("%s (%s)", entityConfig.getFriendlyName(), entityConfig.getEntityName()));
                DefaultMutableTreeNode paramsNode = new DefaultMutableTreeNode("Parameter");
                DefaultMutableTreeNode locationsNode = new DefaultMutableTreeNode("Standorte");
                DefaultMutableTreeNode orgsNode = new DefaultMutableTreeNode("Organisationen");

                entityNode.add(paramsNode);
                entityConfig.getParameters().forEach((param, value) -> {
                    DefaultMutableTreeNode paramNode = new DefaultMutableTreeNode(new TreeParameterNode(param, value));
                    paramsNode.add(paramNode);
                });

                entityNode.add(locationsNode);
                entityConfig.getLocations().forEach(location -> locationsNode.add(new DefaultMutableTreeNode(new TreeLocationNode(location))));

                entityNode.add(orgsNode);

                //DefaultMutableTreeNode departmentsNode = new DefaultMutableTreeNode("Abteilungen");
                //orgsNode.add(departmentsNode);
                //entityConfig.getOrganisations().forEach(org -> departmentsNode.add(new DefaultMutableTreeNode(new TreeOrgNode(org))));

                //structureOrgs(orgsNode, entityConfig.getOrganisations().stream().filter(organisation -> organisation.getOrgId() > 1).collect(Collectors.toList()));
                rootNode.add(entityNode);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            dispose();
            System.exit(1);
        }

        explorer.setShowsRootHandles(true);
    }

    private void structureOrgs(DefaultMutableTreeNode orgsNode, List<EntityOrganisation> organisations) {
        // Stelle fest, welche orgs Blätter sind und welche nicht
        int height = organisations.stream().mapToInt(EntityOrganisation::getOrgLevel).max().orElse(0);
        HashMap<Integer, List<DefaultMutableTreeNode>> levelToNodes = new HashMap<>();

        for (int i = 2; i <= height; i++) {
            levelToNodes.computeIfAbsent(i, k -> new ArrayList<>());
        }


        organisations.stream().filter(organisation -> organisation.getOrgLevel() == 2)
                .forEach(organisation -> levelToNodes.computeIfAbsent(2, integer -> new ArrayList<>()).add(new DefaultMutableTreeNode(organisation.getOrgName())));

        levelToNodes.get(2).forEach(orgsNode::add);

        organisations.stream().filter(organisation -> organisation.getOrgLevel() == 3)
                .forEach(organisation -> levelToNodes.computeIfAbsent(3, integer -> new ArrayList<>()).add(new DefaultMutableTreeNode(organisation.getOrgName())));

        levelToNodes.get(3).forEach(defaultMutableTreeNode -> {

        });
    }

    public DebugWindow getDebugWindow() {
        return debugWindow;
    }

    public ConsolePrintStream getConsoleOutputStream() {
        return consolePrintStream.orElseThrow(() -> new IllegalStateException("Console is not yet fully initialized"));
    }

    public boolean isDirty() {
        return dirty;
    }

    public void makeDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        root = new JPanel();
        root.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootSplitPane = new JSplitPane();
        rootSplitPane.setContinuousLayout(true);
        rootSplitPane.setOrientation(0);
        root.add(rootSplitPane, new GridConstraints(0, 0, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        mainSplitPane = new JSplitPane();
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setDividerSize(10);
        mainSplitPane.setOrientation(1);
        rootSplitPane.setLeftComponent(mainSplitPane);
        mainSplitPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainSplitPane.setLeftComponent(panel1);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        explorer.setEnabled(true);
        scrollPane1.setViewportView(explorer);
        treeSearchField = new JTextField();
        panel1.add(treeSearchField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        splitPaneWithEditor = new JSplitPane();
        splitPaneWithEditor.setContinuousLayout(true);
        splitPaneWithEditor.setDividerLocation(456);
        splitPaneWithEditor.setLastDividerLocation(456);
        splitPaneWithEditor.setMaximumSize(new Dimension(-1, -1));
        mainSplitPane.setRightComponent(splitPaneWithEditor);
        tabbedPane1 = new JTabbedPane();
        splitPaneWithEditor.setLeftComponent(tabbedPane1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Entity Config", panel2);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setViewportView(entityConfigTable);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Entity Location", panel3);
        final JScrollPane scrollPane3 = new JScrollPane();
        panel3.add(scrollPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane3.setViewportView(entityLocationTable);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Entity Organisation", panel4);
        final JScrollPane scrollPane4 = new JScrollPane();
        panel4.add(scrollPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane4.setViewportView(entityOrganisationTable);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), 0, 0));
        splitPaneWithEditor.setRightComponent(panel5);
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        editorPanel = new JPanel();
        editorPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(editorPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        editorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final Spacer spacer2 = new Spacer();
        panel5.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane5 = new JScrollPane();
        rootSplitPane.setRightComponent(scrollPane5);
        consoleTextPane.setEditable(false);
        Font consoleTextPaneFont = this.$$$getFont$$$("Consolas", -1, 16, consoleTextPane.getFont());
        if (consoleTextPaneFont != null) consoleTextPane.setFont(consoleTextPaneFont);
        scrollPane5.setViewportView(consoleTextPane);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

    public JTextField getTreeSearchField() {
        return treeSearchField;
    }

    public static MainPage getInstance() {
        return INSTANCE;
    }

    public void writeLineToConsole(String str, boolean stamped) {
        if (stamped) getConsoleOutputStream().printlnStamped(str);
        else getConsoleOutputStream().println(str);
    }

    public void writeLineToConsole(String str) {
        writeLineToConsole(str, true);
    }

    public JTable getEntityConfigTable() {
        return entityConfigTable;
    }

    public JTable getEntityLocationTable() {
        return entityLocationTable;
    }

    public JTree getExplorer() {
        return explorer;
    }

    public JPanel getEditorPanel() {
        return editorPanel;
    }

    public JSplitPane getEditorSplitPane() {
        return splitPaneWithEditor;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}
