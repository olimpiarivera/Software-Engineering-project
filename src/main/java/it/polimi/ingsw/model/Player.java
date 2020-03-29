package it.polimi.ingsw.model;

import java.util.Calendar;

public class Player{

    private final String nickname;
    private final Calendar birthday;

    private Worker[] workers;
    private Colour colour;


    public Player(String nickname, Calendar birthday){
        this.nickname=nickname;
        this.birthday=birthday;
        this.workers=new Worker[2];

    }

    public String getNickname() {
        return nickname;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    /*inserire setter degli worker, passando il numero (in base a cui scelgo la posizione nel vettore
    * la posizione iniziale del Worker la creo qui dentro o prima?
    */


}
