package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);

    private List<PlayerConnection> twoPlayerWaitingList =new ArrayList<>();
    private List<PlayerConnection> threePlayerWaitingList =new ArrayList<>();
    private List<TwoPlayerGameConnection> twoPlayerGames = new ArrayList<>();
    private List<ThreePlayerGameConnection> threePlayerGames = new ArrayList<>();


    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                ServerSideConnection socketConnection = new ServerSideConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("ServerSideConnection Error!");
            }
        }
    }

    public synchronized void lobby(PlayerConnection playerConnection) {



        if(playerConnection.getPlayerInfo().getNumberOfPlayers()==2){

            twoPlayerWaitingList.add(playerConnection);

            if(twoPlayerWaitingList.size()==2){


                ArrayList<ServerSideConnection> connections = new ArrayList<>();
                ServerSideConnection c21 = twoPlayerWaitingList.get(0).getServerSideConnection();
                ServerSideConnection c22 = twoPlayerWaitingList.get(1).getServerSideConnection();
                connections.add(c21);
                connections.add(c22);

                ArrayList<Player> players= new ArrayList<>();
                Player player21 = new Player(twoPlayerWaitingList.get(0).getPlayerInfo());
                Player player22 = new Player(twoPlayerWaitingList.get(1).getPlayerInfo());
                players.add(player21);
                players.add(player22);

                twoPlayerGames.add(new TwoPlayerGameConnection(c21,c22));
                twoPlayerWaitingList.clear();

                Controller controller=new Controller(players,connections);
                controller.startGame();

            }

        }else{

            threePlayerWaitingList.add(playerConnection);

            if(threePlayerWaitingList.size()==3){

                ArrayList<ServerSideConnection> connections = new ArrayList<>();
                ServerSideConnection c31 = threePlayerWaitingList.get(0).getServerSideConnection();
                ServerSideConnection c32 = threePlayerWaitingList.get(1).getServerSideConnection();
                ServerSideConnection c33 = threePlayerWaitingList.get(2).getServerSideConnection();
                connections.add(c31);
                connections.add(c32);
                connections.add(c33);

                ArrayList<Player> players= new ArrayList<>();
                Player player31=new Player(threePlayerWaitingList.get(0).getPlayerInfo());
                Player player32=new Player(threePlayerWaitingList.get(1).getPlayerInfo());
                Player player33=new Player(threePlayerWaitingList.get(2).getPlayerInfo());
                players.add(player31);
                players.add(player32);
                players.add(player33);

                threePlayerGames.add(new ThreePlayerGameConnection(c31,c32,c33));
                threePlayerWaitingList.clear();

                Controller controller=new Controller(players,connections);
                controller.startGame();

            }
        }

    }

    public synchronized void unregisterConnection(ServerSideConnection connection) {

        int index;

        index=getConnectionGroupIndex2(connection);

        if(index!=-1){

            twoPlayerGames.get(index).getConnection(0).notInUse();
            twoPlayerGames.get(index).getConnection(1).notInUse();
            twoPlayerGames.remove(index);

        }else{

            index=getConnectionGroupIndex3(connection);
            threePlayerGames.get(index).getConnection(0).notInUse();
            threePlayerGames.get(index).getConnection(1).notInUse();
            threePlayerGames.get(index).getConnection(2).notInUse();
            threePlayerGames.remove(index);
        }

        //TODO perchè parravicini messo l'iterator?
    }

    private int getConnectionGroupIndex2(ServerSideConnection serverSideConnection) {

        for (int i = 0; i < twoPlayerGames.size(); i++) {
            if (twoPlayerGames.get(i).getConnection(0).equals(serverSideConnection) ||
                twoPlayerGames.get(i).getConnection(1).equals(serverSideConnection)) {
                return i;
            }
        }
        return -1;
    }

    private int getConnectionGroupIndex3(ServerSideConnection serverSideConnection) {

        for(int i=0;i<threePlayerGames.size();i++){
            if(threePlayerGames.get(i).getConnection(0).equals(serverSideConnection) ||
               threePlayerGames.get(i).getConnection(1).equals(serverSideConnection) ||
               threePlayerGames.get(i).getConnection(2).equals(serverSideConnection)) {
                return i;
            }
        }
        return -1;
    }




    //TESTING METHOD
    public void closeServerSocket() throws IOException {
        this.serverSocket.close();
    }

    public List<PlayerConnection> getTwoPlayerWaitingList() {
        return twoPlayerWaitingList;
    }

    public List<PlayerConnection> getThreePlayerWaitingList() {
        return threePlayerWaitingList;
    }

    public List<TwoPlayerGameConnection> getTwoPlayerGames() {
        return twoPlayerGames;
    }

    public List<ThreePlayerGameConnection> getThreePlayerGames() {
        return threePlayerGames;
    }
}

