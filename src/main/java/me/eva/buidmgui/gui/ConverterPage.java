package me.eva.buidmgui.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import me.eva.buidmgui.Main;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Level;

public class ConverterPage extends JDialog {
    private JPanel root;
    private JButton ecBrowseButton;
    private JTextPane textPane;
    private JLabel ecCurrentFileLabel;
    private JButton elBrowseButton;
    private JLabel elCurrentFileLabel;
    private JButton eoBrowseButton;
    private JLabel eoCurrentFileLabel;


    public ConverterPage(Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Nach SQL konvertieren");

        textPane.setFont(new Font("Consolas", Font.PLAIN, 18));

        ecBrowseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("C:/User".replace("/", File.separator), FileSystemView.getFileSystemView());
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV Dateien", "csv"));
            fileChooser.showOpenDialog(parent);

            File file = fileChooser.getSelectedFile();
            if (file == null) {
                throw new IllegalArgumentException("File must not be null!");
            }

            ecCurrentFileLabel.setText(file.getName());
            long totalLength = file.length();
            long readLength = 0;

            try {
                Scanner scanner = new Scanner(file);
                String firstLine = scanner.nextLine();
                ArrayList<String> lines = new ArrayList<>();

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    lines.add(line);
                }
                scanner.close();

                lines.stream()
                        .map(str -> str.split(";"))
                        .sorted(Comparator.comparing(o -> o[0]))
                        .map(strings -> {
                            return String.format("INSERT INTO ENTITYCONFIGURATION (ENTITYNAME, PARAMETER_NAME, PARAMETER_VALUE, `ACTIVE`) VALUES ('%s','%s','%s','%s');", strings[0], strings[1], strings[2], strings[3]);
                        })
                        .forEach(line -> {
                            textPane.setText(textPane.getText() + line + System.lineSeparator());
                        });


            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(parent, "Es gab einen Fehler beim Lesen der Datei.", "Konvertierungsfehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        elBrowseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("C:/User".replace("/", File.separator), FileSystemView.getFileSystemView());
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV Dateien", "csv"));
            fileChooser.showOpenDialog(parent);

            File file = fileChooser.getSelectedFile();
            if (file == null) {
                return;
            }

            elCurrentFileLabel.setText(file.getName());
            long totalLength = file.length();
            long readLength = 0;

            try {
                Scanner scanner = new Scanner(file);
                String firstLine = scanner.nextLine();
                ArrayList<String> lines = new ArrayList<>();

                if (!firstLine.equals("ENTITY_NAME;LOCATION_ID;LOCATION_NAME;LOCATION_STREET;LOCATION_POSTCODE;LOCATION_CITY;LOCATION_COUNTRY;ACTIVE")) {
                    JOptionPane.showMessageDialog(parent, "Ungültiger Tabellenkopf!\nTabellenkopf muss 'ENTITY_NAME;LOCATION_ID;LOCATION_NAME;LOCATION_STREET;LOCATION_POSTCODE;LOCATION_CITY;LOCATION_COUNTRY;ACTIVE' enthalten.", "Importfehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    lines.add(line);
                }

                scanner.close();
                textPane.setText("");

                try {
                    lines.stream()
                            .map(str -> str.split(";"))
                            .sorted(Comparator.comparing(o -> o[0]))
                            .map(strings -> {
                                return String.format("ENTITY_NAME, LOCATION_ID, LOCATION_NAME, LOCATION_STREET, LOCATION_POSTCODE, LOCATION_CITY, LOCATION_COUNTRY, `ACTIVE`) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');", strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]);
                            })
                            .forEach(line -> {
                                textPane.setText(textPane.getText() + line + System.lineSeparator());
                            });
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "Es gab einen Fehler beim Lesen der Datei. Sind alle Zeilen mit konform dem Tabellenkopf?", "Konvertierungsfehler", JOptionPane.ERROR_MESSAGE);
                }

            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(parent, "Es gab einen Fehler beim Lesen der Datei.", "Konvertierungsfehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        eoBrowseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("C:/User".replace("/", File.separator), FileSystemView.getFileSystemView());
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV Dateien", "csv"));
            fileChooser.showOpenDialog(parent);

            File file = fileChooser.getSelectedFile();
            if (file == null) {
                return;
            }

            eoCurrentFileLabel.setText(file.getName());
            long totalLength = file.length();
            long readLength = 0;

            try {
                Scanner scanner = new Scanner(file);
                String firstLine = scanner.nextLine();
                ArrayList<String> lines = new ArrayList<>();

                System.out.println(firstLine);
                if (!firstLine.equals("ENTITY_NAME;ORGID;ORGNAME;ORGLEVEL;ORGTYPE;PARENTID;ACTIVE;OEKEY")) {
                    JOptionPane.showMessageDialog(parent, "Ungültiger Tabellenkopf!\nTabellenkopf muss 'ENTITY_NAME;ORGID;ORGNAME;ORGLEVEL;ORGTYPE;PARENTID;ACTIVE;OEKEY' enthalten.", "Importfehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    lines.add(line);
                }
                scanner.close();
                textPane.setText("");

                try {
                    lines.stream()
                            .map(str -> str.split(";"))
                            .sorted(Comparator.comparing(o -> o[0]))
                            .map(strings -> {
                                return String.format("INSERT INTO ENTITYORGANISATION (ENTITY_NAME, ORGID, ORGNAME, ORGLEVEL, ORGTYPE, PARENTID, `ACTIVE`, OEKEY) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');", strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]);
                            })
                            .forEach(line -> {
                                textPane.setText(textPane.getText() + line + System.lineSeparator());
                            });
                } catch (Exception ex) {
                    Main.LOGGER.severe("[" + ex.getClass().getSimpleName() + "] " + ex.getMessage());
                    JOptionPane.showMessageDialog(parent, "Es gab einen Fehler beim Lesen der Datei. Sind alle Zeilen konform mit dem Tabellenkopf?", "Konvertierungsfehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(parent, "Es gab einen Fehler beim Lesen der Datei.", "Konvertierungsfehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(root);
        setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new GridLayoutManager(2, 2, new Insets(10, 10, 10, 10), -1, -1));
        root.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        root.add(scrollPane1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 250), null, 0, false));
        textPane = new JTextPane();
        textPane.setEditable(false);
        scrollPane1.setViewportView(textPane);
        final JTabbedPane tabbedPane1 = new JTabbedPane();
        root.add(tabbedPane1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 50), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Entity Config", panel1);
        ecBrowseButton = new JButton();
        ecBrowseButton.setText("Durchsuchen...");
        panel1.add(ecBrowseButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ecCurrentFileLabel = new JLabel();
        ecCurrentFileLabel.setText("keine Datei ausgewählt");
        panel1.add(ecCurrentFileLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(329, 16), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Entity Location", panel2);
        elBrowseButton = new JButton();
        elBrowseButton.setText("Durchsuchen...");
        panel2.add(elBrowseButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        elCurrentFileLabel = new JLabel();
        elCurrentFileLabel.setText("keine Datei ausgewählt");
        panel2.add(elCurrentFileLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Entity Organisation", panel3);
        eoBrowseButton = new JButton();
        eoBrowseButton.setText("Durchsuchen...");
        panel3.add(eoBrowseButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eoCurrentFileLabel = new JLabel();
        eoCurrentFileLabel.setText("keine Datei ausgewählt");
        panel3.add(eoCurrentFileLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
