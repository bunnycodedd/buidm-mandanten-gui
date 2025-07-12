package me.eva.buidmgui.model;

import javax.swing.tree.TreeModel;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

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
