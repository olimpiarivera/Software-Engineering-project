package it.polimi.ingsw.model.strategy.IntegrationTest;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.PlayerToGameMessages.CompleteMessages.PlayerBuildChoice;
import it.polimi.ingsw.messages.PlayerToGameMessages.CompleteMessages.PlayerEndOfTurnChoice;
import it.polimi.ingsw.messages.PlayerToGameMessages.CompleteMessages.PlayerMessage;
import it.polimi.ingsw.messages.PlayerToGameMessages.CompleteMessages.PlayerMovementChoice;
import it.polimi.ingsw.messages.PlayerToGameMessages.DataMessages.BuildData;
import it.polimi.ingsw.messages.PlayerToGameMessages.DataMessages.MoveData;
import it.polimi.ingsw.messages.PlayerToGameMessages.DataMessages.PlayerInfo;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.piece.Level2Block;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.ServerSideConnection;
import it.polimi.ingsw.supportClasses.FakeConnection;
import it.polimi.ingsw.supportClasses.TestSupportFunctions;
import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

public class MinotaurIntegrationTest {

    Server server;
    ServerSideConnection c1;
    ServerSideConnection c2;
    ServerSideConnection c3;

    Player testPlayer;
    Player enemy1Player;
    Player enemy2Player;
    PlayerInfo testPlayerInfo;
    PlayerInfo enemy1Info;
    PlayerInfo enemy2Info;
    GodCard minotaur;

    Controller controller;
    Model model;
    ArrayList<VirtualView> virtualViews;
    GameBoard gameBoard;
    TurnInfo turnInfo;
    
    TestSupportFunctions testSupportFunctions=new TestSupportFunctions();

