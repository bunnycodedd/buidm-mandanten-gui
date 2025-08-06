package me.eva.buidmgui.model;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ConsolePrintStream extends PrintStream {

    public ConsolePrintStream(OutputStream out) {
        super(out);
    }

    public void printlnStamped(String str) {
        printf("[%s] %s", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), str);
        println();
    }
}
