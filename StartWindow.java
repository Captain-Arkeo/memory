/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memory;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import memory.memory.MemoryGame;

/**
 *
 * @author Captain Arkeo
 */
public class StartWindow extends Application {
    //StartWindow est la fenêtre qui permet à l'utilisateur de saisir toutes les données importantes à la partie
    
    //Par défaut, le nombre de joueurs est à 2, le minimum
    private int nbPlayers = 2;
    
    //Par défaut, on a 6 cartes (3 pairs)
    private int[] gridSize = {3, 2};
    
    //Par défaut, on a "Player 1" et "Player 2" comme nom et il n'y a que deux joueurs
    private String[] playerNames = {"Player 1", "Player 2"};
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Memory Game Setup");

        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        
        //Cette VBox contient tous les champs de texte permettant de donner les noms de joueurs
        VBox playerNameBox = new VBox(10);
        this.updatePlayerNameFields(playerNameBox);

        Label playerLabel = new Label("How many players?");

        //Cette HBox contiendra tous les boutons de sélection du nombre de joueurs
        HBox playerCountBox = new HBox(10);
        playerCountBox.setAlignment(Pos.CENTER);

        //Combien de joueurs (on commence à deux car on a décidé qu'un joueur ne pouvait pas jouer seul)
        for(int i = 2; i < 5; i++){
            //nbToChoose est ici car le setOnAction en bas ne peut pas prendre de valeur "non finale"
            int nbToChoose = i;
            Button newBtn = new Button("" + i);
            newBtn.setOnAction(event -> {
                this.nbPlayers = nbToChoose;
                this.updatePlayerNameFields(playerNameBox);
            });
            playerCountBox.getChildren().add(newBtn);
        }
        
        
        Label gridSizeLabel = new Label("How many cards ?");
        
        //Cette HBox contient tous les boutons de sélection de la taille de la grille
        HBox gridSizeBox = new HBox(10);
        gridSizeBox.setAlignment(Pos.CENTER);
        for(int i = 3; i < 7; i++){
            //nbToChoose est ici car le setOnAction en bas ne peut pas prendre de valeur "non finale"
            int nbToChoose = i;
            Button newBtn = new Button("" + nbToChoose * (nbToChoose - 1));
            newBtn.setOnAction(event -> {
                this.gridSize[0] = nbToChoose;
                this.gridSize[1] = nbToChoose - 1;
            });
            gridSizeBox.getChildren().add(newBtn);
        }
        
        //Ce bouton permet de lancer la partie avec les valeurs choisies
        Button startGame = new Button("Start Game");
        startGame.setOnAction(event -> {
            //On crée une nouvelle fen^tre et ensuite, on passe les paramètres au jeu avant de le lancer
            Stage gameStage = new Stage();
            MemoryGame game = new MemoryGame(this.playerNames, this.gridSize, this.nbPlayers);
            game.start(gameStage);
            
            //On ferme la fenêtre de choix
            primaryStage.close();
        });
        
        //On ajoute tous les éléments au layout
        mainLayout.getChildren().addAll(playerLabel, playerCountBox, playerNameBox, gridSizeLabel, gridSizeBox, startGame);

        Scene scene = new Scene(mainLayout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    //Cette méthode recrée la VBox contenant les champs de texte afin de prendre en compte le nouveau nombre de joueur
    public void updatePlayerNameFields(VBox nameInputBox){
        nameInputBox.getChildren().clear();
        String[] newNames = new String[this.nbPlayers];
        for(int i = 0; i < this.nbPlayers; i++){
            String name = "Player " + (i + 1);
            //Si on avait déjà entré un nom, on le réutilise
            if(this.playerNames.length > i){
                name = this.playerNames[i];
            }
            newNames[i] = name;
            TextField field = new TextField(name);
            //On sauvegarde le i ici pour l'event listener plus bas
            int nameToChange = i;
            //Si le joueur entre un nouveau nom, il est directement sauvegardé
            //textProperty correspond au contenu du TextField
            field.textProperty().addListener((obs, oldText, newText) -> {
                //Le nom sauvegardé est changé à chaque fois que le joueur change le contenu du field
                newNames[nameToChange] = newText;
            });
            nameInputBox.getChildren().add(field);
        }
        //Puisque le nombre de joueurs à changé, les noms de joueurs sauvegardés doivent prendre ceci en compte
        this.playerNames = newNames;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