    @BeforeEach
    void init() {

        try {
            server = new Server();
            c1 = new FakeConnection(new Socket(),server,"c1");
            c2 = new FakeConnection(new Socket(),server,"c2");
            c3 = new FakeConnection(new Socket(),server,"c3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        testPlayerInfo = new PlayerInfo("xXoliTheQueenXx", new GregorianCalendar(1998, Calendar.SEPTEMBER, 9),3);
        testPlayer = new Player(testPlayerInfo);

        enemy1Info = new PlayerInfo("enemy1", new GregorianCalendar(1990, Calendar.NOVEMBER, 30),3);
        enemy1Player = new Player(enemy1Info);

        enemy2Info = new PlayerInfo("enemy2", new GregorianCalendar(1995, Calendar.DECEMBER, 7),3);
        enemy2Player = new Player(enemy2Info);

        //Instancing testPlayer's godcard
        String godDataString[] = {"Minotaur", "Bull-headed Monster", "Simple", "true", "Your Worker may move into an opponent Worker???s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level."};
        minotaur = new GodCard(godDataString);
        testPlayer.setGodCard(minotaur);

        ArrayList<Player> players=new ArrayList<>();
        players.add(testPlayer);
        players.add(enemy1Player);
        players.add(enemy2Player);
        ArrayList<ServerSideConnection> connections=new ArrayList<>();
        connections.add(c1);
        connections.add(c2);
        connections.add(c3);

        controller=new Controller(players,connections);

        virtualViews=controller.getVirtualViews();
        model=controller.getModel();
        turnInfo=model.getTurnInfo();
        gameBoard=model.getGameBoard();

        testPlayer.getWorker(0).setStartingPosition(0, 0);
        testPlayer.getWorker(1).setStartingPosition(1, 0);

        enemy1Player.getWorker(0).setStartingPosition(0, 1);
        enemy1Player.getWorker(1).setStartingPosition(0, 2);

        enemy2Player.getWorker(0).setStartingPosition(0, 3);
        enemy2Player.getWorker(1).setStartingPosition(0, 4);
    }

    
    @AfterEach
    void end(){
        //closing serverSocket
        try {
            server.closeServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //should only be able to move
    @Nested
    class FirstChoice{

        @BeforeEach
        void init() {

            //GAMEBOARD GENERATION
            int[][] towers =
                    {
                            {2, 4, 1, 2, 2},
                            {3, 1, 2, 1, 4},
                            {4, 1, 0, 3, 4},
                            {0, 2, 2, 4, 4},
                            {3, 2, 1, 4, 0}
                    };

            gameBoard.generateBoard(towers);


            //POSITIONING TEST WORKERS
            gameBoard.getTowerCell(1, 1).getFirstNotPieceLevel().setWorker(testPlayer.getWorker(0));
            testPlayer.getWorker(0).movedToPosition(1, 1, 1);

            gameBoard.getTowerCell(2, 3).getFirstNotPieceLevel().setWorker(testPlayer.getWorker(1));
            testPlayer.getWorker(1).movedToPosition(2, 3, 2);

            //POSITIONING OPPONENT WORKERS

            gameBoard.getTowerCell(0, 1).getFirstNotPieceLevel().setWorker(enemy1Player.getWorker(0));
            enemy1Player.getWorker(0).movedToPosition(0, 1, 3);

            gameBoard.getTowerCell(2, 2).getFirstNotPieceLevel().setWorker(enemy1Player.getWorker(1));
            enemy1Player.getWorker(1).movedToPosition(2, 2, 0);

            gameBoard.getTowerCell(2, 1).getFirstNotPieceLevel().setWorker(enemy2Player.getWorker(0));
            enemy2Player.getWorker(0).movedToPosition(2, 1, 2);

            gameBoard.getTowerCell(2, 4).getFirstNotPieceLevel().setWorker(enemy2Player.getWorker(1));
            enemy2Player.getWorker(1).movedToPosition(2, 4, 1);
        }

        @Test
        void EndBeforeEverything(){
            PlayerMessage message = new PlayerEndOfTurnChoice(virtualViews.get(0), testPlayer);
            controller.update(message);
            //method returns immediately, can't end yet

            //turnInfo must still have all his initial values
            testSupportFunctions.baseTurnInfoChecker(turnInfo,false,0,false,0,-1,false,false);
        }

        @Test
        void buildBeforeEverything(){
            PlayerMessage message = new PlayerBuildChoice(virtualViews.get(0), testPlayer, new BuildData(-2, 0, 0, "Block"));
            controller.update(message);
            //wrong worker, but should give error for missing move

            //turnInfo must still have all his initial values
            testSupportFunctions.baseTurnInfoChecker(turnInfo,false,0,false,0,-1,false,false);
        }

        @Test
        void NoPushableMoveBeforeEverything(){
            PlayerMessage message = new PlayerMovementChoice(virtualViews.get(0), testPlayer, new MoveData(0, 2, 2));
            controller.update(message);
            //opponent can't be pushed, invalid move

            //turnInfo must still have all his initial values
            testSupportFunctions.baseTurnInfoChecker(turnInfo,false,0,false,0,-1,false,false);

            assertEquals(testPlayer.getWorker(0),gameBoard.getTowerCell(1,1).getFirstNotPieceLevel().getWorker());
            assertEquals(enemy1Player.getWorker(1),gameBoard.getTowerCell(2,2).getFirstNotPieceLevel().getWorker());
        }

        @Test
        void PushableMoveBeforeEverythingAndWin(){

            PlayerMessage message = new PlayerMovementChoice(virtualViews.get(0), testPlayer, new MoveData(0, 2, 1));
            controller.update(message);
            //opponent can pushed, valid move

            testSupportFunctions.baseTurnInfoChecker(turnInfo,true,1,false,0,0,false,false);


            //enemy worker pushed
            assertEquals(enemy2Player.getWorker(0),gameBoard.getTowerCell(3,1).getFirstNotPieceLevel().getWorker());
            assertAll(
                    ()->assertEquals(3,enemy2Player.getWorker(0).getCurrentPosition().getX()),
                    ()->assertEquals(1,enemy2Player.getWorker(0).getCurrentPosition().getY()),
                    ()->assertEquals(1,enemy2Player.getWorker(0).getCurrentPosition().getZ()),
                    ()->assertEquals(2,enemy2Player.getWorker(0).getPreviousPosition().getX()),
                    ()->assertEquals(1,enemy2Player.getWorker(0).getPreviousPosition().getY()),
                    ()->assertEquals(2,enemy2Player.getWorker(0).getPreviousPosition().getZ())

            );

            //e
            assertEquals(testPlayer.getWorker(0),gameBoard.getTowerCell(2,1).getFirstNotPieceLevel().getWorker());
            assertAll(
                    ()->assertEquals(2,testPlayer.getWorker(0).getCurrentPosition().getX()),
                    ()->assertEquals(1,testPlayer.getWorker(0).getCurrentPosition().getY()),
                    ()->assertEquals(2,testPlayer.getWorker(0).getCurrentPosition().getZ()),
                    ()->assertEquals(1,testPlayer.getWorker(0).getPreviousPosition().getX()),
                    ()->assertEquals(1,testPlayer.getWorker(0).getPreviousPosition().getY()),
                    ()->assertEquals(1,testPlayer.getWorker(0).getPreviousPosition().getZ())

            );
            assertNull(gameBoard.getTowerCell(1,1).getFirstNotPieceLevel().getWorker());

        }
    }

    //should only be able to build (after move)
    @Nested
    class SecondChoice{

        @BeforeEach
        void init() {

            turnInfo.setHasMoved();
            turnInfo.addMove();
            turnInfo.setChosenWorker(0);

            //GAMEBOARD GENERATION
            int[][] towers =
                    {
                            {2, 4, 1, 2, 2},
                            {3, 1, 2, 1, 4},
                            {4, 1, 0, 3, 4},
                            {0, 2, 2, 4, 4},
                            {3, 2, 1, 4, 0}
                    };

            gameBoard.generateBoard(towers);


            //POSITIONING TEST WORKERS
            gameBoard.getTowerCell(2, 1).getFirstNotPieceLevel().setWorker(testPlayer.getWorker(0));
            testPlayer.getWorker(0).movedToPosition(2, 1, 2);

            gameBoard.getTowerCell(2, 3).getFirstNotPieceLevel().setWorker(testPlayer.getWorker(1));
            testPlayer.getWorker(1).movedToPosition(2, 3, 2);

            //POSITIONING OPPONENT WORKERS

            gameBoard.getTowerCell(0, 1).getFirstNotPieceLevel().setWorker(enemy1Player.getWorker(0));
            enemy1Player.getWorker(0).movedToPosition(0, 1, 3);

            gameBoard.getTowerCell(2, 2).getFirstNotPieceLevel().setWorker(enemy1Player.getWorker(1));
            enemy1Player.getWorker(1).movedToPosition(2, 2, 0);

            gameBoard.getTowerCell(3, 1).getFirstNotPieceLevel().setWorker(enemy2Player.getWorker(0));
            enemy2Player.getWorker(0).movedToPosition(3, 1, 1);

            gameBoard.getTowerCell(2, 4).getFirstNotPieceLevel().setWorker(enemy2Player.getWorker(1));
            enemy2Player.getWorker(1).movedToPosition(2, 4, 1);
        }

        @Test
        void EndAfterMove(){

            PlayerMessage message = new PlayerEndOfTurnChoice(virtualViews.get(0), testPlayer);
            controller.update(message);
            //method returns immediately, can't end yet

            //turnInfo must still have all his initial values
            testSupportFunctions.baseTurnInfoChecker(turnInfo,true,1,false,0,0,false,false);
        }

        @Test
        void moveAfterMove(){
            PlayerMessage message = new PlayerMovementChoice(virtualViews.get(0), testPlayer, new MoveData(0, 2, 1));
            controller.update(message);
            //can't move again error, and is moving on his own position

            testSupportFunctions.baseTurnInfoChecker(turnInfo,true,1,false,0,0,false,false);
        }

        @Test
        void wrongBuildAfterMove(){
            PlayerMessage message = new PlayerBuildChoice(virtualViews.get(0), testPlayer, new BuildData(0, 3, 1, "Block"));
            controller.update(message);
            //error, cell occupied by worker

            testSupportFunctions.baseTurnInfoChecker(turnInfo,true,1,false,0,0,false,false);
        }

        @Test
        void wrongBuildAfterMove2(){
            PlayerMessage message = new PlayerBuildChoice(virtualViews.get(0), testPlayer, new BuildData(1, 3, 2, "Block"));
            controller.update(message);
            //error, not same worker

            testSupportFunctions.baseTurnInfoChecker(turnInfo,true,1,false,0,0,false,false);
            assertEquals(3,gameBoard.getTowerCell(3,2).getTowerHeight());
        }

        @Test
        void correctBuildAfterEverything(){

            PlayerMessage message = new PlayerBuildChoice(virtualViews.get(0), testPlayer, new BuildData(0, 2, 0, "Block"));
            controller.update(message);
            //build ok

            testSupportFunctions.baseTurnInfoChecker(turnInfo,true,1,true,1,0,true,true);
            assertEquals(2,gameBoard.getTowerCell(2,0).getTowerHeight());
            assertTrue(gameBoard.getTowerCell(2,0).getLevel(1).getPiece() instanceof Level2Block);
        }
    }

    //should only be able to end (after move and build)
    @Nested
    class ThirdChoice{

        @BeforeEach
        void init() {

            turnInfo.setHasMoved();
            turnInfo.addMove();
            turnInfo.setChosenWorker(0);
            turnInfo.setHasBuilt();
            turnInfo.addBuild();
            turnInfo.setTurnCanEnd();
            turnInfo.setTurnHasEnded();

            //GAMEBOARD GENERATION
            int[][] towers =
                    {
                            {2, 4, 2, 2, 2},
                            {3, 1, 2, 1, 4},
                            {4, 1, 0, 3, 4},
                            {0, 2, 2, 4, 4},
                            {3, 2, 1, 4, 0}
                    };

            gameBoard.generateBoard(towers);


            //POSITIONING TEST WORKERS
            gameBoard.getTowerCell(2, 1).getFirstNotPieceLevel().setWorker(testPlayer.getWorker(0));
            testPlayer.getWorker(0).movedToPosition(2, 1, 2);

            gameBoard.getTowerCell(2, 3).getFirstNotPieceLevel().setWorker(testPlayer.getWorker(1));
            testPlayer.getWorker(1).movedToPosition(2, 3, 2);

            //POSITIONING OPPONENT WORKERS

            gameBoard.getTowerCell(0, 1).getFirstNotPieceLevel().setWorker(enemy1Player.getWorker(0));
            enemy1Player.getWorker(0).movedToPosition(0, 1, 3);

            gameBoard.getTowerCell(2, 2).getFirstNotPieceLevel().setWorker(enemy1Player.getWorker(1));
            enemy1Player.getWorker(1).movedToPosition(2, 2, 0);

            gameBoard.getTowerCell(3, 1).getFirstNotPieceLevel().setWorker(enemy2Player.getWorker(0));
            enemy2Player.getWorker(0).movedToPosition(3, 1, 1);

            gameBoard.getTowerCell(2, 4).getFirstNotPieceLevel().setWorker(enemy2Player.getWorker(1));
            enemy2Player.getWorker(1).movedToPosition(2, 4, 1);
        }
        @Test
        void EndAfterFinish(){
            PlayerMessage message = new PlayerEndOfTurnChoice(virtualViews.get(0), testPlayer);
            controller.update(message);
            //ok, turn must end

            //turnInfo reset
            testSupportFunctions.baseTurnInfoChecker(turnInfo,false,0,false,0,-1,false,false);
            assertEquals(Colour.BLUE,model.getTurn());

        }

        @Test
        void MoveAfterFinish(){
            PlayerMessage message = new PlayerMovementChoice(virtualViews.get(0), testPlayer,new MoveData( 0, 0, 1));
            controller.update(message);
            //can't move again error, turn ended (and invalid position)
            testSupportFunctions.baseTurnInfoChecker(turnInfo,true,1,true,1,0,true,true);
        }

        @Test
        void BuildAfterFinish(){
            PlayerMessage message = new PlayerBuildChoice(virtualViews.get(0), testPlayer, new BuildData(0, 0, 1, "Block"));
            controller.update(message);
            //can't build again error, turn ended
            testSupportFunctions.baseTurnInfoChecker(turnInfo,true,1,true,1,0,true,true);
        }
    }
}
