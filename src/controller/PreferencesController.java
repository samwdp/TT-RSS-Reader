/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import gui.PreferencesView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import preferences.Constants;

/**
 *
 * @author sam
 */
public class PreferencesController implements ActionListener {

    private PreferencesView view;

    public PreferencesController() {
        view = new PreferencesView();
        view.addController(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        JButton b = (JButton) ae.getSource();

        if (b.getText() == "Submit") {
            Constants.updateTime = view.getUpdateFrequency();
            Constants.fontSize = view.getTextSize();

        }
        if (b.getText() == "Cancel") {
            view.dispose();
        }
    }

}
