package it.polimi.ingsw.model.strategy.buildstrategy;

import it.polimi.ingsw.messages.PlayerBuildChoice;
import it.polimi.ingsw.messages.PlayerMovementChoice;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.piece.Piece;
import it.polimi.ingsw.messages.PlayerBuildChoice;

public interface BuildStrategy {

    //TODO Se vogliamo mettere attributo hasMoved, allora bisogna rimetterlo a false alla fine delle fasi di gioco dal controller.
    //TODO qesto vuol dire che getter e setter devono essere già presenti nell'interfaccia

    /**
     * @param gameboard
     * @param  message PlayerBuildChoice message
     */
    public void build(GameBoard gameboard, PlayerBuildChoice message);


    /**
     * @param gameboard
     * @param message PlayerBuildChoice message
     * @return
     */
    boolean checkBuild(GameBoard gameboard, PlayerBuildChoice message);
}
