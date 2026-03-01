/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory.memory;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 *
 * @author Captain Arkeo
 */
//Cette classe crée le menu demandé pour l'étape 2
//Cette class n'est pas complète
public class MenuButtons extends HBox{
    
    
    public MenuButtons(MemoryGame game){
        super();
        
        Button abandonButton = new Button("Abandon Game");
        abandonButton.setOnAction(event -> {
            if(game.isPlayerAllowedInputs() && !game.isPlayerForcedToPlay()){
                
            }
        });
        
        Button saveGame = new Button("Save");
        saveGame.setOnAction(event -> {
            if(game.isPlayerAllowedInputs() && !game.isPlayerForcedToPlay()){
                
            }
        });
        
    }
}
