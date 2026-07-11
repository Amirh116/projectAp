package com.civgame1.controller;

import com.civgame1.model.civilization.Civilization;
import com.civgame1.model.civilization.City;
import com.civgame1.model.core.GameState;
import com.civgame1.model.core.HexCoordinate;
import com.civgame1.model.core.Tile;
import com.civgame1.model.unit.Unit;
import com.civgame1.model.unit.UnitType;

import java.util.List;

public class GameController {

    private final GameState  gameState;
    private final TurnManager turnManager;

    public GameController(int mapWidth, int mapHeight, List<String> civNames) {
        this.gameState   = new GameState(mapWidth, mapHeight, civNames);
        this.turnManager = new TurnManager(gameState);
    }

    public GameState     getGameState()      { return gameState; }
    public Civilization  getCurrentPlayer()  { return gameState.getCurrentCivilization(); }
    public int           getCurrentTurn()    { return gameState.getCurrentTurn(); }

    public boolean moveUnit(Unit unit, HexCoordinate target) {
        Tile targetTile = gameState.getMap().getTile(target);
        if (targetTile == null || targetTile.isImpassable()) return false;

        int cost = targetTile.getMovementCost();
        if (unit.getCurrentMovementPoints() < cost) return false;

        // Move between tiles
        Tile currentTile = gameState.getMap().getTile(unit.getPosition());
        if (currentTile != null) currentTile.removeUnit(unit);
        unit.move(target, cost);
        targetTile.addUnit(unit);
        
        // Update Fog of War based on unit type
        int visionRadius = (unit.getType() == UnitType.EXPLORER) ? 4 : 2;
        gameState.revealTiles(target, visionRadius);
        
        return true;
    }

    public boolean expandBorders(Unit expander) {
        if (expander.getType() != UnitType.BORDER_EXPANDER) return false;
        if (expander.getCurrentMovementPoints() < 1) return false; // Action consumes AP

        HexCoordinate center = expander.getPosition();
        String civName = expander.getOwnerCivName();

        // Claim center and neighbors
        List<Tile> claimedTiles = gameState.getMap().getTilesInRadius(center, 1);
        for (Tile t : claimedTiles) {
            t.setOwnerCivName(civName);
        }

        // Consume unit
        gameState.removeUnit(expander);
        return true;
    }

    public void fortifyUnit(Unit unit) {
        unit.fortify();
    }

    public void endTurn() {
        turnManager.endCurrentTurn();
    }

    private Civilization findCivByName(String name) {
        return gameState.getCivilizations().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
