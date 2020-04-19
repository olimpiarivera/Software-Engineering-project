package it.polimi.ingsw.model;

public class TowerState {

    private final int towerHeight;
    private final boolean isCompleted;
    private final int workerNumber;
    private final Colour workerColour;


    public TowerState(TowerCell towercell){

        towerHeight=towercell.getTowerHeight();

        if(towercell.isTowerCompleted()){
            isCompleted=true;
            workerNumber=-1;
            workerColour=null;
        }else{
            isCompleted=false;
            if(towercell.hasWorkerOnTop()){
                workerNumber=towercell.getFirstNotPieceLevel().getWorker().getNumber();
                workerColour=towercell.getFirstNotPieceLevel().getWorker().getColour();
            }else{
                workerNumber=-1;
                workerColour=null;
            }
        }

    }

    public int getTowerHeight() {
        return towerHeight;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public int getWorkerNumber() {
        return workerNumber;
    }

    public Colour getWorkerColour() {
        return workerColour;
    }
}
