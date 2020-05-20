package it.polimi.ingsw.messages.GameToPlayerMessages.Others;

/**
 * This class contains messages that will appear on screen
 */
public class GameMessage {

    //game phase
    public static String abandonedLobby="You have abandoned the lobby";
    public static String nicknameTaken="This nickname has already been taken";
    public static String win="You Win";
    public static String lose="You Lose";
    public static String quit="You are now going to be disconnected";

    //turn error messages
    public static String eliminated = "You've already been eliminated";
    public static String notEliminated="You haven't been eliminated yet, you are not allowed to quit";
    public static String wrongTurn = "This isn't your turn, please wait";

    public static String turnCompleted = "You completed your turn, please confirm that you want to end it";
    public static String turnNotEnded = "You haven't completed your turn yet, please complete it\n";
    public static String turnAlreadyEnded = "You have already completed your turn, please confirm that you want to end it\n";

    //requests
    public static String moveRequest = "Make your move";
    public static String buildRequest = "Choose your build";

    //actions ok
    public static String moveOK= "ok";
    public static String buildOK= "ok";

    //Generic actions error messages
    public static String NotSameWorker = "You have to use the same worker as before";
    public static String alreadyMoved = "You have already moved";
    public static String hasNotMoved = "You have not moved yet";
    public static String alreadyBuilt = "You have already built";
    public static String notOwnPosition = "This is your current position, choose another one";
    public static String notInSurroundings = "This move is not allowed as it is not to one of the cell surrounding the selected worker";
    public static String notInGameBoard = "This position in outside the gameboard, try again ";
    public static String invalidWorkerNumber= "This worker number is invalid, try again";

    //move error messages
    public static String noMoveToCompleteTower = "You can't move here, this space already contains a complete tower, please choose another position";
    public static String noMovedToOccupiedTower = "You can't move here, this space already contains another worker, please choose another position";
    public static String noHighJump = "This move isn't allowed, you can't move up more than one level. Choose another position";

    //build error messages
    public static String noBuildToCompleteTower = "You can't build here, this space already contains a complete tower, please choose another position";
    public static String noBuildToOccupiedTower = "You can't build here, this space already contains a worker, please choose another position";
    public static String noDomesInBlock = "You can't place a dome in a space reserved to a block. Please try something else";
    public static String noBlocksInDome = "You can't place a block in a space reserved to a dome. Please try something else";
    public static String notSameThatMoved = "You must build with the same worker who moved";

    //game start
    public static String startNotInGameboard="You can't place your workers outside of the gameboard";
    public static String notOnEachOther="You can't place your worker on top of each other";
    public static String notOnOccupiedCell="Your choice is invalid because at least one the cells is already occupied";
    public static String placementOk="ok";


    //GOD SPECIFIC MESSAGES

    //APOLLO
    public static String noMovedToOccupiedTowerApollo = "This cell is occupied by your other worker, please choose another position";
    //ARTEMIS
    public static String alreadyMovedTwice = "You have already moved twice";
    //ATHENA
    public static String athenaNoMoveUp = "This move isn't allowed due to Athena's power";
    //MINOTAUR
    public static String noMovedToOccupiedTowerMinotaur = "This cell is occupied by your other worker, please choose another position";
    public static String CannotForceWorker = "The worker in that position can't be forced backwards by Minotaur's power, please choose another position";
    //ARTEMIS
    public static String ArtemisFirstPosition = "This is your first position, choose another one";
    public static String moveAgainOrBuild = "You can now decide to move again or to build";
    //DEMETER
    public static String DemeterFirstBuild = "This is your first building position, choose another one";
    public static String buildAgainOrEnd = "You can now decide to build again or choose END to end your turn";
    public static String alreadyBuiltTwice = "You have already built twice, end your turn";
    //PROMETHEUS
    public static String prometheusNoMoveUp ="this move isn't allowed due to your decision to build before moving";
    public static String noBuildMoreThanTwice ="you already built twice, your turn should have already ended, ERROR";
    //HEPHAESTUS
    public static String HephaestusWrongBuild = "You must build on top of your first block";
    public static String mustBeBlock = "Your second piece must be a Block";
}
