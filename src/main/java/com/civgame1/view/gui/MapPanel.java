package com.civgame1.view.gui;

import com.civgame1.controller.GameController;
import com.civgame1.model.core.GameMap;
import com.civgame1.model.core.HexCoordinate;
import com.civgame1.model.core.Tile;
import com.civgame1.model.terrain.TerrainType;
import com.civgame1.model.unit.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class MapPanel extends JPanel {

    private final GameController controller;
    private final GamePanel gamePanel;

    private double zoom = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private Point lastMouse;

    private final int HEX_SIZE = 40;
    private Unit selectedUnit = null;

    public MapPanel(GameController controller, GamePanel gamePanel) {
        this.controller = controller;
        this.gamePanel = gamePanel;
        setBackground(new Color(10, 15, 20)); // Ocean/Void color
        
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouse = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                double dx = e.getX() - lastMouse.x;
                double dy = e.getY() - lastMouse.y;
                offsetX += dx / zoom;
                offsetY += dy / zoom;
                lastMouse = e.getPoint();
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    zoom *= 1.1; // Zoom in
                } else {
                    zoom /= 1.1; // Zoom out
                }
                repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
        
        // Initial center
        offsetX = 400;
        offsetY = 300;
    }

    private void handleMouseClick(MouseEvent e) {
        try {
            AffineTransform transform = getTransform();
            Point2D.Double screenPt = new Point2D.Double(e.getX(), e.getY());
            Point2D.Double worldPt = new Point2D.Double();
            transform.inverseTransform(screenPt, worldPt);

            HexCoordinate hex = pixelToHex(worldPt.x, worldPt.y);
            Tile tile = controller.getGameState().getMap().getTile(hex);
            
            if (tile == null || !tile.isExplored()) {
                selectedUnit = null; // click on void/fog cancels selection
                repaint();
                return;
            }

            if (selectedUnit != null) {
                // Try to move
                boolean moved = controller.moveUnit(selectedUnit, hex);
                selectedUnit = null; // Deselect after moving or failing
                gamePanel.updateGUI();
            } else {
                // Try to select
                if (tile.hasUnits()) {
                    // Just select the first unit for now
                    Unit u = tile.getUnits().get(0);
                    // Only select if it belongs to current player
                    if (u.getOwnerCivName().equals(controller.getCurrentPlayer().getName())) {
                        selectedUnit = u;
                    }
                }
                repaint();
            }
            
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
    }

    private AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(getWidth() / 2.0, getHeight() / 2.0);
        tx.scale(zoom, zoom);
        tx.translate(-getWidth() / 2.0 + offsetX, -getHeight() / 2.0 + offsetY);
        return tx;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform tx = getTransform();
        g2.transform(tx);

        GameMap map = controller.getGameState().getMap();
        for (Tile tile : map.getAllTiles()) {
            drawHex(g2, tile);
        }
    }

    private void drawHex(Graphics2D g2, Tile tile) {
        double w = Math.sqrt(3) * HEX_SIZE;
        double h = 2 * HEX_SIZE;
        double x = w * (tile.getPosition().q + tile.getPosition().r / 2.0);
        double y = h * 3.0 / 4.0 * tile.getPosition().r;

        Path2D poly = createHexPolygon(x, y);

        if (!tile.isExplored()) {
            // Unexplored
            g2.setColor(new Color(20, 20, 20));
            g2.fill(poly);
            g2.setColor(new Color(40, 40, 40));
            g2.draw(poly);
            return;
        }

        // Fill Terrain
        g2.setColor(getTerrainColor(tile.getTerrainType()));
        g2.fill(poly);

        // Grid Outline
        g2.setColor(new Color(0, 0, 0, 80));
        g2.draw(poly);

        // Draw Resource if any
        if (tile.getResource() != null) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString(tile.getResource().name().substring(0,2), (int)x - 8, (int)y - 10);
        }

        // Draw City/TownHall
        if (tile.hasCity()) {
            g2.setColor(new Color(255, 200, 0));
            g2.fillRect((int)x - 10, (int)y - 5, 20, 15);
            g2.setColor(Color.BLACK);
            g2.drawRect((int)x - 10, (int)y - 5, 20, 15);
        }

        // Draw Units
        if (tile.hasUnits()) {
            Unit u = tile.getUnits().get(0);
            boolean isSelected = (u == selectedUnit);
            
            g2.setColor(u.getOwnerCivName().equals(controller.getCurrentPlayer().getName()) ? new Color(0, 200, 255) : Color.RED);
            g2.fillOval((int)x - 8, (int)y + 5, 16, 16);
            
            if (isSelected) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval((int)x - 10, (int)y + 3, 20, 20);
                g2.setStroke(new BasicStroke(1));
            }
            
            // Draw Unit Type letter
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.drawString(u.getType().name().substring(0,1), (int)x - 4, (int)y + 17);
        }

        // Vision overlay (if explored but not visible)
        if (!tile.isVisible()) {
            g2.setColor(new Color(0, 0, 0, 120));
            g2.fill(poly);
        }
    }

    private Path2D createHexPolygon(double cx, double cy) {
        Path2D poly = new Path2D.Double();
        for (int i = 0; i < 6; i++) {
            double angle_deg = 60 * i - 30;
            double angle_rad = Math.PI / 180 * angle_deg;
            double px = cx + HEX_SIZE * Math.cos(angle_rad);
            double py = cy + HEX_SIZE * Math.sin(angle_rad);
            if (i == 0) poly.moveTo(px, py);
            else poly.lineTo(px, py);
        }
        poly.closePath();
        return poly;
    }

    private Color getTerrainColor(TerrainType type) {
        return switch (type) {
            case GRASSLAND -> new Color(100, 180, 80);
            case PLAINS    -> new Color(180, 170, 80);
            case FOREST    -> new Color(34, 100, 34);
            case MOUNTAIN  -> new Color(100, 100, 100);
            default        -> Color.MAGENTA;
        };
    }

    private HexCoordinate pixelToHex(double x, double y) {
        double q = (Math.sqrt(3)/3 * x - 1.0/3 * y) / HEX_SIZE;
        double r = (2.0/3 * y) / HEX_SIZE;
        return axialRound(q, r);
    }

    private HexCoordinate axialRound(double q, double r) {
        double s = -q - r;
        int rq = (int) Math.round(q);
        int rr = (int) Math.round(r);
        int rs = (int) Math.round(s);

        double q_diff = Math.abs(rq - q);
        double r_diff = Math.abs(rr - r);
        double s_diff = Math.abs(rs - s);

        if (q_diff > r_diff && q_diff > s_diff) {
            rq = -rr - rs;
        } else if (r_diff > s_diff) {
            rr = -rq - rs;
        }
        return new HexCoordinate(rq, rr);
    }
}
