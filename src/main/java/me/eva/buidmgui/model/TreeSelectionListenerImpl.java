package me.eva.buidmgui.model;

import com.intellij.uiDesigner.core.GridConstraints;
import me.eva.buidmgui.gui.MainPage;
import me.eva.buidmgui.util.Utilities;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.RowMapper;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;

public class TreeSelectionListenerImpl implements TreeSelectionListener, TreeSelectionModel {

    private final MainPage mainPage;

    public TreeSelectionListenerImpl(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        JTree tree = (JTree) e.getSource();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        JPanel editorPanel = mainPage.getEditorPanel();


        mainPage.getTreeSearchField().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mainPage.getExplorer().getSelectionModel()
                            .setSelectionPath(Utilities.stringToTreePath(mainPage.getTreeSearchField().getText()));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        mainPage.getTreeSearchField().setText(Utilities.treePathToString(selectedNode.getPath()));

        if (selectedNode.getUserObject() instanceof IHasConfigMenu) {
            editorPanel.add(((IHasConfigMenu) selectedNode.getUserObject()).getPanel(), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, -1), new Dimension(-1, -1), null, 0, false), 0);
        } else {
            editorPanel.removeAll();
        }

        mainPage.revalidate();
        mainPage.repaint();
    }

    @Override
    public void setSelectionMode(int mode) {
    }

    @Override
    public int getSelectionMode() {
        return SINGLE_TREE_SELECTION;
    }

    @Override
    public void setSelectionPath(TreePath path) {

    }

    @Override
    public void setSelectionPaths(TreePath[] paths) {

    }

    @Override
    public void addSelectionPath(TreePath path) {

    }

    @Override
    public void addSelectionPaths(TreePath[] paths) {

    }

    @Override
    public void removeSelectionPath(TreePath path) {

    }

    @Override
    public void removeSelectionPaths(TreePath[] paths) {

    }

    @Override
    public TreePath getSelectionPath() {
        return null;
    }

    @Override
    public TreePath[] getSelectionPaths() {
        return new TreePath[0];
    }

    @Override
    public int getSelectionCount() {
        return 0;
    }

    @Override
    public boolean isPathSelected(TreePath path) {
        return false;
    }

    @Override
    public boolean isSelectionEmpty() {
        return false;
    }

    @Override
    public void clearSelection() {

    }

    @Override
    public void setRowMapper(RowMapper newMapper) {

    }

    @Override
    public RowMapper getRowMapper() {
        return null;
    }

    @Override
    public int[] getSelectionRows() {
        return new int[0];
    }

    @Override
    public int getMinSelectionRow() {
        return 0;
    }

    @Override
    public int getMaxSelectionRow() {
        return 0;
    }

    @Override
    public boolean isRowSelected(int row) {
        return false;
    }

    @Override
    public void resetRowSelection() {

    }

    @Override
    public int getLeadSelectionRow() {
        return 0;
    }

    @Override
    public TreePath getLeadSelectionPath() {
        return null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void addTreeSelectionListener(TreeSelectionListener x) {

    }

    @Override
    public void removeTreeSelectionListener(TreeSelectionListener x) {

    }
}