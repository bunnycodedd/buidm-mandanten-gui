package me.eva.buidmgui.gui.wizard;

public class EntityCreationContext {

    private String entityName;
    private String entityId;

    private String locationName;
    private String locationStreet;
    private String locationNumber;
    private String locationPostcode;
    private String locationCity;
    private String locationCountry;

    private String telInternal;
    private String telExternal;
    private String fax;

    public EntityCreationContext entityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
    public EntityCreationContext entityId(String entityId) {
        this.entityId = entityId;
        return this;
    }
    public EntityCreationContext locationNumber(String number) {
        this.locationNumber = number;
        return this;
    }

    public EntityCreationContext locationStreet(String locationStreet) {
        this.locationStreet = locationStreet;
        return this;
    }

    public EntityCreationContext locationPostcode(String locationPostcode) {
        this.locationPostcode = locationPostcode;
        return this;
    }

    public EntityCreationContext locationCity(String locationCity) {
        this.locationCity = locationCity;
        return this;
    }

    public EntityCreationContext locationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public EntityCreationContext locationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
        return this;
    }

    public EntityCreationContext telInternal(String telInternal) {
        this.telInternal = telInternal;
        return this;
    }

    public EntityCreationContext telExternal(String telExternal) {
        this.telExternal = telExternal;
        return this;
    }

    public EntityCreationContext fax(String fax) {
        this.fax = fax;
        return this;
    }

    public String getFax() {
        return fax;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityId() {
        return entityId;
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

    public String getAddress() {
        return String.format("%s,\n %s %s, %s", locationStreet, locationPostcode, locationCity, locationCountry);
    }

    public String getTelInternal() {
        return telInternal;
    }

    public String getTelExternal() {
        return telExternal;
    }
}
