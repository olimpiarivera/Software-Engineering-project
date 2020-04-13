package it.polimi.ingsw.model.strategy.losestrategy;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnInfo;

public class BasicLose implements LoseStrategy {

    //chosenWorker is not the base Lose
    @Override
    public boolean movementLoss(TurnInfo turnInfo, GameBoard gameBoard, Player player, int chosenWorker) {

        //lose condition checked only before first movement
        if(turnInfo.getHasAlreadyMoved()){
            return false;
        }

        int possibility = 16;
        int x;
        int y;
        int z;

        for(int w=0;w<2;w++){
            x = player.getWorker(w).getCurrentPosition().getX();
            y = player.getWorker(w).getCurrentPosition().getY();
            z = player.getWorker(w).getCurrentPosition().getZ();

            for(int j=y-1;j<=y+1;j++){
                for(int i=x-1;i<=x+1;i++){

                    if(!(j==y && i==x)) {
                        if (j < 0 || j > 4 || i < 0 || i > 4 ||
                                gameBoard.getTowerCell(i,j).isTowerCompleted() ||
                                gameBoard.getTowerCell(i,j).hasWorkerOnTop() ||
                                gameBoard.getTowerCell(i,j).getTowerHeight() > z + 1 ||
                                (turnInfo.getAthenaPowerActive() && gameBoard.getTowerCell(i,j).getTowerHeight() > z)) {

                            possibility--;
                        }
                    }
                }
            }
        }

        return possibility==0;
    }

    @Override
    public boolean buildingLoss(TurnInfo turnInfo, GameBoard gameBoard, Player player, int chosenWorker) {

        //lose condition checked only after a movement, and only on first build
        if(!turnInfo.getHasAlreadyMoved() || turnInfo.getHasAlreadyBuilt()){
            return false;
        }

        int possibility = 16;
        int x;
        int y;

        //only the worker that moved must be checked
        x = player.getWorker(chosenWorker).getCurrentPosition().getX();
        y = player.getWorker(chosenWorker).getCurrentPosition().getY();

        for(int j=y-1;j<=y+1;j++){
            for(int i=x-1; i<=x+1;i++){

                if(!(j==y && i==x)){

                    if( j<0 || j>4 || i<0 || i>4 ||
                        gameBoard.getTowerCell(i,j).isTowerCompleted() ||
                        gameBoard.getTowerCell(i,j).hasWorkerOnTop()){

                        possibility--;
                    }
                }
            }
        }
        return possibility == 0;
    }
}
