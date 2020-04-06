package it.polimi.ingsw.model;

import java.util.List;
import java.util.ArrayList;

public class Worker {

    private Colour colour;
    private List<Position> turnMovementList = new ArrayList<>();

    public Worker(Colour colour) {
        this.colour=colour;
    }

    public Position getCurrentPosition(){
        return turnMovementList.get(0);
    }

    public Position getPreviousPosition(int index) {
        if (index >= turnMovementList.size()) {
            throw new IllegalArgumentException("your worker didn't move so many times");
        } else {
            return turnMovementList.get(index);
        }
    }

    //to use also when setting the worker's starting position
    public void movedToPosition(Position position){
        Position pos = new Position(position.getX(),position.getY(),position.getZ());
        turnMovementList.add(0,pos);
    }

    public void trimMovementHistory(){
        Position p=turnMovementList.get(0);
        turnMovementList.clear();
        turnMovementList.add(p);
    }

}
