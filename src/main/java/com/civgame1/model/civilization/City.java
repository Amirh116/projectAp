package com.civgame1.model.civilization;

import com.civgame1.model.building.Building;
import com.civgame1.model.building.BuildingType;
import com.civgame1.model.core.HexCoordinate;
import com.civgame1.model.core.Tile;
import com.civgame1.model.production.ProductionQueue;
import com.civgame1.model.unit.UnitType;

import java.util.*;

public class City {

    private final String name;
    private final Civilization owner;
    private final HexCoordinate position;

    private final Set<Tile> workableTiles;
    private final Set<Tile> workedTiles;

    private final List<Building> buildings;
    private final ProductionQueue productionQueue;

    private int borderRadius;

    public City(String name, Civilization owner, HexCoordinate position) {
        this.name = name;
        this.owner = owner;
        this.position = position;
        this.workableTiles = new HashSet<>();
        this.workedTiles = new HashSet<>();
        this.buildings = new ArrayList<>();
        this.productionQueue = new ProductionQueue();
        this.borderRadius = 1;
        
        // A City represents the Town Hall in Phase 1
        this.buildings.add(new Building(BuildingType.TOWN_HALL));
    }

    public void addWorkableTile(Tile tile) {
        if (tile.getPosition().distanceTo(position) <= 3) {
            workableTiles.add(tile);
        }
    }

    public void workTile(Tile tile) {
        if (workableTiles.contains(tile)) {
            workedTiles.add(tile);
        }
    }

    public void stopWorkingTile(Tile tile) {
        workedTiles.remove(tile);
    }

    // calculateTotalProduction removed to fix resource generation bugs

    public void addBuilding(BuildingType type) {
        buildings.add(new Building(type));
    }

    public boolean hasBuilding(BuildingType type) {
        return buildings.stream().anyMatch(b -> b.getType() == type);
    }

    public List<Building> getBuildings() {
        return new ArrayList<>(buildings);
    }

    public ProductionQueue getProductionQueue() {
        return productionQueue;
    }

    public void processProduction(int productionPoints) {
        Object completed = productionQueue.processProduction(productionPoints);
        if (completed != null) {
            onProductionCompleted(completed);
        }
    }

    private void onProductionCompleted(Object item) {
        if (item instanceof UnitType unitType) {
            // Logic to spawn unit on map should be handled by GameState or Controller
        } else if (item instanceof BuildingType buildingType) {
            addBuilding(buildingType);
        } else if (item instanceof Technology tech) {
            // Logic for applying tech
            tech.complete();
        }
    }

    public String getName() {
        return name;
    }

    public Civilization getOwner() {
        return owner;
    }

    public HexCoordinate getPosition() {
        return position;
    }

    public Set<Tile> getWorkableTiles() {
        return new HashSet<>(workableTiles);
    }

    public Set<Tile> getWorkedTiles() {
        return new HashSet<>(workedTiles);
    }

    public int getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
    }

    @Override
    public String toString() {
        return name;
    }
}
