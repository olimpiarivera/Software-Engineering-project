package it.polimi.ingsw.GUI;

import it.polimi.ingsw.client.ClientSideConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndTurnButton extends GameButton implements ActionListener {

    public EndTurnButton(ClientSideConnection connection) {
        super(connection);
        //super.setIcons();
        super.setSize();
        super.setPosition();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
