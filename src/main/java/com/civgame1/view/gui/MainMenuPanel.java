package com.civgame1.view.gui;

import com.civgame1.controller.GameController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

public class MainMenuPanel extends JPanel {

    private final GameWindow window;

    public MainMenuPanel(GameWindow window) {
        this.window = window;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 25)); // Deep dark charcoal

        // Title
        JLabel titleLabel = new JLabel("CIVGAME1", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 72));
        titleLabel.setForeground(new Color(220, 220, 230));
        titleLabel.setBorder(new EmptyBorder(100, 0, 50, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        JButton startBtn = createStyledButton("START GAME");
        JButton settingsBtn = createStyledButton("SETTINGS");
        JButton exitBtn = createStyledButton("EXIT");

        startBtn.addActionListener(e -> {
            // Hardcode map size and players for Phase 3 initially
            GameController controller = new GameController(20, 15, Arrays.asList("Rome", "Egypt"));
            window.startGame(controller);
        });

        settingsBtn.addActionListener(e -> showSettingsDialog());

        exitBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    window, 
                    "Are you sure you want to exit?", 
                    "Exit Game", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        buttonPanel.add(startBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(settingsBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(exitBtn);

        // Center the buttons
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(buttonPanel);

        add(centerWrapper, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(new Color(40, 40, 50));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(60, 60, 75));
                } else {
                    g2.setColor(new Color(30, 30, 40));
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Draw Border
                g2.setColor(new Color(80, 80, 100));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        btn.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(300, 60));
        btn.setMaximumSize(new Dimension(300, 60));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }

    private void showSettingsDialog() {
        JDialog dialog = new JDialog(window, "Settings", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(window);
        dialog.getContentPane().setBackground(new Color(30, 30, 40));
        dialog.setLayout(new GridBagLayout());

        JLabel volLabel = new JLabel("Music Volume:");
        volLabel.setForeground(Color.WHITE);
        volLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JSlider volSlider = new JSlider(0, 100, 50);
        volSlider.setOpaque(false);
        volSlider.setForeground(Color.WHITE);

        JButton closeBtn = createStyledButton("Close");
        closeBtn.setPreferredSize(new Dimension(150, 40));
        closeBtn.addActionListener(e -> dialog.dispose());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(volLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        dialog.add(volSlider, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        dialog.add(closeBtn, gbc);

        dialog.setVisible(true);
    }
}
