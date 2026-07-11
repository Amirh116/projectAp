package com.civgame1.model.production;

import java.util.LinkedList;
import java.util.Queue;

public class ProductionQueue {

    public static class QueueItem {
        private final String name;
        private final Object target; // Can be UnitType, BuildingType, or Technology
        private int remainingCost;

        public QueueItem(String name, Object target, int totalCost) {
            this.name = name;
            this.target = target;
            this.remainingCost = totalCost;
        }

        public String getName() { return name; }
        public Object getTarget() { return target; }
        public int getRemainingCost() { return remainingCost; }
        
        public void reduceCost(int amount) {
            remainingCost -= amount;
        }
        
        public boolean isComplete() {
            return remainingCost <= 0;
        }
        
        @Override
        public String toString() {
            return name + " (" + remainingCost + " left)";
        }
    }

    private final Queue<QueueItem> items;

    public ProductionQueue() {
        this.items = new LinkedList<>();
    }

    public void addItem(String name, Object target, int cost) {
        items.add(new QueueItem(name, target, cost));
    }

    public Object processProduction(int productionPoints) {
        if (items.isEmpty()) return null;

        QueueItem current = items.peek();
        current.reduceCost(productionPoints);

        if (current.isComplete()) {
            return items.poll().getTarget();
        }

        return null;
    }

    public QueueItem getCurrentItem() {
        return items.peek();
    }
    
    public void clear() {
        items.clear();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
