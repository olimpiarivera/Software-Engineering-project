package it.polimi.ingsw.GUI;

import it.polimi.ingsw.model.BoardState;

import javax.swing.*;

public class StartingPositionRequestDialog extends JDialog {

    public StartingPositionRequestDialog(BoardState boardState) {
        setTitle("Starting Position selection");
        setSize(500, 500);
        add(new PositionSelectionPanel(this,boardState));
        pack();
        setVisible(true);

    }

}