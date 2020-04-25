package it.polimi.ingsw.server;

public class ThreePlayerGameConnection {

    private final ServerSideConnection[] connections = new ServerSideConnection[3];

    public ThreePlayerGameConnection(ServerSideConnection connection1, ServerSideConnection connection2, ServerSideConnection connection3){
        this.connections[0]=connection1;
        this.connections[1]=connection2;
        this.connections[2]=connection3;
    }

    public ServerSideConnection getConnection(int n){
        return connections[n];
    }
}
