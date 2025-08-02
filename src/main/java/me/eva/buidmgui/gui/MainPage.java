package me.eva.buidmgui.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import me.eva.buidmgui.controller.MenuBarHandlers;
import me.eva.buidmgui.model.EntityConfig;
import me.eva.buidmgui.model.TreeParameterNode;
import me.eva.buidmgui.model.TreeSelectionListenerImpl;
import me.eva.buidmgui.net.DatabaseConnection;
import me.eva.buidmgui.util.EntityConfigTableModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.ArrayList;

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


    private EntityConfigTableModel entityConfigTableModel;

    private final DatabaseConnection databaseConnection;

    private static MainPage INSTANCE;

    public MainPage(DatabaseConnection databaseConnection) {
        INSTANCE = this;
        this.databaseConnection = databaseConnection;

        $$$setupUI$$$();
        setTitle("Titel hier einfügen");
        setContentPane(root);
        mainSplitPane.setDividerLocation(250);

        setJMenuBar(createMenuBar());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setLocationRelativeTo(null);
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

        JMenu importMenu = new JMenu("Importieren...");
        JMenuItem exportItem = new JMenuItem("Exportieren");

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> dispose());

        // file/import
        JMenuItem importEntityConfigItem = new JMenuItem("ENTITYCONFIGURATION");
        importEntityConfigItem.addActionListener(e -> {
            MenuBarHandlers.handleImportEntityConfig(e, root);
        });

        JMenuItem importEntityLocationItem = new JMenuItem("ENTITYLOCATION");
        JMenuItem importEntityOrganisationItem = new JMenuItem("ENTITYORGANISATION");

        importMenu.add(importEntityConfigItem);
        importMenu.add(importEntityLocationItem);
        importMenu.add(importEntityOrganisationItem);

        fileMenu.add(newItem);

        fileMenu.addSeparator();


        fileMenu.add(importMenu);

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
                DefaultMutableTreeNode entityNode = new DefaultMutableTreeNode(entityConfig.getEntityName());
                DefaultMutableTreeNode paramsNode = new DefaultMutableTreeNode("Parameter");
                entityNode.add(paramsNode);
                entityConfig.getParameters().forEach((param, value) -> {
                    DefaultMutableTreeNode paramNode = new DefaultMutableTreeNode(new TreeParameterNode(this, param, value));

                    explorer.getSelectionModel().addTreeSelectionListener(e -> {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) explorer.getLastSelectedPathComponent();
                        String parameterValue = entityConfig.getParameters().get(node.getUserObject().toString());

                        valueLabel.setText("Wert: " + entityConfig.getParameters().getOrDefault(node.getUserObject().toString(), ""));

                    });
                    paramsNode.add(paramNode);
                });

                rootNode.add(entityNode);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            dispose();
            System.exit(1);
        }

        explorer.setShowsRootHandles(true);
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
        mainSplitPane = new JSplitPane();
        mainSplitPane.setDividerSize(10);
        mainSplitPane.setOrientation(1);
        root.add(mainSplitPane, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(100, 100), new Dimension(200, 200), null, 0, false));
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
        splitPaneWithEditor.setDividerLocation(532);
        splitPaneWithEditor.setMaximumSize(new Dimension(500, -1));
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
        editorPanel = new JPanel();
        editorPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        editorPanel.setAlignmentX(0.0f);
        editorPanel.setAlignmentY(0.0f);
        editorPanel.setMaximumSize(new Dimension(-1, -1));
        editorPanel.setMinimumSize(new Dimension(-1, -1));
        editorPanel.setName("Editor");
        editorPanel.setPreferredSize(new Dimension(-1, -1));
        editorPanel.setToolTipText("Editor");
        splitPaneWithEditor.setRightComponent(editorPanel);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        root.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        valueLabel = new JLabel();
        valueLabel.setText("Test: ");
        panel5.add(valueLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

    public static MainPage getInstance() {
        return INSTANCE;
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
}
