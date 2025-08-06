package me.eva.buidmgui;

import me.eva.buidmgui.gui.LoginPage;
import me.eva.buidmgui.gui.MainPage;
import me.eva.buidmgui.net.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.SQLException;


public class Main {

    public static final Logger LOGGER = LoggerFactory.getLogger("SYSTEM");
    public static String ARG_USER;
    public static String ARG_PASSWORD;
    public static String ARG_HOST;

    public static void main(String[] args) {

        // Positional args: user password host
        if(args.length == 3) {
            ARG_USER = args[0];
            ARG_PASSWORD = args[1];
            ARG_HOST = args[2];
        }

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
        if(ARG_USER == null) {
            LoginPage frame = new LoginPage();
        } else {
            try {
                DatabaseConnection connection = new DatabaseConnection(
                        ARG_HOST,
                        "identityiqPlugin",
                        ARG_USER,
                        ARG_PASSWORD
                );
                MainPage mainPage = new MainPage(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}