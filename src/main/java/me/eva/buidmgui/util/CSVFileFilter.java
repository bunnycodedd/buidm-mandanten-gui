package me.eva.buidmgui.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class CSVFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        System.out.println(f.getName());
        return FilenameUtils.getExtension(f.getName()).equals("csv");
    }

    @Override
    public String getDescription() {
        return "CSV files (*.csv)";
    }
}
