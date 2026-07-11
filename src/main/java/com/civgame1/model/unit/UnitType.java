package com.civgame1.model.unit;

public enum UnitType {

    EXPLORER(0, 0, 50, 4, true, 4),
    BUILDER(0, 0, 50, 2, true, 2),
    WORKER(0, 0, 40, 2, true, 2),
    BORDER_EXPANDER(0, 0, 100, 2, true, 2);

    public final int attack;
    public final int defense;
    public final int productionCost;
    public final int movementPoints;
    public final boolean isCivilian;
    public final int visionRadius;

    UnitType(int attack, int defense, int productionCost,
             int movementPoints, boolean isCivilian, int visionRadius) {
        this.attack         = attack;
        this.defense        = defense;
        this.productionCost = productionCost;
        this.movementPoints = movementPoints;
        this.isCivilian     = isCivilian;
        this.visionRadius   = visionRadius;
    }
}
