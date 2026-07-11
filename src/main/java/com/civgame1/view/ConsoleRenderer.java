package com.civgame1.view;

import com.civgame1.model.civilization.City;
import com.civgame1.model.civilization.Civilization;
import com.civgame1.model.core.GameMap;
import com.civgame1.model.core.GameState;
import com.civgame1.model.core.HexCoordinate;
import com.civgame1.model.core.Tile;
import com.civgame1.model.unit.Unit;

public class ConsoleRenderer {

    private static final String RESET  = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN  = "\u001B[32m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GREY   = "\u001B[90m";

    private final int mapCols;
    private final int mapRows;

    public ConsoleRenderer(int mapCols, int mapRows) {
        this.mapCols = mapCols;
        this.mapRows = mapRows;
    }

    public void render(GameState state) {
        clearScreen();
        printHeader(state);
        renderMap(state.getMap());
        printLegend();
        renderCivPanel(state.getCurrentCivilization());
        System.out.println("=".repeat(62));
        System.out.print("Command: ");
    }

    private void printHeader(GameState state) {
        System.out.println("=".repeat(62));
        System.out.printf("  CIVGAME1  |  Turn: %-4d |  Active: %s%n",
                state.getCurrentTurn(),
                CYAN + state.getCurrentCivilization().getName() + RESET);
        System.out.println("=".repeat(62));
    }

    private void renderMap(GameMap map) {
        System.out.println();
        for (int r = 0; r < mapRows; r++) {
            if (r % 2 == 1) System.out.print("  ");
            for (int q = 0; q < mapCols; q++) {
                int axialQ = q - r / 2;
                Tile tile = map.getTile(new HexCoordinate(axialQ, r));
                System.out.print(tileSymbol(tile) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private String tileSymbol(Tile tile) {
        if (tile == null)          return GREY + "   " + RESET;
        if (!tile.isExplored())    return GREY + " ? " + RESET;

        if (tile.getCity() != null)
            return YELLOW + "[" + tile.getCity().getName().charAt(0) + "]" + RESET;

        if (!tile.getUnits().isEmpty()) {
            Unit u = tile.getUnits().get(0);
            return GREEN + "[" + u.getType().name().charAt(0) + "]" + RESET;
        }

        return switch (tile.getTerrainType()) {
            case GRASSLAND -> " . ";
            case PLAINS    -> " , ";
            case FOREST    -> " ^ ";
            case MOUNTAIN  -> "/^^";
        };
    }

    private void printLegend() {
        System.out.println("Legend: [T]=Town Hall  [U]=Unit  .=Grass  ,=Plains");
        System.out.println("        ^=Forest   /^^=Mountain  ?=Unexplored");
    }

    private void renderCivPanel(Civilization civ) {
        System.out.println();
        System.out.printf("  %-12s | Food: %-4d | Wood: %-4d | Stone: %-4d | Iron: %-4d%n",
                civ.getName(), civ.getFood(), civ.getWood(), civ.getStone(), civ.getIron());

        if (!civ.getCities().isEmpty()) {
            System.out.println("\n  Cities (Town Halls):");
            for (City city : civ.getCities()) {
                String prod = city.getProductionQueue().isEmpty() 
                        ? "- idle -" 
                        : city.getProductionQueue().getCurrentItem().getName();
                System.out.printf("    %-14s Producing: %s%n",
                        city.getName(), prod);
            }
        }

        if (!civ.getUnits().isEmpty()) {
            System.out.println("\n  Units:");
            civ.getUnits().forEach(u ->
                    System.out.printf("    %s @ (%d,%d)%n",
                            u, u.getPosition().q, u.getPosition().r));
        }
        System.out.println();
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
