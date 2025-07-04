package me.eva.buidmgui;

import me.eva.buidmgui.gui.MainPage;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                launch();
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void launch() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        MainPage frame = new MainPage();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setVisible(true);
    }

}