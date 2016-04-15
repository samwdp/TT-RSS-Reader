package preferences;

import controller.SimpleViewController;
import java.util.Set;

import gui.SimpleView;
import model.Article;
import model.Feed;
import model.api.TTRSSApi;

/**
 * Holds all the values that repeatedly get used throughout the system so that
 * they do not have to be created again
 *
 * @author Sam
 *
 */
public class Constants {

    public static String URL;
    public static String username;
    public static String password;
    public static String sessionID;
    public static Set<Article> articles;
    public static Set<Feed> feeds;
    public static TTRSSApi api;
    public static int feedAmount = 100;
    public static SimpleViewController simpleView;
    public static long updateTime = 1;
    public static boolean isOnline = true;
    public static int fontSize = 10;
}
