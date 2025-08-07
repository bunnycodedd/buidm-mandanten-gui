package me.eva.buidmgui.model;

import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Locale;
import java.util.regex.Pattern;

public class ConsolePrintStream extends PrintStream {

    public ConsolePrintStream(OutputStream out) {
        super(out);
    }

    public void printlnStamped(String str) {
        printf("[%s] %s", LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss").withLocale(Locale.GERMANY)),
                str);
        println();
    }
}
