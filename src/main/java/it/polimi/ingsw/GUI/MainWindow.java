package it.polimi.ingsw.GUI;

import it.polimi.ingsw.messages.GameToPlayerMessages.Notify.GameStartMessage;
import it.polimi.ingsw.model.BoardState;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Worker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainWindow{

    private final JPanel mainPanel;
    private GameWindow gameWindow;
    private JPanel cardsPanel;
    private JPanel choicePanel;

    public MainWindow(JFrame frame, GameStartMessage message, GuiController guiController){

        mainPanel=new JPanel();
        mainPanel.setPreferredSize(new Dimension(frame.getHeight(),frame.getWidth()));
        mainPanel.setBackground(Color.BLACK);
        setMatchGui(message,guiController);
    }

    private void setMatchGui(GameStartMessage message, GuiController guiController){
        mainPanel.setLayout(new GridBagLayout());

        gameWindow=new GameWindow(guiController);
        choicePanel=new ChoicePanel(guiController);
        cardsPanel=new CardsPanel(message);
        setInternalPanel(gameWindow.getGamePanel(),3,0,1,20,10,1,0,0,0,0,0,0,10,1);
        setInternalPanel(choicePanel,1,0,1,2,2,1,0,0,0,0,0,0,10,1);
        setInternalPanel(cardsPanel,0,0,1,1,1,1,0,0,0,0,0,0,10,1);

    }


    private void setInternalPanel(JPanel panel, int gridx, int gridy, int gridheight, int gridwidth, int weightx, int weighty,int ipadx, int ipady, int insetsTop,int insetsBottom, int insetsLeft, int insetsRight, int anchor, int fill){

            GridBagConstraints gridBagConstraints=new GridBagConstraints();

            //x position of component
            gridBagConstraints.gridx = gridx;
            //y position of the component
            gridBagConstraints.gridy = gridy;
            //rows used by the component
            gridBagConstraints.gridheight = gridheight;
            // Number of columns the component takes up
            gridBagConstraints.gridwidth = gridwidth;

            // Gives the layout manager a hint on how to adjust
            // component width (0 equals fixed)
            gridBagConstraints.weightx = weightx;

            // Gives the layout manager a hint on how to adjust
            // component height (0 equals fixed)
            gridBagConstraints.weighty = weighty;

            gridBagConstraints.ipadx= ipadx;
            gridBagConstraints.ipady= ipady;

            // Defines padding top, left, bottom, right
            gridBagConstraints.insets = new Insets(insetsTop,insetsLeft,insetsBottom,insetsRight);

            // Defines where to place components if they don't
            // fill the space: CENTER, NORTH, SOUTH, EAST, WEST
            // NORTHEAST, etc.
            gridBagConstraints.anchor = anchor;

            // How should the component be stretched to fill the
            // space: NONE, HORIZONTAL, VERTICAL, BOTH
            gridBagConstraints.fill = fill;

            mainPanel.add(panel,gridBagConstraints);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void updateBoard(BoardState boardState) {
        gameWindow.updateBoard(boardState);
    }



}
