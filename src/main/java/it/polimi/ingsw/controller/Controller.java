package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.observe.Observer;

public class Controller implements Observer<Message>{

    private final Model model;

    /**
     * Costruttore della classe Controller
     * @param model
     */
    public Controller(Model model){
        super();
        this.model = model;
    }

    /**
     * Checks if it's the player's turn and calls the player's GodCard's MoveStrategy methods;
     *
     * @param message oggetto-messaggio contentente le informazioni riguardanti lo spostamento
     */
    private synchronized void performMove(PlayerMovementChoice message) {

        String checkResult;

        //TODO implementare reportError
        if(!model.isPlayerTurn(message.getPlayer())){
            //message.getView().reportError(gameMessage.wrongTurn);
            return;
        }

        if(model.getTurnInfo().getTurnHasEnded()){
            //message.getView().reportError(gameMessage.turnAlreadyEnded);
            return;
        }

        checkResult=message.getPlayer().getGodCard().getMoveStrategy().checkMove(model.getTurnInfo(), model.getGameBoard(), message.getPlayer(), message.getChosenWorker(), message.getMovingTo());

        if(checkResult.equals(GameMessage.moveOK)){
            //execute move
            message.getPlayer().getGodCard().getMoveStrategy().move(model.getTurnInfo(), model.getGameBoard(), message.getPlayer(), message.getChosenWorker(), message.getMovingTo());

            //execute win check
            if(message.getPlayer().getGodCard().getWinStrategy().checkWin(message.getPlayer(),message.getChosenWorker())){
                //fare qualcosa
            }

        }else{
            //TODO impementare reportError
            //message.getView().reportError(checkResult);
        }
    }

    /**
     *
     * Checks if it's the player's turn and calls the player's GodCard's BuildStrategy
     *
     * @param message messaggio di tipo PlayerBuildChoice
     */
    private synchronized void performBuild(PlayerBuildChoice message){

        String checkResult;

        //TODO implementare reporError
        if(!model.isPlayerTurn(message.getPlayer())){
            //message.getView().reportError(gameMessage.wrongTurn);
            return;
        }

        if(model.getTurnInfo().getTurnHasEnded()){
            //message.getView().reportError(gameMessage.turnAlreadyEnded);
            return;
        }

        checkResult=message.getPlayer().getGodCard().getBuildStrategy().checkBuild(model. getTurnInfo(), model.getGameBoard(), message.getPlayer(),message.getBuildingInto(), message.getPieceType());

        if(checkResult.equals(GameMessage.buildOK) ) {
            message.getPlayer().getGodCard().getBuildStrategy().build(model.getTurnInfo(), model.getGameBoard(), message.getPlayer(), message.getBuildingInto(), message.getPieceType());
        }
        else{
            //TODO impementare reportError
            //message.getView().reportError(checkResult);
        }
    }

    /**
     * Checks if it's player's turn, checks this player win conditions and next player lose conditions. Updates turn
     * @param message PlayerEndOfTurnChoice message
     */
    private synchronized void endTurn(PlayerEndOfTurnChoice message){
        //TODO implementare reporError
        if(!model.isPlayerTurn(message.getPlayer())){
            //message.getView().reportError(gameMessage.wrongTurn);
            return;
        }

        if(!model.getTurnInfo().getTurnCanEnd()){
            //message.getView().reportError(gameMessage.turnNotEnded);
        }

        //TODO stabilire cosa va qui
        model.updateTurn();

        // TODO check sconfitta giocatore successivo con eventule rimozione dal gioco

    }

    /**
     * Invokes Controller's methods on the basis of message's subclass
     *
     * @param message Message
     */
    @Override
    public void update(Message message) {

        if(message instanceof PlayerMovementChoice){
            performMove((PlayerMovementChoice)message);
        }

        if (message instanceof PlayerBuildChoice){
            performBuild((PlayerBuildChoice)message);
        }

        if(message instanceof PlayerEndOfTurnChoice){
            endTurn((PlayerEndOfTurnChoice)message);
        }

    }


}
