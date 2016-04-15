package controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.SubscribeController;
import gui.SubscribeView;

/**
 * Controller that performs the action from main screens GUI to open the
 * Subscription view
 * 
 * @author Sam
 *
 */
public class SimpleViewSubscribeActionListener implements ActionListener {

	public void actionPerformed(ActionEvent arg0) {
		SubscribeController c = new SubscribeController();
	}

}
