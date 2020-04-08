package it.polimi.ingsw.model.strategy.buildstrategy;

import it.polimi.ingsw.messages.GameMessage;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.piece.*;
import it.polimi.ingsw.messages.PlayerBuildChoice;

/**
 * This class represents the basic build strategy
 */
public class BasicBuild implements BuildStrategy {

    boolean alreadyBuilt;


    public BasicBuild(){
        this.alreadyBuilt = false;
    }

    /**
     * Checks if player can build
     *
     * @param gameboard
     * @param message message PlayerBuildChoice
     * @return
     */
   @Override
   public String checkBuild(GameBoard gameboard, PlayerBuildChoice message){

       Worker worker = message.getPlayer().getWorker(message.getChosenWorker());
       int x = message.getBuildingInto()[0];
       int y = message.getBuildingInto()[1];
       int z = gameboard.getTowerCell(x, y).getTowerHeight();

       //Player has not moved yet
       if(!message.getPlayer().getGodCard().getMoveStrategy().getAlreadyMoved()){
           return GameMessage.hasNotMoved;
       }

       //alreadyBuilt must be false
       if(alreadyBuilt){
           return GameMessage.alreadyBuilt;
       }

       //x e y must be inside the board
       if (x < 0 || x > 4 || y < 0 || y > 4) {
          return GameMessage.notInGameboard;
       }

       //workerPosition must be adjacent to buildingPosition
       if (!worker.getCurrentPosition().adjacent(x,y)){
           return GameMessage.notInSurroundings;
       }

       //tower must not be completed
       if(gameboard.getTowerCell(x,y).isTowerCompleted()) {
          return GameMessage.noBuildToCompleteTower;
       }

       //the chosen piece must not be a "Block" when tower's height is 3
       if(gameboard.getTowerCell(x,y).getTowerHeight()==3 &&  message.getPieceType().equals("Block")){
           return GameMessage.noBlocksInDome;
       }

       //the chosen piece must not be a "Dome" when tower's height is <3
       if(gameboard.getTowerCell(x,y).getTowerHeight()<3 &&  message.getPieceType().equals("Dome")) {
           return GameMessage.noDomesInBlock;
       }

       //control whether there are pieces left
       if (gameboard.getTowerCell(x,y).getTowerHeight()==0 && !Level1Block.areTherePiecesLeft()){
          return GameMessage.noLevel1Left;
       }

       if (gameboard.getTowerCell(x,y).getTowerHeight()==1 && !Level2Block.areTherePiecesLeft()){
           return GameMessage.noLevel2Left;
       }

       if (gameboard.getTowerCell(x,y).getTowerHeight()==2 && !Level3Block.areTherePiecesLeft()){
           return GameMessage.noLevel3Left;
       }

       if (gameboard.getTowerCell(x,y).getTowerHeight()==3 && !Dome.areTherePiecesLeft()){
           return GameMessage.noDomesLeft;
       }

       //there must not be a worker in the building position
       if (gameboard.getTowerCell(x, y).hasWorkerOnTop()){
           return GameMessage.noBuildToOccupiedTower;
       }

       return GameMessage.buildOK;
   }


    /**
     * changes tower's height and checks if tower is complete
     *
     * @param gameboard
     * @param message message PlayerBuildChoice
     */
    @Override
    public void build(GameBoard gameboard, PlayerBuildChoice message) {


        int x = message.getBuildingInto()[0];
        int y = message.getBuildingInto()[1];
        int z = gameboard.getTowerCell(x, y).getTowerHeight();
        Piece piece = null;

       //create the right Piece and decrease the number

       if (gameboard.getTowerCell(x,y).getTowerHeight()==0){
           piece = new Level1Block();
           
       }
       else if (gameboard.getTowerCell(x,y).getTowerHeight()==1){
           piece = new Level2Block();
       }
       else if (gameboard.getTowerCell(x,y).getTowerHeight()==2){
           piece = new Level3Block();
       }
       else if (gameboard.getTowerCell(x,y).getTowerHeight()==3){
           piece = new Dome();
       }

       //set the Piece
        gameboard.getTowerCell(x,y).getFirstNotPieceLevel().setPiece(piece);

       //increase tower's height
        gameboard.getTowerCell(x, y).increaseTowerHeight();

       //check if tower is complete
        gameboard.getTowerCell(x,y).checkCompletion();

        this.alreadyBuilt = true;
        //TODO notify()-> spedire messaggio con copia delle informazioni utili dello stato della board

    }
}
