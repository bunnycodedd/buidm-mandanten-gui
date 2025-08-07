package me.eva.buidmgui.model;

public class EntityLocation {

    private EntityConfig entity;
    private final String entityName;
    private final int locationId;
    private final String locationName;
    private final String locationStreet;
    private final String locationPostcode;
    private final String locationCity;
    private final String locationCountry;
    private final int active;

    public EntityLocation(EntityConfig entity, int locationId, String locationName, String locationStreet, String locationPostcode, String locationCity, String locationCountry, int active) {
        this.entity = entity;
        this.entityName = entity.getFriendlyName();
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationStreet = locationStreet;
        this.locationPostcode = locationPostcode;
        this.locationCity = locationCity;
        this.locationCountry = locationCountry;
        this.active = active;
    }


    public void setEntity(EntityConfig entity) {
        this.entity = entity;
    }

    public String getEntityName() {
        return entityName;
    }

    public EntityConfig getEntity() {
        if (entity == null) throw new IllegalStateException("Entity has not been set");
        return entity;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationStreet() {
        return locationStreet;
    }

    public String getLocationPostcode() {
        return locationPostcode;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public int getActive() {
        return active;
    }
}