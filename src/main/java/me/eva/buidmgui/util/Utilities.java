package me.eva.buidmgui.util;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Utilities {

    public static String csvToSQL(String csv, String tableName, String head, String delimiter) {
        String[] rows = csv.split("\n");
        String[] columns = head.split(delimiter);

        StringBuilder builder = new StringBuilder("INSERT INTO " + tableName + "(");
        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i]);
            if (i < columns.length - 1) {
                builder.append(",");
            }
        }
        builder.append(") VALUES (");
        for (int i = 0; i < rows.length; i++) {
            builder.append(rows[i]);
            if (i < rows.length - 1) {
                builder.append(",");
            }
        }
        /*builder.append(") ON DUPLICATE KEY UPDATE ");
        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i]).append("=").append(rows[i]);
            if(i < columns.length - 1) {
                builder.append(",");
            }
        }*/
        return builder.toString();
    }

    public static String treePathToString(TreeNode[] path) {
        StringBuilder builder = new StringBuilder();
        for (TreeNode treeNode : path) {
            if (!treeNode.isLeaf())
                builder.append(treeNode.toString()).append("/");
            else builder.append(treeNode.toString());
        }
        return builder.toString();
    }

    public static MouseListener getTreeSelectionListener(JTree tree) {
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int selRow = tree.getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    tree.setSelectionPath(selPath);
                    if (selRow > -1) {
                        tree.setSelectionRow(selRow);
                    }
                }
            }
        };
        return ml;
    }
}
