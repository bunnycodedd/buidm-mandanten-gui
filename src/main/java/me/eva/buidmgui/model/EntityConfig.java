package me.eva.buidmgui.model;

import javax.swing.tree.TreeModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Beschreibt eine Entity-Konfiguration basierend auf der Tabelle ENTITYCONFIGURATION
 * Dabei ist dies <b>KEINE</b> 1:1 übersetzung aus der Zeilen der Tabelle. Vielmehr werden alle Parameter
 * ihre Werte unter einem Namen zusammengeführt.
 */
public class EntityConfig implements Comparable<EntityConfig> {

    private final String entityName;
    private final TreeMap<String, String> parameters;

    public EntityConfig(String entityName) {
        this.entityName = entityName;
        parameters = new TreeMap<>();
    }


    @Override
    public int compareTo(EntityConfig o) {
        return o.entityName.compareTo(entityName);
    }

    public String getEntityName() {
        return entityName;
    }

    public TreeMap<String, String> getParameters() {
        return parameters;
    }

}
