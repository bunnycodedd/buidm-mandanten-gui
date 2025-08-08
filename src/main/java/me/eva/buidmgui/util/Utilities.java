package me.eva.buidmgui.util;

import me.eva.buidmgui.gui.MainPage;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class Utilities {

    public static final String ENTITY_CONFIG_CSV_HEAD = "ENTITYNAME;PARAMETER_NAME;PARAMETER_VALUE;ACTIVE";
    public static final String ENTITY_LOCATION_CSV_HEAD = "ENTITYNAME;LOCATION_ID;LOCATION_NAME;LOCATION_STREET;LOCATION_CITY;LOCATION_COUNTRY;ACTIVE";

    public static String treePathToString(TreeNode[] path) {
        StringBuilder builder = new StringBuilder();
        for (TreeNode treeNode : path) {
            if (!treeNode.isLeaf())
                builder.append(treeNode.toString()).append("/");
            else builder.append(treeNode.toString());
        }
        return builder.toString();
    }

    public static TreePath stringToTreePath(String path) {
        String[] strings = path.split("/");

        return new TreePath(Arrays.stream(strings)
                .map(s -> new DefaultMutableTreeNode())
                .toArray());
    }

    public static void shiftComponents(JPanel panel, Component component, int index) {
        HashMap<Integer, Component> components = new HashMap<>();
        for (int i = 0; i < panel.getComponents().length; i++) {
            components.put(i, panel.getComponents()[i]);
        }
        components.forEach((i, comp) -> {
            if (i > index) i++;
        });
        System.out.println(components);
    }

    public static ArrayList<String> toEntityConfigCsv() {
        ArrayList<String> csv = new ArrayList<>();
        String[][] data = MainPage.getInstance().getDatabaseConnection().getEntityConfigsRaw();

        csv.add("# FOR TABLE: ENTITYCONFIG");
        csv.add("# CREATED AT: " + LocalDate.now().format(DateTimeFormatter.ISO_DATE.withLocale(Locale.GERMANY)));

        csv.add(ENTITY_CONFIG_CSV_HEAD);
        for (int i = 0; i < data.length; i++) {
            Iterator<String> iterator = Arrays.stream(data[i]).iterator();
            StringBuilder rowBuilder = new StringBuilder();
            do {
                rowBuilder.append(iterator.next()).append(";");
            } while (iterator.hasNext());
            csv.add(rowBuilder.substring(0, rowBuilder.toString().length() - 1));
        }
        MainPage.getInstance().getConsoleOutputStream().printlnStamped(csv.size()-2 + " Zeilen in ENTITYCONFIG.csv geschrieben");

        return csv;
    }
}
