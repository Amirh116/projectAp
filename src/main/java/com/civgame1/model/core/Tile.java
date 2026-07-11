package com.civgame1.model.core;

import com.civgame1.model.building.Building;
import com.civgame1.model.civilization.City;
import com.civgame1.model.terrain.ResourceType;
import com.civgame1.model.terrain.TerrainType;
import com.civgame1.model.unit.Unit;

import java.util.ArrayList;
import java.util.List;


public class Tile {

    private final HexCoordinate position;
    private final TerrainType terrainType;
    private ResourceType resource;

    private Building building;
    private City city;
    private final List<Unit> units;


    private boolean explored;
    private boolean visible;
    
    private String ownerCivName;

    public Tile(HexCoordinate position, TerrainType terrainType, ResourceType resource) {
        this.position = position;
        this.terrainType = terrainType;
        this.resource = resource;
        this.units = new ArrayList<>();
        this.explored = false;
        this.visible = false;
    }



    public int getMovementCost() {
        return terrainType.getMovementCost();
    }

    public boolean isImpassable() {
        // According to requirements, Mountain has cost 4, so no terrain is strictly impassable by default.
        return false;
    }

    public String getOwnerCivName() {
        return ownerCivName;
    }

    public void setOwnerCivName(String ownerCivName) {
        this.ownerCivName = ownerCivName;
    }



    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public boolean hasUnits() {
        return !units.isEmpty();
    }

    public List<Unit> getUnits() {
        return new ArrayList<>(units);
    }



    public void setBuilding(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }

    public boolean hasBuilding() {
        return building != null;
    }



    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public boolean hasCity() {
        return city != null;
    }



    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isExplored() {
        return explored;
    }

    public boolean isVisible() {
        return visible;
    }



    public HexCoordinate getPosition() {
        return position;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public ResourceType getResource() {
        return resource;
    }

    public void setResource(ResourceType resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "pos=" + position +
                ", terrain=" + terrainType +
                ", resource=" + resource +
                ", units=" + units.size() +
                '}';
    }
}
