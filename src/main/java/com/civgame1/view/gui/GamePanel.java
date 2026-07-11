package com.civgame1.view.gui;

import com.civgame1.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private final GameWindow window;
    private final GameController controller;
    
    private final MapPanel mapPanel;
    private final HUDPanel hudPanel;

    public GamePanel(GameWindow window, GameController controller) {
        this.window = window;
        this.controller = controller;
        
        setLayout(new BorderLayout());

        this.mapPanel = new MapPanel(controller, this);
        this.hudPanel = new HUDPanel(controller, this);
        
        add(hudPanel, BorderLayout.NORTH);
        add(mapPanel, BorderLayout.CENTER);
    }
    
    public void updateGUI() {
        hudPanel.refresh();
        mapPanel.repaint();
    }
    
    public void backToMenu() {
        window.showMenu();
    }
}
