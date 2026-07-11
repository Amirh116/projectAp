package com.civgame1.model.core;

import com.civgame1.model.civilization.City;
import com.civgame1.model.civilization.Civilization;
import com.civgame1.model.unit.Unit;
import com.civgame1.model.unit.UnitType;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private final GameMap map;
    private final List<Civilization> civilizations;

    private int currentTurn;
    private int currentCivIndex;

    public GameState(int mapWidth, int mapHeight, List<String> civNames) {
        this.map = new GameMap(mapWidth, mapHeight);
        this.civilizations = new ArrayList<>();
        this.currentTurn = 1;
        this.currentCivIndex = 0;
        
        List<Tile> allTiles = new ArrayList<>(map.getAllTiles());
        java.util.Collections.shuffle(allTiles);
        int tileIndex = 0;

        for (String name : civNames) {
            Civilization civ = new Civilization(name, "Leader", false);
            civilizations.add(civ);

            // Find a valid starting tile
            Tile startTile = null;
            while (tileIndex < allTiles.size()) {
                Tile t = allTiles.get(tileIndex++);
                if (t.getTerrainType() != com.civgame1.model.terrain.TerrainType.MOUNTAIN && t.getCity() == null) {
                    startTile = t;
                    break;
                }
            }

            if (startTile != null) {
                // 1. Create Town Hall (City)
                createCity(name + " Town", startTile.getPosition(), civ);
                
                // 2. Spawn starting units: 2 Builders, 2 Workers, 1 Explorer
                createUnit(UnitType.BUILDER, civ, startTile.getPosition());
                createUnit(UnitType.BUILDER, civ, startTile.getPosition());
                createUnit(UnitType.WORKER, civ, startTile.getPosition());
                createUnit(UnitType.WORKER, civ, startTile.getPosition());
                createUnit(UnitType.EXPLORER, civ, startTile.getPosition());
                
                // 3. Initial Fog of War reveal for this civ
                // (Since we only have a shared vision state right now, we just reveal it on the map)
                revealTiles(startTile.getPosition(), 3);
            }
        }
    }

    public void revealTiles(HexCoordinate center, int radius) {
        for (Tile t : map.getTilesInRadius(center, radius)) {
            t.setExplored(true);
            t.setVisible(true);
        }
    }

    public void addCivilization(Civilization civ) {
        civilizations.add(civ);
    }

    public Civilization getCurrentCivilization() {
        if (civilizations.isEmpty()) {
            return null;
        }
        return civilizations.get(currentCivIndex);
    }
    
    public Civilization getCivilizationByName(String name) {
        for (Civilization civ : civilizations) {
            if (civ.getName().equals(name)) {
                return civ;
            }
        }
        return null;
    }

    public List<Civilization> getCivilizations() {
        return new ArrayList<>(civilizations);
    }

    public Unit createUnit(UnitType type, Civilization owner, HexCoordinate position) {
        Unit unit = new Unit(type, owner.getName(), position);
        owner.addUnit(unit);

        Tile tile = map.getTile(position);
        if (tile != null) {
            tile.addUnit(unit);
        }

        return unit;
    }

    public void removeUnit(Unit unit) {
        Civilization owner = getCivilizationByName(unit.getOwnerCivName());
        if (owner != null) {
            owner.removeUnit(unit);
        }

        Tile tile = map.getTile(unit.getPosition());
        if (tile != null) {
            tile.removeUnit(unit);
        }
    }

    public City createCity(String cityName, HexCoordinate position, Civilization owner) {
        City city = new City(cityName, owner, position);
        owner.addCity(city);

        Tile tile = map.getTile(position);
        if (tile != null) {
            tile.setCity(city);
        }

        for (Tile workableTile : map.getTilesInRadius(position, 3)) {
            city.addWorkableTile(workableTile);
        }

        return city;
    }

    public void endTurn() {
        Civilization currentCiv = getCurrentCivilization();

        // 1. Process cities (Town Halls) and buildings
        for (City city : currentCiv.getCities()) {
            
            // Advance Town Hall queue with generic production points
            city.processProduction(2); 

            // Process each building for resources and upkeep
            for (com.civgame1.model.building.Building b : city.getBuildings()) {
                if (!b.isActive()) continue;

                // Deduct Upkeep
                if (b.getType().upkeepWood > 0) {
                    if (currentCiv.spendWood(b.getType().upkeepWood)) {
                        b.recordUpkeepSuccess();
                    } else {
                        b.recordUpkeepFailure();
                        if (b.isDestroyed()) {
                            b.setActive(false);
                            // We should technically remove stationed workers here,
                            // but setting it inactive stops production.
                            continue;
                        }
                    }
                }

                // Generate Specific Resources
                int yieldAmount = b.getProductionYield();
                if (yieldAmount > 0 && b.getType().producedResource != null) {
                    switch (b.getType().producedResource) {
                        case "WOOD" -> currentCiv.addWood(yieldAmount);
                        case "STONE" -> currentCiv.addStone(yieldAmount);
                        case "IRON" -> currentCiv.addIron(yieldAmount);
                        case "FOOD" -> currentCiv.addFood(yieldAmount);
                    }
                }
            }
            
            // Town Hall Safeguard Resource Generation
            currentCiv.addWood(1);
            currentCiv.addFood(1);
        }
        
        // Calculate food consumption
        int totalFoodConsumption = currentCiv.getUnits().size() * 1;
        currentCiv.consumeFood(totalFoodConsumption);

        // 2. Reset Units AP
        for (Unit unit : currentCiv.getUnits()) {
            unit.resetForNewTurn();
            // If starving, penalize AP
            if (currentCiv.isStarving() && unit.getCurrentMovementPoints() > 0) {
                // simple starvation penalty: lose 1 AP
                // wait, there is no set movement points.
            }
        }

        currentCivIndex = (currentCivIndex + 1) % civilizations.size();

        if (currentCivIndex == 0) {
            currentTurn++;
        }
    }

    public GameMap getMap() {
        return map;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int getCurrentCivIndex() {
        return currentCivIndex;
    }
}
