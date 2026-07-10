package com.civgame1.model.terrain;

public enum TerrainType {
    GRASSLAND(2, 0, 0),
    PLAINS(1, 1, 0),
    DESERT(0, 0, 0),
    TUNDRA(1, 0, 0),
    SNOW(0, 0, 0),
    HILL(0, 2, 0),
    MOUNTAIN(0, 0, 0),
    OCEAN(0, 0, 1),
    COAST(1, 0, 1);

    private final int food;
    private final int production;
    private final int gold;

    TerrainType(int food, int production, int gold) {
        this.food = food;
        this.production = production;
        this.gold = gold;
    }

    public int getFood() { return food; }
    public int getProduction() { return production; }
    public int getGold() { return gold; }
}
