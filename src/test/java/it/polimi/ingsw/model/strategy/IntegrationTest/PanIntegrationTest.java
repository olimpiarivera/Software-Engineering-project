package it.polimi.ingsw.model.strategy.IntegrationTest;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.PlayerInfo;
import it.polimi.ingsw.messages.PlayerToGameMessages.PlayerBuildChoice;
import it.polimi.ingsw.messages.PlayerToGameMessages.PlayerEndOfTurnChoice;
import it.polimi.ingsw.messages.PlayerToGameMessages.PlayerMessage;
import it.polimi.ingsw.messages.PlayerToGameMessages.PlayerMovementChoice;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
;

public class PanIntegrationTest {

    GodCard pan;
    Model model;
    Controller controller;
    TurnInfo turnInfo;
    GameBoard gameBoard;

    Player testPlayer;
    Player enemy1Player;
    Player enemy2Player;
    PlayerInfo playerInfo;
    PlayerInfo enemy1Info;
    PlayerInfo enemy2Info;

    @BeforeEach
    void init() {

        model = new Model(3);
        controller = new Controller(model);

        gameBoard = model.getGameBoard();
        turnInfo = model.getTurnInfo();

        playerInfo = new PlayerInfo("xXoliTheQueenXx", new GregorianCalendar(1998, Calendar.SEPTEMBER, 9));
        testPlayer = new Player(playerInfo);
        testPlayer.setColour(Colour.WHITE);
        testPlayer.getWorker(0).setStartingPosition(0, 0);
        testPlayer.getWorker(1).setStartingPosition(1, 0);

        enemy1Info = new PlayerInfo("enemy1", new GregorianCalendar(2000, Calendar.NOVEMBER, 30));
        enemy1Player = new Player(playerInfo);
        enemy1Player.setColour(Colour.BLUE);
        enemy1Player.getWorker(0).setStartingPosition(0, 1);
        enemy1Player.getWorker(1).setStartingPosition(0, 2);

        enemy2Info = new PlayerInfo("enemy2", new GregorianCalendar(1999, Calendar.DECEMBER, 7));
        enemy2Player = new Player(playerInfo);
        enemy2Player.setColour(Colour.GREY);
        enemy2Player.getWorker(0).setStartingPosition(0, 3);
        enemy2Player.getWorker(1).setStartingPosition(0, 4);

        //Instancing testPlayer's godcard
        String godDataString[] = {"Pan", "God of the Wild", "Simple", "true", "You also win if your Worker moves down two or more levels."};
        pan = new GodCard(godDataString);
        testPlayer.setGodCard(pan);
    }

    @Nested
    class FirstChoice{

        @BeforeEach
        void init(){

            //GAMEBOARD GENERATION
            int[][] towers=
                    {
                            {2,4,1,2,2},
                            {3,1,2,1,4},
                            {4,1,0,3,4},
                            {0,2,1,4,4},
                            {0,1,1,4,0}
                    };

            gameBoard.generateBoard(towers);


            //POSITIONING TEST WORKERS
            gameBoard.getTowerCell(1,1).getFirstNotPieceLevel().setWorker(testPlayer.getWorker(0));
            testPlayer.getWorker(0).movedToPosition(1,1,1);

            gameBoard.getTowerCell(2,3).getFirstNotPieceLevel().setWorker(testPlayer.getWorker(1));
            testPlayer.getWorker(1).movedToPosition(2,3,1);

            //POSITIONING OPPONENT WORKERS

            gameBoard.getTowerCell(0,1).getFirstNotPieceLevel().setWorker(enemy1Player.getWorker(0));
            enemy1Player.getWorker(0).movedToPosition(0,1,3);

            gameBoard.getTowerCell(2,2).getFirstNotPieceLevel().setWorker(enemy1Player.getWorker(1));
            enemy1Player.getWorker(1).movedToPosition(2,2,0);

            gameBoard.getTowerCell(4,4).getFirstNotPieceLevel().setWorker(enemy2Player.getWorker(0));
            enemy2Player.getWorker(0).movedToPosition(4,4,0);

            gameBoard.getTowerCell(2,4).getFirstNotPieceLevel().setWorker(enemy2Player.getWorker(1));
            enemy2Player.getWorker(1).movedToPosition(2,4,1);
        }

        //turn starts here, only move should be performed

