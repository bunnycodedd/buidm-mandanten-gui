package me.eva.buidmgui.model;

import com.intellij.uiDesigner.core.GridConstraints;
import me.eva.buidmgui.gui.MainPage;
import me.eva.buidmgui.util.Utilities;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TreeSelectionListenerImpl implements TreeSelectionListener {

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
                    //mainPage.getExplorer().getSelectionModel().;
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
}