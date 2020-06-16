package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.ClientSideConnection;
import it.polimi.ingsw.messages.GameToPlayerMessages.Notify.GameStartMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.Notify.NewBoardStateMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.Others.ErrorMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.Others.InfoMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.Others.PlayerInfoRequest;
import it.polimi.ingsw.messages.GameToPlayerMessages.Others.PossibleCardsMessage;
import it.polimi.ingsw.messages.PlayerToGameMessages.DataMessages.*;
import it.polimi.ingsw.view.ClientViewSupportFunctions;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Cli extends View {


    private Scanner stdin;
    public ClientViewSupportFunctions sf = new ClientViewSupportFunctions();

    public Cli(ClientSideConnection clientSideConnection) {

        super(clientSideConnection);
        stdin = new Scanner(System.in);
    }

    @Override
    public void handleNewBoardStateMessage(NewBoardStateMessage message) {
        Matrix matrix = new Matrix();
        matrix.printMatrix(matrix.convertToMatrix(message));

    }

    @Override
    public void handleInfoMessage(InfoMessage message) {
        System.out.println(message.getInfo());
    }

    @Override
    public void handleErrorMessage(ErrorMessage message) {
        System.out.println(message.getError());
    }

    @Override
    public void handlePlayerInfoRequest(PlayerInfoRequest message) {
        getClientSideConnection().send(createPlayerInfo(message));
    }

    @Override
    public void handleCardMessageRequest(PossibleCardsMessage message) {
        getClientSideConnection().send(createCardChoice(message));

    }

    @Override
    public void handleStartingPositionRequest() {
        getClientSideConnection().send(createStartingPositionChoice());
    }

    @Override
    public void handleGameStartMessage(GameStartMessage message) {
        startActionRequestThread();
    }

    @Override
    public void handleCloseConnectionMessage() {
        System.out.println("You have been disconnected");
    }


    /**
     * Creates a PlayerInfo message based on user's inputs
     * @return the playerInfo object craeted
     */
    private PlayerInfo createPlayerInfo(PlayerInfoRequest message){

        if(message.isNicknameTaken()){ System.out.println("This name already exist, choose another one: ");}
        else{ System.out.println("Insert your nickname:"); }
        String nickname = stdin.nextLine();

        int day=0;
        int month=0;
        int year=0;
        String dayString;
        String monthString;
        String yearString;


        boolean validDate=false;
        do{

            System.out.println("Insert birthday day:");
            dayString = stdin.nextLine();

            System.out.println("Insert birthday month:");
            monthString = stdin.nextLine();

            System.out.println("Insert birthday year:");
            yearString = stdin.nextLine();

            String dateString = dayString+"-"+monthString+"-"+yearString;

            validDate = sf.isDateValid(dateString,dayString,monthString,yearString);

            if(!validDate)System.out.println("The date inserted is not valid, try again");

        }while(!validDate);

        day = Integer.parseInt(dayString);
        month = Integer.parseInt(monthString);
        year = Integer.parseInt(yearString);

        //need to create a new Calendar object with birthdayString data
        int numberOfPlayers = 0;
        String numberOfPlayersString;
        boolean validNumberOfPlayers=false;
        do{

            System.out.println("Insert number of players in the match (2 or 3):");
            numberOfPlayersString = stdin.nextLine();

            validNumberOfPlayers = sf.isValidNumberOfPlayers(numberOfPlayersString);
            if(!validNumberOfPlayers)System.out.println("That number is not valid, try again");


        }while(!validNumberOfPlayers);

        numberOfPlayers = Integer.parseInt(numberOfPlayersString);

        return (new PlayerInfo(nickname,new GregorianCalendar(year,month-1,day),numberOfPlayers));
    }

    /**
     * Creates a StartingPositionChoice message based on user's inputs
     * @return StartingPositionChoice message
     */
    private StartingPositionChoice createStartingPositionChoice() {

        String pos;
        String delims = ",";
        String[] tokens;
        ArrayList<Integer> positions = new ArrayList<>();
        boolean validPos = false;


        for(int i=0; i<2;i++){

            do{
                try {
                    System.out.println("Insert worker"+(i+1)+" position x,y :");
                    pos = stdin.nextLine();

                    if(!sf.isPositionValid(pos)){
                        System.out.print("Invalid position, try again");
                    }else{
                        validPos=true;
                        tokens = pos.split(delims);
                        positions.add(Integer.parseInt(tokens[0]));
                        positions.add(Integer.parseInt(tokens[1]));
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Not a number, try again");

                }
            }while(!validPos);
            validPos = false;
        }

        return (new StartingPositionChoice(positions.get(0),positions.get(1),positions.get(2),positions.get(3)));


    }

    /**
     * creates a CardChoice message based on user's inputs
     * @param message has a number of choices attribute and a list of possible Gods
     * @return the CardChoice message created
     */
    private CardChoice createCardChoice(PossibleCardsMessage message) {

        ClientViewSupportFunctions cvsf = new ClientViewSupportFunctions();
        String[] chosenGods;
        String choice;
        boolean validChosenGods = false;

        if(message.getNumberOfChoices()>0){

            chosenGods = new String[message.getNumberOfChoices()];
            System.out.println("Choose "+message.getNumberOfChoices()+" of the following gods: ");
            for(String s : message.getGods()){
                System.out.println(s);
            }

            do{
                for(int n=0 ; n<message.getNumberOfChoices();n++){
                    System.out.println("Choose the God: ");
                    choice = stdin.nextLine();
                    //correct format is first letter uppercase
                    chosenGods[n]= cvsf.nameToCorrectFormat(choice);
                }
                if(sf.isChosenGodsValid(chosenGods, message.getNumberOfChoices(), message)){
                    validChosenGods=true;
                }else{
                    System.out.println("Invalid choice, try again");
                }

            }while(!validChosenGods);

            return(new CardChoice(chosenGods));
        }

        else{

            throw new IllegalArgumentException();
        }
    }

    /**
     * given the input data, it creates the appropriate message and sends it to the server
     * @param tokens contains the data to send in the message
     */
    public void handleInput(String[] tokens){

        sf = new ClientViewSupportFunctions();

        switch (sf.nameToCorrectFormat(tokens[0])) {
            case "End" -> this.getClientSideConnection().send(new EndChoice());
            case "Quit" -> this.getClientSideConnection().send(new QuitChoice());
            case "Move" -> this.getClientSideConnection().send(new MoveData(Integer.parseInt(tokens[1]) - 1, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
            case "Build" -> this.getClientSideConnection().send(new BuildData(Integer.parseInt(tokens[1]) - 1, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), sf.nameToCorrectFormat(tokens[4])));
            default -> System.out.println("Command not found");
        }

    }


    /**
     * creates a thread that continuously reads the user's inputs, and calls handleInput() is
     * the command is valid
     */
    private void startActionRequestThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    while (getClientSideConnection().isActive()) {

                        System.out.println("Insert a command:");
                        System.out.println("move worker(1/2) x,y");
                        System.out.println("build worker(1/2) x,y block/dome");

                        System.out.println("end (to end your turn)");
                        System.out.println("quit (to leave the game, only if already eliminated");
                        String inputLine = stdin.nextLine();
                        String delims = "[, ]";
                        String[] tokens;
                        tokens = inputLine.split(delims);
                        if (sf.isValidInputString(tokens)) {
                            handleInput(tokens);
                        } else {
                            System.out.println("Command is not valid");
                        }

                    }

                } catch (Exception e) {

                    getClientSideConnection().setActive(false);
                    e.printStackTrace();
                } finally {
                    stdin.close();
                }
            }
        });
        t.start();
    }
}
