package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientSideConnection;
import it.polimi.ingsw.messages.GameMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.NewBoardStateMessage;
import it.polimi.ingsw.messages.InfoMessage;
import it.polimi.ingsw.messages.PlayerInfo;
import it.polimi.ingsw.observe.Observer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Scanner;

//TODO Cli e Gui nei loro metodi, quando viene fatta una mossa, creano il dataMessage e chiamano client.writeToSocket(dataMessage)
//TODO La View non deve avere un attributo Player vero? Sarebbe un problema perché voglio inizializzare la view separatamente senza avere bisogno di oggetto Player
public abstract class View implements Observer<Object> {


    private ClientSideConnection clientSideConnection;

    public View(ClientSideConnection clientSideConnection){

        this.clientSideConnection = clientSideConnection;
    }

    abstract public void showNewBoard(NewBoardStateMessage message);
    abstract public PlayerInfo createPlayerInfo();


    @Override
    public void update(Object message) {

        if(message instanceof NewBoardStateMessage){
            System.out.println("NewBoardStateMessage message arrived to client!");
            showNewBoard((NewBoardStateMessage) message);
        } else if (message instanceof InfoMessage){

            System.out.println("Infomessage arrived to view, here it is: "+((InfoMessage) message).getInfo());
            if(((InfoMessage) message).getInfo().equals(GameMessage.welcome)){
                clientSideConnection.asyncSend(createPlayerInfo());
            }

        } else {
            throw new IllegalArgumentException();
        }

    }


    public boolean isDateValid(String date){

        String DATE_FORMAT = "dd-MM-yyyy";

        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

}
