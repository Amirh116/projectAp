package com.civgame1.view.gui;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainContainer;
    
    private final MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;

    public GameWindow() {
        super("CIVGAME1 - Phase 3");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        

        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainMenuPanel = new MainMenuPanel(this);
        
        mainContainer.add(mainMenuPanel, "MENU");

        add(mainContainer);
        
        showMenu();
    }

    public void showMenu() {
        cardLayout.show(mainContainer, "MENU");
    }

    public void startGame(com.civgame1.controller.GameController controller) {
        if (gamePanel != null) {
            mainContainer.remove(gamePanel);
        }
        gamePanel = new GamePanel(this, controller);
        mainContainer.add(gamePanel, "GAME");
        cardLayout.show(mainContainer, "GAME");
        

        gamePanel.requestFocusInWindow();
    }
}
