package me.eva.buidmgui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Beschreibt eine readonly Entity-Konfiguration basierend auf der Tabelle ENTITYCONFIGURATION.
 * Dabei ist dies <b>keine</b> 1:1 übersetzung aus der Zeilen der Tabelle. Vielmehr werden alle Parameter
 * ihre Werte unter einem Namen zusammengeführt.
 */
public class EntityConfig implements Comparable<EntityConfig> {

    private final String entityName;
    private final String friendlyName;
    private final TreeMap<String, String> parameters;
    private List<EntityLocation> locations;
    private List<EntityOrganisation> organisations;

    public EntityConfig(String entityName, String friendlyName) {
        this.entityName = entityName;
        this.friendlyName = friendlyName;
        this.parameters = new TreeMap<>();
        this.locations = new ArrayList<>();
        this.organisations = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EntityConfig) {
            EntityConfig other = (EntityConfig) obj;
            return entityName.equals(other.entityName);
        }
        return false;
    }

    @Override
    public int compareTo(EntityConfig o) {
        return o.entityName.compareTo(entityName);
    }

    public List<EntityOrganisation> getOrganisations() {
        return organisations;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public List<EntityLocation> getLocations() {
        return locations;
    }

    public TreeMap<String, String> getParameters() {
        return parameters;
    }

}
