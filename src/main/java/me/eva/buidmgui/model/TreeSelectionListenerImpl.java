package me.eva.buidmgui.model;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import me.eva.buidmgui.gui.MainPage;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Arrays;

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

        if (selectedNode.getUserObject() instanceof IHasConfigMenu) {
            editorPanel.removeAll();
            editorPanel.add(((IHasConfigMenu) selectedNode.getUserObject()).getPanel(), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        } else {
            editorPanel.removeAll();
        }

        mainPage.revalidate();
        mainPage.repaint();
    }
}