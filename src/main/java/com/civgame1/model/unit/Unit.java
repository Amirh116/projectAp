package com.civgame1.model.unit;

import com.civgame1.model.core.HexCoordinate;

public class Unit {
    private final UnitType type;
    private final String ownerCivName;
    private HexCoordinate position;
    private int currentMovementPoints;
    private int currentHealth;
    private boolean hasActed;
    private boolean fortified;
    
    private int charges;
    private com.civgame1.model.building.Building stationedBuilding;

    public Unit(UnitType type, String ownerCivName, HexCoordinate position) {
        this.type = type;
        this.ownerCivName = ownerCivName;
        this.position = position;
        this.currentMovementPoints = type.movementPoints;
        this.currentHealth = 100;
        this.hasActed = false;
        this.fortified = false;
        this.charges = type == UnitType.BUILDER ? 3 : 0;
        this.stationedBuilding = null;
    }

    public void resetForNewTurn() {
        if (stationedBuilding == null) {
            currentMovementPoints = type.movementPoints;
        }
        hasActed = false;
    }

    public boolean canMove() {
        return currentMovementPoints > 0 && !hasActed;
    }

    public void move(HexCoordinate newPosition, int moveCost) {
        this.position = newPosition;
        this.currentMovementPoints = Math.max(0, currentMovementPoints - moveCost);
        this.fortified = false;
    }

    public void fortify() {
        this.fortified = true;
        this.hasActed = true;
        this.currentMovementPoints = 0;
    }

    public void takeDamage(int damage) {
        this.currentHealth = Math.max(0, currentHealth - damage);
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }


    public UnitType getType()                    { return type; }
    public String getOwnerCivName()              { return ownerCivName; }
    public HexCoordinate getPosition()           { return position; }
    public int getCurrentMovementPoints()        { return currentMovementPoints; }
    public int getCurrentHealth()                { return currentHealth; }
    public boolean hasActed()                    { return hasActed; }
    public boolean isFortified()                 { return fortified; }
    public void setHasActed(boolean hasActed)    { this.hasActed = hasActed; }
    
    public int getCharges()                      { return charges; }
    public void useCharge()                      { if (charges > 0) charges--; }
    public boolean isConsumed()                  { return type == UnitType.BUILDER && charges <= 0; }
    
    public com.civgame1.model.building.Building getStationedBuilding() { return stationedBuilding; }
    public void setStationedBuilding(com.civgame1.model.building.Building building) { this.stationedBuilding = building; }

    @Override
    public String toString() {
        return String.format("%s [HP:%d MP:%d]%s%s",
                type.name(), currentHealth,
                currentMovementPoints, fortified ? " (Fortified)" : "",
                stationedBuilding != null ? " (Stationed)" : "");
    }
}
