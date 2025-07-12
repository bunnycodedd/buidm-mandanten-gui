package me.eva.buidmgui.util;

public class Utilities {

    public static String csvToSQL(String csv, String tableName, String head, String delimiter) {
        String[] rows = csv.split("\n");
        String[] columns = head.split(delimiter);

        StringBuilder builder = new StringBuilder("INSERT INTO " + tableName + "(");
        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i]);
            if(i < columns.length - 1) {
                builder.append(",");
            }
        }
        builder.append(") VALUES (");
        for (int i = 0; i < rows.length; i++) {
            builder.append(rows[i]);
            if(i < rows.length - 1) {
                builder.append(",");
            }
        }
        /*builder.append(") ON DUPLICATE KEY UPDATE ");
        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i]).append("=").append(rows[i]);
            if(i < columns.length - 1) {
                builder.append(",");
            }
        }*/
        return builder.toString();
    }

}
