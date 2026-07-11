package com.civgame1.model.civilization;

public class Technology {

    private final String name;
    private final int productionCost;
    private final Technology[] prerequisites;

    private boolean researched;

    public Technology(String name, int productionCost, Technology... prerequisites) {
        this.name = name;
        this.productionCost = productionCost;
        this.prerequisites = prerequisites;
        this.researched = false;
    }

    public boolean canResearch() {
        for (Technology prereq : prerequisites) {
            if (!prereq.isResearched()) {
                return false;
            }
        }
        return true;
    }

    public void complete() {
        this.researched = true;
    }

    public String getName() {
        return name;
    }

    public int getProductionCost() {
        return productionCost;
    }

    public Technology[] getPrerequisites() {
        return prerequisites;
    }

    public boolean isResearched() {
        return researched;
    }

    @Override
    public String toString() {
        return name + " (" + productionCost + " production)";
    }
}
