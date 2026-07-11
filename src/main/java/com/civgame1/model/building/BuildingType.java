package com.civgame1.model.building;

public enum BuildingType {

    TOWN_HALL(0, 0, 0, 0, 0, "NONE"),
    LUMBER_MILL(10, 1, 2, 5, 0, "WOOD"),
    STONE_MINE(20, 1, 3, 5, 0, "STONE"),
    IRON_MINE(30, 1, 3, 3, 0, "IRON"),
    FARM(15, 1, 2, 5, 0, "FOOD"),
    STABLE(15, 1, 2, 4, 0, "FOOD"),
    TOWN(50, 2, 0, 0, 5, "NONE");

    public final int productionCost;
    public final int upkeepWood;
    public final int maxWorkers;
    public final int baseProduction;
    public final int unitCapBonus;
    public final String producedResource;

    BuildingType(int productionCost, int upkeepWood, int maxWorkers, int baseProduction, int unitCapBonus, String producedResource) {
        this.productionCost = productionCost;
        this.upkeepWood = upkeepWood;
        this.maxWorkers = maxWorkers;
        this.baseProduction = baseProduction;
        this.unitCapBonus = unitCapBonus;
        this.producedResource = producedResource;
    }
}
