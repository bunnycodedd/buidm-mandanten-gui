package me.eva.buidmgui.model;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ConsoleOutputStream extends OutputStream {

    private final JTextPane textPane;

    public ConsoleOutputStream(JTextPane textPane) {
        this.textPane = textPane;
    }

    @Override
    public void write(int b) throws IOException {
        // ü -> ue
        textPane.setText(textPane.getText() + (char) b);
    }

}
