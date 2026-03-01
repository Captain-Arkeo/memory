/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory.memory;

import javafx.scene.layout.VBox;

/**
 *
 * @author Captain Arkeo
 */
//Cette classe permet d'enregistrer l'état de la partie
//Cette class n'est pas complète
public class SavedState {
    
    private CardGrid savedGrid;
    private VBox savedPlayerBox;
    private int savedNbPlayers;
    private int savedCurrentPlayer;
    private int savedLowestScorePlayer;
    private int savedTotalPairsFound;

    // Constructeur permettant de sauvegarder l'état actuel du jeu
    public SavedState(MemoryGame game, CardGrid grid, VBox playerBox) {
        this.savedGrid = grid;
        this.savedPlayerBox = playerBox;
        this.savedNbPlayers = game.getNbPlayers();
        this.savedCurrentPlayer = game.getCurrentPlayer();
        this.savedLowestScorePlayer = game.getLowestScorePlayer();
        this.savedTotalPairsFound = game.getTotalPairsFound();
    }
}
