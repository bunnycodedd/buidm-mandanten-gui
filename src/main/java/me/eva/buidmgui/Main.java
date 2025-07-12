package me.eva.buidmgui;

import me.eva.buidmgui.gui.LoginPage;

import javax.swing.*;
import java.util.logging.Logger;


public class Main {

    public static final Logger LOGGER = Logger.getLogger("MANDANTENGUI");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                launch();
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void launch() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        LoginPage frame = new LoginPage();
    }

}