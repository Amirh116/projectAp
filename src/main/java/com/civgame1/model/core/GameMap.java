package com.civgame1.model.core;

import com.civgame1.model.terrain.ResourceType;
import com.civgame1.model.terrain.TerrainType;

import java.util.*;


public class GameMap {

    private final int width;
    private final int height;
    private final Map<HexCoordinate, Tile> tiles;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new HashMap<>();

        generateMap();
    }


    private void generateMap() {
        Random random = new Random();

        for (int q = -width / 2; q <= width / 2; q++) {
            for (int r = -height / 2; r <= height / 2; r++) {
                HexCoordinate coord = new HexCoordinate(q, r);

                TerrainType terrain = TerrainType.values()[random.nextInt(TerrainType.values().length)];

                ResourceType resource = null;
                if (random.nextDouble() < 0.2) {
                    switch (terrain) {
                        case MOUNTAIN:
                            resource = random.nextBoolean() ? ResourceType.STONE : ResourceType.IRON;
                            break;
                        case PLAINS:
                            resource = ResourceType.ANIMALS;
                            break;
                        case GRASSLAND:
                            resource = random.nextBoolean() ? ResourceType.WHEAT : ResourceType.RICE;
                            break;
                        default:
                            resource = null; // Forest, etc. do not have specific resource tokens
                            break;
                    }
                }

                tiles.put(coord, new Tile(coord, terrain, resource));
            }
        }
    }



    public Tile getTile(HexCoordinate coord) {
        return tiles.get(coord);
    }

    public Tile getTile(int q, int r) {
        return tiles.get(new HexCoordinate(q, r));
    }

    public boolean isValidCoordinate(HexCoordinate coord) {
        return tiles.containsKey(coord);
    }


    public List<Tile> getNeighbors(HexCoordinate coord) {
        List<Tile> neighbors = new ArrayList<>();

        for (HexCoordinate neighbor : coord.neighbors()) {
            if (isValidCoordinate(neighbor)) {
                neighbors.add(getTile(neighbor));
            }
        }

        return neighbors;
    }


    public List<Tile> getTilesInRadius(HexCoordinate center, int radius) {
        List<Tile> result = new ArrayList<>();

        for (Tile tile : tiles.values()) {
            if (tile.getPosition().distanceTo(center) <= radius) {
                result.add(tile);
            }
        }

        return result;
    }



    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Collection<Tile> getAllTiles() {
        return new ArrayList<>(tiles.values());
    }

    public int getTileCount() {
        return tiles.size();
    }
}
