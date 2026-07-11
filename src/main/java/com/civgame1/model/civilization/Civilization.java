package com.civgame1.model.civilization;

import com.civgame1.model.unit.Unit;
import java.util.ArrayList;
import java.util.List;

public class Civilization {

    private final String name;
    private final String leaderName;
    private final boolean isAI;

    private int food;
    private int wood;
    private int stone;
    private int iron;

    private int storageCapacity;
    private int unitCap;

    private final List<Unit> units;
    private final List<City> cities;
    private final TechnologyTree technologyTree;

    public Civilization(String name, String leaderName, boolean isAI) {
        this.name = name;
        this.leaderName = leaderName;
        this.isAI = isAI;
        
        this.food = 50;
        this.wood = 50;
        this.stone = 0;
        this.iron = 0;
        this.storageCapacity = 200;
        this.unitCap = 5;
        
        this.units = new ArrayList<>();
        this.cities = new ArrayList<>();
        this.technologyTree = new TechnologyTree();
    }

    public void addFood(int amount) {
        food = Math.min(food + amount, storageCapacity);
    }
    
    public void consumeFood(int amount) {
        food -= amount;
    }

    public void addWood(int amount) {
        wood = Math.min(wood + amount, storageCapacity);
    }

    public boolean spendWood(int amount) {
        if (wood >= amount) {
            wood -= amount;
            return true;
        }
        return false;
    }

    public void addStone(int amount) {
        stone = Math.min(stone + amount, storageCapacity);
    }

    public boolean spendStone(int amount) {
        if (stone >= amount) {
            stone -= amount;
            return true;
        }
        return false;
    }

    public void addIron(int amount) {
        iron = Math.min(iron + amount, storageCapacity);
    }

    public boolean spendIron(int amount) {
        if (iron >= amount) {
            iron -= amount;
            return true;
        }
        return false;
    }
    
    public boolean isStarving() {
        return food < 0;
    }
    
    public void increaseStorageCapacity(int amount) {
        this.storageCapacity += amount;
    }
    
    public void increaseUnitCap(int amount) {
        this.unitCap += amount;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public List<Unit> getUnits() {
        return new ArrayList<>(units);
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public void removeCity(City city) {
        cities.remove(city);
    }

    public List<City> getCities() {
        return new ArrayList<>(cities);
    }

    public TechnologyTree getTechnologyTree() {
        return technologyTree;
    }

    public String getName() {
        return name;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public boolean isAI() {
        return isAI;
    }

    public int getFood() { return food; }
    public int getWood() { return wood; }
    public int getStone() { return stone; }
    public int getIron() { return iron; }
    public int getStorageCapacity() { return storageCapacity; }
    public int getUnitCap() { return unitCap; }

    @Override
    public String toString() {
        return name + " (Leader: " + leaderName + ")";
    }
}
