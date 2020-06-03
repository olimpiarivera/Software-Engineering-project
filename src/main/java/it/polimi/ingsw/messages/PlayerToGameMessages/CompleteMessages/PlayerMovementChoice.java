package it.polimi.ingsw.messages.PlayerToGameMessages.CompleteMessages;

import it.polimi.ingsw.messages.PlayerToGameMessages.DataMessages.MoveData;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.VirtualView;

/**
 * PlayerMovementChoice message contains information about a specific move (which Player, which worker, which Towercell).
 */
public class PlayerMovementChoice extends PlayerMessage {

    private final MoveData moveData;

    //chosenWorker must be 0 or 1
    public PlayerMovementChoice(VirtualView virtualView, Player player, MoveData moveData) {
        super(virtualView,player);
        this.moveData=moveData;
    }

    public int getChosenWorker() { return moveData.getChosenWorker();}

    public int[] getMovingTo() {
        return moveData.getMovingTo();
    }
}
