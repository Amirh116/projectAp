package com.civgame1.model.terrain;

public enum ResourceType {
    WHEAT(3, 0, 0),
    CATTLE(2, 0, 1),
    FISH(3, 0, 0),
    DEER(1, 1, 0),
    STONE(0, 1, 0),
    IRON(0, 2, 0),
    GOLD(0, 0, 3),
    GEMS(0, 0, 4);

    private final int food;
    private final int production;
    private final int gold;

    ResourceType(int food, int production, int gold) {
        this.food = food;
        this.production = production;
        this.gold = gold;
    }

    public int getFood() { return food; }
    public int getProduction() { return production; }
    public int getGold() { return gold; }
}
