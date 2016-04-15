package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import gui.SubscribeView;
import model.Feed;
import preferences.Constants;

/**
 * Controller that deals with the subscription to a new feed
 * 
 * @author Sam
 *
 */
public class SubscribeController implements java.awt.event.ActionListener {

	private SubscribeView v;

	public SubscribeController() {
		v = new SubscribeView();
               
		v.addController(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			// subscribe to the feed
			Constants.api.feedSubscribe(v.getURL(), 0);
                       // Constants.feeds = Constants.api.getFeeds();
                       Constants.simpleView.getFeed();
			
			//v.hideDisplay();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
