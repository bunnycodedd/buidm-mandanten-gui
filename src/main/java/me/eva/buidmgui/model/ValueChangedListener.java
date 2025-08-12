package me.eva.buidmgui.model;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

public class ValueChangedListener implements DocumentListener {

    private Consumer<DocumentEvent> run;

    public ValueChangedListener(Consumer<DocumentEvent> run) {
        this.run = run;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        run.accept(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        run.accept(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        run.accept(e);
    }
}
