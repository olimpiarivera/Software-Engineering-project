package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.ClientSideConnection;
import it.polimi.ingsw.messages.GameToPlayerMessages.NewBoardStateMessage;
import it.polimi.ingsw.messages.PlayerInfo;
import it.polimi.ingsw.view.View;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Cli extends View {


    private Scanner stdin;


    public Cli(ClientSideConnection clientSideConnection) {

        super(clientSideConnection);
        stdin = new Scanner(System.in);


    }

    public void showNewBoard(NewBoardStateMessage message){

        System.out.println("Mostro la board sulla Command Line");

    }

    public PlayerInfo createPlayerInfo(){

        System.out.println("What's your nickname?");
        String nickname = stdin.nextLine();

        int day=0;
        int month=0;
        int year=0;
        boolean validDate=false;
        do{
            try {
                System.out.println("Insert birthday day:");
                day = Integer.parseInt(stdin.nextLine());
                System.out.println("Insert birthday month:");
                month = Integer.parseInt(stdin.nextLine());
                System.out.println("Insert birthday year:");
                year = Integer.parseInt(stdin.nextLine());

                String dateString = day +"-"+month+"-"+year;
                validDate=isDateValid(dateString);
                if(!validDate){System.out.print("Not valid, try again");}

            } catch (NumberFormatException e) {
                //TODO non posso stampare la stacktrace su GUI, e in realtà non la voglio vedere nenache su CLI
                e.printStackTrace();
                System.out.print("Not a number, try again");
            }
        }while(!validDate);

        //need to create a new Calendar object with birthdayString data
        int numberOfPlayers = 0;
        boolean validNumberOfPlayers=false;
        do{
            try {
                System.out.println("Insert number of players:");
                numberOfPlayers = Integer.parseInt(stdin.nextLine());
                if(numberOfPlayers!=2 && numberOfPlayers!=3){
                    System.out.print("Not valid, try again");
                }else{
                    validNumberOfPlayers=true;
                }

            } catch (NumberFormatException e) {
                //TODO non posso stampare la stacktrace su GUI, e in realtà non la voglio vedere nenache su CLI
                System.out.print("Not a number, try again");
                e.printStackTrace();
            }
        }while(!validNumberOfPlayers);

        return (new PlayerInfo(nickname,new GregorianCalendar(year,month-1,day),numberOfPlayers));
    }


    @Override
    public void run() {

        try {

            while(getClientSideConnection().isActive()) {

                String inputLine = stdin.nextLine();
                //trasforma la stringa in un oggetto messaggio in base a cosa c'è scritto e poi chiama clientSideConn.asyncsend(messaggio)
                System.out.println("thread lettore cli ha letto una stringa");
            }

        } catch (Exception e) {

            getClientSideConnection().setActive(false);
            e.printStackTrace();

        } finally {

            stdin.close();

        }


    }
}
