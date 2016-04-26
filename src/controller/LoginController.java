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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private SimpleViewController simpleViewController;

    public LoginController() {
        loginAPI = new TTRSSApi();

        view = new LoginScreenView();
        view.addController(this);
    }

    /**
     * action listener that is attached to the button of the login screen, sets
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
                Constants.api = loginAPI;
                view.hideDisplay();
                simpleViewController = new SimpleViewController();
            }
            // view2 = new SimpleView();
            // getFeed(view2);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (InterruptedException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
