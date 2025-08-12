package me.eva.buidmgui.controller;

import me.eva.buidmgui.Main;
import me.eva.buidmgui.gui.MainPage;
import me.eva.buidmgui.util.Utilities;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

            if (!firstLine.equalsIgnoreCase(Utilities.ENTITY_LOCATION_CSV_HEAD)) {
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

    public static void handleExport(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(".", FileSystemView.getFileSystemView());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showOpenDialog(MainPage.getInstance());

        File file = fileChooser.getSelectedFile();
        if (file == null) return;
        if (!file.isDirectory()) return;

        String path = file.getAbsolutePath();
        ArrayList<String> ecCsv = Utilities.toEntityConfigCsv();
        File ecCsvFile = new File(path, "ENTITYCONFIG.csv");
        try {
            if(ecCsvFile.exists()) {
                if(JOptionPane.showConfirmDialog(MainPage.getInstance().getRootPane(), "Es existiert bereits eine Datei mit diesem Namen. Soll diese überschrieben werden?", "Datei existiert bereits", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 1) {
                    ecCsvFile.delete();
                }
            }

            if(!ecCsvFile.exists()) {
                ecCsvFile.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(ecCsvFile);
            System.out.println(fileWriter.getEncoding());
            for (String s : ecCsv) {
                fileWriter.write(s + "\n");
            }
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
