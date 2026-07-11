package com.civgame1.view.gui;

import com.civgame1.controller.GameController;
import com.civgame1.model.civilization.Civilization;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HUDPanel extends JPanel {

    private final GameController controller;
    private final GamePanel gamePanel;
    
    private final JLabel civLabel;
    private final JLabel turnLabel;
    private final JLabel resourcesLabel;
    private final JButton endTurnBtn;

    public HUDPanel(GameController controller, GamePanel gamePanel) {
        this.controller = controller;
        this.gamePanel = gamePanel;
        
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 35));
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(50, 50, 60)));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        leftPanel.setOpaque(false);
        
        civLabel = new JLabel();
        civLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        civLabel.setForeground(new Color(0, 200, 255));
        
        turnLabel = new JLabel();
        turnLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        turnLabel.setForeground(Color.LIGHT_GRAY);
        
        leftPanel.add(civLabel);
        leftPanel.add(turnLabel);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        centerPanel.setOpaque(false);
        
        resourcesLabel = new JLabel();
        resourcesLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        resourcesLabel.setForeground(new Color(220, 220, 220));
        centerPanel.add(resourcesLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        rightPanel.setOpaque(false);
        
        endTurnBtn = createStyledButton("End Turn");
        endTurnBtn.addActionListener(e -> {
            controller.endTurn();
            gamePanel.updateGUI();
        });
        
        JButton menuBtn = createStyledButton("Menu");
        menuBtn.addActionListener(e -> gamePanel.backToMenu());
        
        rightPanel.add(endTurnBtn);
        rightPanel.add(menuBtn);

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        
        refresh();
    }
    
    public void refresh() {
        Civilization civ = controller.getCurrentPlayer();
        if (civ == null) return;
        
        civLabel.setText(civ.getName());
        turnLabel.setText("Turn: " + controller.getCurrentTurn());
        
        String resText = String.format("Food: %d | Wood: %d | Stone: %d | Iron: %d | Units: %d/%d",
                civ.getFood(), civ.getWood(), civ.getStone(), civ.getIron(),
                civ.getUnits().size(), civ.getUnitCap());
                
        if (civ.isStarving()) {
            resText += "  [STARVING!]";
            resourcesLabel.setForeground(new Color(255, 100, 100));
        } else {
            resourcesLabel.setForeground(new Color(220, 220, 220));
        }
        
        resourcesLabel.setText(resText);
    }
    
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(new Color(50, 50, 60));
                else if (getModel().isRollover()) g2.setColor(new Color(70, 70, 80));
                else g2.setColor(new Color(40, 40, 50));
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.GRAY);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 30));
        return btn;
    }
}