        @Test
        void EndBeforeEverything(){

            PlayerMessage message=new PlayerEndOfTurnChoice(new View(),testPlayer);
            controller.update(message);
            //method returns immediately

            //turnInfo must still have all his initial values
            assertEquals(0,turnInfo.getNumberOfMoves());
            assertEquals(0,turnInfo.getNumberOfBuilds());
            assertFalse(turnInfo.getHasAlreadyMoved());
            assertFalse(turnInfo.getHasAlreadyBuilt());
            assertEquals(-1,turnInfo.getChosenWorker());
            assertFalse(turnInfo.getTurnCanEnd());
            assertFalse(turnInfo.getTurnHasEnded());
        }

        @Test
        void BuildBeforeEverything(){

            PlayerMessage message=new PlayerBuildChoice(new View(),testPlayer,1,1,2,"Block");
            controller.update(message);

            //turnInfo must still have all his initial values
            assertEquals(0,turnInfo.getNumberOfMoves());
            assertEquals(0,turnInfo.getNumberOfBuilds());
            assertFalse(turnInfo.getHasAlreadyMoved());
            assertFalse(turnInfo.getHasAlreadyBuilt());
            assertEquals(-1,turnInfo.getChosenWorker());
            assertFalse(turnInfo.getTurnCanEnd());
            assertFalse(turnInfo.getTurnHasEnded());
        }

        @Test //ok
        void MoveBeforeEverything(){
            PlayerMessage message=new PlayerMovementChoice(new View(),testPlayer,1,1,2);
            controller.update(message);

            //turnInfo must have been modified
            assertEquals(1,turnInfo.getNumberOfMoves());
            assertEquals(0,turnInfo.getNumberOfBuilds());
            assertTrue(turnInfo.getHasAlreadyMoved());
            assertFalse(turnInfo.getHasAlreadyBuilt());
            assertEquals(1,turnInfo.getChosenWorker());
            assertFalse(turnInfo.getTurnCanEnd());
            assertFalse(turnInfo.getTurnHasEnded());
        }

    }

    @Nested
    class SecondChoice {

        @BeforeEach
        void init() {

            //SETS INITIAL STATE
            turnInfo.setChosenWorker(1);
            turnInfo.setHasMoved();
            turnInfo.addMove();

            //GAMEBOARD GENERATION
            int[][] towers=
                    {
                            {2,4,1,2,2},
                            {3,1,2,1,4},
                            {4,1,0,3,4},
                            {0,2,1,4,4},
                            {0,1,1,4,0}
                    };

            gameBoard.generateBoard(towers);


            //POSITIONING TEST WORKERS
            gameBoard.getTowerCell(1,1).getFirstNotPieceLevel().setWorker(testPlayer.getWorker(0));
            testPlayer.getWorker(0).movedToPosition(1,1,1);

            gameBoard.getTowerCell(1,2).getFirstNotPieceLevel().setWorker(testPlayer.getWorker(1));
            testPlayer.getWorker(1).movedToPosition(1,2,1);

            //POSITIONING OPPONENT WORKERS

            gameBoard.getTowerCell(0,1).getFirstNotPieceLevel().setWorker(enemy1Player.getWorker(0));
            enemy1Player.getWorker(0).movedToPosition(0,1,3);

            gameBoard.getTowerCell(2,2).getFirstNotPieceLevel().setWorker(enemy1Player.getWorker(1));
            enemy1Player.getWorker(1).movedToPosition(2,2,0);

            gameBoard.getTowerCell(4,4).getFirstNotPieceLevel().setWorker(enemy2Player.getWorker(0));
            enemy2Player.getWorker(0).movedToPosition(4,4,0);

            gameBoard.getTowerCell(2,4).getFirstNotPieceLevel().setWorker(enemy2Player.getWorker(1));
            enemy2Player.getWorker(1).movedToPosition(2,4,1);

        }

        //move done, only build should be performed

        @Test
        void EndAftertMove() {
        }

        @Test
        void MoveAfterMove() {
        }

        @Test
            //ok
        void BuildAfterMove() {
        }
    }

    @Nested
    class ThirdChoice {

        @BeforeEach
        void init() {
            //GAMEBOARD GENERATION
            int[][] towers =
                    {
                            {0, 0, 2, 2, 2},
                            {0, 0, 0, 0, 3},
                            {0, 1, 0, 3, 4},
                            {0, 0, 1, 2, 3},
                            {0, 0, 1, 0, 0}
                    };

            gameBoard.generateBoard(towers);
            turnInfo.setChosenWorker(0);
            turnInfo.setHasMoved();
            turnInfo.addMove();
            turnInfo.setHasBuilt();
            turnInfo.addBuild();


        }

        //turn finishes here, only end should be performed

        @Test
        void MoveAfterFinish() {
        }

        @Test
        void BuildAfterFinish() {
        }

        @Test
        void EndAfterFinish() {
        }
    }







}
