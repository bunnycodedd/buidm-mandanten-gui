package me.eva.buidmgui.model;

public class EntityOrganisation {

    private final EntityConfig entity;
    private final String entityName;
    private final int orgId;
    private final String orgName;
    private final int orgLevel;
    private final String orgType; // TODO: möglicherweise könnte man orgTypes manuell erstellen und zuordnen
    private final int parentId;
    private final int active;
    private final int oeKey = -1;

    public EntityOrganisation(EntityConfig entity, int orgId, String orgName, int orgLevel, String orgType, int parentId, int active) {
        this.entity = entity;
        this.entityName = entity.getFriendlyName();
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgLevel = orgLevel;
        this.orgType = orgType;
        this.parentId = parentId;
        this.active = active;
    }

    public EntityConfig getEntity() {
        return entity;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getOrgId() {
        return orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public int getOrgLevel() {
        return orgLevel;
    }

    public String getOrgType() {
        return orgType;
    }

    public int getParentId() {
        return parentId;
    }

    public int getActive() {
        return active;
    }

    public int getOeKey() {
        return oeKey;
    }
}