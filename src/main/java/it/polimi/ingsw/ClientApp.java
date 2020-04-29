package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import java.io.IOException;

public class ClientApp {

    public static void main(String[] args){

        Client client=new Client("127.0.0.1",12345);

        //Chiedere se si vuole Cli o Gui
        //Aggiungere dopo un if per istanziare quella giusta
        try {
            client.run();
        } catch (IOException e) {
            System.err.println("An error occurred while the client was running" + e.getMessage());
            e.printStackTrace();
        }




    }




}
