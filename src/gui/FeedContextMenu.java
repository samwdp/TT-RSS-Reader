/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.SimpleViewController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Sam
 */
public class FeedContextMenu extends JPopupMenu {

    private JMenuItem unsubscribe;

    public FeedContextMenu(SimpleViewController controller) {

        unsubscribe = new JMenuItem("Unsubsribe");
        unsubscribe.setEnabled(false);
        unsubscribe.addMouseListener(controller);
            
        add(unsubscribe);
    }

}
