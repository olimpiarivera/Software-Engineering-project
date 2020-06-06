package it.polimi.ingsw.GUI;

import it.polimi.ingsw.client.ClientSideConnection;
import it.polimi.ingsw.messages.GameToPlayerMessages.Notify.GameStartMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.Notify.NewBoardStateMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.Others.ErrorMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.Others.InfoMessage;
import it.polimi.ingsw.messages.GameToPlayerMessages.Others.PlayerInfoRequest;
import it.polimi.ingsw.messages.GameToPlayerMessages.Others.PossibleCardsMessage;
import it.polimi.ingsw.view.View;

import javax.swing.*;

public class GUI extends View{

    private JFrame frame;
    private JPanel mainPanel;
    private GuiController guiController;

    public GUI(ClientSideConnection connection){
        super(connection);
        SwingUtilities.invokeLater(this::createGUI);
    }

    private void createGUI(){
        frame =new MainFrame();
        mainPanel=new MainPanel(frame);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        guiController=new GuiController(getClientSideConnection());
    }


    @Override
    public void handleNewBoardStateMessage(NewBoardStateMessage message) {
        guiController.setCurrentBoardState(message.getBoardState());
    }

    @Override
    public void handleInfoMessage(InfoMessage message) {
        JOptionPane.showMessageDialog(mainPanel,message.getInfo(),"Info Message",JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void handleErrorMessage(ErrorMessage message) {
        JOptionPane.showMessageDialog(mainPanel,message.getError(),"Error Message",JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void handlePlayerInfoRequest(PlayerInfoRequest message) {
        new PlayerInfoRequestDialog(message.isNicknameTaken());
    }

    @Override
    public void handleCardMessageRequest(PossibleCardsMessage message) {
        new DivinityChoiceDialog(message.getGods(),message.getNumberOfChoices());
    }

    @Override
    public void handleStartingPositionRequest() {
        new StartingPositionRequestDialog(guiController.getCurrentBoardState());
    }

    @Override
    public void handleGameStartMessage(GameStartMessage message) {
        setCanStart(true);
    }


    @Override
    public void handleCloseConnectionMessage() {
        JOptionPane.showMessageDialog(mainPanel,"You have been disconnected","disconnection",JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void run() {

    }

}

