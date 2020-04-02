package it.polimi.ingsw.model.strategy.movestrategy;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Worker;

public interface MoveStrategy {

    void move(GameBoard gameboard, Worker worker, int x, int y);
}
