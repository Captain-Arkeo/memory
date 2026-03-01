package memory.memory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import memory.memory.CardGrid;
import memory.memory.BottomButtons;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import memory.StartWindow;
import memory.glues.HorizontalGlue;

/**
 *
 * @author Captain Arkeo
 */
public class MemoryGame extends Application {
    //Le nombre de joueurs
    private int nbPlayers;
    
    //Les noms de tous les joueurs
    private String[] players;
    
    //La taille de la grille des cartes
    private final int[] gridSize;
    
    //Les cartes contenant les noms et scores des joueurs
    private PlayerCard[] playerCards;
    
    //Le bouton permettant de rejouer au jeu
    private final Button restartButton = new Button("Game Over - Click here to restart");
    
    //Position du joueur en train de jouer dans le tableau playerCards
    private int currentPlayer = 0;
    
    //Position du joueur avec le plus petit score dans le tableau playerCards
    //-1 si plusieurs joueurs ont le même plus petit score
    private int lowestScorePlayer = -1;
    
    //Permet de savoir si le joueur est en train de mélanger les cartes (ce qu'il peut faire s'il a le plus petit score)
    private boolean isSwapping = false;
    
    //Ce booléen permet de savoir si le joueur est autorisé à intéragir avec la fenêtre
    private boolean isInputAllowed = true;
    
    //Ce booléen permet de savoir si le joueur a déjà retourné une carte et, donc, ne peut pas passer son tour
    private boolean isForcedToPlay = false;
    
    //Nombres de paires trouvées. La partie est finie si totalPairsFound = gridSize[0] * gridSize[1] / 2
    private int totalPairsFound = 0;
    
    /*//La dernière sauvegarde
    //Pas utilisé
    private SavedState save;*/
    
    
    public MemoryGame(String[] playerNames, int[] newGridSize, int newNbPlayers){
        super();
        this.players = playerNames;
        this.gridSize = newGridSize;
        this.nbPlayers = newNbPlayers;
        this.playerCards = new PlayerCard[nbPlayers];
    }
    
    // start est appelée automatiquement pour lancer l'application, 
    // elle a en parametre la fenetre principale. 
    @Override
    public void start(Stage primaryStage) {
        BorderPane mainGame = new BorderPane();
        
        //On crée les joueurs et on les mets dans une boîte verticale
        VBox playerBox = new VBox(20);
        for(int i = 0; i < this.nbPlayers; i++){
            PlayerCard player = new PlayerCard(this.players[i]);
            if(i == 0){
                player.select();
            }
            this.playerCards[i] = player;
            playerBox.getChildren().add(player);
        }
        
        //La grille des cartes
        CardGrid memoryGrid = new CardGrid(this.gridSize[0], this.gridSize[1], this);
        //Les boutons apparaissant en bas
        BottomButtons bottomButtonsBox = new BottomButtons(this);
        
        //Bouton permettant de rejouer
        this.restartButton.setVisible(false);
        this.restartButton.setOnAction(event -> {
            //On relance la fenêtre permettant de choisir des paramètres et on la fenêtre courante
            Stage newStage = new Stage();
            StartWindow newStartWindow = new StartWindow();
            newStartWindow.start(newStage);
            primaryStage.close();
        });
        
        mainGame.setCenter(memoryGrid);
        mainGame.setRight(playerBox);
        mainGame.setBottom(bottomButtonsBox);
        mainGame.setTop(this.restartButton);
        
        Scene scene = new Scene(mainGame, 800, 800);
        
        primaryStage.setTitle("Jeu de Memory");
        primaryStage.setScene(scene);
        // On ouvre la fenetre. 
        primaryStage.show();
    }
    
    //Permet de savoir si le joueur a le droit d'intéragir avec la fenêtre
    public boolean isPlayerAllowedInputs(){
        return this.isInputAllowed;
    }
    
    //Permet de savoir si le joueur a déjà retourné une carte et, donc, ne peut pas passer son tour
    public boolean isPlayerForcedToPlay(){
        return this.isForcedToPlay;
    }
    
    //Cette méthode permet de changer l'autorisation du joueur sur l'intéraction de la fenêtre
    public void disallowOrAllowInputs(){
        this.isInputAllowed = !this.isInputAllowed;
    }
    
    //Cette méthode permet de libérer le joueur ou de le forcer à continuer son tour
    public void forceOrFreePlayerFromPlaying(){
        this.isForcedToPlay = !this.isForcedToPlay;
    }
    
    //Permet de savoir si le joueur est en train de mélanger des cartes
    public boolean isPlayerSwapping(){
        return this.isSwapping;
    }
    
    //Cette méthode permet de changer ce que le joueur est en train de faire : jouer normalement ou mélanger deux cartes
    public void toggleSwapping(){
        this.isSwapping = !this.isSwapping;
    }
    
    //Cette méthode permet de passer au tour du joueur suivant
    public void nextPlayerTurn(){
        //On mets le background du joueur actuel en blanc
        PlayerCard currentPlayerCard = this.getCurrentPlayerCard();
        currentPlayerCard.unselect();
                
        //Si le joueur est le dernier, on revient au premier. Sinon, au passe au suivant.
        if(this.currentPlayer == this.playerCards.length - 1){
            this.currentPlayer = 0;
        } else {
            this.currentPlayer++;
        }
        //On mets le background du nouveau joueur en jaune
        currentPlayerCard = this.getCurrentPlayerCard();
        currentPlayerCard.select();
    }
    
    //Cette méthode récupère la carte du joueur 
    public PlayerCard getCurrentPlayerCard(){
        return this.playerCards[this.currentPlayer];
    }
    
    //Cette méthode permet de savoir si le joueur courant est le joueur avec le plus petit score
    public boolean isCurrentPlayerLast(){
        return this.lowestScorePlayer == this.currentPlayer;
    }
    
    //Cette méthode calcule le nouveau joueur en dernière position
    //Si plusieurs joueurs sont en dernière position, aucun n'est considéré dernier
    public void newLastPlayer(){
        int minScore = Integer.MAX_VALUE;
        int minIndex = -1;
        //isTie vérifie qu'il n'y ait pas deux joueurs étant en dernière position
        boolean isTie = false;

        for (int i = 0; i < this.nbPlayers; i++) {
            int score = this.playerCards[i].getScore();
            if (score < minScore) {
                minScore = score;
                minIndex = i;
                isTie = false;
            } else if (score == minScore) {
                isTie = true;
            }
        }

        if(isTie){
            this.lowestScorePlayer = -1;
        } else {
            this.lowestScorePlayer = minIndex;
        }
    }
    
    //Cette méthode incrémente le nombre de paires trouvées et renvoie un booléen indiquant si la partie est finie.
    public boolean incrementPairsFound(){
        this.totalPairsFound++;
        return (this.totalPairsFound == (this.gridSize[0] * this.gridSize[1] / 2));
    }
    
    //Si la partie est finie, on rajoute un bouton en haut pour rejouer
    public void gameOver(boolean gameOver){
        if(gameOver){
            this.restartButton.setVisible(true);
        }
    }
    
    //Autres getters
    public int getNbPlayers() {
        return this.nbPlayers;
    }

    public String[] getPlayers() {
        return this.players;
    }

    public int[] getGridSize() {
        return this.gridSize;
    }

    public PlayerCard[] getPlayerCards() {
        return this.playerCards;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int getLowestScorePlayer() {
        return this.lowestScorePlayer;
    }

    public int getTotalPairsFound() {
        return this.totalPairsFound;
    }
}

