package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientSideConnection;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Cli;

public class ClientApp {

//TODO CLI/GUI ASSEGNAMETO INIZIALE
    public static void main(String[] args){

        View view;
        ClientSideConnection clientSideConnection = new ClientSideConnection("192.168.1.10",12345);
        Thread t0;
        Thread t1;

        /////////per adesso istanzio direttamente CLI
        view = new Cli(clientSideConnection);
        clientSideConnection.addObserver(view);


        t0 = new Thread(clientSideConnection);
        t0.start();

        //ATTENZIONE: sia questo processo che il thread t0 vanno a guardare/cambiare canStart. Ho sincronizzato set/get sulla classe
        while(!view.getCanStart() && clientSideConnection.isActive()){
            //do nothing

        }
        t1 = new Thread(view);
        t1.start();


        try {
            t0.join();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }




}
