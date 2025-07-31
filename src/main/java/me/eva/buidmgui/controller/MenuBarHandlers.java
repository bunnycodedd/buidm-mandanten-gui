package me.eva.buidmgui.controller;

import me.eva.buidmgui.Main;
import me.eva.buidmgui.gui.MainPage;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class MenuBarHandlers {

    public static void handleImportEntityLocation(JPanel parent) {
        JFileChooser fileChooser = new JFileChooser(".", FileSystemView.getFileSystemView());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showOpenDialog(parent);

        File file = fileChooser.getSelectedFile();
        if (file == null) {
            return;
        }
        if (!FilenameUtils.getExtension(file.getName()).equals("csv")) {
            JOptionPane.showMessageDialog(parent, "Datei ist keine .csv-Datei", "Importfehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Scanner scanner = new Scanner(new FileReader(file));
            String firstLine = scanner.nextLine().substring(1);
            System.out.println(firstLine);

            if (!firstLine.equalsIgnoreCase("ENTITYNAME;LOCATION_ID;LOCATION_NAME;LOCATION_STREET;LOCATION_CITY;LOCATION_COUNTRY;ACTIVE")) {
                JOptionPane.showMessageDialog(parent, "Ungültiger Tabellenkopf!\nTabellenkopf muss 'ENTITYNAME;LOCATION_ID;LOCATION_NAME;LOCATION_STREET;LOCATION_POSTCODE;LOCATION_CITY;LOCATION_COUNTRY;ACTIVE' enthalten.", "Importfehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String> rows = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                rows.add(line);
            }

            String[][] data = rows.stream().sorted().map(s -> s.split(";")).toArray(String[][]::new);
            for (int i = 0; i < data.length; i++) {
                ((DefaultTableModel) MainPage.getInstance().getEntityLocationTable().getModel()).addRow(data[i]);
            }
        } catch (FileNotFoundException e) {
            Main.LOGGER.severe(e.getMessage());
        }
    }

    public static void handleImportEntityConfig(ActionEvent event, JPanel parent) {
        JFileChooser fileChooser = new JFileChooser(".", FileSystemView.getFileSystemView());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showOpenDialog(parent);

        File file = fileChooser.getSelectedFile();
        if (file == null) {
            return;
        }
        if (!FilenameUtils.getExtension(file.getName()).equals("csv")) {
            JOptionPane.showMessageDialog(parent, "Datei ist keine .csv-Datei", "Importfehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Scanner scanner = new Scanner(new FileReader(file));
            String firstLine = scanner.nextLine().substring(1);
            System.out.println(firstLine);

            if (!firstLine.equalsIgnoreCase("ENTITYNAME;PARAMETER_NAME;PARAMETER_VALUE;ACTIVE")) {
                JOptionPane.showMessageDialog(parent, "Ungültiger Tabellenkopf!\nTabellenkopf muss 'ENTITYNAME;PARAMETER_NAME;PARAMETER_VALUE;ACTIVE' enthalten.", "Importfehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String> rows = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                rows.add(line);
            }

            String[][] data = rows.stream().sorted().map(s -> s.split(";")).toArray(String[][]::new);
            for (int i = 0; i < data.length; i++) {
                ((DefaultTableModel) MainPage.getInstance().getEntityConfigTable().getModel()).addRow(data[i]);
            }
        } catch (FileNotFoundException e) {
            Main.LOGGER.severe(e.getMessage());
        }
    }
}
