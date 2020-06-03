package it.polimi.ingsw.supportClasses;

import it.polimi.ingsw.messages.GameToPlayerMessages.Notify.LoseMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.Notify.NewBoardStateMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.Notify.NotifyMessages;
import it.polimi.ingsw.messages.GameToPlayerMessages.Notify.WinMessage;
import it.polimi.ingsw.messages.PlayerToGameMessages.DataMessages.DataMessage;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observe.Observer;
import it.polimi.ingsw.server.ServerSideConnection;
import it.polimi.ingsw.view.VirtualView;

/**
 * Integration tests need a VirtualView. This class is a "empty virtualview" for testing purposes
 *
 */
public class EmptyVirtualView extends VirtualView implements Observer<NotifyMessages> {

    //ATTRIBUTES

    private Player player;

    private ServerSideConnection connectionToClient;

    public EmptyVirtualView(Player player, ServerSideConnection c) {
        super(player, c);
    }

    //this class's update is triggered by ServerSideConnection reading a player messages and notifies the virtual view itself
    private class PlayerMessageReceiver implements Observer<DataMessage> {

        @Override
        public void update(DataMessage message) {
            System.out.println("VirtualView's MessageReceiver's update() triggered");
        }

    }

    public Player getPlayer(){
        return player;
    }


    public void reportToClient(Object message){
        System.out.println("VirtualView's reportToClient() triggered");
    }


    //TODO
    @Override
    public void update(NotifyMessages message) {

        if(message instanceof NewBoardStateMessage){
            System.out.println("VirtualView ha ricevuto NewBoardStateMessage");
            //reportToClient((NewBoardStateMessage)message);
        }else if (message instanceof LoseMessage){

            if(((LoseMessage) message).getPlayer()==this.getPlayer()){
                System.out.println("VirtualView ha ricevuto messaggio lose del player giocante");
                //reportToClient(new InfoMessage(GameMessage.lose));
            }else{
                System.out.println("VirtualView ha ricevuto messaggio lose di uno degli enemy del player giocante");
                //reportToClient(new InfoMessage("Player "+((LoseMessage) message).getPlayer().getNickname()+ "lost\n"));
            }
        }else if( message instanceof WinMessage){

            if(((WinMessage)message).getPlayer()==this.getPlayer()){
                System.out.println("VirtualView ha ricevuto messaggio win del player giocante");
                //reportToClient(new InfoMessage(GameMessage.win));
            }else{
                System.out.println("VirtualView ha ricevuto messaggio win di un enemy del player giocante");
                //reportToClient(new InfoMessage("Player "+((WinMessage) message).getPlayer().getNickname()+ "won\n"));
            }

        }


    }
}
