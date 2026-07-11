package com.civgame1.model.building;

import com.civgame1.model.unit.Unit;
import java.util.ArrayList;
import java.util.List;

public class Building {

    private final BuildingType type;
    private boolean active;
    
    private List<Unit> stationedWorkers;
    private int unpaidUpkeepTurns;

    public Building(BuildingType type) {
        this.type = type;
        this.active = true;
        this.stationedWorkers = new ArrayList<>();
        this.unpaidUpkeepTurns = 0;
    }

    public boolean stationWorker(Unit unit) {
        if (stationedWorkers.size() < type.maxWorkers) {
            stationedWorkers.add(unit);
            unit.setStationedBuilding(this);
            return true;
        }
        return false;
    }

    public void removeWorker(Unit unit) {
        if (stationedWorkers.remove(unit)) {
            unit.setStationedBuilding(null);
        }
    }
    
    public int getProductionYield() {
        if (!active) return 0;
        return stationedWorkers.size() * type.baseProduction;
    }

    public void recordUpkeepFailure() {
        unpaidUpkeepTurns++;
    }
    
    public void recordUpkeepSuccess() {
        unpaidUpkeepTurns = 0;
    }
    
    public boolean isDestroyed() {
        return unpaidUpkeepTurns >= 3;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public BuildingType getType() {
        return type;
    }
    
    public List<Unit> getStationedWorkers() {
        return new ArrayList<>(stationedWorkers);
    }

    @Override
    public String toString() {
        return type.name() + (active ? "" : " (Inactive)") + " [Workers: " + stationedWorkers.size() + "/" + type.maxWorkers + "]";
    }
}
