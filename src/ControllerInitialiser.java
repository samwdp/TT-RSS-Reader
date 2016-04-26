import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import controller.LoginController;
import controller.SimpleViewController;
import gui.LoginScreenView;
import gui.PreferencesView;
import model.api.TTRSSApi;

/**
 * Houses the main method that initialises the fist controller
 * 
 * @author Sam
 *
 */
public class ControllerInitialiser {
    private static LoginController c;

	public static void main(String[] args) throws IOException, JSONException {
		// TODO Auto-generated method stub
		c = new LoginController();
	}

}
