package it.polimi.ingsw.model.strategy.buildstrategy;

import it.polimi.ingsw.messages.GameMessage;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.piece.*;

/**
 * This class represents the basic build strategy
 */
public class BasicBuild implements BuildStrategy {

   @Override
   public String checkBuild(TurnInfo turnInfo,  GameBoard gameboard, Player player,int chosenWorker, int[] buildingInto, String pieceType){

       int x = buildingInto[0];
       int y = buildingInto[1];

       //Player must have moved
       if(!turnInfo.getHasAlreadyMoved()){
           return GameMessage.hasNotMoved;
       }

       //Player has not already built
       if(turnInfo.getHasAlreadyBuilt()){
           return GameMessage.alreadyBuilt;
       }

       //x e y must be inside the board
       if (x < 0 || x > 4 || y < 0 || y > 4) {
           return GameMessage.notInGameboard;
       }
       int z = gameboard.getTowerCell(x, y).getTowerHeight();

       //chosenWorker must be a valid number
       if(chosenWorker!=0 && chosenWorker!=1){
           return GameMessage.invalidWorkerNumber;
       }
       Worker worker = player.getWorker(chosenWorker);

       //worker must be the same that has moved
       if(chosenWorker != turnInfo.getChosenWorker()){
           return GameMessage.notSameThatMoved;
       }

       //workerPosition must not be the destination position
       if (worker.getCurrentPosition().getX()==x && worker.getCurrentPosition().getY()==y){
           return GameMessage.notOwnPosition;
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
       if(z==3 &&  pieceType.equals("Block")){
           return GameMessage.noBlocksInDome;
       }


       //the chosen piece must not be a "Dome" when tower's height is <3
       if(z<3 &&  pieceType.equals("Dome")) {
           return GameMessage.noDomesInBlock;
       }

       //there must not be a worker in the building position
       if (gameboard.getTowerCell(x, y).hasWorkerOnTop()){
           return GameMessage.noBuildToOccupiedTower;
       }

       return GameMessage.buildOK;
   }

    @Override
    public String build(TurnInfo turnInfo, GameBoard gameboard, Player player,int chosenWorker, int[] buildingInto, String pieceType) {

        int x = buildingInto[0];
        int y = buildingInto[1];
        int z = gameboard.getTowerCell(x, y).getTowerHeight();
        Piece piece = null;

        //create the right Piece

       if (z==0){
           piece = new Level1Block();
       }
       else if (z==1){
           piece = new Level2Block();
       }
       else if (z==2){
           piece = new Level3Block();
       }
       else if (z==3){
           piece = new Dome();
       }

       //set the Piece
        gameboard.getTowerCell(x,y).getFirstNotPieceLevel().setPiece(piece);

       //increase tower's height
        gameboard.getTowerCell(x, y).increaseTowerHeight();

       //check if tower is complete
        gameboard.getTowerCell(x,y).checkCompletion();

        turnInfo.setHasBuilt();
        turnInfo.addBuild();
        turnInfo.setTurnCanEnd();
        turnInfo.setTurnHasEnded();
        //TODO notify()-> spedire messaggio con copia delle informazioni utili dello stato della board

        return GameMessage.turnCompleted;
    }

}
