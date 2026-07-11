package com.civgame1.controller;

import com.civgame1.model.core.GameState;
import com.civgame1.model.civilization.Civilization;


public class TurnManager {

    private final GameState gameState;

    public TurnManager(GameState gameState) {
        this.gameState = gameState;
    }


    public void endCurrentTurn() {
        gameState.endTurn();

        Civilization nextCiv = gameState.getCurrentCivilization();
        System.out.println("Turn " + gameState.getCurrentTurn() + " - " + nextCiv.getName() + "'s turn");


        if (nextCiv.isAI()) {
            processAITurn(nextCiv);
        }
    }


    private void processAITurn(Civilization aiCiv) {

        System.out.println(aiCiv.getName() + " (AI) is thinking...");
        endCurrentTurn();
    }


    public void startGame() {
        System.out.println("Game started!");
        Civilization firstCiv = gameState.getCurrentCivilization();
        System.out.println("Turn 1 - " + firstCiv.getName() + "'s turn");
    }
}
