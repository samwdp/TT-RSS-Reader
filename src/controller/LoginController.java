package controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.json.JSONException;

import gui.LoginScreenView;
import gui.SimpleView;
import model.Article;
import model.Category;
import model.Feed;
import model.api.TTRSSApi;
import preferences.Constants;

/**
 * Controller that deals with the logging in of the user
 * 
 * @author Sam
 *
 */
public class LoginController implements java.awt.event.ActionListener {

	private TTRSSApi loginAPI;
	private LoginScreenView view;

	public LoginController() {
		loginAPI = new TTRSSApi();
		Constants.api = loginAPI;
		view = new LoginScreenView();
		view.addController(this);
	}

	/**
	 * action listener that is attatched to the button of the login screen, sets
	 * the username, password and URL from user send information to the login
	 * function of the API after successful login, user is sent to the screen of
	 * choice
	 */
	public void actionPerformed(ActionEvent arg0) {
		try {
			Constants.username = view.getUsername();
			Constants.password = view.getPassword();
			Constants.URL = view.getURL();
			boolean login = loginAPI.login();
			if (login == false) {
				view.setError("Check the criteria and try again", "Login unsuccessful");
			} else {
				view.hideDisplay();
				SimpleViewController simpleViewController = new SimpleViewController();
			}
			// view2 = new SimpleView();
			// getFeed(view2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}
}