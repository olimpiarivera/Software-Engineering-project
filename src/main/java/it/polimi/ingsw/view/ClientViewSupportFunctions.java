package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.GameToPlayerMessages.Others.PossibleCardsMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientViewSupportFunctions {

    public String nameToCorrectFormat(String s){
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }

    /**
     * checks if position is inside of the borders
     * @param pos
     * @return
     */
    public boolean isPositionValid(String pos){

        String delims = ",";
        String[] tokens = pos.split(delims);

        for(String s : tokens){
            try{
                Integer.parseInt(s);
            }catch(NumberFormatException e) {
                return false;
            }
        }


        return (Integer.parseInt(tokens[0]) >= 0) && (Integer.parseInt(tokens[0]) <= 4) && (Integer.parseInt(tokens[1]) >= 0) && (Integer.parseInt(tokens[1]) <= 4);

    }

    /**
     * Checks if date is a valid date format and if integers are numbers
     * @param date
     * @param dayString
     * @param monthString
     * @param yearString
     * @return
     */
    public boolean isDateValid(String date, String dayString, String monthString, String yearString){

        String DATE_FORMAT = "dd-MM-yyyy";
        int day=0;
        int month=0;
        int year=0;

        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);

        } catch (ParseException e) {
            return false;
        }

        try{

            day = Integer.parseInt(dayString);
            month = Integer.parseInt(monthString);
            year = Integer.parseInt(yearString);

        }catch(NumberFormatException e){
            return false;
        }
        return true;

    }

    //TODO BISOGNA TESTARE ISVALIDNUMBEROFPLAYERS()
    /**
     * Checks if String is a number and if it's 2||3
     * @param string
     * @return
     */
    public boolean isValidNumberOfPlayers(String string){

        int number;
        try{

            number = Integer.parseInt(string);
        }
        catch(NumberFormatException e){

            return false;

        }
        if(number==2 || number ==3)return true;

        else return false;

    }

    /**
     * all chosen gods must be different and every god must be in the message's gods-arraylist
     * @param chosenGods Godcards chosen by the user
     * @param numberOfChoices number of godcards
     * @return boolean
     */
    public boolean isChosenGodsValid(String[] chosenGods, int numberOfChoices, PossibleCardsMessage message){

       boolean trovato = false;

       ArrayList<String> gods = new ArrayList<>(message.getGods());
       Iterator<String> itr;

        for(int i=0; i<numberOfChoices; i++){

            itr = gods.iterator();
            while(itr.hasNext()){

                String god = itr.next();
                System.out.println(god);

                if(chosenGods[i].equals(god)){
                    trovato = true;
                    itr.remove();
                    break;
                }
            }
            if(!trovato) return false;
            trovato = false;
        }
        return true;

    }
}
